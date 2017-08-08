package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RoboRio;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RegularDrive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Scheduler;
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
	
	
	private PIDController straightDrivePidController = new PIDController(-0.4, 0.0, 0.0, (PIDSource) RoboRio.gyro, (value) -> {
		//SmartDashboard.putNumber("DriveTrain.straightDrive.pidResult", value);
		straightDriveRotateCompensationValue = value;
	}, 0.01);
	
	
	private PIDController rotateByAnglePidController = new PIDController(0, 0, 0, (PIDSource) RoboRio.gyro, (value) -> {
		SmartDashboard.putNumber("DriveTrain.rotateByAngle.gyroAngle", RoboRio.gyro.getAngle());
		rotateByAnglePidResult = value;
	}, 0.01);
	
	
	private PIDController driveToDistancePidController = new PIDController(0, 0, 0, (PIDSource) RoboRio.driveTrainEncoder, (value) -> {
		SmartDashboard.putNumber("DriveTrain.encoderDistance", RoboRio.driveTrainEncoder.getDistance());
		driveToDistancePidResult = value;
	}, 0.02);

	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new RegularDrive());
		driveToDistancePidController.setOutputRange(-0.9, 0.9);
		straightDrivePidController.setOutputRange(-0.4, 0.4);
		straightDrivePidController.setSetpoint(0);
		straightDrivePidController.enable();
	}

	public void arcadeDrive(double moveValue, double rotateValue, boolean squaredInputs, boolean constantRadius) {
		//SmartDashboard.putNumber("DriveTrain.moveValue", moveValue);
		//SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		//SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);
		//SmartDashboard.putBoolean("DriveTrain.constantRadius", constantRadius);

		double actualMoveValue = -moveValue;
		double actualRotateValue = rotateValue;
		
		if (moveValue < 0.15 && moveValue > -0.15) {
			if (rotateValue < 0.15 && rotateValue > -0.15) {

				rotateInPlaceCurrentRampFactor = 0;
				rotateInPlaceStartTime = System.currentTimeMillis();
			}
			actualMoveValue = 0;
			setDriveMode(DriveMode.RotateInPlace);
			
			//SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
			//SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);

			// TODO handle ramp of rotate
			rotateInPlaceCurrentRampFactor = Math.min(1, (System.currentTimeMillis() - rotateInPlaceStartTime) / (double) DEFAULT_ROTATE_RAMP_TIME);
			actualRotateValue = rotateValue * rotateInPlaceCurrentRampFactor;
		} else {
			setDriveMode(DriveMode.Regular);
			
			if (constantRadius) {
				actualRotateValue = rotateValue * actualMoveValue;
			}
		}
		
		SmartDashboard.putNumber("DriveTrain.encoderDistance", RoboRio.driveTrainEncoder.getDistance());
		SmartDashboard.putNumber("DriveTrain.angle", RoboRio.gyro.getAngle());
		
		actualRotateValue = driveStraightCorrection(moveValue, rotateValue);
		
		RoboRio.driveTrainRobotDrive.arcadeDrive(actualMoveValue, actualRotateValue, squaredInputs);
	}
	
	private double driveStraightCorrection(double moveValue, double rotateValue) {
		if (rotateValue < 0.2 && rotateValue > -0.2) {
			if (shouldSetPoint || (moveValue < 0.1 && moveValue > -0.1)) {
				RoboRio.gyro.reset();
				shouldSetPoint = false;
			}
			
			return -straightDriveRotateCompensationValue;
		} else {
			shouldSetPoint = true;
			return rotateValue;
		}
	}
	
	double driveDistanceRampFactor = 0;
	boolean isDecline = false;
	
	public void driveDistance(boolean killScheduler) {
		SmartDashboard.putNumber("DriveTrain.encoderDistance", RoboRio.driveTrainEncoder.getDistance());

		RoboRio.driveTrainRobotDrive.arcadeDrive(driveToDistancePidResult, -straightDriveRotateCompensationValue, false);
	}
	
	public void rotateByAngle() {
		RoboRio.driveTrainRobotDrive.arcadeDrive(0, rotateByAnglePidResult, false);
	}

	public void rotate(double rotateValue, boolean squaredInputs) {
		setDriveMode(DriveMode.RotateInPlace);
		
		//SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		//SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);

		// TODO handle ramp of rotate
		rotateInPlaceCurrentRampFactor = Math.min(1, (System.currentTimeMillis() - rotateInPlaceStartTime) / DEFAULT_ROTATE_RAMP_TIME);
		
		RoboRio.driveTrainRobotDrive.arcadeDrive(0, rotateValue * rotateInPlaceCurrentRampFactor, squaredInputs);
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

		isDecline = false;
		
		RoboRio.gyro.reset();
		
		rotateByAngleTargetAngle = 0.972217 * degrees;
		
		if (degrees > 0) {
			rotateByAngleTargetAngle -= 0.211377;
		} else {
			rotateByAngleTargetAngle += 0.211377;
		}
		
		double p = Preferences.getInstance().getDouble("DriveTrain.angle.p", 0.35);
		double i = Preferences.getInstance().getDouble("DriveTrain.angle.i", 0.0);
		double d = Preferences.getInstance().getDouble("DriveTrain.angle.d", 2.1);
		
		rotateByAnglePidController.setPID(p, i, d);
		rotateByAnglePidController.setContinuous();
		rotateByAnglePidController.setOutputRange(-0.8, 0.8);
		rotateByAnglePidController.setAbsoluteTolerance(1);
		rotateByAnglePidController.setToleranceBuffer(40);
		
		rotateByAnglePidController.setSetpoint(rotateByAngleTargetAngle);
		rotateByAnglePidController.enable();
	}
	
	double driveDistanceStartTime = 0;
	
	public void setDriveDistance(double distance) {
		setDriveMode(DriveMode.DriveToDistance);

		RoboRio.gyro.reset();
		
		isDecline = false;
		driveDistanceStartTime = System.currentTimeMillis();
		
		double p = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.p", 0.35);
		double i = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.i", 0.0);
		double d = Preferences.getInstance().getDouble("DriveTrain.driveToDistance.d", 2.3);
		
		RoboRio.driveTrainEncoder.reset();
		
		driveToDistanceTargetDistance = distance;

		SmartDashboard.putNumber("drive.p", p);
		SmartDashboard.putNumber("drive.i", i);
		SmartDashboard.putNumber("drive.d", d);
		
		driveToDistancePidController.setPID(p, i, d);
		driveToDistancePidController.setContinuous();
		driveToDistancePidController.setOutputRange(-0.5, 0.5);
		driveToDistancePidController.setAbsoluteTolerance(0.5);
		driveToDistancePidController.setToleranceBuffer(20);
		
		driveToDistancePidController.setSetpoint(driveToDistanceTargetDistance);
		driveToDistancePidController.enable();
	}

	public void stop() {
		RoboRio.driveTrainRobotDrive.stopMotor();
	}

	private void setDriveMode(DriveMode mode) {
		if (driveMode == mode) return;
		
		rotateInPlaceCurrentRampFactor = 0;
		rotateInPlaceStartTime = System.currentTimeMillis();
		
		driveMode = mode;
	}
	
}
