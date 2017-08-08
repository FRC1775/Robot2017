package org.usfirst.frc.team1775.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Blue_Left_PlaceGearThenShoot extends CommandGroup {

	public Blue_Left_PlaceGearThenShoot() {
		addSequential(new Blue_Left_PlaceGear(true));
		
		// TODO
	}
}
