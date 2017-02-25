package org.usfirst.frc.team1775.robot;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

public class Cameras {
	// General camera settings
	private static final PixelFormat PIXEL_FORMAT = PixelFormat.kMJPEG;
	private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 180;
    private static final int FPS = 15;
    
    // Shooter camera settings
    private static final String SHOOTER_CAMERA_NAME = "Shooter camera";
    private static final int SHOOTER_CAMERA_DEVICE = 0;
    private static final int SHOOTER_CAMERA_EXPOSURE = 0;
    
    // Gear camera settings
    private static final String GEAR_CAMERA_NAME = "Gear camera";
    private static final int GEAR_CAMERA_DEVICE = 1;
    private static final int GEAR_CAMERA_EXPOSURE = 0;
    
    private Thread cameraThread;
	
	public void init() {
	    CvSource imageSource = CameraServer.getInstance().putVideo("Camera Viewer", IMG_WIDTH, IMG_HEIGHT);
	    
	    cameraThread = new Thread(() -> {
			UsbCamera shooterCamera = new UsbCamera(SHOOTER_CAMERA_NAME, SHOOTER_CAMERA_DEVICE);
			shooterCamera.setPixelFormat(PIXEL_FORMAT);
			shooterCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
			shooterCamera.setFPS(FPS);
			shooterCamera.setExposureManual(SHOOTER_CAMERA_EXPOSURE);
		    
		    UsbCamera gearCamera = new UsbCamera(GEAR_CAMERA_NAME, GEAR_CAMERA_DEVICE);
		    gearCamera.setPixelFormat(PIXEL_FORMAT);
		    gearCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		    gearCamera.setFPS(FPS);
		    gearCamera.setExposureManual(GEAR_CAMERA_EXPOSURE);
		    
		    CvSink shooterCameraSink = new CvSink(SHOOTER_CAMERA_NAME + " sink");
		    shooterCameraSink.setSource(shooterCamera);
		    
		    CvSink gearCameraSink = new CvSink(GEAR_CAMERA_NAME + " sink");
		    gearCameraSink.setSource(gearCamera);
		    
		    
		    Mat inputImage = new Mat();
	    	
    		boolean showShooterCamera = false;
    		
            GripPipeline pipeline = new GripPipeline();
            
    		
		    while(!Thread.interrupted()) {
		    	
		    	if(Robot.oi.driverAButton.get()) {
		    		showShooterCamera = !showShooterCamera;
            	}
            	
                if(showShooterCamera) {
                	gearCameraSink.setEnabled(false);
                	shooterCameraSink.setEnabled(true);
                	
	  		    	long frameTime = shooterCameraSink.grabFrame(inputImage);
	  		    	if (frameTime == 0) continue;
                } else {
                	shooterCameraSink.setEnabled(false);
                	gearCameraSink.setEnabled(true);
                	
	  		    	long frameTime = gearCameraSink.grabFrame(inputImage);
	  		    	if (frameTime == 0) continue;
                }
                
                pipeline.process(inputImage);
                DriverStation.reportError(""+pipeline.filterContoursOutput().size(), false);
            	ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
                Imgproc.drawContours(inputImage, contours, -1, new Scalar(0, 255, 0)); //changed -1 to -5
                DriverStation.reportError("number of countours "+contours.size(), false);
                
                // TODO do other processing here
                
                /*
                MatOfPoint contour1 = contours.get(0);
                Rect r = Imgproc.boundingRect(contour1);
                double testDistance = ((15.0*(double)IMG_WIDTH)/(2.0*r.width*Math.tan(((60.0*Math.PI)/180)/2.0)));
            	DriverStation.reportError("Distance to boiler: "+testDistance, false);
		    	*/
                
		    	imageSource.putFrame(inputImage);
		    }
	    });
	    
	    cameraThread.start();
	}
	
    /*
     * 
     * 
     * Various bits of old code below
     * 
     * DO NOT USE
     * 
     * 
     */
    
    
	/*Thread t = new Thread(() -> {
    
		boolean allowCam1 = false;
        
        CvSink cvSink1 = CameraServer.getInstance().getVideo(camera1);
        CvSink cvSink2 = CameraServer.getInstance().getVideo(camera2);
        CvSource outputStream = CameraServer.getInstance().putVideo("Switcher", 320, 180);
        
        Mat image = new Mat();
        
        GripPipeline pipeline = new GripPipeline();
        
        while(!Thread.interrupted()) {
        	
        	if(oi.joystick1.getRawButton(1)) {
        		allowCam1 = !allowCam1;
        	}
        	
            if(allowCam1){
              cvSink2.setEnabled(false);
              cvSink1.setEnabled(true);
              cvSink1.grabFrame(image);
            } else{
              cvSink1.setEnabled(false);
              cvSink2.setEnabled(true);
              cvSink2.grabFrame(image);    
            }
            
            pipeline.process(image);
            DriverStation.reportError(""+pipeline.filterContoursOutput().size(), false);
        	ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
            Imgproc.drawContours(image, contours, -1, new Scalar(0, 255, 0));
            DriverStation.reportError("number of countours "+contours.size(), false);
            outputStream.putFrame(image);
        }
        
    });
    t.start();*/
	
