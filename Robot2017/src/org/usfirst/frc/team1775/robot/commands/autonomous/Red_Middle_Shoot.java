package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.shooter.AdjustShooterSpeed;
import org.usfirst.frc.team1775.robot.commands.shooter.StartRegulator;
import org.usfirst.frc.team1775.robot.commands.shooter.StartShooter;
import org.usfirst.frc.team1775.robot.commands.shooter.StartSingulator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Red_Middle_Shoot extends CommandGroup {

	public Red_Middle_Shoot() {
		addSequential(new StartShooter());
		addSequential(new DriveDistance(30), 2);
		addSequential(new RotateByAngle(24), 1);
		addSequential(new WaitCommand(1.500));
		addSequential(new RotateByAngle(), 1);
		addSequential(new WaitCommand(.500));
		addSequential(new RotateByAngle(), 1);
		addSequential(new WaitCommand(.500));
		addSequential(new AdjustShooterSpeed(), 1);
		addSequential(new StartSingulator());
		addSequential(new WaitCommand(.200));
		addSequential(new StartRegulator());
	}
}
