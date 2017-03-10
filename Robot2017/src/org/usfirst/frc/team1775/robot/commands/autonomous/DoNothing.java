package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class DoNothing extends Command {

	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	public DoNothing() {
		requires(driveTrain);
	}
	
	public void execute() {
		driveTrain.arcadeDrive(0, 0, true, true);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
