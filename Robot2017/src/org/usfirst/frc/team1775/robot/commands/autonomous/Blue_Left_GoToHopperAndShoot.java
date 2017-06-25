package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;
import org.usfirst.frc.team1775.robot.commands.shooter.StartRegulator;
import org.usfirst.frc.team1775.robot.commands.shooter.StartShooter;
import org.usfirst.frc.team1775.robot.commands.shooter.StartSingulator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Left_GoToHopperAndShoot extends CommandGroup {

	public Blue_Left_GoToHopperAndShoot() {
		addSequential(new DriveDistance(78));    //78.5 = distance to infront of hopper - 26.6= distance of 1 container - 3 more inches to hit thing
		addParallel(new StopDrive());

		addSequential(new WaitCommand(.2));
		addSequential(new RotateByAngle(-90, 2400)); // turn left
		addParallel(new StopDrive());

		addSequential(new WaitCommand(.1));
		addSequential(new DriveDistance(44), 3.55); //I'm estimating, could be less, probably not more
		addParallel(new StopDrive());

		addSequential(new WaitCommand(2.25)); // let balls enter robot FOR 3 SECONDS
		addSequential(new DriveDistance(-18)); //backup 13 inches
		addParallel(new StopDrive());
		addSequential(new StartShooter(1950));  //NEED to change RPM's only for auto
		addSequential(new WaitCommand(.1));
		
		addSequential(new RotateByAngle(-67, 1320)); //Angle NEEDS to be CHANGED!! RIGHT HERE, in the last few degrees, i want the shooter to start shooting 
							    //so maybe have Singulator AND Regulator go?
		addSequential(new StartSingulator());
		addSequential(new StartRegulator());
	}
}
