package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.shooter.AdjustShooterSpeed;
import org.usfirst.frc.team1775.robot.commands.shooter.StartRegulator;
import org.usfirst.frc.team1775.robot.commands.shooter.StartShooter;
import org.usfirst.frc.team1775.robot.commands.shooter.StartSingulator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Middle_Shoot extends CommandGroup {

	public Blue_Middle_Shoot() {
		addSequential(new StartShooter());
		addSequential(new DriveDistance(20), 2);
		addSequential(new RotateByAngle(-14), 1);
		addSequential(new WaitCommand(1.500));
		addSequential(new RotateByAngle(), 1);
		addSequential(new WaitCommand(.500));
		addSequential(new RotateByAngle(), 1);
		addSequential(new WaitCommand(.500));
		addSequential(new AdjustShooterSpeed(), 1);
		addSequential(new StartSingulator());
		addSequential(new WaitCommand(.200));
		addSequential(new StartRegulator());
		addSequential(new WaitCommand(3.500));
		addSequential(new RotateByAngle(100));
		addSequential(new DriveDistance(60));
	}
}
