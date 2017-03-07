package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.gearassembly.CloseGear;
import org.usfirst.frc.team1775.robot.commands.gearassembly.ReleaseGear;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Left_PlaceGear extends CommandGroup {

	public Left_PlaceGear() {
		this(false);
	}
	
	public Left_PlaceGear(boolean changeCamera) {
		addSequential(new DriveDistance(68), 3);
		addSequential(new Wait(400));
		addSequential(new RotateByAngle(43), 1.5);
		addSequential(new Wait(300));
		addSequential(new RotateByAngle(), 1);
		addSequential(new Wait(300));
		addSequential(new RotateByAngle(), 1);
		if (changeCamera) {
			addSequential(new ChangeCamera());
		}
		addSequential(new DriveDistance(22), 1.5);
		addSequential(new ReleaseGear(true));
		addSequential(new Wait(1000));
		addSequential(new DriveDistance(-18), 1.5);
		addSequential(new CloseGear());
	}

}
