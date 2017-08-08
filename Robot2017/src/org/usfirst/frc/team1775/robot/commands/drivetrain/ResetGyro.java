package org.usfirst.frc.team1775.robot.commands.drivetrain;

import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

public class ResetGyro extends Command {

	public void execute() {
		RobotMap.gyro.reset();
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
