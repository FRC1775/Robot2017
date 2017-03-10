package org.usfirst.frc.team1775.robot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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

public class Cameras {
	
	// General camera settings
	private static final PixelFormat PIXEL_FORMAT = PixelFormat.kMJPEG;
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 180;
	private static final int FPS = 15;

	// Shooter camera settings
	private static final String SHOOTER_CAMERA_NAME = "Shooter camera";
	private static final int SHOOTER_CAMERA_DEVICE = 1;
	private static final int SHOOTER_CAMERA_BRIGHTNESS = 40;
	private static final int SHOOTER_CAMERA_EXPOSURE = 0;
	private static final int SHOOTER_CAMERA_FOCUS_AUTO = 0;
	private static final int SHOOTER_CAMERA_FOCUS_ABSOLUTE = 0;
	private static final int SHOOTER_CAMERA_WHITE_BALANCE_TEMP_AUTO = 0;
	private static final int SHOOTER_CAMERA_WHITE_BALANCE_TEMP = 4000;

	// Gear camera settings
	private static final String GEAR_CAMERA_NAME = "Gear camera";
	private static final int GEAR_CAMERA_DEVICE = 0;
	private static final int GEAR_CAMERA_EXPOSURE = 1;
	private static final int GEAR_CAMERA_FOCUS_AUTO = 0;
	private static final int GEAR_CAMERA_FOCUS_ABSOLUTE = 0;
	private static final int GEAR_CAMERA_WHITE_BALANCE_TEMP_AUTO = 0;
	private static final int GEAR_CAMERA_WHITE_BALANCE_TEMP = 4000;

	private static AtomicBoolean shouldChangeCamera;
	public static volatile double angleOffCenter = 0;
	public static volatile double distance = 0;
	
	private int numCameras = 0;

	private Thread cameraThread;

	public static void changeCamera() {
		shouldChangeCamera.set(true);
	}

	public void init() {
		CvSource imageSource = CameraServer.getInstance().putVideo("Camera Viewer", IMG_WIDTH, IMG_HEIGHT);
		NetworkTable.getTable("").putString("CameraSelection", "Camera Viewer");

		numCameras = UsbCamera.enumerateUsbCameras().length;

		cameraThread = new Thread(() -> {
			try {
				UsbCamera shooterCamera = initShooterCamera();
				UsbCamera gearCamera = initGearCamera();
				//CvSink shooterCameraSink = initShooterCameraSink(shooterCamera);
				//CvSink gearCameraSink = initGearCameraSink(gearCamera);
				
				CvSink videoSink = initVideoSink(gearCamera);

				Mat inputImage = new Mat();

				boolean showShooterCamera = false;

				GripPipeline pipeline = new GripPipeline();
				GearPipeline gearPipeline = new GearPipeline();

				while (!Thread.interrupted()) {

					if (shouldChangeCamera.getAndSet(false)) {
						if (showShooterCamera && gearCamera != null) {
								videoSink.setSource(gearCamera);
						} else if(!showShooterCamera && shooterCamera != null) {
							videoSink.setSource(shooterCamera);
						}
						showShooterCamera = !showShooterCamera;
					}

					if (showShooterCamera) {
						if (shooterCamera != null) {
							//videoSink.setSource(shooterCamera);

							long frameTime = videoSink.grabFrame(inputImage);
							if (frameTime == 0) {
								continue;
							}
						}
						/*
						if (gearCameraSink != null) {
							gearCameraSink.setEnabled(false);
						}
						if (shooterCameraSink != null) {
							shooterCameraSink.setEnabled(true);

							long frameTime = shooterCameraSink.grabFrame(inputImage);
							if (frameTime == 0) {
								continue;
							}
						}
						*/
						
						processShooterCamera(pipeline, inputImage);
					} else {
						if (gearCamera != null) {
							//videoSink.setSource(gearCamera);

							long frameTime = videoSink.grabFrame(inputImage);
							if (frameTime == 0) {
								continue;
							}
						}
						/*
						if (shooterCameraSink != null) {
							shooterCameraSink.setEnabled(false);
						}
						if (gearCameraSink != null) {
							gearCameraSink.setEnabled(true);

							long frameTime = gearCameraSink.grabFrame(inputImage);
							if (frameTime == 0) {
								continue;
							}
						}
						*/
						
						processGearCamera(gearPipeline, inputImage);
					}

					imageSource.putFrame(inputImage);
				}
			} catch (Exception exception) {
				DriverStation.reportError("Something went wrong with cameras", true);
				DriverStation.reportError(exception.getMessage(), false);
				StackTraceElement[] els = exception.getStackTrace();
				for (int i = 0; i < els.length; i++) {
					DriverStation.reportError(els[i].toString(), false);
				}
			}

		});

		cameraThread.start();
	}
	
