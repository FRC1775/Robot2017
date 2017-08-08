package org.usfirst.frc.team1775.robot.vision;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GearCamera implements ICamera {
	
	// General camera settings
	private static final PixelFormat PIXEL_FORMAT = PixelFormat.kMJPEG;
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 180;
	private static final int FPS = 15;

	// Gear camera settings
	private static final String GEAR_CAMERA_NAME = "Gear camera";
	private static final int GEAR_CAMERA_DEVICE = 0;
	private static final int GEAR_CAMERA_EXPOSURE = 1;
	private static final int GEAR_CAMERA_FOCUS_AUTO = 0;
	private static final int GEAR_CAMERA_FOCUS_ABSOLUTE = 0;
	private static final int GEAR_CAMERA_WHITE_BALANCE_TEMP_AUTO = 0;
	private static final int GEAR_CAMERA_WHITE_BALANCE_TEMP = 4000;

	public static volatile double angleOffCenter = 0;
	public static volatile double distance = 0;

	private Thread cameraThread;

	public void init() {
		CvSource imageSource = CameraServer.getInstance().putVideo("Camera Viewer", IMG_WIDTH, IMG_HEIGHT);
		NetworkTable.getTable("").putString("CameraSelection", "Camera Viewer");

		cameraThread = new Thread(() -> {
			try {
				UsbCamera gearCamera = initGearCamera();
				CvSink videoSink = CameraServer.getInstance().getVideo(gearCamera);

				Mat inputImage = new Mat();

				GearPipeline gearPipeline = new GearPipeline();

				while (!Thread.interrupted()) {
					if (gearCamera != null) {
						long frameTime = videoSink.grabFrame(inputImage);
						if (frameTime == 0) {
							continue;
						}

						processGearCamera(gearPipeline, inputImage);
					}
					
					imageSource.putFrame(inputImage);
				}
			} catch (Exception exception) {
				System.out.println("Something went wrong with cameras");
				//DriverStation.reportError("Something went wrong with cameras", true);
				//DriverStation.reportError(exception.getMessage(), false);
				//StackTraceElement[] els = exception.getStackTrace();
				//for (int i = 0; i < els.length; i++) {
				//	DriverStation.reportError(els[i].toString(), false);
				//}
			}

		});

		cameraThread.start();
	}

	private UsbCamera initGearCamera() {
		if (UsbCamera.enumerateUsbCameras().length == 1) {
			UsbCamera gearCamera = new UsbCamera(GEAR_CAMERA_NAME, getGearCameraDevice());
			gearCamera.setPixelFormat(PIXEL_FORMAT);
			gearCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
			gearCamera.setFPS(FPS);
			gearCamera.setExposureManual(GEAR_CAMERA_EXPOSURE);
			gearCamera.getProperty("white_balance_temperature_auto").set(GEAR_CAMERA_WHITE_BALANCE_TEMP_AUTO);
			gearCamera.getProperty("white_balance_temperature").set(GEAR_CAMERA_WHITE_BALANCE_TEMP);
			gearCamera.getProperty("focus_auto").set(GEAR_CAMERA_FOCUS_AUTO);
			gearCamera.getProperty("focus_absolute").set(GEAR_CAMERA_FOCUS_ABSOLUTE);
			
			return gearCamera;
		}
		
		return null;
	}
	
	private int getGearCameraDevice() {
		return Preferences.getInstance().getInt("Cameras.gear", GEAR_CAMERA_DEVICE);
	}
	
	private void processGearCamera(GearPipeline pipeline, Mat inputImage) {
		if (inputImage.empty()) {
			angleOffCenter = 0;
			distance = 0;
			SmartDashboard.putNumber("Camera.shooter.angle", 0);
			return;
		}
		
		pipeline.process(inputImage);
		ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();

		// We may find more than two contours, and we need to filter them further. The gear in particular tends to show up.
		if (contours.size() >= 2) {
			ArrayList<Rect> rects = filterContours(contours, inputImage);

			// Ah! These are the droids we're looking for
			if (rects.size() == 2) {
				Rect left = findLeftRect(rects);
				Rect right = findRightRect(rects);
				
				calculateGearAngle(left, right);
				SmartDashboard.putNumber("Camera.shooter.angle", angleOffCenter);
			} else {
				angleOffCenter = 0;
				distance = 0;
				SmartDashboard.putNumber("Camera.shooter.angle", 0);
			}
		} else {
			angleOffCenter = 0;
			distance = 0;
			SmartDashboard.putNumber("Camera.shooter.angle", 0);
		}
	}
	
	private ArrayList<Rect> filterContours(ArrayList<MatOfPoint> contours, Mat inputImage) {
		ArrayList<Rect> rects = new ArrayList<Rect>(2);
		
		for (int i = 0; i < contours.size(); i++) {
			Rect rect = Imgproc.boundingRect(contours.get(i));
			if (rect.y < 110) {
				Imgproc.drawContours(inputImage, contours, i, new Scalar(0, 255, 0));
				rects.add(rect);
			}
			if (rects.size() == 2) {
				break;
			}
		}
		
		return rects;
	}
	
	private Rect findLeftRect(ArrayList<Rect> rects) {
		if (rects.get(0).x < rects.get(1).x) {
			return rects.get(0);
		}
		
		return rects.get(1);
	}
	
	private Rect findRightRect(ArrayList<Rect> rects) {
		if (rects.get(0).x < rects.get(1).x) {
			return rects.get(1);
		}
		
		return rects.get(0);
	}
	
	private void calculateGearAngle(Rect left, Rect right) {
		double offCenter = ((((double) right.width + (double) right.x) - (double) left.x) / 2.0
				+ (double) left.x) - ((double) IMG_WIDTH / 2.0);

		angleOffCenter = (65 / (double) IMG_WIDTH) * offCenter / 1.5;
	}

	@Override
	public double getDistance() {
		return distance;
	}

	@Override
	public double getAngleOffCenter() {
		return angleOffCenter;
	}
}
