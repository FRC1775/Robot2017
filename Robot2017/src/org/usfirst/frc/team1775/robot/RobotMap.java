package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// Gyro
	public static Gyro gyro;
	
	// Drive Train
	public static SpeedController driveTrainLeftController;
	public static SpeedController driveTrainRightController;
	public static RobotDrive driveTrainRobotDrive;
	public static Encoder driveTrainEncoder;
	
	// Shooter
	public static SpeedController shooterSingulatorController;
	public static SpeedController shooterRegulatorController;
	public static CANTalon shooterController;
	
	// Gear Assembly
	public static Solenoid gearRelease;
	public static SpeedController gearRotatorController;
	public static DigitalInput gearSpokeDetectorOne;
	public static DigitalInput gearSpokeDetectorTwo;
	
	// Winch
	public static SpeedController winchController;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	public static void init() {
		initGyro();
		initDriveTrain();
		initShooter();
		initGearAssembly();
		initWinch();
	}
	
	private static void initGyro() {
		gyro = new ADXRS450_Gyro();
		gyro.calibrate();
	}
	
	private static void initDriveTrain() {
		driveTrainLeftController = new Talon(getDriveTrainLeftPWM());
		LiveWindow.addActuator("DriveTrain", "LeftController", (Talon) driveTrainLeftController);

		driveTrainRightController = new Talon(getDriveTrainRightPWM());
		LiveWindow.addActuator("DriveTrain", "RightController", (Talon) driveTrainRightController);

		driveTrainRobotDrive = new RobotDrive(driveTrainLeftController, driveTrainRightController);

		driveTrainRobotDrive.setSafetyEnabled(true);
		driveTrainRobotDrive.setExpiration(0.1);
		driveTrainRobotDrive.setSensitivity(0.5);
		driveTrainRobotDrive.setMaxOutput(getMaxOutput());
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
		
		// TODO drive train encoder
	}
	
	private static void initShooter() {
		shooterSingulatorController = new Talon(2);
		LiveWindow.addActuator("Shooter", "SingulatorController", (Talon) shooterSingulatorController);
		
		shooterRegulatorController = new Talon(4);
		LiveWindow.addActuator("Shooter", "RegulatorController", (Talon) shooterRegulatorController);
		
		shooterController = new CANTalon(0);
		//shooterController.changeControlMode(TalonControlMode.PercentVbus);
		shooterController.reverseSensor(false);
		shooterController.reverseOutput(true);
		shooterController.configNominalOutputVoltage(+0.0, -0.0);
		shooterController.configPeakOutputVoltage(+12.0, -12.0);
		shooterController.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooterController.configEncoderCodesPerRev(20);
		shooterController.setProfile(0);
		shooterController.setF(Preferences.getInstance().getDouble("Shooter.F", 2300));
		shooterController.setP(Preferences.getInstance().getDouble("Shooter.P", 1));
		shooterController.setI(Preferences.getInstance().getDouble("Shooter.I", 0));
		shooterController.setD(Preferences.getInstance().getDouble("Shooter.D", 0));
		//shooterController.clearIAccum();
		
		// Must be true!!
		shooterController.enableBrakeMode(true);
		shooterController.enableLimitSwitch(false, false);
		shooterController.enableForwardSoftLimit(false);
		shooterController.enableReverseSoftLimit(false);
		//shooterController.enableZeroSensorPositionOnForwardLimit(false);
		//shooterController.enableZeroSensorPositionOnReverseLimit(false);
		shooterController.enableControl();
		//shooterController.setVoltageRampRate(12);
		shooterController.enable();
	}
	
	private static void initGearAssembly() {
		// TODO initialize gear components
		// PWM 3
	}
	
	private static void initWinch() {
		winchController = new Talon(5);
		//LiveWindow.addActuator("Winch", "WinchController", (Talon) winchController);
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
	
	private static int getDriveTrainLeftPWM() {
		return Preferences.getInstance().getInt("DriveTrain.leftPWM", 0);
	}
	
	private static int getDriveTrainRightPWM() {
		return Preferences.getInstance().getInt("DriveTrain.rightPWM", 1);
	}
}
