
package org.usfirst.frc.team1775.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

import java.util.ArrayList;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import org.usfirst.frc.team1775.robot.commands.Example;
import org.usfirst.frc.team1775.robot.subsystems.DriveTrainSubsystem;
import org.usfirst.frc.team1775.robot.subsystems.ShooterSubsystem;

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
	public static OI oi;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
    
	private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;
    
    private VisionThread visionThread;
    private double centerX = 0.0;
     
    private final Object imgLock = new Object();
     
     
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		RobotMap.init();
	    /*DigitalInput digital1;
	    DigitalInput digital2;
	        // This is Kate's sensor for the gear
	    	digital1 = new DigitalInput(8);
	    	digital2 = new DigitalInput(9);
	    
	    if (digital1.get() & digital2.get()){
	    	SmartDashboard.putBoolean("both?", true);
	    }
	    */
		
		
		
        driveTrain = new DriveTrainSubsystem();
        shooter = new ShooterSubsystem();
        
		oi = new OI();
		
		chooser.addDefault("Default Auto", new Example());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		
		SmartDashboard.putData(Scheduler.getInstance());
		
		/*
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    	camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
    	//camera.setWhiteBalanceAuto();
    	//camera.setExposureManual(1);
    	camera.setExposureManual(1);
    	
    	//camera.
    	//camera.setWhiteBalanceAuto();
    	//camera.setBrightness(10);
    	visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
        	//DriverStation.reportError("HERE", false);
            if (!pipeline.filterContoursOutput().isEmpty()) {
            	DriverStation.reportError(""+pipeline.filterContoursOutput().size(), false);
            	ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
            	DriverStation.reportError("Count: " + contours.size(), false);
            	MatOfPoint contour1 = contours.get(0);
                Rect r = Imgproc.boundingRect(contour1);
                //MatOfPoint contour2 = contours.get(1);
                //Rect r2 = Imgproc.boundingRect(contour2);
                 
                synchronized (imgLock) {
                	// 0.45 => 20/44.5" calibration of frame view from 47"
                	// 0.884 is view angle in radians
                	// Calculate angle by ratio of screen to ratio of view angle
                	//double anglularDiameter = (0.884 * (r.width / (double)IMG_WIDTH)) / 0.45;
                	double testDistance = ((20.0*(double)IMG_WIDTH)/(2.0*r.width*Math.tan(((50.7*Math.PI)/180)/2.0)));
                	//double testDistance2 = Math.sqrt((Math.pow(testDistance, 2)-Math.pow(120, 2)));
                	// Use angular diameter equation solving for D (distance to object)
                	//double distance = 20.0 / Math.tan(anglularDiameter / 2.0);
                	//DriverStation.reportError("Distance: " + distance, false);
                 	
                    centerX = r.x + (r.width / 2);
                    double opp = IMG_WIDTH / 2 - centerX;
                    // Calculate angle of robot to target by using ratio of 1/2 view angle compared to percentage of screen width
                    // between center of frame and center of target
                    double angle = 180.0/Math.PI * ((0.442 * (opp / (double)IMG_WIDTH)) / 0.45);
                    //DriverStation.reportError("Angle: " + angle, false);
                    //DriverStation.reportError(""+r.width, false);
                    //DriverStation.reportError("This is a test distance " + testDistance, false);
                    //DriverStation.reportError(""+r.width, false);
                    //DriverStation.reportError("This is a test distance " + testDistance, false);
                    //DriverStation.reportError("This is the length of the peg tape: "+ r2.height, false);
                    //DriverStation.reportError("Contour 1 X: "+r.x, false);
                    //DriverStation.reportError("Contour 2 X: "+r2.x , false);
                }
            }
        });
        visionThread.start();
        
        */
        
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
