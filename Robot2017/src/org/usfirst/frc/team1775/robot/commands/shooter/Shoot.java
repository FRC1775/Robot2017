package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.commands.oi.LeftJoystickRumble;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Shoot extends CommandGroup {
	private static final double DEFAULT_JOYSTICK_RUMBLE = 0.3;
	private static final int DEFAULT_SHOOTER_RPM = 2300;
	private static final double DEFAULT_REGULATOR_SPEED = 0.33;
	private static final double DEFAULT_SINGULATOR_SPEED = 0.3;

	public Shoot() {
		addParallel(new LeftJoystickRumble(getJoystickRumble()));
		addSequential(new StartSingulator(getSingulatorSpeed()));
		addSequential(new StartShooter(getShooterRpm()));
		addSequential(new StartRegulator(getRegulatorSpeed()));
	}

	@Override
	protected void interrupted() {
		Robot.shooter.stop();
	}
	
	protected void end() {
		Robot.shooter.stop();
	}
	
	private int getShooterRpm() {
		return Preferences.getInstance().getInt("Shooter.rpm", DEFAULT_SHOOTER_RPM);
	}
	
	private double getSingulatorSpeed() {
		return Preferences.getInstance().getDouble("Shooter.singulatorSpeed", DEFAULT_SINGULATOR_SPEED);
	}
	
	private double getRegulatorSpeed() {
		return Preferences.getInstance().getDouble("Shooter.regulatorSpeed", DEFAULT_REGULATOR_SPEED);
	}
	
	private double getJoystickRumble() {
		return Preferences.getInstance().getDouble("Shooter.joystickRumble", DEFAULT_JOYSTICK_RUMBLE);
	}
}
