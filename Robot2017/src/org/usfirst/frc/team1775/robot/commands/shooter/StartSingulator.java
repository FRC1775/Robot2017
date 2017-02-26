package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StartSingulator extends Command {
	
	private double speed;

	public StartSingulator(double speed) {
		requires(Robot.shooter);
		this.speed = speed;
	}

	protected void execute() {
		Robot.shooter.startSingulator(speed);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}
}
