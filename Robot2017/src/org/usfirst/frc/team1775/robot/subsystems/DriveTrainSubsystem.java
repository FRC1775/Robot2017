package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.commands.RegularDriveCommand;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainSubsystem extends Subsystem {

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RegularDriveCommand());
	}

	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs, boolean constantRadius) {
		SmartDashboard.putNumber("DriveTrain.moveValue", moveValue);
		SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);
		SmartDashboard.putBoolean("DriveTrain.constantRadius", constantRadius);

		double actualRotateValue = rotateValue;
		if (constantRadius) {
			actualRotateValue = rotateValue * -moveValue;
		}
		RobotMap.driveTrainRobotDrive.arcadeDrive(moveValue, actualRotateValue, squaredInputs);
	}

	public void rotate(double rotateValue, boolean squaredInputs) {
		SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);

		RobotMap.driveTrainRobotDrive.arcadeDrive(0, rotateValue, squaredInputs);
	}

	public void stop() {
		RobotMap.driveTrainRobotDrive.stopMotor();
	}

}
