package org.usfirst.frc.team1775.robot.commands.drivetrain;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ResetGyro extends InstantCommand {

	public void execute() {
		Robot.roboRio.gyro.reset();
	}

}
