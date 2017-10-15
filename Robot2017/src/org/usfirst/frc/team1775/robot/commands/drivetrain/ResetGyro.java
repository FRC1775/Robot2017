package org.usfirst.frc.team1775.robot.commands.drivetrain;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ResetGyro extends InstantCommand {

	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	public void execute() {
		driveTrain.resetGyro();
	}

}
