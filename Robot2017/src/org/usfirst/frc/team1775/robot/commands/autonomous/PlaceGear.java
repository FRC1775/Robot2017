package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.gearassembly.ReleaseGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class PlaceGear extends CommandGroup {

	public PlaceGear() {
		// Steps to place a gear
		
		// 1. Find and determine distance / angle to gear peg
		// 2. Navigate to gear peg
		// 3. Once gear is on peg
		// 4. Release gear
		// 5. Reverse a bit to get away from peg
		// 6. Reset gear release
		// 7. Resume normal operation
		addSequential(new DriveDistance(80));
		addSequential(new Wait(100));
		addSequential(new RotateByAngle(45));
		addSequential(new Wait(100));
		addSequential(new DriveDistance(33));
		addSequential(new ReleaseGear(), 4);
		addSequential(new DriveDistance(-13));
		
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
