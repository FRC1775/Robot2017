package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.gearassembly.ReleaseGear;
import org.usfirst.frc.team1775.robot.commands.shooter.PrepareShoot;
import org.usfirst.frc.team1775.robot.commands.winch.WindWinch;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	
	public final static int DRIVER_JOYSTICK = 0;
	public final static int OPERATOR_JOYSTICK = 1;
	
	public final static int XBOX_A = 1;
	public final static int XBOX_B = 2;
	public final static int XBOX_X = 3;
	public final static int XBOX_Y = 4;
	public final static int XBOX_LEFT_BUMPER = 5;
	public final static int XBOX_RIGHT_BUMPER = 6;
	public final static int XBOX_BACK = 7;
	public final static int XBOX_START = 8;

	public final static int XBOX_LEFT_JOYSTICK_X_AXIS = 0;
	public final static int XBOX_LEFT_JOYSTICK_Y_AXIS = 1;
	public final static int XBOX_LEFT_TRIGGER = 2;
	public final static int XBOX_RIGHT_TRIGGER = 3;
	public final static int XBOX_RIGHT_JOYSTICK_X_AXIS = 4;
	public final static int XBOX_RIGHT_JOYSTICK_Y_AXIS = 5;

	public Joystick driverJoystick;
	public Joystick operatorJoystick;

	public JoystickButton driverAButton;
	public JoystickButton driverBButton;
	public JoystickButton driverXButton;
	public JoystickButton driverYButton;
	public JoystickButton driverLeftBumper;
	public JoystickButton driverRightBumper;
	public JoystickButton driverBackButton;
	public JoystickButton driverStartButton;

	public JoystickButton operatorAButton;
	public JoystickButton operatorBButton;
	public JoystickButton operatorXButton;
	public JoystickButton operatorYButton;
	public JoystickButton operatorLeftBumper;
	public JoystickButton operatorRightBumper;
	public JoystickButton operatorBackButton;
	public JoystickButton operatorStartButton;

	public OI() {
		initDriverJoystick();
		initOperatorJoystick();
	}
	
	private void initDriverJoystick() {
		driverJoystick = new Joystick(DRIVER_JOYSTICK);
		
		// A button
		driverAButton = new JoystickButton(driverJoystick, XBOX_A);
		
		// B button
		driverBButton = new JoystickButton(driverJoystick, XBOX_B);
		//bButton.whenPressed(new RotateByAngle(90));
		
		// X button
		driverXButton = new JoystickButton(driverJoystick, XBOX_X);
		
		// Y button
		driverYButton = new JoystickButton(driverJoystick, XBOX_Y);
		driverYButton.whileHeld(new WindWinch());

		// Left bumper
		driverLeftBumper = new JoystickButton(driverJoystick, XBOX_LEFT_BUMPER);
		driverLeftBumper.toggleWhenPressed(new PrepareShoot());
		
		// Right bumper
		driverRightBumper = new JoystickButton(driverJoystick, XBOX_RIGHT_BUMPER);
		
		// Back button
		driverBackButton = new JoystickButton(driverJoystick, XBOX_BACK);
		driverBackButton.whileHeld(new ReleaseGear());
		
		// Start button 
		driverStartButton = new JoystickButton(driverJoystick, XBOX_START);
		driverStartButton.whileHeld(new DriveDistance(60));
		
	}
	
	private void initOperatorJoystick() {
		operatorJoystick = new Joystick(OPERATOR_JOYSTICK);
		
		// A button
		operatorAButton = new JoystickButton(operatorJoystick, XBOX_A);
		
		// B button
		operatorBButton = new JoystickButton(operatorJoystick, XBOX_B);
		//operatorBButton.whenPressed(new RotateByAngle(90));
		
		// X button
		operatorXButton = new JoystickButton(operatorJoystick, XBOX_X);
		
		// Y button
		operatorYButton = new JoystickButton(operatorJoystick, XBOX_Y);
		operatorYButton.whileHeld(new WindWinch());

		// Left bumper
		operatorLeftBumper = new JoystickButton(operatorJoystick, XBOX_LEFT_BUMPER);
		operatorLeftBumper.toggleWhenPressed(new PrepareShoot());
		
		// Right bumper
		operatorRightBumper = new JoystickButton(operatorJoystick, XBOX_RIGHT_BUMPER);
		
		// Back button
		operatorBackButton = new JoystickButton(operatorJoystick, XBOX_BACK);
		operatorBackButton.whileHeld(new ReleaseGear());
		
		// Start button 
		operatorStartButton = new JoystickButton(operatorJoystick, XBOX_START);
		operatorStartButton.whileHeld(new DriveDistance(60));
	}
}
