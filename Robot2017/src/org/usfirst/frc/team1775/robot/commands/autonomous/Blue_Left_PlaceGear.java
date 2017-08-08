package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Left_PlaceGear extends CommandGroup {

	public Blue_Left_PlaceGear() {
		this(false);
	}
	
	public Blue_Left_PlaceGear(boolean changeCamera) {
		addSequential(new DriveDistance(77));
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.2));
		addSequential(new RotateByAngle(48, 1500));
		addParallel(new StopDrive());
		
		addSequential(new CenterOnHook());
		
		addSequential(new DriveDistance(13), 1.25);
		addParallel(new StopDrive());
		
		addSequential(new ReleaseGearAndReverse());
	}

}
