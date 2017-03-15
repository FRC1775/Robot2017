package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class CenterOnHook extends CommandGroup {
	
	public CenterOnHook() {
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.3));
		addSequential(new RotateByAngle(), .5);
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.3));
		addSequential(new RotateByAngle(), .5);
		addParallel(new StopDrive(), .3);
	}
}
