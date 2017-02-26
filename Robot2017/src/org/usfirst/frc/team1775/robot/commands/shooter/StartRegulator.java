package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StartRegulator extends Command {

	private double speed;
	
	public StartRegulator(double speed) {
		requires(Robot.shooter);
		
		this.speed = speed;
	}

	protected void execute() {
		Robot.shooter.startRegulator(speed);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}

}
