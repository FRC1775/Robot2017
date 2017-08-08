package org.usfirst.frc.team1775.robot.commands.shooter;

import org.usfirst.frc.team1775.robot.Robot;
import org.usfirst.frc.team1775.robot.commands.oi.LeftJoystickRumble;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Shoot extends CommandGroup {
	private static final double DEFAULT_JOYSTICK_RUMBLE = 0.3;

	public Shoot() {
		addParallel(new LeftJoystickRumble("Shooter.joystickRumble", DEFAULT_JOYSTICK_RUMBLE));
		addSequential(new StartSingulator());
		addSequential(new StartShooter());
		addSequential(new StartRegulator());
	}
	
	public Shoot(int rpm) {
		addParallel(new LeftJoystickRumble("Shooter.joystickRumble", DEFAULT_JOYSTICK_RUMBLE));
		addSequential(new StartSingulator());
		addSequential(new StartShooter(rpm));
		addSequential(new StartRegulator());
	}

	@Override
	protected void interrupted() {
		Robot.shooter.stop();
	}
	
	protected void end() {
		Robot.shooter.stop();
	}	

}
