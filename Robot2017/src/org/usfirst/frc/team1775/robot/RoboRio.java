package org.usfirst.frc.team1775.robot;

public abstract class RoboRio {
	public abstract boolean isCompetitionBot();
	
	public abstract int getDriveTrainLeftControllerPwmChannel();
	
	public abstract int getDriveTrainRightControllerPwmChannel();
	
	public abstract int getDriveTrainEncoderDioChannelA();
	
	public abstract int getDriveTrainEncoderDioChannelB();
	
	public abstract int getShooterSingulatorControllerPwmChannel();
	
	public abstract int getShooterRegulatorControllerPwmChannel();
	
	public abstract int getShooterControllerPwmChannel();
	
	public abstract int getShooterEncoderDioChannelA();
	
	public abstract int getShooterEncoderDioChannelB();
	
	public abstract int getGearTrayActuatorPcmChannel();
	
	public abstract int getGearFeedControllerPwmChannel();
	
	public abstract int getGearDetectorDioChannel();
	
	public abstract int getGearIndicatorRelayChannel();

	public abstract int getWinchControllerPwmChannel();
}
