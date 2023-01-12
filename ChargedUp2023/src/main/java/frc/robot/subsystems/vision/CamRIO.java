// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.vision;

import java.io.Console;

import org.opencv.core.Mat;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

/**
 * RoboRIO 2 is reccomended for best performance
 * with the RIO.
 * 
 * Vision/vision processing with the RoboRIO
 * performance may not be stable,
 * and more work is needed to implement features.
 * 
 * Try using lower resolutions to improve fps and RIO performance
 * 
 * This assumes that a camera, like a Logitech HD webcam
 * (or something like that), is connected via USB
 * to the RoboRIO.
 */
public class CamRIO extends SubsystemBase {
  /** Creates a new CamRIO. */
  public CamRIO() {}

  // set vision processing on another thread so it does not slow down the main thread
  Thread visionThread;

  public static Constants.CameraVision cameraVision = new Constants.CameraVision();

  //public UsbCamera camera = CameraServer.startAutomaticCapture();

  /**
   * call this in robotInit()
   */
  public void camInit() {

    //this is where the code gets really ugly

    //init visionThread
    visionThread = new Thread(
      () -> {
        // Get the USB camera from CameraServer
        UsbCamera camera = CameraServer.startAutomaticCapture();

        //set resolution/viewport
        //lower resolutions == more fps, better RIO perfromance
        camera.setResolution(cameraVision.ViewportWidth, cameraVision.ViewportHeight);

        //get the CvSink, this caputres/records video
        CvSink cvSink = CameraServer.getVideo();

        CvSource videoStream = CameraServer.putVideo(cameraVision.cameraName, 640, 480);

        // We should re-use one mat instead of 2, beacuse mats are memory heavy
        // 2 mats, one for regular images, one for grayscale (AprilTags)
        Mat mat = new Mat();
        Mat greymap = new Mat();

        while (!Thread.interrupted()) {
          // tell sink to grab video frames from the camera and put it into the source mat
          if (cvSink.grabFrame(mat) == 0) {
            videoStream.notifyError("There was an error grabbing a frame from: ");
            videoStream.notifyError(cameraVision.cameraName);
            videoStream.notifyError(" sink err");
            videoStream.notifyError(cvSink.getError());
            continue;
          }
          videoStream.putFrame(mat);
        }

        
      });
      visionThread.setDaemon(true);
      visionThread.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
