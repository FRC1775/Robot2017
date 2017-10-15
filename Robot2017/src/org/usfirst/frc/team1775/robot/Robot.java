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
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;
import org.usfirst.frc.team1775.robot.subsystems.GearAssemblySubsystem;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;
import org.usfirst.frc.team1775.robot.subsystems.WinchSubsystem;
import org.usfirst.frc.team1775.robot.vision.GearCamera;
import org.usfirst.frc.team1775.robot.vision.ICamera;

public class Robot extends IterativeRobot {
    public static DriveTrainSubsystem driveTrain;
    public static ShooterSubsystem shooter;
    public static GearAssemblySubsystem gearAssembly;
    public static WinchSubsystem winch;
    
    public static ICamera camera;
    
    public static RoboRio roboRio;
	public static OperatorInterface operatorInterface;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
    
	@Override
	public void robotInit() {
		initRoboRio();
		initOperatorInterface();
        initSubsystems();
        initCameras();
		initDashboard();
	}

	@Override
	public void disabledInit() {
		shooter.stop();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		ensureAutonomousIsStopped();
		
		while (!isOperatorInterfaceInitialized()) {
			initOperatorInterface();
		}
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().enable();
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	private void initRoboRio() {
		roboRio = new PracticeRoboRio();
	}
	
	private void initOperatorInterface() {
		operatorInterface = new OperatorInterface();
	}
	
	private void initSubsystems() {
        driveTrain = new DriveTrainSubsystem();
        driveTrain.init();
        SmartDashboard.putData(driveTrain);
        
        shooter = new ShooterSubsystem();
        shooter.init();
        SmartDashboard.putData(shooter);
        
        gearAssembly = new GearAssemblySubsystem();
        gearAssembly.init();
        SmartDashboard.putData(gearAssembly);
        
        winch = new WinchSubsystem();
        winch.init();
        SmartDashboard.putData(winch);
	}
    
	private void initCameras() {
		camera = new GearCamera();
		camera.init();
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
	
	private void ensureAutonomousIsStopped() {
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
	}
	
	private boolean isOperatorInterfaceInitialized() {
		return operatorInterface != null && operatorInterface.driverJoystick != null;
	}
}
