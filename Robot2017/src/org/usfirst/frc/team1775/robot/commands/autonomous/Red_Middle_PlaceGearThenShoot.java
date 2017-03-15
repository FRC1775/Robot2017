package org.usfirst.frc.team1775.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Red_Middle_PlaceGearThenShoot extends CommandGroup {

	public Red_Middle_PlaceGearThenShoot() {
		addSequential(new Middle_PlaceGear());
	}
}
