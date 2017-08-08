package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.drivetrain.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.drivetrain.StopDrive;
import org.usfirst.frc.team1775.robot.commands.shooter.StartRegulator;
import org.usfirst.frc.team1775.robot.commands.shooter.StartShooter;
import org.usfirst.frc.team1775.robot.commands.shooter.StartSingulator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class Blue_Left_GoToHopperAndShootDiag extends CommandGroup {

	public Blue_Left_GoToHopperAndShootDiag() {
   addSequential(new DriveDistance(-117.5));
   addParallel(new StopDrive());
   addSequential(new WaitCommand(.1)); 
   
   addSequential(new RotateByAngle(10, 1500));   // 80deg is over estimate, but it should be fine since there is a wall to stop it
   addParallel(new StopDrive());
   
   addSequential(new StartShooter());
   addSequential(new DriveDistance(20));
   //addSequential(new WaitCommand(2500)); 		//Dont think i need this 
   addSequential(new StartSingulator());
	addSequential(new StartRegulator());
   
		
		
//		addSequential(new DriveDistance(95.5)); //Might need just a little more
//		addParallel(new StopDrive());
//							//if we dont NEED the stop and wait, rather not have it because i'd like to maxamize time shooting
//		
//		addSequential(new WaitCommand(.1)); 
//		addSequential(new RotateByAngle(-69, 2500));   // 80deg is over estimate, but it should be fine since there is a wall to stop it
//		addParallel(new StopDrive());
//
//		addSequential(new WaitCommand(2.3));  //SHOULD adjust number later, depending on how much extra time we just sit there. Wait 2.3 seconds
//		addSequential(new DriveDistance(-13));  // backup 13 inches
//		addParallel(new StopDrive());
//
//		addSequential(new WaitCommand(.2));
//		addSequential(new StartShooter());  //NEED to change RPM's only for auto
//		addSequential(new RotateByAngle(-45, 1300)); //Angle NEEDS to be CHANGED!! RIGHT HERE, in the last few degrees, i want the shooter to start shooting 
//							    //so maybe have Singulator AND Regulator go?
//		addSequential(new StartSingulator());
//		addSequential(new StartRegulator());
	}
}
