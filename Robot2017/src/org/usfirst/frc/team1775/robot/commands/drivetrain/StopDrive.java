package org.usfirst.frc.team1775.robot.commands.drivetrain;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StopDrive extends Command {

	public StopDrive() {
		requires(Robot.driveTrain);
	}
	
	public void execute() {
		Robot.driveTrain.stop();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
