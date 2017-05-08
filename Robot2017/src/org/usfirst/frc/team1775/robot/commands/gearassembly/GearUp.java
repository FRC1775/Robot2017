package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class GearUp extends InstantCommand {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	public GearUp() {
		requires(gearAssembly);
	}
	
	@Override
	protected void execute() {
		gearAssembly.up();
	}
	
}
