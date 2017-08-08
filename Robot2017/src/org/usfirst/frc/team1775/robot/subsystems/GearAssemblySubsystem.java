package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RoboRio;
import org.usfirst.frc.team1775.robot.commands.gearassembly.HoldGear;

import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearAssemblySubsystem extends Subsystem {
	
	private boolean isDown = false;
	private boolean hasGear = false;
	private boolean isReleasing = false;
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new HoldGear()); 
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
			RoboRio.gearIndicatorRelay.set(Value.kOn);
			hasGear = true;
			return true;
		} else {
			RoboRio.gearIndicatorRelay.set(Value.kOff);
			return false;
		}
	}
	
	public boolean sensesGear() {
		SmartDashboard.putBoolean("GearDetector", !RoboRio.gearDetector.get());
		return !RoboRio.gearDetector.get();
	}
	
	public void up() {
		RoboRio.gearTrayActuator.set(false);
		isDown = false;
		isReleasing = false;
	}
	
	public void down() {
		RoboRio.gearTrayActuator.set(true);
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
		RoboRio.gearFeedController.stopMotor();
	}
	
	public void release() {
		RoboRio.gearFeedController.set(0.5);
		isReleasing = true;
		hasGear = false;
	}
	
	public void gripHeldGear() {
		RoboRio.gearFeedController.set(-0.7);
	}
	
	public void startGearIntake() {
		RoboRio.gearFeedController.set(-1.0);
		isReleasing = false;
	}

}
