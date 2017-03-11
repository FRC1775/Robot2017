package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.shooter.AdjustShooterSpeed;
import org.usfirst.frc.team1775.robot.commands.shooter.StartRegulator;
import org.usfirst.frc.team1775.robot.commands.shooter.StartSingulator;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Blue_Middle_Shoot extends CommandGroup {

	public Blue_Middle_Shoot() {
		// Change cameras - we should set the starting camera just for that match to be the shooter
		// Drive to shooting position
		// Shoot
		addSequential(new ChangeCamera());
		
		addSequential(new DriveDistance(25), 1);
		addSequential(new RotateByAngle(-75), 1);
		addSequential(new Wait(400));
		addSequential(new DriveDistance(25), 1);
		addSequential(new Wait(400));
		addSequential(new RotateByAngle(), 1.0);
		addSequential(new Wait(400));
		addSequential(new RotateByAngle(), 1);
		addSequential(new Wait(700));
		addSequential(new AdjustShooterSpeed());
		addSequential(new StartSingulator());
		addSequential(new Wait(200));
		addSequential(new StartRegulator());
	}
}
