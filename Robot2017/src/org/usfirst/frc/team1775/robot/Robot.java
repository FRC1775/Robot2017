
package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1775.robot.commands.autonomous.DoNothing;
import org.usfirst.frc.team1775.robot.commands.autonomous.DriveAcrossBaseline;
import org.usfirst.frc.team1775.robot.commands.autonomous.Blue_Left_Shoot;
import org.usfirst.frc.team1775.robot.commands.autonomous.Middle_PlaceGear;
import org.usfirst.frc.team1775.robot.commands.autonomous.Red_Left_PlaceGear;
import org.usfirst.frc.team1775.robot.commands.autonomous.Red_Right_PlaceGear;
import org.usfirst.frc.team1775.robot.commands.autonomous.Red_Right_PlaceGearThenShoot;
import org.usfirst.frc.team1775.robot.commands.autonomous.Red_Right_Shoot;
import org.usfirst.frc.team1775.robot.commands.drivetrain.DriveDistance;
import org.usfirst.frc.team1775.robot.commands.autonomous.Blue_Right_PlaceGear;
import org.usfirst.frc.team1775.robot.commands.autonomous.Blue_Left_GoToHopperAndShoot;
import org.usfirst.frc.team1775.robot.commands.autonomous.Blue_Left_GoToHopperAndShootDiag;
import org.usfirst.frc.team1775.robot.commands.autonomous.Blue_Left_PlaceGear;
import org.usfirst.frc.team1775.robot.commands.autonomous.Blue_Left_PlaceGearThenShoot;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;
import org.usfirst.frc.team1775.robot.subsystems.WinchSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
    public static DriveTrainSubsystem driveTrain;
    public static ShooterSubsystem shooter;
    public static GearAssemblySubsystem gearAssembly;
    public static WinchSubsystem winch;
    
    public static Cameras cameras;
    public static ICamera camera;
    
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
    
     
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
     
	@Override
	public void robotInit() {
		RobotMap.init();

        driveTrain = new DriveTrainSubsystem();
        SmartDashboard.putData(driveTrain);
        shooter = new ShooterSubsystem();
        SmartDashboard.putData(shooter);
        gearAssembly = new GearAssemblySubsystem();
        SmartDashboard.putData(gearAssembly);
        winch = new WinchSubsystem();
        SmartDashboard.putData(winch);
		
		oi = new OI();

        
        initCameras();
		initDashboard();
	}
	
	private void initDashboard() {
		chooser.addDefault("Do Nothing", new DoNothing());
		chooser.addObject("Drive Across Base Line", new DriveAcrossBaseline());
		
		/*
		 * THIS IS THE BLUE SIDE
		 */
		chooser.addObject("Blue - Left (Boiler) - Place Gear", new Blue_Left_PlaceGear());
		//chooser.addObject("Blue - Left (Boiler) - Place Gear Then Shoot", new Blue_Left_PlaceGearThenShoot());
		chooser.addObject("Blue - Left (Boiler) - Shoot", new Blue_Left_Shoot());
		chooser.addObject("Blue - Boiler(Left) - Go to Hopper And Shoot", new Blue_Left_GoToHopperAndShoot());
		chooser.addObject("Blue - Boiler(Left) - Go to Hopper And Shoot Diag", new Blue_Left_GoToHopperAndShootDiag());

		chooser.addObject("Blue - Middle - Place Gear", new Middle_PlaceGear());
		//chooser.addObject("Blue - Middle - Place Gear Then Shoot", new Blue_Middle_PlaceGearThenShoot());
		//chooser.addObject("Blue - Middle - Shoot", new Blue_Middle_Shoot());
		
		chooser.addObject("Blue - Right (Loading) - Place Gear", new Blue_Right_PlaceGear());
		
		
		/*
		 * THIS IS THE RED SIDE
		 */
		chooser.addObject("Red - Left (Loading) - Place Gear", new Red_Left_PlaceGear());

		chooser.addObject("Red - Middle - Place Gear", new Middle_PlaceGear());
		//chooser.addObject("Red - Middle - Place Gear Then Shoot", new Red_Middle_PlaceGearThenShoot());
		//chooser.addObject("Red - Middle - Shoot", new Red_Middle_Shoot());
		
		chooser.addObject("Red - Right (Boiler) - Place Gear", new Red_Right_PlaceGear());
		chooser.addObject("Red - Right (Boiler) - Place Gear Then Shoot", new Red_Right_PlaceGearThenShoot());
		chooser.addObject("Red - Right (Boiler) - Shoot", new Red_Right_Shoot());
		//chooser.addObject("Red - Boiler(Right) - Go to Hopper And Shoot", new Red_Right_GoToHopperAndShoot());
		
		
		SmartDashboard.putData("Auto Mode", chooser);
		SmartDashboard.putData(Scheduler.getInstance());
		SmartDashboard.putData(new DriveDistance(45));
	}
    
	private void initCameras() {
		//cameras = new Cameras();
		//cameras.init();
		camera = new GearCamera();
		camera.init();
	}
	
	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		shooter.stop();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */ 
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		
		while (oi.driverJoystick == null) {
			oi = new OI();
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().enable();
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
