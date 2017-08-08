package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Left_PlaceGear extends CommandGroup {

	public Blue_Left_PlaceGear() {
		addSequential(new DriveDistance(AutoConstants.BLUE_LEFT_PLACE_GEAR_DRIVE_1));
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.2));
		addSequential(new RotateByAngle(48)); //GABE i changed this -44 to -48, gkc
		addParallel(new StopDrive());
		
		addSequential(new CenterOnHook());
		
		addSequential(new DriveDistance(AutoConstants.BLUE_LEFT_PLACE_GEAR_DRIVE_2), 2);
		addParallel(new StopDrive());
		
		addSequential(new ReleaseGearAndReverse());
	}

}
