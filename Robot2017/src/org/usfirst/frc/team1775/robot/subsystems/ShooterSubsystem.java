package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends Subsystem {
	public static final double DEFAULT_SHOOTER_BANG_BANG_MAX = 1; // Max shooter output during bang-bang
	public static final double DEFAULT_SHOOTER_BANG_BANG_MIN = 0; // Min shooter output during bang-bang
	public static final int DEFAULT_SHOOTER_BANG_BANG_RATE = 10; // Time in milliseconds between each bang-bang
	public static final int DEFAULT_SHOOTER_BANG_BANG_READY_TOLERANCE = 200; // Number of +/- rpm tolerance for bang-bang to be considered ready
	
	public static final int MAX_WAIT_FOR_BANG_BANG_THREAD_DEATH = 1000; // Time in milliseconds for bang-bang thread to die
	
	private static final int SECONDS_PER_MINUTE = 60;
	private static final int DEGREES_IN_REVOLUTION = 360;
	
	private SpeedController singulatorController;
	private SpeedController regulatorController;
	private SpeedController shooterController;
	private Encoder shooterEncoder;
	
	private int shooterRpmTarget;
	private Thread shooterSpeedRegulator;

	@Override
	protected void initDefaultCommand() { }
	
	public void init() {
		singulatorController = new Talon(Robot.roboRio.getShooterSingulatorControllerPwmChannel());
		LiveWindow.addActuator("Shooter", "SingulatorController", (Talon) singulatorController);
		
		regulatorController = new Talon(Robot.roboRio.getShooterRegulatorControllerPwmChannel());
		LiveWindow.addActuator("Shooter", "RegulatorController", (Talon) regulatorController);
		
		shooterController = new Talon(Robot.roboRio.getShooterControllerPwmChannel());
		shooterController.setInverted(true);
		LiveWindow.addActuator("Shooter", "Controller", (Talon) shooterController);
		
		shooterEncoder = new Encoder(Robot.roboRio.getShooterEncoderDioChannelA(), Robot.roboRio.getShooterEncoderDioChannelB(), false, Encoder.EncodingType.k1X);
		shooterEncoder.setDistancePerPulse(360 / 20);
		shooterEncoder.setSamplesToAverage(10);
		LiveWindow.addSensor("Shooter", "Encoder", shooterEncoder);
	}
	
	public void startRegulator(double speed) {
		regulatorController.set(speed);
	}
	
	public void startSingulator(double speed) {
		singulatorController.set(speed);
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
					
					if (getCurrentShooterRpm() < shooterRpmTarget) {
						shooterController.set(getShooterBangBangMax());
					} else {
						shooterController.set(getShooterBangBangMin());
					}

					//SmartDashboard.putNumber("Shooter.output", RobotMap.shooterController.get());
					SmartDashboard.putNumber("Shooter.targetRpm", shooterRpmTarget);
					SmartDashboard.putNumber("Shooter.rpm", getCurrentShooterRpm());
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
		singulatorController.stopMotor();
		regulatorController.stopMotor();
		
		if (shooterSpeedRegulator != null) {
			try {
				shooterSpeedRegulator.interrupt();
				// Wait for thread to die after interrupt
				shooterSpeedRegulator.join(MAX_WAIT_FOR_BANG_BANG_THREAD_DEATH);
			} catch (InterruptedException e) {
				// Do nothing
			} finally {
				// Always attempt to stop the shooter motor
				shooterController.stopMotor();
			}
		}
	}
	
	public boolean isShooterReady() {
		return getCurrentShooterRpm() < shooterRpmTarget + getShooterBangBangReadyTolerance() && getCurrentShooterRpm() > shooterRpmTarget - getShooterBangBangReadyTolerance();
	}
	
	public boolean isStopped() {
		// TODO this may be a bad idea for checking stopped state.
		return shooterController.get() == 0 && singulatorController.get() == 0 && regulatorController.get() == 0;
	}
	
	private int getCurrentShooterRpm() {
		return (int) Math.round((shooterEncoder.getRate() / DEGREES_IN_REVOLUTION) * SECONDS_PER_MINUTE);
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
