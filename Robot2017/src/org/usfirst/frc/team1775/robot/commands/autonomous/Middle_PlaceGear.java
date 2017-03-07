package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.gearassembly.CloseGear;
import org.usfirst.frc.team1775.robot.commands.gearassembly.ReleaseGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Middle_PlaceGear extends CommandGroup {

	public Middle_PlaceGear() {
		// Drive forward to peg
		addSequential(new DriveDistance(40), 3);
		addSequential(new Wait(600));
		addSequential(new RotateByAngle(), 1);
		addSequential(new Wait(200));
		addSequential(new RotateByAngle(), 1);
		addSequential(new DriveDistance(24), 1.5);
		addSequential(new ReleaseGear(true));
		addSequential(new Wait(1000));
		addSequential(new DriveDistance(-18), 1.5);
		addSequential(new CloseGear());
		// Release Gear
		// Reverse from peg
		// Close Gear
	}
}
