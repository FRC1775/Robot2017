package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class GearDown extends TimedCommand {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	public GearDown() {
		super(0.5);
		requires(gearAssembly);
	}
	
	@Override
	protected void execute() {
		gearAssembly.down();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}
	
}
