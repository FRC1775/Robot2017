package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StartShooter extends Command {

	private int rpm;
	
	public StartShooter(int rpm) {
		requires(Robot.shooter);
		
		this.rpm = rpm;
	}

	protected void execute() {
		Robot.shooter.startShooter(rpm);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.shooter.isShooterReady();
	}
}
