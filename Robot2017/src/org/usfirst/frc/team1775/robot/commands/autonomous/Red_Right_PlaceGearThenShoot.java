package org.usfirst.frc.team1775.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Red_Right_PlaceGearThenShoot extends CommandGroup {

	public Red_Right_PlaceGearThenShoot() {
		addSequential(new Red_Right_PlaceGear(true));
		
		// TODO
	}
}
