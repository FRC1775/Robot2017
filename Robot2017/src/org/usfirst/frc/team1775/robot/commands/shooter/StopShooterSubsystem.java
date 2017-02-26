package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StopShooterSubsystem extends Command {

	public StopShooterSubsystem() {
		requires(Robot.shooter);
	}

	protected void execute() {
		Robot.shooter.stop();
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.shooter.isStopped();
	}
	
}
