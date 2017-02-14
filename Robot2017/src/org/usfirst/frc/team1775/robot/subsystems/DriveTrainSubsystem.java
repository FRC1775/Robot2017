package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.commands.RegularDrive;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainSubsystem extends Subsystem {
	
	double straightRotate = 0;
	boolean shouldSetPoint = true;
	
	PIDController straightDrive = new PIDController(-0.3, 0.0, 0.0, (PIDSource) RobotMap.gyro, (value) -> {
		SmartDashboard.putNumber("PID Result", value);
		straightRotate = value;
	});

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RegularDrive());
	}

	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs, boolean constantRadius) {
		SmartDashboard.putNumber("DriveTrain.moveValue", moveValue);
		SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);
		SmartDashboard.putBoolean("DriveTrain.constantRadius", constantRadius);

		double actualRotateValue = rotateValue;
		if (constantRadius) {
			actualRotateValue = rotateValue * moveValue;
		}
		
		if (rotateValue < 0.2 && rotateValue > -0.2) {
			if (shouldSetPoint || (moveValue < 0.1 && moveValue > -0.1)) {
				straightDrive.setOutputRange(-0.3, 0.3);
				straightDrive.setSetpoint(RobotMap.gyro.getAngle());
				shouldSetPoint = false;
				straightDrive.enable();
			}
			
			SmartDashboard.putNumber("Angle", RobotMap.gyro.getAngle());
			actualRotateValue = straightRotate;
		} else {
			//straightDrive.disable();
			straightDrive.disable();
			shouldSetPoint = true;
		}
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(moveValue, actualRotateValue, squaredInputs);
	}

	public void rotate(double rotateValue, boolean squaredInputs) {
		SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);

		RobotMap.driveTrainRobotDrive.arcadeDrive(0, rotateValue * 0.8, squaredInputs);
	}
	
	public void rotateToAngle(double angleInDegrees) {
		// TODO
	}

	public void stop() {
		RobotMap.driveTrainRobotDrive.stopMotor();
	}

}
