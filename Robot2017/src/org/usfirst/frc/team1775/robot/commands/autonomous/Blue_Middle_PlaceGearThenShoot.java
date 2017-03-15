package org.usfirst.frc.team1775.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Blue_Middle_PlaceGearThenShoot extends CommandGroup {

	public Blue_Middle_PlaceGearThenShoot() {
		addSequential(new Middle_PlaceGear());
	}
}
