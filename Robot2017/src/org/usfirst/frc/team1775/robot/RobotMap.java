package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;
	public static SpeedController driveTrainLeftController;
	public static SpeedController driveTrainRightController;
	public static RobotDrive driveTrainRobotDrive;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static void init() {
		driveTrainLeftController = new Talon(1);
		LiveWindow.addActuator("DriveTrain", "LeftController", (Talon) driveTrainLeftController);

		driveTrainRightController = new Talon(0);
		LiveWindow.addActuator("DriveTrain", "RightController", (Talon) driveTrainRightController);

		driveTrainRobotDrive = new RobotDrive(driveTrainLeftController, driveTrainRightController);

		driveTrainRobotDrive.setSafetyEnabled(true);
		driveTrainRobotDrive.setExpiration(0.1);
		driveTrainRobotDrive.setSensitivity(0.5);
		driveTrainRobotDrive.setMaxOutput(getMaxOutput());
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
	}

	private static double getMaxOutput() {
		double maxOutput = Preferences.getInstance().getDouble("DriveTrain.maxOutput", 1.0);

		if (maxOutput > 1)
			maxOutput = 1.0;
		if (maxOutput < 0)
			maxOutput = 0;
		SmartDashboard.putNumber("DriveTrain.maxOutput", maxOutput);

		return maxOutput;
	}
}
