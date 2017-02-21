package org.usfirst.frc.team1775.robot.commands;
import org.usfirst.frc.team1775.robot.*;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.time.Clock;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

public class DriveDistance extends Command {
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	public DriveDistance() {
		requires(driveTrain);
	}

	@Override
	protected void initialize() {
		//SmartDashboard.putData(driveTrain);
	}

	@Override
	protected void execute() {
			//RobotMap.driveTrainEncoder.get
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
