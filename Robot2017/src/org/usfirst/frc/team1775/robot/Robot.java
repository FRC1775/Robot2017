
package org.usfirst.frc.team1775.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1775.robot.commands.autonomous.PlaceGear;
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
        shooter = new ShooterSubsystem();
        gearAssembly = new GearAssemblySubsystem();
        winch = new WinchSubsystem();
		
		oi = new OI();
        
        initCameras();
		initDashboard();
        
		
		Thread rpm = new Thread(() -> {
			
			while (true) {
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//SmartDashboard.putNumber("Shooter.rpm", RobotMap.shooterController.getSpeed());
			}
			
		});
		rpm.start();
		
		/*
		Thread reportErrors = new Thread(() -> {
			while (!Thread.interrupted()) {
				DriverStation.reportError("digitalioport2: "+RobotMap.driveTrainEncoder.getDistance(), false);
				//DriverStation.reportError("Gyro: "+RobotMap.gyro.getAngle(), false);
			}
		});
		reportErrors.start();
		*/
		
	    /*DigitalInput digital1;
	    DigitalInput digital2;
	        // This is Kate's sensor for the gear
	    	digital1 = new DigitalInput(8);
	    	digital2 = new DigitalInput(9);
	    
	    if (digital1.get() & digital2.get()){
	    	SmartDashboard.putBoolean("both?", true);
	    }
	    */
	}
	
	private void initDashboard() {
		chooser.addDefault("Default Auto", new PlaceGear());
		SmartDashboard.putData("Auto mode", chooser);
		
		SmartDashboard.putData(Scheduler.getInstance());
	}
    
	private void initCameras() {
		cameras = new Cameras();
		cameras.init();
	}
	
	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

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
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
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
