package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearAssemblySubsystem extends Subsystem {

	@Override
	protected void initDefaultCommand() { }
	
	public void release() {
		SmartDashboard.putBoolean("Gear.isOpen", true);
		RobotMap.gearRelease.set(true);
	}
	
	public void reset() {
		SmartDashboard.putBoolean("Gear.isOpen", false);
		RobotMap.gearRelease.set(false);
	}

}
