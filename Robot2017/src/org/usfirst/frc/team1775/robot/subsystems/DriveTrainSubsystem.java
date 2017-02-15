package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.commands.RegularDrive;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrainSubsystem extends Subsystem {
	
	double straightRotate = 0;
	boolean shouldSetPoint = true;
	
	// -0.3 for straight drive
	PIDController straightDrive = new PIDController(0.01, 0.0, 0.0, (PIDSource) RobotMap.gyro, (value) -> {
		SmartDashboard.putNumber("PID Result", value);
		straightRotate = value;
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
	
	public boolean isFinished() {
		//if (straightDrive.isEnabled() && straightDrive.getError() < 1) {
		//	straightDrive.disable();
		//	return true;
		//}
		return false;
	}
	
	double angle = 0;
	public void setRotateByAngle(double degrees) {

		double p = Preferences.getInstance().getDouble("P", 0.01);
		double i = Preferences.getInstance().getDouble("I", 0.0);
		double d = Preferences.getInstance().getDouble("D", 0.0);
		angle = Preferences.getInstance().getDouble("angle", 90);
		straightDrive.disable();
		straightDrive.free();
		straightDrive = new PIDController(p, i, d, (PIDSource) RobotMap.gyro, (value) -> {
			SmartDashboard.putNumber("PID Result", value);
			straightRotate = value;
		}, 0.01);
		
		straightDrive.setSetpoint(RobotMap.gyro.getAngle() + angle);
		straightDrive.setOutputRange(-1, 1);
		straightDrive.enable();
		
	}
	
	public void rotateByAngle() {
		SmartDashboard.putNumber("DriveTrain.straightRotate", straightRotate);
		SmartDashboard.putNumber("DriveTrain.rotateError", straightDrive.getError());
		SmartDashboard.putNumber("Angle", RobotMap.gyro.getAngle());
		//if (Math.abs(straightRotate) < 0.01 && Math.abs(straightRotate) > 0.001) {
		//	straightRotate *= 5;
		//}
		RobotMap.driveTrainRobotDrive.arcadeDrive(0, -straightRotate * (1.0/Math.min(angle/90.0, 1)), false);
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
