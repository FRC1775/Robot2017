package org.usfirst.frc.team1775.robot.commands.oi;

import org.usfirst.frc.team1775.robot.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RightJoystickRumble extends Command {

	private String key;
	private double amount;
	
	public RightJoystickRumble(double amount) {
		this.amount = amount;
	}
	
	public RightJoystickRumble(String key, double backup) {
		this.key = key;
		this.amount = backup;
	}
	
	public void execute() {
		Robot.oi.rumbleRight(getAmount());
	}
	
	@Override
	public void interrupted() {
		Robot.oi.rumbleRight(0);
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
