package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.commands.gearassembly.HoldGear;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearAssemblySubsystem extends Subsystem {
	private Solenoid gearTrayActuator;
	private SpeedController gearFeedController;
	private DigitalInput gearDetector;
	private Relay gearIndicatorRelay;
	
	private boolean isDown = false;
	private boolean hasGear = false;
	private boolean isReleasing = false;
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new HoldGear()); 
	}
	
	public void init() {
		gearTrayActuator = new Solenoid(Robot.roboRio.getGearTrayActuatorPcmChannel());
		LiveWindow.addActuator("Gear", "TrayActuator", gearTrayActuator);
		
		gearFeedController = new Talon(Robot.roboRio.getGearFeedControllerPwmChannel());
		LiveWindow.addActuator("Gear", "FeedController", (Talon) gearFeedController);
		
		gearDetector = new DigitalInput(Robot.roboRio.getGearDetectorDioChannel());
		LiveWindow.addActuator("Gear", "Tray Actuator", gearTrayActuator);
		
		gearIndicatorRelay = new Relay(Robot.roboRio.getGearIndicatorRelayChannel());
		gearIndicatorRelay.setDirection(Direction.kForward);
	}
	
	public boolean isDown() {
		return isDown;
	}
	
	public boolean hasGear() {
		return hasGear;
	}
	
	public void hasGear(boolean hasGear) {
		this.hasGear = hasGear;
	}
	
	public boolean checkForGear() {
		if (sensesGear()) {
			gearIndicatorRelay.set(Value.kOn);
			hasGear = true;
			return true;
		} else {
			gearIndicatorRelay.set(Value.kOff);
			return false;
		}
	}
	
	public boolean sensesGear() {
		SmartDashboard.putBoolean("GearDetector", !gearDetector.get());
		return !gearDetector.get();
	}
	
	public void up() {
		gearTrayActuator.set(false);
		isDown = false;
		isReleasing = false;
	}
	
	public void down() {
		gearTrayActuator.set(true);
		if (hasGear()) {
			release();
		} else {
			startGearIntake();
		}
		isDown = true;
	}
	
	public boolean isReleasing() {
		return isReleasing;
	}
	
	public void stopFeed() {
		gearFeedController.stopMotor();
	}
	
	public void release() {
		gearFeedController.set(0.5);
		isReleasing = true;
		hasGear = false;
	}
	
	public void gripHeldGear() {
		gearFeedController.set(-0.7);
	}
	
	public void startGearIntake() {
		gearFeedController.set(-1.0);
		isReleasing = false;
	}

}
