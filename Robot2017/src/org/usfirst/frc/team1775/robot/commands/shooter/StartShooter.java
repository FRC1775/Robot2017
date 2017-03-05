package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Cameras;
import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StartShooter extends Command {

	private int rpm;
	
	public StartShooter() {
		requires(Robot.shooter);
	}
	
	public void initialize() {
		this.rpm = (int)(294.73 * Cameras.distance * 0.025 + 1060); // Calculate rpms from distance in inches
	}

	protected void execute() {
		Robot.shooter.startShooter(rpm);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.shooter.isShooterReady();
	}
}
