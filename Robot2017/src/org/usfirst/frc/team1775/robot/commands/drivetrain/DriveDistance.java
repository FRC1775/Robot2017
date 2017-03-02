package org.usfirst.frc.team1775.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1775.robot.Cameras;
import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

public class DriveDistance extends Command {
	
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	private double distance;
	
	public DriveDistance() {
		requires(driveTrain);
	}
	
	public DriveDistance(double distance) {
		requires(driveTrain);
		
		this.distance = distance;
	}

	@Override
	protected void initialize() {
		SmartDashboard.putData(driveTrain);
		
		System.out.println("HERE");
		driveTrain.setDriveDistance(distance);
	}

	@Override
	protected void execute() {
		driveTrain.driveDistance();
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
