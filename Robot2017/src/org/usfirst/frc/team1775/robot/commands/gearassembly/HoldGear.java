package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class HoldGear extends Command {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	public HoldGear() {
		super(0.5);
		requires(gearAssembly);
	}
	
	@Override
	protected void execute() {
		gearAssembly.checkForGear();
		if (gearAssembly.isReleasing() && isTimedOut() && !Robot.oi.getAButton()) {
			gearAssembly.stopFeed();
			gearAssembly.up();
		} else if (gearAssembly.isDown() && gearAssembly.hasGear()) {
			gearAssembly.stopFeed();
			gearAssembly.up();
		} else if (gearAssembly.hasGear() && !gearAssembly.sensesGear()) {
			gearAssembly.gripHeldGear();
		} else {
			gearAssembly.stopFeed();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
	
}
