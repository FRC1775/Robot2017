package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterSubsystem extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub

	}

	public void runSingulator() {
		double singulatorSpeed = Robot.oi.joystick1.getRawAxis(3);
		SmartDashboard.putNumber("Shooter.singulatorSpeed", singulatorSpeed);
		RobotMap.shooterSingulatorController.set(singulatorSpeed);
	}

	public void stop() {
		RobotMap.shooterSingulatorController.stopMotor();
	}
}
