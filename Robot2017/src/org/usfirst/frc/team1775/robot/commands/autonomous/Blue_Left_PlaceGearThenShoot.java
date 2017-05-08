package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;
import org.usfirst.frc.team1775.robot.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Left_PlaceGearThenShoot extends CommandGroup {

	public Blue_Left_PlaceGearThenShoot() {
		addSequential(new Blue_Left_PlaceGear());
		
		addSequential(new DriveDistance(-51));
		addParallel(new StopDrive());
		
		addSequential(new WaitCommand(.2));
		addSequential(new RotateByAngle(139, 2000));
		addParallel(new StopDrive());
		
		//addSequential(new DriveDistance(0), 2);
		//addParallel(new StopDrive());
		
		//addSequential(new ReleaseGearAndReverse());
		addParallel(new Shoot());
	}
}
