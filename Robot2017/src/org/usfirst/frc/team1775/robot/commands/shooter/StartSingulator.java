package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class StartSingulator extends InstantCommand {
	private static final double DEFAULT_SINGULATOR_SPEED = 0.9;

	public static ShooterSubsystem shooter = Robot.shooter;
	
	private boolean usePref;
	private double speed;
	
	public StartSingulator() {
		requires(shooter);
		
		this.usePref = true;
	}
	
	public StartSingulator(double speed) {
		requires(shooter);
		
		this.speed = speed;
	}

	protected void execute() {
		shooter.startSingulator(getSpeed());
	}

	private double getSpeed() {
		if (usePref) {
			return Preferences.getInstance().getDouble("Shooter.singulatorSpeed", DEFAULT_SINGULATOR_SPEED);
		}
		
		return speed;
	}

}
