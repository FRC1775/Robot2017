package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveAcrossBaseline extends CommandGroup {

	public DriveAcrossBaseline() {
		addSequential(new DriveDistance(50));
	}
	
}
