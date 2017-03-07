package org.usfirst.frc.team1775.robot;

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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	private static final int SHOOTER_CAMERA_FOCUS_AUTO = 0;
	private static final int SHOOTER_CAMERA_FOCUS_ABSOLUTE = 0;
	private static final int SHOOTER_CAMERA_WHITE_BALANCE_TEMP_AUTO = 0;
	private static final int SHOOTER_CAMERA_WHITE_BALANCE_TEMP = 4000;

	// Gear camera settings
	private static final String GEAR_CAMERA_NAME = "Gear camera";
	private static final int GEAR_CAMERA_DEVICE = 1;
	private static final int GEAR_CAMERA_EXPOSURE = 1;
	private static final int GEAR_CAMERA_FOCUS_AUTO = 0;
	private static final int GEAR_CAMERA_FOCUS_ABSOLUTE = 0;
	private static final int GEAR_CAMERA_WHITE_BALANCE_TEMP_AUTO = 0;
	private static final int GEAR_CAMERA_WHITE_BALANCE_TEMP = 4000;

	public static double angleOffCenter;
	public static double distance;

	private Thread cameraThread;

	private int numCameras = 1;
	
	private static boolean shouldChangeCamera;
	
	public static void changeCamera() {
		shouldChangeCamera = true;
	}

	public void init() {
		CvSource imageSource = CameraServer.getInstance().putVideo("Camera Viewer", IMG_WIDTH, IMG_HEIGHT);

		numCameras = UsbCamera.enumerateUsbCameras().length;

		cameraThread = new Thread(() -> {
			try {
				UsbCamera shooterCamera = new UsbCamera(SHOOTER_CAMERA_NAME, SHOOTER_CAMERA_DEVICE);
				shooterCamera.setPixelFormat(PIXEL_FORMAT);
				shooterCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
				shooterCamera.setFPS(FPS);
				shooterCamera.setExposureManual(SHOOTER_CAMERA_EXPOSURE);
				shooterCamera.getProperty("brightness").set(40);
				shooterCamera.getProperty("white_balance_temperature_auto").set(SHOOTER_CAMERA_WHITE_BALANCE_TEMP_AUTO);
				shooterCamera.getProperty("white_balance_temperature").set(SHOOTER_CAMERA_WHITE_BALANCE_TEMP);
				shooterCamera.getProperty("focus_auto").set(SHOOTER_CAMERA_FOCUS_AUTO);
				shooterCamera.getProperty("focus_absolute").set(SHOOTER_CAMERA_FOCUS_ABSOLUTE);

				CvSink shooterCameraSink = new CvSink(SHOOTER_CAMERA_NAME + " sink");
				shooterCameraSink.setSource(shooterCamera);

				CvSink gearCameraSink = null;

				if (numCameras > 1) {
					UsbCamera gearCamera = new UsbCamera(GEAR_CAMERA_NAME, GEAR_CAMERA_DEVICE);
					gearCamera.setPixelFormat(PIXEL_FORMAT);
					gearCamera.setResolution(IMG_WIDTH, IMG_HEIGHT);
					gearCamera.setFPS(FPS);
					gearCamera.setExposureManual(GEAR_CAMERA_EXPOSURE);
					gearCamera.getProperty("white_balance_temperature_auto").set(GEAR_CAMERA_WHITE_BALANCE_TEMP_AUTO);
					gearCamera.getProperty("white_balance_temperature").set(GEAR_CAMERA_WHITE_BALANCE_TEMP);
					gearCamera.getProperty("focus_auto").set(GEAR_CAMERA_FOCUS_AUTO);
					gearCamera.getProperty("focus_absolute").set(GEAR_CAMERA_FOCUS_ABSOLUTE);

					gearCameraSink = new CvSink(GEAR_CAMERA_NAME + " sink");
					gearCameraSink.setSource(gearCamera);
				}
				/*
				 * if (!shooterCamera.isConnected() ||
				 * !gearCamera.isConnected()) { System.out.println("HERE");
				 * cameraThread.interrupt(); }
				 */

				Mat inputImage = new Mat();

				boolean showShooterCamera = false;

				GripPipeline pipeline = new GripPipeline();
				GearPipeline gearPipeline = new GearPipeline();

				while (!Thread.interrupted()) {

					if (Robot.oi.getYButton() || shouldChangeCamera) {
						// This line isn't thread-safe
						shouldChangeCamera = false;
						showShooterCamera = !showShooterCamera;
					}

					if (showShooterCamera) {
						if (gearCameraSink != null) {
							gearCameraSink.setEnabled(false);
						}
						shooterCameraSink.setEnabled(true);

						long frameTime = shooterCameraSink.grabFrame(inputImage);
						if (frameTime == 0)
							continue;
					} else {
						shooterCameraSink.setEnabled(false);

						if (gearCameraSink != null) {
							gearCameraSink.setEnabled(true);

							long frameTime = gearCameraSink.grabFrame(inputImage);
							if (frameTime == 0)
								continue;
						}
					}

					if (showShooterCamera) {
						pipeline.process(inputImage);
						ArrayList<MatOfPoint> contours = pipeline.filterContoursOutput();
						if (contours.size() > 0) {
							// TODO set to EQUAL to 2
							if (contours.size() >= 2) {
								Rect r1 = Imgproc.boundingRect(contours.get(0));

								Rect r2 = Imgproc.boundingRect(contours.get(1));

								Rect top;

								if (r1.height > r2.height) {
									top = r1;
								} else {
									top = r2;
								}

								double offCenter = (double) top.x + (double) top.width / 2.0
										- ((double) IMG_WIDTH / 2.0);

								angleOffCenter = (65 / (double) IMG_WIDTH) * offCenter;
								SmartDashboard.putNumber("Camera.shooter.angle", angleOffCenter);
								double bandDistance = 0.0000002 * Math.pow(top.y, 4) - 0.00004 * Math.pow(top.y, 3)
										+ 0.0049 * Math.pow(top.y, 2) + 0.0484 * top.y + 78.792;
								SmartDashboard.putNumber("Camera.shooter.bandDistance", bandDistance);
								// ad 16.5 for the shooter distance to camera and the mid boiler distance from the outside of the boiler
								distance = Math.sqrt(Math.pow(bandDistance, 2) - Math.pow(66, 2)) + 16.5;
								
								SmartDashboard.putNumber("DistanceIWant", distance);
							}
						}
					} else {
						gearPipeline.process(inputImage);
						ArrayList<MatOfPoint> contours = gearPipeline.filterContoursOutput();
						
						if (contours.size() > 0) {
							// TODO set to EQUAL to 2
							if (contours.size() >= 2) {
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
								
								if (rects.size() == 2) {
									Rect r1 = rects.get(0);
	
									Rect r2 = rects.get(1);
	
									Rect left, right;
	
									if (r1.x < r2.x) {
										left = r1;
										right = r2;
									} else {
										right = r2;
										left = r1;
									}
	
									double offCenter = ((((double)right.width + (double)right.x) - (double) left.x) / 2.0 + (double)left.x) - ((double) IMG_WIDTH / 2.0);
	
									angleOffCenter = (65 / (double) IMG_WIDTH) * offCenter;
									SmartDashboard.putNumber("Camera.shooter.angle", angleOffCenter * 0.2);
								}
							}
						}
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
	
}
