package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;
import org.usfirst.frc.team1775.robot.commands.gearassembly.CloseGear;
import org.usfirst.frc.team1775.robot.commands.gearassembly.GearDown;
import org.usfirst.frc.team1775.robot.commands.gearassembly.GearUp;
import org.usfirst.frc.team1775.robot.commands.gearassembly.ReleaseGear;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ReleaseGearAndReverse extends CommandGroup {

	public ReleaseGearAndReverse() {
		addParallel(new StopDrive());
		
		//addSequential(new WaitCommand(.4));
		addSequential(new GearDown());
		
		addSequential(new WaitCommand(.1));
		addSequential(new DriveDistance(-18), 1.5);
		
		addSequential(new GearUp());
	}
	
}
