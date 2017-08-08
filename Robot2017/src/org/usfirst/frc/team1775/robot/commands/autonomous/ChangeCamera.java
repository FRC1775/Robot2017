package org.usfirst.frc.team1775.robot.commands.autonomous;

import org.usfirst.frc.team1775.robot.Cameras;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ChangeCamera extends InstantCommand {

	public void execute() {
		Cameras.changeCamera();
	}
}
