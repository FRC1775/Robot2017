package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}
	
	private Thread shooterThread;
	private boolean runLoop = false;

	public void runSingulator() {
		if (shooterThread == null) {
			shooterThread = new Thread(() ->{
				while (true) {
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (runLoop) {
						double rpm = (RobotMap.shooterEncoder.getRate() / 360) * 60;
						double target = Preferences.getInstance().getDouble("Shooter.rpm", 2300);
						
						if (rpm < target) {
							RobotMap.shooterController.set(0.7);
						} else {
							RobotMap.shooterController.set(0.4);
						}
	
						SmartDashboard.putNumber("Shooter.output", RobotMap.shooterController.get());
						SmartDashboard.putNumber("Shooter.rpm", rpm);
					}
				}
			});
			shooterThread.start();
		}
		
		runLoop = true;
		
		double singulatorSpeed = Preferences.getInstance().getDouble("Shooter.singulatorSpeed", 0.25);
		double regulatorSpeed = Preferences.getInstance().getDouble("Shooter.regulatorSpeed", 0.25);
		RobotMap.shooterSingulatorController.set(singulatorSpeed);
		RobotMap.shooterRegulatorController.set(regulatorSpeed);
		
		//double trigger = Robot.oi.joystick1.getRawAxis(2);

		
		//int rpm = Preferences.getInstance().getInt("Shooter.rpm", 2750);
		
		
		
		//if (trigger > 0.5) {
		//	SmartDashboard.putNumber("Angle", RobotMap.gyro.getAngle());
		//	SmartDashboard.putNumber("Shooter.singulatorSpeed", singulatorSpeed);
		//	SmartDashboard.putNumber("Shooter.regulatorSpeed", regulatorSpeed);
			
		/*
			RobotMap.shooterSingulatorController.set(singulatorSpeed);
			RobotMap.shooterRegulatorController.set(regulatorSpeed);
			
			// Set shooter speed
			RobotMap.shooterController.changeControlMode(TalonControlMode.Speed);
			RobotMap.shooterController.setF(Preferences.getInstance().getDouble("Shooter.F", 1.6));
			RobotMap.shooterController.setP(Preferences.getInstance().getDouble("Shooter.P", 3.3));
			RobotMap.shooterController.setI(Preferences.getInstance().getDouble("Shooter.I", 0));
			RobotMap.shooterController.setD(Preferences.getInstance().getDouble("Shooter.D", 115));
			RobotMap.shooterController.set(Preferences.getInstance().getDouble("Shooter.rpm", 0));
			SmartDashboard.putNumber("Shooter.trigger", trigger);
			//SmartDashboard.putNumber("Shooter.rpm", RobotMap.shooterController.getSpeed());
			SmartDashboard.putNumber("Shooter.output", RobotMap.shooterController.getOutputVoltage());
			SmartDashboard.putNumber("Shooter.cle", RobotMap.shooterController.getClosedLoopError());
		//} else {
		//	stop();
		//}
		 * */
	}

	public void stop() {
		RobotMap.shooterSingulatorController.stopMotor();
		RobotMap.shooterRegulatorController.stopMotor();
		//RobotMap.shooterController.changeControlMode(TalonControlMode.PercentVbus);
		RobotMap.shooterController.set(0);
		shooterThread.interrupt();
		shooterThread = null;
		runLoop = false;
	}
}
