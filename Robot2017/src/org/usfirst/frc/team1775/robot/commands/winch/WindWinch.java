package org.usfirst.frc.team1775.robot.commands.winch;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.WinchSubsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

public class WindWinch extends Command {
	public static final double DEFAULT_WINCH_SPEED_CROSSOVER = 0.8;
	public static final double DEFAULT_SLOW_JOYSTICK_RUMBLE = 0.2;
	public static final double DEFAULT_FAST_JOYSTICK_RUMBLE = 0.8;

	private static WinchSubsystem winch = Robot.winch;
	
	public WindWinch() {
		requires(winch);
	}
	
	protected void execute() {
		if (Robot.operatorInterface.getRightTrigger() > getSpeedCrossover()) {
			Robot.operatorInterface.rumbleBoth(getFastJoystickRumble());
			winch.wind(WinchSubsystem.Speed.Fast);
		} else {
			Robot.operatorInterface.rumbleBoth(getSlowJoystickRumble());
			winch.wind(WinchSubsystem.Speed.Slow);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return winch.isAtLimit();
	}
	
	protected void end() {
		Robot.operatorInterface.rumbleBoth(0);
		winch.stop();
	}

	@Override
	protected void interrupted() {
		Robot.operatorInterface.rumbleBoth(0);
		winch.stop();
	}
	
	public double getSpeedCrossover() {
		return Preferences.getInstance().getDouble("Winch.speedCrossover", DEFAULT_WINCH_SPEED_CROSSOVER);
	}
	
	public double getSlowJoystickRumble() {
		return Preferences.getInstance().getDouble("Winch.slowJoystickRumble", DEFAULT_SLOW_JOYSTICK_RUMBLE);
	}
	
	public double getFastJoystickRumble() {
		return Preferences.getInstance().getDouble("Winch.fastJoystickRumble", DEFAULT_FAST_JOYSTICK_RUMBLE);
	}
	
}
