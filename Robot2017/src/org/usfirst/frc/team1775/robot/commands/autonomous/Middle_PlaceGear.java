package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Middle_PlaceGear extends CommandGroup {

	public Middle_PlaceGear() {
		addSequential(new DriveDistance(67), 4); //60 is what we started with
		addParallel(new StopDrive());
		
		//addSequential(new CenterOnHook());
		
		//addSequential(new WaitCommand(.2));
		//addSequential(new DriveDistance(20), 1.25);
		//addParallel(new StopDrive());
		
		addSequential(new ReleaseGearAndReverse());
	}

}
