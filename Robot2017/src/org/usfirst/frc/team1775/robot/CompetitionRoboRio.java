package org.usfirst.frc.team1775.robot;

public class CompetitionRoboRio extends RoboRio {
	public final static int DRIVE_TRAIN_LEFT_CONTROLLER_PWM_CHANNEL = 2;
	public final static int DRIVE_TRAIN_RIGHT_CONTROLLER_PWM_CHANNEL = 1;
	public final static int DRIVE_TRAIN_ENCODER_DIO_CHANNEL_A = 3;
	public final static int DRIVE_TRAIN_ENCODER_DIO_CHANNEL_B = 2;
	
	public final static int SHOOTER_SINGULATOR_CONTROLLER_PWM_CHANNEL = 0;
	public final static int SHOOTER_REGULATOR_CONTROLLER_PWM_CHANNEL = 3;
	public final static int SHOOTER_CONTROLLER_PWM_CHANNEL = 6;
	public final static int SHOOTER_ENCODER_DIO_CHANNEL_A = 4;
	public final static int SHOOTER_ENCODER_DIO_CHANNEL_B = 5;
	
	public final static int GEAR_TRAY_ACTUATOR_PCM_CHANNEL = 2;
	public final static int GEAR_FEED_CONTROLLER_PWM_CHANNEL = 5;
	public final static int GEAR_DETECTOR_DIO_CHANNEL = 9;
	public final static int GEAR_INDICATOR_RELAY_CHANNEL = 0;
	
	public final static int WINCH_CONTROLLER_PWM_CHANNEL = 4;
	
	public boolean isCompetitionBot() {
		return true;
	}
	
	public int getDriveTrainLeftControllerPwmChannel() {
		return DRIVE_TRAIN_LEFT_CONTROLLER_PWM_CHANNEL;
	}
	
	public int getDriveTrainRightControllerPwmChannel() {
		return DRIVE_TRAIN_RIGHT_CONTROLLER_PWM_CHANNEL;
	}
	
	public int getDriveTrainEncoderDioChannelA() {
		return DRIVE_TRAIN_ENCODER_DIO_CHANNEL_A;
	}
	
	public int getDriveTrainEncoderDioChannelB() {
		return DRIVE_TRAIN_ENCODER_DIO_CHANNEL_B;
	}
	
	public int getShooterSingulatorControllerPwmChannel() {
		return SHOOTER_SINGULATOR_CONTROLLER_PWM_CHANNEL;
	}
	
	public int getShooterRegulatorControllerPwmChannel() {
		return SHOOTER_REGULATOR_CONTROLLER_PWM_CHANNEL;
	}
	
	public int getShooterControllerPwmChannel() {
		return SHOOTER_CONTROLLER_PWM_CHANNEL;
	}
	
	public int getShooterEncoderDioChannelA() {
		return SHOOTER_ENCODER_DIO_CHANNEL_A;
	}
	
	public int getShooterEncoderDioChannelB() {
		return SHOOTER_ENCODER_DIO_CHANNEL_B;
	}
	
	public int getGearTrayActuatorPcmChannel() {
		return GEAR_TRAY_ACTUATOR_PCM_CHANNEL;
	}
	
	public int getGearFeedControllerPwmChannel() {
		return GEAR_FEED_CONTROLLER_PWM_CHANNEL;
	}
	
	public int getGearDetectorDioChannel() {
		return GEAR_DETECTOR_DIO_CHANNEL;
	}
	
	public int getGearIndicatorRelayChannel() {
		return GEAR_INDICATOR_RELAY_CHANNEL;
	}

	public int getWinchControllerPwmChannel() {
		return WINCH_CONTROLLER_PWM_CHANNEL;
	}
}
