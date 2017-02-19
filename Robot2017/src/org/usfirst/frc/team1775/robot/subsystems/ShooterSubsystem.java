package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.RobotMap;

import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public void runSingulator() {
		double trigger = Robot.oi.joystick1.getRawAxis(2);

		double singulatorSpeed = Preferences.getInstance().getDouble("Shooter.singulatorSpeed", 0.25);
		double regulatorSpeed = Preferences.getInstance().getDouble("Shooter.regulatorSpeed", 0.25);
		int rpm = Preferences.getInstance().getInt("Shooter.rpm", 2750);
		
		//if (trigger > 0.5) {
		//	SmartDashboard.putNumber("Angle", RobotMap.gyro.getAngle());
		//	SmartDashboard.putNumber("Shooter.singulatorSpeed", singulatorSpeed);
		//	SmartDashboard.putNumber("Shooter.regulatorSpeed", regulatorSpeed);
			
			RobotMap.shooterSingulatorController.set(singulatorSpeed);
			RobotMap.shooterRegulatorController.set(regulatorSpeed);
			
			// Set shooter speed
			RobotMap.shooterController.changeControlMode(TalonControlMode.Speed);
			RobotMap.shooterController.setF(Preferences.getInstance().getDouble("Shooter.F", 1.4));
			RobotMap.shooterController.setP(Preferences.getInstance().getDouble("Shooter.P", 1));
			RobotMap.shooterController.setI(Preferences.getInstance().getDouble("Shooter.I", 0));
			RobotMap.shooterController.setD(Preferences.getInstance().getDouble("Shooter.D", 0));
			RobotMap.shooterController.set(2500);
			SmartDashboard.putNumber("Shooter.trigger", trigger);
			SmartDashboard.putNumber("Shooter.rpm", RobotMap.shooterController.getSpeed());
			SmartDashboard.putNumber("Shooter.output", RobotMap.shooterController.getOutputVoltage());
			SmartDashboard.putNumber("Shooter.cle", RobotMap.shooterController.getClosedLoopError());
		//} else {
		//	stop();
		//}
	}

	public void stop() {
		RobotMap.shooterSingulatorController.stopMotor();
		RobotMap.shooterRegulatorController.stopMotor();
		RobotMap.shooterController.changeControlMode(TalonControlMode.PercentVbus);
		RobotMap.shooterController.set(0);
	}
}
