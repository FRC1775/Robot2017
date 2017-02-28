package org.usfirst.frc.team1775.robot.commands.winch;

import org.usfirst.frc.team1775.robot.OI;
import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.WinchSubsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

public class WindWinch extends Command {
	private static final boolean FAST = true;
	private static final boolean SLOW = false;
	
	public static final double DEFAULT_SLOW_JOYSTICK_RUMBLE = 0.5;
	public static final double DEFAULT_FAST_JOYSTICK_RUMBLE = 1.0;

	private static WinchSubsystem winch = Robot.winch;
	
	public WindWinch() {
		requires(winch);
	}
	
	protected void execute() {
		if (Robot.oi.driverJoystick.getRawAxis(OI.XBOX_RIGHT_TRIGGER) > 0.5) {
			Robot.oi.driverJoystick.setRumble(RumbleType.kRightRumble, getFastJoystickRumble());
			winch.wind(FAST);
		} else {
			Robot.oi.driverJoystick.setRumble(RumbleType.kRightRumble, getSlowJoystickRumble());
			winch.wind(SLOW);
		}
	}
	
	@Override
	protected boolean isFinished() {
		return winch.isAtLimit();
	}
	
	protected void end() {
		Robot.oi.driverJoystick.setRumble(RumbleType.kRightRumble, 0);
		winch.stop();
	}

	@Override
	protected void interrupted() {
		Robot.oi.driverJoystick.setRumble(RumbleType.kRightRumble, 0);
		winch.stop();
	}
	
	public double getSlowJoystickRumble() {
		return Preferences.getInstance().getDouble("Winch.slowJoystickRumble", DEFAULT_SLOW_JOYSTICK_RUMBLE);
	}
	
	public double getFastJoystickRumble() {
		return Preferences.getInstance().getDouble("Winch.fastJoystickRumble", DEFAULT_FAST_JOYSTICK_RUMBLE);
	}
	
}
