package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Red_Left_PlaceGear extends CommandGroup {

	public Red_Left_PlaceGear() {
		addSequential(new DriveDistance(86)); // 88
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.2));
		addSequential(new RotateByAngle(60, 1200));
		addParallel(new StopDrive());
		
		addSequential(new CenterOnHook());
		
		addSequential(new DriveDistance(19), 2); // 19
		addParallel(new StopDrive());
		
		addSequential(new ReleaseGearAndReverse());
	}

}
