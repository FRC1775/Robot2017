package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.OI;
import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.commands.WindWinch;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WinchSubsystem extends Subsystem {

	PowerDistributionPanel pdp;
	
	@Override
	protected void initDefaultCommand() {
		//setDefaultCommand(new WindWinch());
		pdp = new PowerDistributionPanel();
	}

	public void wind() {
		double trigger = Robot.oi.driverJoystick.getRawAxis(OI.XBOX_LEFT_TRIGGER);
		double speed = Math.abs(trigger)*0.75;
		SmartDashboard.putNumber("Winch.speed", speed);
		double current = pdp.getCurrent(4);
		SmartDashboard.putNumber("Winch.current", speed);
		RobotMap.winchController.set(speed);
	}
	
	public void stop() {
		RobotMap.winchController.set(0);
	}
	
	public boolean isAtLimit() {
		// TODO If we add some sort of limit sensor, check it here
		return false;
	}
}
