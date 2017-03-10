package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.shooter.Shoot;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Red_Right_Shoot extends CommandGroup {

	public Red_Right_Shoot() {
		addSequential(new DriveDistance(60), 3);
		addSequential(new RotateByAngle(-110), 2);
		addSequential(new Wait(200));
		addSequential(new RotateByAngle(), 1);
		addSequential(new Wait(200));
		addSequential(new RotateByAngle(), 1);
		//addSequential(new RotateByAngle());
		addSequential(new Shoot(), 5);
	}
}
