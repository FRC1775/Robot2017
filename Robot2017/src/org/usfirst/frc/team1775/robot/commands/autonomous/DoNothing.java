package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DoNothing extends Command {

	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	public DoNothing() {
		requires(driveTrain);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
