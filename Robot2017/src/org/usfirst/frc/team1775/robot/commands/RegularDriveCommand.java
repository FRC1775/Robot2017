package org.usfirst.frc.team1775.robot.commands;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RegularDriveCommand extends Command {
	
	public static DriveTrainSubsystem driveTrain = Robot.driveTrain;
	
	public RegularDriveCommand() {
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
    	
    	//if ((drive <= 0.1 && drive >= -0.1) && (rotate >= 0.2 || rotate <= -0.2)) {
    	//	driveTrain.arcadeDrive(-drive, rotate, squaredInputs, false);
    	//} else {
    		driveTrain.arcadeDrive(-drive, rotate, squaredInputs, constantRadius);
    	//}
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
