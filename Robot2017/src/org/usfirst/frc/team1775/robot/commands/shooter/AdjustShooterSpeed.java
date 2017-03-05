package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class AdjustShooterSpeed extends InstantCommand {
	public static final int DEFAULT_SHOOTER_RPM_CHANGE_AMOUNT = 25;
	
	public enum ChangeDirection {
		Up, Down
	}

	public static ShooterSubsystem shooter = Robot.shooter;
	
	private boolean usePreference;
	private int rpmChange;
	private ChangeDirection changeDirection;
	
	// DO NOT Require shooter subsystem - if you do, it will kill shooting when changing speed
	
	public AdjustShooterSpeed(int rpmChange) {
		this.rpmChange = rpmChange;
	}
	
	public AdjustShooterSpeed(ChangeDirection changeDirection) {
		this.changeDirection = changeDirection;
		this.usePreference = true;
	}
	
	public void execute() {
		shooter.adjustShooter(getRpmChange());
	}
	
	private int getRpmChange() {
		if (usePreference) {
			int amount = Preferences.getInstance().getInt("Shooter.rpmChangeAmount", DEFAULT_SHOOTER_RPM_CHANGE_AMOUNT);
			if (changeDirection == ChangeDirection.Up) {
				return amount;
			}
			return -amount;
		}
		
		return rpmChange;
	}
}
