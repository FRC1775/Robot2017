package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;
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
			RobotMap.gearIndicatorRelay.set(Value.kOn);
			hasGear = true;
			return true;
		} else {
			RobotMap.gearIndicatorRelay.set(Value.kOff);
			return false;
		}
	}
	
	public boolean sensesGear() {
		SmartDashboard.putBoolean("GearDetector", !RobotMap.gearDetector.get());
		return !RobotMap.gearDetector.get();
	}
	
	public void up() {
		RobotMap.gearTrayActuator.set(false);
		isDown = false;
		isReleasing = false;
	}
	
	public void down() {
		RobotMap.gearTrayActuator.set(true);
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
		RobotMap.gearFeedController.stopMotor();
	}
	
	public void release() {
		RobotMap.gearFeedController.set(0.5);
		isReleasing = true;
		hasGear = false;
	}
	
	public void gripHeldGear() {
		RobotMap.gearFeedController.set(-0.7);
	}
	
	public void startGearIntake() {
		RobotMap.gearFeedController.set(-1.0);
		isReleasing = false;
	}

}
