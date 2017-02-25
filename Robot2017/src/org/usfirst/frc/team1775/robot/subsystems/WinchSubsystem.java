package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.OI;
import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WinchSubsystem extends Subsystem {
	
	public static final double DEFAULT_WINCH_MAX_SPEED = 0.75;

	@Override
	protected void initDefaultCommand() { }

	public void wind() {
		double leftTriggerValue = Robot.oi.driverJoystick.getRawAxis(OI.XBOX_LEFT_TRIGGER);
		double speed = Math.abs(leftTriggerValue) * getMaxSpeed();
		
		SmartDashboard.putNumber("Winch.speed", speed);
		
		RobotMap.winchController.set(speed);
	}
	
	public void stop() {
		RobotMap.winchController.set(0);
	}
	
	public boolean isAtLimit() {
		// TODO If we add some sort of limit sensor, check it here
		return false;
	}
	
	private double getMaxSpeed() {
		return Preferences.getInstance().getDouble("Winch.maxSpeed", DEFAULT_WINCH_MAX_SPEED);
	}
}
