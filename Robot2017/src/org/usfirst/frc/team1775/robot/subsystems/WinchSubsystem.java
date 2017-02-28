package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WinchSubsystem extends Subsystem {
	
	public static final double DEFAULT_WINCH_SLOW_SPEED = 0.5;
	public static final double DEFAULT_WINCH_FAST_SPEED = 1.0;

	@Override
	protected void initDefaultCommand() { }

	public void wind(boolean goFast) {
		double speed = getSlowSpeed();
		
		if (goFast) {
			speed = getFastSpeed();
		}
		
		SmartDashboard.putNumber("Winch.speed", speed);
		
		RobotMap.winchController.set(speed);
	}
	
	public void stop() {
		RobotMap.winchController.stopMotor();
	}
	
	public boolean isAtLimit() {
		// TODO If we add some sort of limit sensor, check it here
		return false;
	}
	
	private double getSlowSpeed() {
		return Preferences.getInstance().getDouble("Winch.slowSpeed", DEFAULT_WINCH_SLOW_SPEED);
	}
	
	private double getFastSpeed() {
		return Preferences.getInstance().getDouble("Winch.fastSpeed", DEFAULT_WINCH_FAST_SPEED);
	}
}
