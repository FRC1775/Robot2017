package org.usfirst.frc.team1775.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

public class DriveDistance extends Command {
	
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	private double distance = 0;

	private boolean killScheduler = true;
	
	public DriveDistance() {
		requires(driveTrain);
	}
	
	public DriveDistance(double distance) {
		requires(driveTrain);
		
		this.distance = distance;
	}
	
	public DriveDistance(double distance, boolean killScheduler, int timeout) {
		requires(driveTrain);
		this.distance = distance;
		this.killScheduler = killScheduler;
		setTimeout((double)timeout / 1000.0);
	}
	
	
	@Override
	protected void initialize() {
		SmartDashboard.putData(driveTrain);
		
		if (distance == 0) {
			driveTrain.setDriveDistance(Preferences.getInstance().getDouble("distance", 25.0));
		} else {
			driveTrain.setDriveDistance(distance);
		}
	}

	@Override
	protected void execute() {
		driveTrain.driveDistance(killScheduler);
	}

	@Override
	protected void interrupted() {
		driveTrain.stop();
	}
	
	@Override
	protected boolean isFinished() {
		return isTimedOut() || driveTrain.isFinished();
	}

}
