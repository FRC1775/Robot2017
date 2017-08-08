package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;
import org.usfirst.frc.team1775.robot.commands.gearassembly.HoldGear;

import edu.wpi.first.wpilibj.Relay.Direction;
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
	
	public boolean sensesGear() {
		SmartDashboard.putBoolean("GearDetector", !RobotMap.gearDetector.get());
		boolean sensesGear = !RobotMap.gearDetector.get();
		if (sensesGear) {
			RobotMap.gearIndicatorRelay.set(Value.kOn);
		} else {
			RobotMap.gearIndicatorRelay.set(Value.kOff);
		}
		return sensesGear;
	}
	
	public void hasGear(boolean hasGear) {
		this.hasGear = hasGear;
	}
	
	public void up() {
		RobotMap.gearRelease.set(false);
		isDown = false;
	}
	
	public void down() {
		RobotMap.gearRelease.set(true);
		if (shouldRelease()) {
			System.out.println("Should Release");
			release();
		} else {
			runGrip();
		}
		isDown = true;
	}
	
	public boolean shouldRelease() {
		return !isDown() && (hasGear() || sensesGear());
	}
	
	public void runGrip() {
		if (hasGear()) {
			RobotMap.gearFeedController.set(-0.7);
		} else {
			RobotMap.gearFeedController.set(-1.0);
		}
	}
	
	public boolean isReleasing() {
		return isReleasing;
	}
	
	public void isReleasing(boolean releasing) {
		isReleasing = releasing;
	}
	
	public void stopGrip() {
		RobotMap.gearFeedController.stopMotor();
	}
	
	public void release() {
		RobotMap.gearFeedController.set(0.5);
		isReleasing = true;
	}
	
	public void reset() {
		//RobotMap.gearRelease.set(false);
	}

}
