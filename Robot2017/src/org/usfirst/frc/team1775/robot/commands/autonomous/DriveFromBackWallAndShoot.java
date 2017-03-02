package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveFromBackWallAndShoot extends CommandGroup {

	public DriveFromBackWallAndShoot() {
		addSequential(new DriveDistance(60));
		addSequential(new RotateByAngle(-110));
		addSequential(new Wait(200));
		addSequential(new RotateByAngle());
		addSequential(new Wait(200));
		addSequential(new RotateByAngle());
		//addSequential(new RotateByAngle());
		addSequential(new Shoot(), 3);
	}
}
