package org.usfirst.frc.team1775.robot.commands.drivetrain;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RotateByAngle extends Command {
	
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	private double degrees;
	
	public RotateByAngle(double degrees) {
		requires(driveTrain);
		
		this.degrees = degrees;
	}
	
	public void initialize() {
		SmartDashboard.putData(driveTrain);
		
		driveTrain.setRotateByAngle(degrees);
	}

	public void execute() {
		driveTrain.rotateByAngle();
	}

	@Override
	protected void interrupted() {
		driveTrain.stop();
	}
	
	@Override
	protected boolean isFinished() {
		return driveTrain.isFinished();
	}
}
