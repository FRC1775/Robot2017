package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.Cameras;
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
	
	private PIDController rotateByAnglePidController;
	private PIDController driveToDistancePidController;

	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RegularDrive());
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
		
		/*
		if (rotateValue < 0.2 && rotateValue > -0.2) {
			if (shouldSetPoint || (moveValue < 0.1 && moveValue > -0.1)) {
				straightDrivePidController.setOutputRange(-0.3, 0.3);
				straightDrivePidController.setSetpoint(RobotMap.gyro.getAngle());
				shouldSetPoint = false;
				straightDrivePidController.enable();
			}
			
			SmartDashboard.putNumber("Angle", RobotMap.gyro.getAngle());
			actualRotateValue = -straightDriveRotateCompensationValue;
		} else {
			straightDrivePidController.disable();
			shouldSetPoint = true;
		}
		*/
		
		SmartDashboard.putNumber("DriveTrain.actualRotateValue", actualRotateValue);
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(actualMoveValue, actualRotateValue, squaredInputs);
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
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(driveToDistancePidResult, 0, false);
	}
	
	public void rotateByAngle() {
		SmartDashboard.putNumber("DriveTrain.straightRotate", rotateByAnglePidResult);
		SmartDashboard.putNumber("DriveTrain.rotateError", rotateByAnglePidController.getError());
		SmartDashboard.putNumber("DriveTrain.angle", RobotMap.gyro.getAngle());
		//if (Math.abs(straightRotate) < 0.01 && Math.abs(straightRotate) > 0.001) {
		//	straightRotate *= 5;
		//}
		//  * (1.0/Math.min(rotateByAngleTargetAngle/90.0, 1))
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
	
	private int counts = 0;
	
	public boolean isFinished() {
		// TODO figure out when to say we are finished
		if (driveMode == DriveMode.RotateByAngle) {
			return rotateByAnglePidController.onTarget();
			/*
			if ( < 0.25) {
				counts++;
			} else {
				counts = 0;
			}
			
			return counts > 20;
			*/
		} else if (driveMode == DriveMode.DriveToDistance) {
			if (Math.abs(driveToDistanceTargetDistance - RobotMap.driveTrainEncoder.getDistance()) < 5) {
				counts++;
			} else {
				counts = 0;
			}
			
			return counts > 10;
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
		counts = 0;
		// TODO replace with rotateByAngleTargetAngle = degrees;
		RobotMap.gyro.reset();
		rotateByAngleTargetAngle = degrees + offset;
		//rotateByAngleTargetAngle = Preferences.getInstance().getDouble("DriveTrain.rotateByAngle.angle", 90);
		
		if (rotateByAnglePidController == null) {
			rotateByAnglePidController = new PIDController(p, i, d, (PIDSource) RobotMap.gyro, (value) -> {
				SmartDashboard.putNumber("DriveTrain.rotateByAngle.pidResult", value);
				rotateByAnglePidResult = value;
			}, 0.01);
		}
		rotateByAnglePidController.setPID(p, i, d);
		rotateByAnglePidController.setInputRange(-180, 180);
		rotateByAnglePidController.setPercentTolerance(0.4);
		//rotateByAnglePidController.setAbsoluteTolerance(2);
		
		//this set the tolerance degrees
		//rotateByAnglePidController.setAbsoluteTolerance(1);;
		
		rotateByAnglePidController.setSetpoint(rotateByAngleTargetAngle);
		rotateByAnglePidController.enable();
	}
	
	public void setDriveDistance(double distance) {
		setDriveMode(DriveMode.DriveToDistance);

		
		double p = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.p", 0.01);
		double i = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.i", 0.0);
		double d = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.d", 0.0);
		counts = 0;
		// TODO replace with 
		driveToDistanceTargetDistance = RobotMap.driveTrainEncoder.getDistance() + distance;
		//driveToDistanceTargetDistance = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.distance", 60);
		
		driveToDistancePidController = new PIDController(p, i, d, (PIDSource) RobotMap.driveTrainEncoder, (value) -> {
			SmartDashboard.putNumber("DriveTrain.driveToDistance.pidResult", value);
			driveToDistancePidResult = value;
		}, 0.01);
		
		driveToDistancePidController.setSetpoint(driveToDistanceTargetDistance);
		driveToDistancePidController.enable();
	}

	public void stop() {
		RobotMap.driveTrainRobotDrive.stopMotor();
		stopPidControllers();
	}

	private void setDriveMode(DriveMode mode) {
		if (driveMode == mode) return;
		
		if (rotateByAnglePidController != null) {
			//rotateByAnglePidController.disable();
			//rotateByAnglePidController.free();
			//rotateByAnglePidController = null;
		}
		
		if (driveToDistancePidController != null) {
			driveToDistancePidController.disable();
			driveToDistancePidController.free();
			driveToDistancePidController = null;
		}
		
		rotateInPlaceCurrentRampFactor = 0;
		rotateInPlaceStartTime = System.currentTimeMillis();
		
		driveMode = mode;
	}
	
	private void stopPidControllers() {
		// TODO can we include straight drive?
		if (rotateByAnglePidController != null) {
			//rotateByAnglePidController.disable();
			//rotateByAnglePidController.free();
			//rotateByAnglePidController = null;
		}
		
		if (driveToDistancePidController != null) {
			driveToDistancePidController.disable();
			driveToDistancePidController.free();
			driveToDistancePidController = null;
		}
	}
	
}
