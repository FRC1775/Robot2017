package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	public final static boolean IS_COMPETITION_BOT = false;
	
	/*
	 * 
	 * Competition Robot Channel Configuration
	 * 
	 */
	
	public final static int COMPETITION_DRIVE_TRAIN_LEFT_CONTROLLER_PWM_CHANNEL = 2;
	public final static int COMPETITION_DRIVE_TRAIN_RIGHT_CONTROLLER_PWM_CHANNEL = 1;
	public final static int COMPETITION_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_A = 2;
	public final static int COMPETITION_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_B = 3;
	
	public final static int COMPETITION_SHOOTER_SINGULATOR_CONTROLLER_PWM_CHANNEL = 0;
	public final static int COMPETITION_SHOOTER_REGULATOR_CONTROLLER_PWM_CHANNEL = 3;
	public final static int COMPETITION_SHOOTER_CONTROLLER_PWM_CHANNEL = 6;
	public final static int COMPETITION_SHOOTER_ENCODER_DIO_CHANNEL_A = 4;
	public final static int COMPETITION_SHOOTER_ENCODER_DIO_CHANNEL_B = 5;
	
	public final static int COMPETITION_GEAR_RELEASE_PCM_CHANNEL = 0;
	public final static int COMPETITION_WINCH_CONTROLLER_PWM_CHANNEL = 4;
	
	/*
	 * 
	 * Practice Robot Channel Configuration
	 * 
	 */
	
	public final static int PRACTICE_DRIVE_TRAIN_LEFT_CONTROLLER_PWM_CHANNEL = 2;
	public final static int PRACTICE_DRIVE_TRAIN_RIGHT_CONTROLLER_PWM_CHANNEL = 1;
	public final static int PRACTICE_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_A = 2;
	public final static int PRACTICE_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_B = 3;
	
	public final static int PRACTICE_SHOOTER_SINGULATOR_CONTROLLER_PWM_CHANNEL = 0;
	public final static int PRACTICE_SHOOTER_REGULATOR_CONTROLLER_PWM_CHANNEL = 3;
	public final static int PRACTICE_SHOOTER_CONTROLLER_PWM_CHANNEL = 6;
	public final static int PRACTICE_SHOOTER_ENCODER_DIO_CHANNEL_A = 4;
	public final static int PRACTICE_SHOOTER_ENCODER_DIO_CHANNEL_B = 5;
	
	public final static int PRACTICE_GEAR_RELEASE_PCM_CHANNEL = 0;
	public final static int PRACTICE_WINCH_CONTROLLER_PWM_CHANNEL = 4;
	
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
	public static Encoder shooterEncoder;
	//public static CANTalon shooterController;
	
	// Gear Assembly
	public static Solenoid gearRelease;
	// public static SpeedController gearRotatorController;
	// public static DigitalInput gearSpokeDetectorOne;
	// public static DigitalInput gearSpokeDetectorTwo;
	
	// Winch
	public static SpeedController winchController;
	
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
		driveTrainLeftController = new Talon(getDriveTrainLeftControllerPwmChannel());
		LiveWindow.addActuator("Drive Train", "LeftController", (Talon) driveTrainLeftController);

		driveTrainRightController = new Talon(getDriveTrainRightControllerPwmChannel());
		LiveWindow.addActuator("Drive Train", "RightController", (Talon) driveTrainRightController);

		driveTrainRobotDrive = new RobotDrive(driveTrainLeftController, driveTrainRightController);

		driveTrainRobotDrive.setSafetyEnabled(true);
		driveTrainRobotDrive.setExpiration(0.1);
		driveTrainRobotDrive.setSensitivity(0.5);
		driveTrainRobotDrive.setMaxOutput(1);
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
		// Drive train encoder
		driveTrainEncoder = new Encoder(getDriveTrainEncoderDioChannelA(), getDriveTrainEncoderDioChannelB(), false, Encoder.EncodingType.k4X);
		
		//driveTrainEncoder.setDistancePerPulse(10);
		//Quadrature 4x
		double distancePerPulse = ((3.5*Math.PI)/250.0)*1.067; // distance in inches multiplied by ratio error factor
		//if because this is a 4x encoder that I need to divide by 1000 or 250
		driveTrainEncoder.setDistancePerPulse(distancePerPulse);
		//double encoderValue = driveTrainEncoder.getDistance();
		//SmartDashboard.putNumber("DriveTrainEncoder", encoderValue );
	}
	
	private static void initShooter() {
		shooterSingulatorController = new Talon(getShooterSingulatorControllerPwmChannel());
		LiveWindow.addActuator("Shooter", "SingulatorController", (Talon) shooterSingulatorController);
		
		shooterRegulatorController = new Talon(getShooterRegulatorControllerPwmChannel());
		LiveWindow.addActuator("Shooter", "RegulatorController", (Talon) shooterRegulatorController);
		
		shooterController = new Talon(getShooterControllerPwmChannel());
		LiveWindow.addActuator("Shooter", "Controller", (Talon) shooterController);
		shooterController.setInverted(true);
		
		shooterEncoder = new Encoder(getShooterEncoderDioChannelA(), getShooterEncoderDioChannelB(), false, Encoder.EncodingType.k1X);
		shooterEncoder.setDistancePerPulse(360 / 20);
		shooterEncoder.setSamplesToAverage(10);
		
		/*
		 * Talon SRX which we aren't using now.
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
		gearRelease = new Solenoid(getGearReleasePcmChannel());
		LiveWindow.addActuator("Gear Assembly", "Release", gearRelease);
	}
	
	private static void initWinch() {
		winchController = new Talon(getWinchControllerPwmChannel());
		LiveWindow.addActuator("Winch", "Controller", (Talon) winchController);
	}
	
	private static int getDriveTrainLeftControllerPwmChannel() {
		if (isCompetitionBot()) {
			return COMPETITION_DRIVE_TRAIN_LEFT_CONTROLLER_PWM_CHANNEL;
		} else {
			return PRACTICE_DRIVE_TRAIN_LEFT_CONTROLLER_PWM_CHANNEL;
		}
	}
	
	private static int getDriveTrainRightControllerPwmChannel() {
		if (isCompetitionBot()) {
			return COMPETITION_DRIVE_TRAIN_RIGHT_CONTROLLER_PWM_CHANNEL;
		} else {
			return PRACTICE_DRIVE_TRAIN_RIGHT_CONTROLLER_PWM_CHANNEL;
		}
	}
	
	private static int getDriveTrainEncoderDioChannelA() {
		if (isCompetitionBot()) {
			return COMPETITION_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_A;
		} else {
			return PRACTICE_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_A;
		}
	}
	
	private static int getDriveTrainEncoderDioChannelB() {
		if (isCompetitionBot()) {
			return COMPETITION_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_B;
		} else {
			return PRACTICE_DRIVE_TRAIN_ENCODER_DIO_CHANNEL_B;
		}
	}
	
	private static int getShooterSingulatorControllerPwmChannel() {
		if (isCompetitionBot()) {
			return COMPETITION_SHOOTER_SINGULATOR_CONTROLLER_PWM_CHANNEL;
		} else {
			return PRACTICE_SHOOTER_SINGULATOR_CONTROLLER_PWM_CHANNEL;
		}
	}
	
	private static int getShooterRegulatorControllerPwmChannel() {
		if (isCompetitionBot()) {
			return COMPETITION_SHOOTER_REGULATOR_CONTROLLER_PWM_CHANNEL;
		} else {
			return PRACTICE_SHOOTER_REGULATOR_CONTROLLER_PWM_CHANNEL;
		}
	}
	
	private static int getShooterControllerPwmChannel() {
		if (isCompetitionBot()) {
			return COMPETITION_SHOOTER_CONTROLLER_PWM_CHANNEL;
		} else {
			return PRACTICE_SHOOTER_CONTROLLER_PWM_CHANNEL;
		}
	}
	
	private static int getShooterEncoderDioChannelA() {
		if (isCompetitionBot()) {
			return COMPETITION_SHOOTER_ENCODER_DIO_CHANNEL_A;
		} else {
			return PRACTICE_SHOOTER_ENCODER_DIO_CHANNEL_A;
		}
	}
	
	private static int getShooterEncoderDioChannelB() {
		if (isCompetitionBot()) {
			return COMPETITION_SHOOTER_ENCODER_DIO_CHANNEL_B;
		} else {
			return PRACTICE_SHOOTER_ENCODER_DIO_CHANNEL_B;
		}
	}
	
	private static int getGearReleasePcmChannel() {
		if (isCompetitionBot()) {
			return COMPETITION_GEAR_RELEASE_PCM_CHANNEL;
		} else {
			return PRACTICE_GEAR_RELEASE_PCM_CHANNEL;
		}
	}

	private static int getWinchControllerPwmChannel() {
		if (isCompetitionBot()) {
			return COMPETITION_WINCH_CONTROLLER_PWM_CHANNEL;
		} else {
			return PRACTICE_WINCH_CONTROLLER_PWM_CHANNEL;
		}
	}
	
	private static boolean isCompetitionBot() {
		return IS_COMPETITION_BOT;
	}
}
