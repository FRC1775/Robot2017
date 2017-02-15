package org.usfirst.frc.team1775.robot.commands;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Command;

public class ShowCameraOne extends Command {

	public void execute() {
		UsbCamera c = CameraServer.getInstance().startAutomaticCapture(0);
		//UsbCamera camera1 = new UsbCamera("test", UsbCamera.enumerateUsbCameras()[0].path);//CameraServer.getInstance().startAutomaticCapture(0);
		
		c.setVideoMode(PixelFormat.kYUYV, 320, 240, 5);
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
}
