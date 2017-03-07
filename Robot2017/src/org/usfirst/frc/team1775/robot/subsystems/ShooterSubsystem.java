package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends Subsystem {
	public static final double DEFAULT_SHOOTER_BANG_BANG_MAX = 1; // Max shooter output during bang-bang
	public static final double DEFAULT_SHOOTER_BANG_BANG_MIN = 0; // Min shooter output during bang-bang
	public static final int DEFAULT_SHOOTER_BANG_BANG_RATE = 10; // Time in milliseconds between each bang-bang
	public static final int DEFAULT_SHOOTER_BANG_BANG_READY_TOLERANCE = 200; // Number of +/- rpm tolerance for bang-bang to be considered ready
	
	public static final int MAX_WAIT_FOR_BANG_BANG_THREAD_DEATH = 1000; // Time in milliseconds for bang-bang thread to die
	
	private static final int SECONDS_PER_MINUTE = 60;
	private static final int DEGREES_IN_REVOLUTION = 360;

	@Override
	protected void initDefaultCommand() { }
	
	private int shooterRpmTarget;
	private Thread shooterSpeedRegulator;
	
	public void startRegulator(double speed) {
		RobotMap.shooterRegulatorController.set(speed);
	}
	
	public void startSingulator(double speed) {
		RobotMap.shooterSingulatorController.set(speed);
	}

	public void startShooter(int rpm) {
		if (shooterSpeedRegulator == null || !shooterSpeedRegulator.isAlive()) {
			this.shooterRpmTarget = rpm;
			
			shooterSpeedRegulator = new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(getShooterBangBangRate());
					} catch (InterruptedException e) {
						return;
					}
					
					double currentShooterRpm = (RobotMap.shooterEncoder.getRate() / DEGREES_IN_REVOLUTION) * SECONDS_PER_MINUTE;
					
					if (currentShooterRpm < shooterRpmTarget) {
						RobotMap.shooterController.set(getShooterBangBangMax());
					} else {
						RobotMap.shooterController.set(getShooterBangBangMin());
					}

					SmartDashboard.putNumber("Shooter.output", RobotMap.shooterController.get());
					SmartDashboard.putNumber("Shooter.targetRpm", shooterRpmTarget);
					SmartDashboard.putNumber("Shooter.rpm", currentShooterRpm);
				}
			});
			shooterSpeedRegulator.start();
		}
	}
	
	public void adjustShooter(int rpmChange) {
		adjustShooter(rpmChange, false);
	}
	
	public void adjustShooter(int rpmChange, boolean absolute) {
		if (absolute) {
			this.shooterRpmTarget = rpmChange;
		} else {
			this.shooterRpmTarget += rpmChange;
		}
	}

	public void stop() {
		RobotMap.shooterSingulatorController.stopMotor();
		RobotMap.shooterRegulatorController.stopMotor();
		
		if (shooterSpeedRegulator != null) {
			try {
				shooterSpeedRegulator.interrupt();
				// Wait for thread to die after interrupt
				shooterSpeedRegulator.join(MAX_WAIT_FOR_BANG_BANG_THREAD_DEATH);
			} catch (InterruptedException e) {
				// Do nothing
			} finally {
				// Always attempt to stop the shooter motor
				RobotMap.shooterController.stopMotor();
			}
		}
	}
	
	public boolean isShooterReady() {
		return getCurrentShooterRpm() < shooterRpmTarget + getShooterBangBangReadyTolerance() && getCurrentShooterRpm() > shooterRpmTarget - getShooterBangBangReadyTolerance();
	}
	
	public boolean isStopped() {
		// TODO this may be a bad idea for checking stopped state.
		return RobotMap.shooterController.get() == 0 && RobotMap.shooterSingulatorController.get() == 0 && RobotMap.shooterRegulatorController.get() == 0;
	}
	
	private static int getCurrentShooterRpm() {
		return (int) Math.round((RobotMap.shooterEncoder.getRate() / DEGREES_IN_REVOLUTION) * SECONDS_PER_MINUTE);
	}
	
	private static int getShooterBangBangRate() {
		return Preferences.getInstance().getInt("Shooter.bangBangRate", DEFAULT_SHOOTER_BANG_BANG_RATE);
	}
	
	private static double getShooterBangBangMax() {
		return Preferences.getInstance().getDouble("Shooter.bangBangMax", DEFAULT_SHOOTER_BANG_BANG_MAX);
	}
	
	private static double getShooterBangBangMin() {
		return Preferences.getInstance().getDouble("Shooter.bangBangMin", DEFAULT_SHOOTER_BANG_BANG_MIN);
	}
	
	private static int getShooterBangBangReadyTolerance() {
		return Preferences.getInstance().getInt("Shooter.bangBangReadyTolerance", DEFAULT_SHOOTER_BANG_BANG_READY_TOLERANCE);
	}
	
}
