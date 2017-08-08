package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotRio is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public abstract class RoboRio {
	// Gyro
	public ADXRS450_Gyro gyro;
	
	// Drive Train
	public SpeedController driveTrainLeftController;
	public SpeedController driveTrainRightController;
	public RobotDrive driveTrainRobotDrive;
	public Encoder driveTrainEncoder;
	
	// Shooter
	public SpeedController shooterSingulatorController;
	public SpeedController shooterRegulatorController;
	public SpeedController shooterController;
	public Encoder shooterEncoder;
	
	// Gear Assembly
	public Solenoid gearTrayActuator;
	public SpeedController gearFeedController;
	public DigitalInput gearDetector;
	public Relay gearIndicatorRelay;
	
	// Winch
	public SpeedController winchController;

	public void init() {
		initGyro();
		initDriveTrain();
		initShooter();
		initGearAssembly();
		initWinch();
	}
	
	protected void initGyro() {
		gyro = new ADXRS450_Gyro();
		LiveWindow.addSensor("Drive Train", "Gyro", gyro);
	}

	protected void initDriveTrain() {
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
		
		driveTrainEncoder = new Encoder(getDriveTrainEncoderDioChannelA(), getDriveTrainEncoderDioChannelB(), false, Encoder.EncodingType.k1X);
		double distancePerPulse = ((3.19*Math.PI)/250.0);
		driveTrainEncoder.setDistancePerPulse(distancePerPulse);
		LiveWindow.addSensor("Drive Train", "Encoder", driveTrainEncoder);
		
	}
	
	protected void initShooter() {
		shooterSingulatorController = new Talon(getShooterSingulatorControllerPwmChannel());
		LiveWindow.addActuator("Shooter", "SingulatorController", (Talon) shooterSingulatorController);
		
		shooterRegulatorController = new Talon(getShooterRegulatorControllerPwmChannel());
		LiveWindow.addActuator("Shooter", "RegulatorController", (Talon) shooterRegulatorController);
		
		shooterController = new Talon(getShooterControllerPwmChannel());
		shooterController.setInverted(true);
		LiveWindow.addActuator("Shooter", "Controller", (Talon) shooterController);
		
		shooterEncoder = new Encoder(getShooterEncoderDioChannelA(), getShooterEncoderDioChannelB(), false, Encoder.EncodingType.k1X);
		shooterEncoder.setDistancePerPulse(360 / 20);
		shooterEncoder.setSamplesToAverage(10);
		LiveWindow.addSensor("Shooter", "Encoder", shooterEncoder);
	}
	
	protected void initGearAssembly() {
		gearTrayActuator = new Solenoid(getGearTrayActuatorPcmChannel());
		LiveWindow.addActuator("Gear", "TrayActuator", gearTrayActuator);
		
		gearFeedController = new Talon(getGearFeedControllerPwmChannel());
		LiveWindow.addActuator("Gear", "FeedController", (Talon) gearFeedController);
		
		gearDetector = new DigitalInput(getGearDetectorDioChannel());
		LiveWindow.addActuator("Gear", "Tray Actuator", gearTrayActuator);
		
		gearIndicatorRelay = new Relay(getGearIndicatorRelayChannel());
		gearIndicatorRelay.setDirection(Direction.kForward);
	}
	
	protected void initWinch() {
		winchController = new Talon(getWinchControllerPwmChannel());
		LiveWindow.addActuator("Winch", "Controller", (Talon) winchController);
	}
	
	protected abstract boolean isCompetitionBot();
	
	protected abstract int getDriveTrainLeftControllerPwmChannel();
	
	protected abstract int getDriveTrainRightControllerPwmChannel();
	
	protected abstract int getDriveTrainEncoderDioChannelA();
	
	protected abstract int getDriveTrainEncoderDioChannelB();
	
	protected abstract int getShooterSingulatorControllerPwmChannel();
	
	protected abstract int getShooterRegulatorControllerPwmChannel();
	
	protected abstract int getShooterControllerPwmChannel();
	
	protected abstract int getShooterEncoderDioChannelA();
	
	protected abstract int getShooterEncoderDioChannelB();
	
	protected abstract int getGearTrayActuatorPcmChannel();
	
	protected abstract int getGearFeedControllerPwmChannel();
	
	protected abstract int getGearDetectorDioChannel();
	
	protected abstract int getGearIndicatorRelayChannel();

	protected abstract int getWinchControllerPwmChannel();
}
