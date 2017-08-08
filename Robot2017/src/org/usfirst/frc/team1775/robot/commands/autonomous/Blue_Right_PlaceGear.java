package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Right_PlaceGear extends CommandGroup {

	public Blue_Right_PlaceGear() {
		addSequential(new DriveDistance(AutoConstants.BLUE_RIGHT_PLACE_GEAR_DRIVE_1));
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.2));
		addSequential(new RotateByAngle(-48, 1500));
		addParallel(new StopDrive());
		
		addSequential(new CenterOnHook());
		
		addSequential(new DriveDistance(AutoConstants.BLUE_RIGHT_PLACE_GEAR_DRIVE_2), 2);
		addParallel(new StopDrive());
		
		addSequential(new ReleaseGearAndReverse());
	}
	
}
