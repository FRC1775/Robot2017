package org.usfirst.frc.team1775.robot.commands.autonomous;

import edu.wpi.first.wpilibj.command.Command;

public class Wait extends Command {

	private long start = 0;
	private int milliseconds;
	
	public Wait(int milliseconds) {
		this.milliseconds = milliseconds;
	}
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		start = System.currentTimeMillis();
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return (System.currentTimeMillis() - start) >= milliseconds;
	}

}
