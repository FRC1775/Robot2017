package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ReleaseGear extends Command {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	public ReleaseGear() {
		requires(Robot.gearAssembly);
	}
	
	@Override
	protected void execute() {
		gearAssembly.release();
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
		System.out.println("HEREHERE");
		gearAssembly.reset();
	}
}
