package org.usfirst.frc.team1775.robot.commands.oi;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LeftJoystickRumble extends Command {

	private String key;
	private double amount;
	
	public LeftJoystickRumble(double amount) {
		this.amount = amount;
	}
	
	public LeftJoystickRumble(String key, double backup) {
		this.key = key;
		this.amount = backup;
	}
	
	public void execute() {
		Robot.operatorInterface.rumbleLeft(getAmount());
	}
	
	@Override
	public void interrupted() {
		Robot.operatorInterface.rumbleLeft(0);
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	private double getAmount() {
		if (key != null) {
			double prefAmount = Preferences.getInstance().getDouble(key, amount);
			SmartDashboard.putNumber(key, prefAmount);
			return prefAmount;
		}
		
		return amount;
	}
	
}
