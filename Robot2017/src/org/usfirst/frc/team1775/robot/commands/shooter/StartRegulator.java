package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class StartRegulator extends InstantCommand {
	
	private static final double DEFAULT_REGULATOR_SPEED = 0.33;

	public static ShooterSubsystem shooter = Robot.shooter;

	private boolean usePref;
	private double speed;
	
	public StartRegulator() {
		requires(shooter);
		
		this.usePref = true;
	}
	
	public StartRegulator(double speed) {
		this();
		
		this.speed = speed;
	}

	protected void execute() {
		shooter.startRegulator(getSpeed());
	}

	private double getSpeed() {
		if (usePref) {
			return Preferences.getInstance().getDouble("Shooter.regulatorSpeed", DEFAULT_REGULATOR_SPEED);
		}
		
		return speed;
	}

}