	//UsbCamera c = CameraServer.getInstance().startAutomaticCapture(0);
	//UsbCamera camera1 = new UsbCamera("test", UsbCamera.enumerateUsbCameras()[0].path);//CameraServer.getInstance().startAutomaticCapture(0);
	
	//c.setVideoMode(PixelFormat.kYUYV, 320, 240, 5);
	//CameraServer.getInstance().removeCamera(c.getName());
	
	//UsbCamera c2 = CameraServer.getInstance().startAutomaticCapture(1);

	//c2.setVideoMode(PixelFormat.kYUYV, 320, 240, 5);
	
	/*
	// * This code can be used to enumerate the properties of the camera
	// * 
	for (VideoProperty vp : camera1.enumerateProperties()) {
		DriverStation.reportError("\n" + vp.getName() + ": " + vp.getKind());
		DriverStation.reportError("Range: " + vp.getMin() + " - " + vp.getMax());
		if (vp.getKind() == Kind.kInteger || vp.getKind() == Kind.kBoolean || vp.getKind() == Kind.kEnum) {
			DriverStation.reportError("Default: " + vp.getDefault());
			DriverStation.reportError("Value: " + vp.get());
			DriverStation.reportError("Step: " + vp.getStep());
		}
		if (vp.getKind() == Kind.kString) {
			DriverStation.reportError("String: " + vp.getString());
		}
		if (vp.getKind() == Kind.kEnum) {
			DriverStation.reportError("Choices: " + Arrays.toString(vp.getChoices()));
		}
	}
	*/
    
	//UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(0);
	//VideoProperty focus = camera1.getProperty("focus_auto");
	//DriverStation.reportError("auto focus? "+focus, false);
	//camera1.setResolution(IMG_WIDTH, IMG_HEIGHT);
	
	//camera2.setResolution(IMG_WIDTH, IMG_HEIGHT);
	//camera.setWhiteBalanceAuto();
	//camera1.setExposureManual(0);
	//camera1.getProperty("focus_auto").set(0);
	//camera1.setExposureManual(5);
	//camera1.getProperty("zoom_absolute").s
	//camera2.setExposureManual(0);
	
	//camera.
	//camera.setWhiteBalanceAuto();
	//camera.setBrightness(10);
	
    
	// visionThread = new VisionThread(camera1, new GripPipeline(), pipeline -> {
    	//DriverStation.reportError("HERE", false);
        //if (!pipeline.filterContoursOutput().isEmpty()) {
        //if (!VisionThread.interrupted()) {
            
             
        //    synchronized (imgLock) {
            	//DriverStation.reportError(""+pipeline.filterContoursOutput().size(), false);
            	//ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
            	//DriverStation.reportError("Count: " + contours.size(), false);
            	//MatOfPoint contour1 = contours.get(0);
                //Rect r = Imgproc.boundingRect(contour1);
                //MatOfPoint contour2 = contours.get(1);
                //Rect r2 = Imgproc.boundingRect(contour2);
            	
            	// 0.45 => 20/44.5" calibration of frame view from 47"
            	// 0.884 is view angle in radians
            	// Calculate angle by ratio of screen to ratio of view angle
            	//double anglularDiameter = (0.884 * (r.width / (double)IMG_WIDTH)) / 0.45;
                //double TEST_WIDTH = 1000.0;
            	//double testDistance = ((15.0*(double)IMG_WIDTH)/(2.0*r.width*Math.tan(((60.0*Math.PI)/180)/2.0)));
            	//double testDistance2 = Math.sqrt((Math.pow(testDistance, 2)-Math.pow(120, 2)));
            	// Use angular diameter equation solving for D (distance to object)
            	//double distance = 20.0 / Math.tan(anglularDiameter / 2.0);
            	//DriverStation.reportError("Distance: " + distance, false);
            	//DriverStation.reportError("LeftX: "+r.x + " RightX: "+r2.x, false);
                //centerX = r.x + (r.width / 2);
                //double opp = IMG_WIDTH / 2 - centerX;
                // Calculate angle of robot to target by using ratio of 1/2 view angle compared to percentage of screen width
                // between center of frame and center of target
                //double angle = 180.0/Math.PI * ((0.442 * (opp / (double)IMG_WIDTH)) / 0.45);
                //DriverStation.reportError("Angle: " + angle, false);
                //DriverStation.reportError(""+r.width, false);
                //DriverStation.reportError("This is a test distance " + testDistance, false);
                //DriverStation.reportError(""+r.width, false);
                //DriverStation.reportError("This is a test distance " + testDistance, false);
                //DriverStation.reportError("This is the length of the peg tape: "+ r2.height, false);
                //DriverStation.reportError("Contour 1 X: "+r.x, false);
                //DriverStation.reportError("Contour 2 X: "+r2.x , false);
        //    }
     ///   }
    //});
    //visionThread.start();
}
