package org.usfirst.frc.team1775.robot.subsystems;

import org.usfirst.frc.team1775.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

public class GearAssemblySubsystem extends Subsystem {

	@Override
	protected void initDefaultCommand() { }
	
	public void release() {
		RobotMap.gearRelease.set(true);
	}
	
	public void reset() {
		RobotMap.gearRelease.set(false);
	}

}
