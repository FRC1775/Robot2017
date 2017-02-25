package org.usfirst.frc.team1775.robot.commands;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.command.Command;

//This auto will place the gear on the middle peg starting from the middle position
public class PlaceGear extends Command {
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	public PlaceGear() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.gearAssembly);
		
		//addSequential(new piece);
		//addParallel(new piece);
		//write addParallel the line above the addSequential if you want those 2 to run together
	}
	
	@Override
	protected void execute() {
		System.out.println("test");
		RobotMap.gearRelease.set(true);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		RobotMap.gearRelease.set(false);
	}
}
