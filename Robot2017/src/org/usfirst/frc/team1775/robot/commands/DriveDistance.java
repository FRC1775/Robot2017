package org.usfirst.frc.team1775.robot.commands;
import org.usfirst.frc.team1775.robot.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.time.Clock;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

public class DriveDistance extends Command {
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	private double distance;
	public DriveDistance(double distance) {
		requires(driveTrain);
		this.distance = distance;
	}

	@Override
	protected void initialize() {
		//SmartDashboard.putData(driveTrain);
		SmartDashboard.putData(driveTrain);
		driveTrain.setDriveDistance(distance);
	}

	@Override
	protected void execute() {
		driveTrain.driveToDistance();
	}

	@Override
	protected void interrupted() {
		driveTrain.stop();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
