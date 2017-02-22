package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.commands.RegularDrive;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainSubsystem extends Subsystem {

	double rotateByAngleAngle = 0;
	double rotateByAngleValue = 0;
	
	double distanceValue = 0;
	double distance = 0;
	
	double straightDriveRotateCompensationValue = 0;
	
	// Used for straight drive
	boolean shouldSetPoint = true;
	
	PIDController straightDrivePidController = new PIDController(-0.3, 0.0, 0.0, (PIDSource) RobotMap.gyro, (value) -> {
		SmartDashboard.putNumber("StraightDrive.pidResult", value);
		straightDriveRotateCompensationValue = value;
	}, 0.01);
	
	PIDController rotateByAnglePidController = new PIDController(0.01, 0.0, 0.0, (PIDSource) RobotMap.gyro, (value) -> {
		SmartDashboard.putNumber("RotateByAngle.pidResult", value);
		rotateByAngleValue = value;
	}, 0.01);
	
	PIDController distancePidController = new PIDController(0.01, 0.0, 0.0, (PIDSource) RobotMap.driveTrainEncoder, (value) -> {
		SmartDashboard.putNumber("Distance.pidResult", value);
		distanceValue = value;
	}, 0.01);

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
		SmartDashboard.putNumber("DriveTrain.actualRotateValue", actualRotateValue);
		
		RobotMap.driveTrainRobotDrive.arcadeDrive(-moveValue, actualRotateValue, squaredInputs);
	}
	
	public boolean isFinished() {
		// TODO figure out when to say we are finished
		return false;
	}
	
	public void setRotateByAngle(double degrees) {

		double p = Preferences.getInstance().getDouble("P", 0.01);
		double i = Preferences.getInstance().getDouble("I", 0.0);
		double d = Preferences.getInstance().getDouble("D", 0.0);
		rotateByAngleAngle = Preferences.getInstance().getDouble("angle", 90);
		rotateByAnglePidController.disable();
		rotateByAnglePidController.free();
		rotateByAnglePidController = new PIDController(p, i, d, (PIDSource) RobotMap.gyro, (value) -> {
			SmartDashboard.putNumber("StraightDrive.pidResult", value);
			straightDriveRotateCompensationValue = value;
		}, 0.01);
		
		rotateByAnglePidController.setSetpoint(RobotMap.gyro.getAngle() + rotateByAngleAngle);
		rotateByAnglePidController.setOutputRange(-1, 1);
		rotateByAnglePidController.enable();
		
	}
	public void setDriveDistance(double distance) {

		double p = Preferences.getInstance().getDouble("distanceP", 0.01);
		double i = Preferences.getInstance().getDouble("distanceI", 0.0);
		double d = Preferences.getInstance().getDouble("distanceD", 0.0);
		distance = Preferences.getInstance().getDouble("distance", 60);
		//distance.disable();
		//distance.free();
		distancePidController = new PIDController(p, i, d, (PIDSource) RobotMap.driveTrainEncoder, (value) -> {
			SmartDashboard.putNumber("driveTrainEncoder.pidResult", value);
			distanceValue = value;
		}, 0.01);
		
		
		distancePidController.setSetpoint(RobotMap.driveTrainEncoder.getDistance() + distance);
		distancePidController.setOutputRange(-1, 1);
		distancePidController.enable();
		
	}
	public void driveToDistance() {
		SmartDashboard.putNumber("encoder.distance ", RobotMap.driveTrainEncoder.getDistance());
		RobotMap.driveTrainRobotDrive.arcadeDrive(distanceValue, 0, false);
	}
	
	public void rotateByAngle() {
		SmartDashboard.putNumber("DriveTrain.straightRotate", straightDriveRotateCompensationValue);
		SmartDashboard.putNumber("DriveTrain.rotateError", rotateByAnglePidController.getError());
		SmartDashboard.putNumber("Angle", RobotMap.gyro.getAngle());
		//if (Math.abs(straightRotate) < 0.01 && Math.abs(straightRotate) > 0.001) {
		//	straightRotate *= 5;
		//}
		RobotMap.driveTrainRobotDrive.arcadeDrive(0, -straightDriveRotateCompensationValue * (1.0/Math.min(rotateByAngleAngle/90.0, 1)), false);
	}

	public void rotate(double rotateValue, boolean squaredInputs) {
		SmartDashboard.putNumber("DriveTrain.rotateValue", rotateValue);
		SmartDashboard.putBoolean("DriveTrain.squaredInputs", squaredInputs);

		RobotMap.driveTrainRobotDrive.arcadeDrive(0, rotateValue * 0.8, squaredInputs);
	}

	public void stop() {
		RobotMap.driveTrainRobotDrive.stopMotor();
	}

}
