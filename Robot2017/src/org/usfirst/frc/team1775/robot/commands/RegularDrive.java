package org.usfirst.frc.team1775.robot.commands;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RegularDrive extends Command {
	
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	public RegularDrive() {
		requires(driveTrain);
	}
	
	public void initialize() {
		SmartDashboard.putData(driveTrain);
	}

	public void execute() {
		double drive = Robot.oi.joystick1.getRawAxis(1);
    	double rotate = Robot.oi.joystick1.getRawAxis(4);
    	boolean squaredInputs = Preferences.getInstance().getBoolean("DriveTrain.squaredInputs", true);
    	boolean constantRadius = Preferences.getInstance().getBoolean("DriveTrain.constantRadius", true);
    	
    	double rightTrigger = Robot.oi.joystick1.getRawAxis(3);
    	if (rightTrigger >0) {
    		Robot.driveTrain.rotate(-rotate, true);
    	}
    	else {
    		Robot.driveTrain.arcadeDrive(-drive, -rotate, squaredInputs, constantRadius);
    	}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void interrupted() {
		driveTrain.stop();
	}
}
