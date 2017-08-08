package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;
import org.usfirst.frc.team1775.robot.commands.shooter.StartRegulator;
import org.usfirst.frc.team1775.robot.commands.shooter.StartShooter;
import org.usfirst.frc.team1775.robot.commands.shooter.StartSingulator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Left_Shoot extends CommandGroup {

	public Blue_Left_Shoot() {
		addSequential(new StartShooter());
		addSequential(new DriveDistance(65), 2);
		addParallel(new StopDrive());
		
		addSequential(new RotateByAngle(-84), 3); //GABE changed this from -82 to -84
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.3));
		addSequential(new DriveDistance(4), 1);
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.3));
		addSequential(new StartSingulator());
		addSequential(new StartRegulator());
	}
}
