package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

import org.usfirst.frc.team1775.robot.commands.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.PlaceGear;
import org.usfirst.frc.team1775.robot.commands.PrepareShoot;
import org.usfirst.frc.team1775.robot.commands.RotateByAngle;
import org.usfirst.frc.team1775.robot.commands.ShowCameraOne;
import org.usfirst.frc.team1775.robot.commands.WindWinch;

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

	public Joystick joystick1;
	public Joystick joystick2;
	
	public JoystickButton leftBumper;
	public JoystickButton rightBumper;
	public JoystickButton yButton;
	public JoystickButton bButton;
	public JoystickButton cbButton;
	
	//temporary button
	public JoystickButton csButton;

	public OI() {
		// Driver joystick
		joystick1 = new Joystick(0);
		
		// Operator joystick
		joystick2 = new Joystick(1);

		// Left bumper
		leftBumper = new JoystickButton(joystick1, 5);
		leftBumper.toggleWhenPressed(new PrepareShoot());
		
		// Right bumper
		
		// Y button
		yButton = new JoystickButton(joystick1, 4);
		yButton.whileHeld(new WindWinch());
		
		// B button
		//bButton = new JoystickButton(joystick1, 3);
		//bButton.whenPressed(new RotateByAngle(90));
		
		//center 'back' button
		cbButton = new JoystickButton(joystick1, 7);
		cbButton.whileHeld(new PlaceGear());
		
		//center 'start' button 
		csButton = new JoystickButton(joystick1, 8 );
		csButton.whileHeld(new DriveDistance(60));
	}
}
