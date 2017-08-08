package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class GearUp extends Command {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	public GearUp() {
		requires(gearAssembly);
	}
	
	@Override
	protected void execute() {
		gearAssembly.up();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}
	
}
