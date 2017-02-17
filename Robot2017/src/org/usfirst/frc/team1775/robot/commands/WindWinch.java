package org.usfirst.frc.team1775.robot.commands;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.WinchSubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class WindWinch extends Command {

	public static WinchSubsystem winch = Robot.winch;
	
	public WindWinch() {
		requires(winch);
	}
	
	protected void execute() {
		winch.wind();
	}
	
	@Override
	protected boolean isFinished() {
		return !winch.isAtLimit();
	}
	
	@Override
	public synchronized void cancel() {
		winch.stop();
		super.cancel();
	}

	@Override
	protected void interrupted() {
		winch.stop();
	}
}
