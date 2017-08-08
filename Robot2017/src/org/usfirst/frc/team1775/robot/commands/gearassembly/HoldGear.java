package org.usfirst.frc.team1775.robot.commands.gearassembly;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;

import edu.wpi.first.wpilibj.command.Command;

public class HoldGear extends Command {
	
	public static GearAssemblySubsystem gearAssembly = Robot.gearAssembly;
	
	public HoldGear() {
		super(1);
		requires(gearAssembly);
	}

	long startTime = 0;
	
	@Override
	protected void execute() {
		if (gearAssembly.isReleasing()) {
			System.out.println("Releasing");
			gearAssembly.release();
			if (startTime == 0) {
				startTime = System.currentTimeMillis();
			}
			if ((System.currentTimeMillis() - startTime) > 500 && !Robot.oi.getAButton()) {
				System.out.println("Done Releasing");
				gearAssembly.stopGrip();
				gearAssembly.hasGear(false);
				gearAssembly.up();
				gearAssembly.isReleasing(false);
				startTime = 0;
			}
		} else if (gearAssembly.isDown() && !gearAssembly.sensesGear()) {
			gearAssembly.runGrip();
		} else if (gearAssembly.isDown() && gearAssembly.sensesGear()) {
			gearAssembly.stopGrip();
			gearAssembly.hasGear(true);
			gearAssembly.up();
		} else if (gearAssembly.hasGear() && !gearAssembly.sensesGear()) {
			gearAssembly.runGrip();
		} else {
			gearAssembly.stopGrip();
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
	
}
