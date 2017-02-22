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
import com.ctre.CANTalon.VelocityMeasurementPeriod;

import edu.wpi.first.wpilibj.Encoder;

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
	public static SpeedController shooterController;
	//public static CANTalon shooterController;
	public static Encoder shooterEncoder;
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
		driveTrainLeftController = new Talon(2);
		LiveWindow.addActuator("DriveTrain", "LeftController", (Talon) driveTrainLeftController);

		driveTrainRightController = new Talon(1);
		LiveWindow.addActuator("DriveTrain", "RightController", (Talon) driveTrainRightController);

		driveTrainRobotDrive = new RobotDrive(driveTrainLeftController, driveTrainRightController);

		driveTrainRobotDrive.setSafetyEnabled(true);
		driveTrainRobotDrive.setExpiration(0.1);
		driveTrainRobotDrive.setSensitivity(0.5);
		driveTrainRobotDrive.setMaxOutput(getMaxOutput());
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
		// TODO drive train encoder
		driveTrainEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
		
		//driveTrainEncoder.setDistancePerPulse(10);
		//Quadrature 4x
		double distancePerPulse = ((3.5*Math.PI)/250.0)*1.067; // distance in inches multiplied by ratio error factor
		//if because this is a 4x encoder that I need to divide by 1000 or 250
		driveTrainEncoder.setDistancePerPulse(distancePerPulse);
		double encoderValue = driveTrainEncoder.getDistance();
		//SmartDashboard.putNumber("DriveTrainEncoder", encoderValue );
		
		
	}
	
	private static void initShooter() {
		shooterSingulatorController = new Talon(0);
		LiveWindow.addActuator("Shooter", "SingulatorController", (Talon) shooterSingulatorController);
		
		shooterRegulatorController = new Talon(3);
		LiveWindow.addActuator("Shooter", "RegulatorController", (Talon) shooterRegulatorController);
		
		shooterEncoder = new Encoder(4, 5, false, Encoder.EncodingType.k1X);
		shooterEncoder.setDistancePerPulse(360 / 20);
		shooterEncoder.setSamplesToAverage(10);
		
		shooterController = new Talon(6);
		shooterController.setInverted(true);
		
		/*
		shooterController = new CANTalon(0);
		shooterController.reverseSensor(false);
		shooterController.reverseOutput(true);
		shooterController.configNominalOutputVoltage(+0.0, -0.0);
		shooterController.configPeakOutputVoltage(+12.0, -12.0);
		shooterController.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		shooterController.configEncoderCodesPerRev(20);
		shooterController.setProfile(0);
		shooterController.setPosition(0);
		shooterController.SetVelocityMeasurementPeriod(VelocityMeasurementPeriod.Period_100Ms);
		shooterController.SetVelocityMeasurementWindow(64);
		shooterController.setAllowableClosedLoopErr(1);
		
		// Must be true!!
		shooterController.enableBrakeMode(true);
		shooterController.enableLimitSwitch(false, false);
		shooterController.enableControl();
		shooterController.enable();
		//shooterController.setVoltageRampRate(12);
		 */
	}
	
	private static void initGearAssembly() {
		// TODO initialize gear components

		
		gearRelease = new Solenoid( 0 ) ;
		//gearRelease.set(true);
		//gearRelease.set(false);
		/*
		gearRotatorController = new Talon( 5 ) ;
		
		gearSpokeDetectorOne = new DigitalInput(   ) ;
		gearSpokeDetectorTwo = new DigitalInput(   ) ;
		
		//public static SpeedController gearRotatorController;
		//public static DigitalInput gearSpokeDetectorOne;
		//public static DigitalInput gearSpokeDetectorTwo;
		*/
	}
	
	private static void initWinch() {
		winchController = new Talon(4);
		LiveWindow.addActuator("Winch", "WinchController", (Talon) winchController);
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
