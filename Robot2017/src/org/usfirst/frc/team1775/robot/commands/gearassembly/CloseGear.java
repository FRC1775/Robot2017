package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CloseGear extends InstantCommand {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	public CloseGear() {
		requires(gearAssembly);
	}

	public void execute() {
		gearAssembly.reset();
	}
	
}
