package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RegularDrive;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainSubsystem extends Subsystem {
	private static final int DEFAULT_ROTATE_RAMP_TIME = 200; // Time in ms to ramp up to 100% of rotateValue
	
	private enum DriveMode {
		Regular, RotateInPlace, RotateByAngle, DriveToDistance
	}
	
	private static DriveMode driveMode = DriveMode.Regular;

	private long rotateInPlaceStartTime;

	double rotateByAnglePidResult = 0;
	double rotateByAngleTargetAngle = 0;
	
	double driveToDistancePidResult = 0;
	double driveToDistanceTargetDistance = 0;
	
	
	double rotateInPlaceCurrentRampFactor = 0;
	double straightDriveRotateCompensationValue = 0;
	
	// Used for straight drive
	boolean shouldSetPoint = true;
	
	
	private PIDController straightDrivePidController = new PIDController(-0.3, 0.0, 0.0, (PIDSource) RobotMap.gyro, (value) -> {
		SmartDashboard.putNumber("DriveTrain.straightDrive.pidResult", value);
		straightDriveRotateCompensationValue = value;
	}, 0.01);
	
	private PIDController rotateByAnglePidController = new PIDController(0, 0, 0, (PIDSource) RobotMap.gyro, (value) -> {
		SmartDashboard.putNumber("DriveTrain.rotateByAngle.pidResult", value);
		rotateByAnglePidResult = value;
	}, 0.01);
	
	private PIDController driveToDistancePidController = new PIDController(0, 0, 0, (PIDSource) RobotMap.driveTrainEncoder, (value) -> {
		SmartDashboard.putNumber("DriveTrain.driveToDistance.pidResult", value);
		driveToDistancePidResult = value;
	}, 0.01);

	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RegularDrive());
		driveToDistancePidController.setOutputRange(-0.9, 0.9);
		straightDrivePidController.setOutputRange(-0.3, 0.3);
		straightDrivePidController.setSetpoint(0);
		straightDrivePidController.enable();
	}

	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs, boolean constantRadius) {
		SmartDashboard.putNumber("DriveTrain.moveValue", moveValue);
		SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);
		SmartDashboard.putBoolean("DriveTrain.constantRadius", constantRadius);

		double actualMoveValue = -moveValue;
		double actualRotateValue = rotateValue;
		
		if (moveValue < 0.15 && moveValue > -0.15) {
			if (rotateValue < 0.15 && rotateValue > -0.15) {

				rotateInPlaceCurrentRampFactor = 0;
				rotateInPlaceStartTime = System.currentTimeMillis();
			}
			actualMoveValue = 0;
			setDriveMode(DriveMode.RotateInPlace);
			
			SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
			SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);

			// TODO handle ramp of rotate
			rotateInPlaceCurrentRampFactor = Math.min(1, (System.currentTimeMillis() - rotateInPlaceStartTime) / (double) DEFAULT_ROTATE_RAMP_TIME);
			actualRotateValue = rotateValue * rotateInPlaceCurrentRampFactor;
		} else {
			setDriveMode(DriveMode.Regular);
			
			if (constantRadius) {
				actualRotateValue = rotateValue * actualMoveValue;
			}
		}
		
		SmartDashboard.putNumber("DriveTrain.encoderDistance", RobotMap.driveTrainEncoder.getDistance());
		
		actualRotateValue = driveStraightCorrection(moveValue, rotateValue);
		
		SmartDashboard.putNumber("DriveTrain.actualRotateValue", actualRotateValue);
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(actualMoveValue, actualRotateValue, squaredInputs);
	}
	
	private double driveStraightCorrection(double moveValue, double rotateValue) {
		if (rotateValue < 0.2 && rotateValue > -0.2) {
			if (shouldSetPoint || (moveValue < 0.1 && moveValue > -0.1)) {
				RobotMap.gyro.reset();
				shouldSetPoint = false;
			}
			
			return -straightDriveRotateCompensationValue;
		} else {
			shouldSetPoint = true;
			return rotateValue;
		}
	}
	
	private int detectEncoderCount = 0;
	private double distanceEncoderValue = 0;
	
	public void driveDistance() {
		SmartDashboard.putNumber("DriveTrain.encoderDistance", RobotMap.driveTrainEncoder.getDistance());
		
		if (distanceEncoderValue == RobotMap.driveTrainEncoder.getDistance()) {
			detectEncoderCount++;
			if (detectEncoderCount > 10) {
				stop();
			}
		} else {
			distanceEncoderValue = RobotMap.driveTrainEncoder.getDistance();
			detectEncoderCount = 0;
		}
		
		
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(driveToDistancePidResult, -straightDriveRotateCompensationValue, false);
	}
	
	public void rotateByAngle() {
		SmartDashboard.putNumber("DriveTrain.straightRotate", rotateByAnglePidResult);
		SmartDashboard.putNumber("DriveTrain.rotateError", rotateByAnglePidController.getError());
		SmartDashboard.putNumber("DriveTrain.angle", RobotMap.gyro.getAngle());
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(0, rotateByAnglePidResult, false);
	}

	public void rotate(double rotateValue, boolean squaredInputs) {
		setDriveMode(DriveMode.RotateInPlace);
		
		SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);

		// TODO handle ramp of rotate
		rotateInPlaceCurrentRampFactor = Math.min(1, (System.currentTimeMillis() - rotateInPlaceStartTime) / DEFAULT_ROTATE_RAMP_TIME);
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(0, rotateValue * rotateInPlaceCurrentRampFactor, squaredInputs);
	}
	
	public boolean isFinished() {
		if (driveMode == DriveMode.RotateByAngle) {
			return rotateByAnglePidController.onTarget();
		} else if (driveMode == DriveMode.DriveToDistance) {
			return driveToDistancePidController.onTarget();
		}
		
		return false;
	}
	
	public void setRegularDrive() {
		setDriveMode(DriveMode.Regular);
	}
	
	public void setRotateByAngle(double degrees) {
		setDriveMode(DriveMode.RotateByAngle);

		double p, i = 0, d, offset;
		if (degrees > 0) {
			offset = 2.5;
		} else {
			offset = -2.5;
		}
		if (Math.abs(degrees) < 16) {
			p = 0.15;
			d = 0.4;
			offset = -1;
		} else {
			p = 0.15;
			d = 0.4;
			//offset = -2.5;
		}
		
		//p = Preferences.getInstance().getDouble("DriveTrain.rotateByAngle.p", 0.01);
		//d = Preferences.getInstance().getDouble("DriveTrain.rotateByAngle.d", 0.0);
		//offset = 0;
		
		RobotMap.gyro.reset();
		
		rotateByAngleTargetAngle = degrees + offset;
		
		rotateByAnglePidController.setPID(p, i, d);
		rotateByAnglePidController.setInputRange(-180, 180);
		rotateByAnglePidController.setPercentTolerance(0.4);
		
		rotateByAnglePidController.setSetpoint(rotateByAngleTargetAngle);
		rotateByAnglePidController.enable();
	}
	
	public void setDriveDistance(double distance) {
		setDriveMode(DriveMode.DriveToDistance);

		RobotMap.gyro.reset();
		
		double p = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.p", 0.01);
		double i = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.i", 0.0);
		double d = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.d", 0.0);
		
		RobotMap.driveTrainEncoder.reset();
		
		driveToDistanceTargetDistance = distance;

		driveToDistancePidController.setPID(p, i, d);
		driveToDistancePidController.setInputRange(-100, 200);
		driveToDistancePidController.setPercentTolerance(0.3);
		
		driveToDistancePidController.setSetpoint(driveToDistanceTargetDistance);
		driveToDistancePidController.enable();
	}

	public void stop() {
		RobotMap.driveTrainRobotDrive.stopMotor();
	}

	private void setDriveMode(DriveMode mode) {
		if (driveMode == mode) return;
		
		rotateInPlaceCurrentRampFactor = 0;
		rotateInPlaceStartTime = System.currentTimeMillis();
		
		driveMode = mode;
	}
	
}
