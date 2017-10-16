package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WinchSubsystem extends Subsystem {
	public static final double DEFAULT_WINCH_SLOW_SPEED = 0.5;
	public static final double DEFAULT_WINCH_FAST_SPEED = 1.0;
	
	public enum Speed {
		Slow, Fast
	}
	
	private SpeedController winchController;
	
	@Override
	protected void initDefaultCommand() { }
	
	public void init() {
		winchController = new Talon(Robot.roboRio.getWinchControllerPwmChannel());
		LiveWindow.addActuator("Winch", "Controller", (Talon) winchController);
	}

	public void wind(Speed speed) {
		SmartDashboard.putNumber("Winch.slowSpeed", getSlowSpeed());
		SmartDashboard.putNumber("Winch.fastSpeed", getFastSpeed());

		double motorSpeed = speed == Speed.Fast ? getFastSpeed() : getSlowSpeed();
		SmartDashboard.putNumber("Winch.speed", motorSpeed);

		winchController.set(motorSpeed);
	}
	
	public void stop() {
		winchController.stopMotor();
	}
	
	public boolean isAtLimit() {
		// If we add some sort of limit sensor, check it here
		return false;
	}
	
	private double getSlowSpeed() {
		return Preferences.getInstance().getDouble("Winch.slowSpeed", DEFAULT_WINCH_SLOW_SPEED);
	}
	
	private double getFastSpeed() {
		return Preferences.getInstance().getDouble("Winch.fastSpeed", DEFAULT_WINCH_FAST_SPEED);
	}
}
