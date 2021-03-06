package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Red_Right_PlaceGear extends CommandGroup {

	public Red_Right_PlaceGear() {
		addSequential(new DriveDistance(AutoConstants.RED_RIGHT_PLACE_GEAR_DRIVE_1)); //GABE	i changed this from 74 to 77, a 3 inch increase to adjust at competition gkc
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.2));
		addSequential(new RotateByAngle(-55)); //GABE i changed this -44 to -48, gkc
		addParallel(new StopDrive());
		
		addSequential(new CenterOnHook());
		
		addSequential(new DriveDistance(AutoConstants.RED_RIGHT_PLACE_GEAR_DRIVE_2), 2);
		addParallel(new StopDrive());
		
		addSequential(new ReleaseGearAndReverse());
	}
	
}
