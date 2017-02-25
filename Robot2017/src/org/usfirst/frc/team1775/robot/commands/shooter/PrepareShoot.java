package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PrepareShoot extends Command {

	public static ShooterSubsystem shooter = Robot.shooter;
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	public PrepareShoot() {
		requires(Robot.shooter);
	}
	
	@Override
	protected void initialize() {
		SmartDashboard.putData(shooter);
		SmartDashboard.putData(driveTrain);
		
		notifyReady();
	}
	
	protected void execute() {
		
		Robot.shooter.runSingulator();
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void interrupted() {
		Robot.shooter.stop();
	}

	private void notifyReady() {
		Robot.oi.driverJoystick.setRumble(RumbleType.kRightRumble, 1);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Robot.oi.driverJoystick.setRumble(RumbleType.kRightRumble, 0);
	}
}