	private UsbCamera initShooterCamera() {
		if (numCameras >= 1) {
			UsbCamera shooterCamera = new UsbCamera(SHOOTER_CAMERA_NAME, getShooterCameraDevice());
			shooterCamera.setPixelFormat(PIXEL_FORMAT);
			shooterCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
			shooterCamera.setFPS(FPS);
			shooterCamera.setExposureManual(SHOOTER_CAMERA_EXPOSURE);
			shooterCamera.getProperty("brightness").set(SHOOTER_CAMERA_BRIGHTNESS);
			shooterCamera.getProperty("white_balance_temperature_auto").set(SHOOTER_CAMERA_WHITE_BALANCE_TEMP_AUTO);
			shooterCamera.getProperty("white_balance_temperature").set(SHOOTER_CAMERA_WHITE_BALANCE_TEMP);
			shooterCamera.getProperty("focus_auto").set(SHOOTER_CAMERA_FOCUS_AUTO);
			shooterCamera.getProperty("focus_absolute").set(SHOOTER_CAMERA_FOCUS_ABSOLUTE);
			
			return shooterCamera;
		}
		
		return null;
	}
	
	private CvSink initShooterCameraSink(UsbCamera shooterCamera) {
		if (shooterCamera != null) {
			CvSink shooterCameraSink = new CvSink(SHOOTER_CAMERA_NAME + " sink");
			shooterCameraSink.setSource(shooterCamera);
			
			return shooterCameraSink;
		}
		
		return null;
	}
	
	private int getShooterCameraDevice() {
		return Preferences.getInstance().getInt("Cameras.shooter", SHOOTER_CAMERA_DEVICE);
	}
	
	private UsbCamera initGearCamera() {
		if (numCameras >= 2) {
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
	
	private CvSink initGearCameraSink(UsbCamera gearCamera) {
		if (gearCamera != null) {
			CvSink gearCameraSink = new CvSink(GEAR_CAMERA_NAME + " sink");
			gearCameraSink.setSource(gearCamera);
			
			return gearCameraSink;
		}
		
		return null;
	}
	
	private CvSink initVideoSink(UsbCamera camera) {
		return CameraServer.getInstance().getVideo(camera);
	}
	
	private int getGearCameraDevice() {
		return Preferences.getInstance().getInt("Cameras.gear", GEAR_CAMERA_DEVICE);
	}
	
	private void processShooterCamera(GripPipeline pipeline, Mat inputImage) {
		if (inputImage.empty()) {
			angleOffCenter = 0;
			distance = 0;
			SmartDashboard.putNumber("Camera.shooter.angle", 0);
			return;
		}
		
		pipeline.process(inputImage);
		ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
		
		// Always draw contours
		if (contours.size() > 0) {
			Imgproc.drawContours(inputImage, contours, -1, new Scalar(0, 255, 0));
		}
		
		// If we have two contours, we assume they are the droids we're looking for.
		if (contours.size() >= 2) {
			Rect r1 = Imgproc.boundingRect(contours.get(0));
			Rect r2 = Imgproc.boundingRect(contours.get(1));
			Rect top = findTopRect(r1, r2);
			
			calculateShooterAngle(top);
			calculateShooterDistance(top);
			
		} else {
			angleOffCenter = 0;
			distance = 0;
			SmartDashboard.putNumber("Camera.shooter.angle", 0);
		}
	}
	
	private Rect findTopRect(Rect one, Rect two) {
		if (one.height > two.height) {
			return one;
		}
		
		return two;
	}
	
	private void calculateShooterAngle(Rect top) {
		double offCenter = (double) top.x + (double) top.width / 2.0 - ((double) IMG_WIDTH / 2.0);
		angleOffCenter = (65 / (double) IMG_WIDTH) * offCenter;
		
		SmartDashboard.putNumber("Camera.shooter.angle", angleOffCenter);
	}
	
	private void calculateShooterDistance(Rect top) {
		double bandDistance = 0.0000002 * Math.pow(top.y, 4) - 0.00004 * Math.pow(top.y, 3)
				+ 0.0049 * Math.pow(top.y, 2) + 0.0484 * top.y + 78.792;
		// ad 16.5 for the shooter distance to camera
		// and the mid boiler distance from the outside
		// of the boiler
		distance = Math.sqrt(Math.pow(bandDistance, 2) - Math.pow(66, 2)) + 19.5;
		distance = distance * 1.2279 - 18.55;
		
		SmartDashboard.putNumber("Camera.shooter.bandDistance", bandDistance);
		SmartDashboard.putNumber("DistanceIWant", distance);
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

		angleOffCenter = (65 / (double) IMG_WIDTH) * offCenter;
		SmartDashboard.putNumber("Camera.shooter.angle", angleOffCenter * 0.2);
	}
}
