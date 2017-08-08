package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class StartSingulator extends InstantCommand {
	private static final double DEFAULT_SINGULATOR_SPEED = 0.9;

	public static ShooterSubsystem shooter = Robot.shooter;
	
	public StartSingulator() {
		requires(shooter);
	}

	protected void execute() {
		shooter.startSingulator(DEFAULT_SINGULATOR_SPEED);
	}

}
