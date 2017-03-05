package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class ReleaseGear extends Command {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	private boolean keepOpen = false;
	
	public ReleaseGear() {
		requires(gearAssembly);
	}
	
	public ReleaseGear(boolean keepOpen) {
		this();
		
		this.keepOpen = keepOpen;
	}
	
	@Override
	protected void execute() {
		gearAssembly.release();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void interrupted() {
		if (!keepOpen) {
			gearAssembly.reset();
		}
	}
}
