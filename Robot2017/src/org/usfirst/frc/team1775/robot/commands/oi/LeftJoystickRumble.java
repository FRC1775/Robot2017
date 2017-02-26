package org.usfirst.frc.team1775.robot.commands.oi;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;

public class LeftJoystickRumble extends Command {

	private double amount;
	
	public LeftJoystickRumble(double amount) {
		this.amount = amount;
	}
	
	public void execute() {
		Robot.oi.driverJoystick.setRumble(RumbleType.kLeftRumble, amount);
	}
	
	@Override
	public void interrupted() {
		Robot.oi.driverJoystick.setRumble(RumbleType.kLeftRumble, 0);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
