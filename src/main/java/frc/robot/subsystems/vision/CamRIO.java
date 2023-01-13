// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.vision;

import java.io.Console;
import java.util.HashSet;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagDetector.QuadThresholdParameters;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Timer;
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
 * 16:9 resolutions are reccomended but other resolution ratios will work
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

        camera.setResolution(640, 480);

        //get the CvSink, this caputres/records video
        CvSink cvSink = CameraServer.getVideo();

        CvSource videoStream = CameraServer.putVideo("Logitech c270", 640, 480);

        double fx = 0;
        double fy = 0;
        double cx = 0;
        double cy = 0;

        // We should re-use one mat instead of 2, beacuse mats are memory heavy
        // 2 mats, one for regular images, one for grayscale (AprilTags)
        Mat mat = new Mat();
        Mat greymap = new Mat();

        // points
        Point point1 = new Point();
        Point point2 = new Point();
        Point point3 = new Point();
        Point point4 = new Point();

        Point centerPoint = new Point();

        // for some dumb fucking reason, the Scalar values are Blue, Green, Red
        // instead of the traditional Red, Green, Blue.
        Scalar redScalar = new Scalar(0, 0, 255);
        Scalar greenScalar = new Scalar(0, 255, 0);

        AprilTagDetector detector = new AprilTagDetector();
        AprilTagDetector.Config config = detector.getConfig();

        detector.setConfig(config);

        // wtf is a quad sigma?
        config.quadSigma = 0.8f;

        QuadThresholdParameters quadThresholdParameters = detector.getQuadThresholdParameters();

        // these variables might need to change
        // especially minClusterPixels
        quadThresholdParameters.minClusterPixels = 250; // pixel size
        quadThresholdParameters.criticalAngle *= 50; // default is 10
        quadThresholdParameters.maxLineFitMSE *= 1.5;
        detector.setQuadThresholdParameters(quadThresholdParameters);
        detector.addFamily("tag16h5");

        Timer timer = new Timer();
        int count = 0;

        while (!Thread.interrupted()) {
          // tell sink to grab video frames from the camera and put it into the source mat
          if (cvSink.grabFrame(mat) == 0) {
            videoStream.notifyError("There was an error grabbing a frame from: ");
            videoStream.notifyError(cameraVision.cameraName);
            videoStream.notifyError(" sink err");
            videoStream.notifyError(cvSink.getError());
            continue;
          }

          // convert frame into grayscale
          Imgproc.cvtColor(mat, greymap, Imgproc.COLOR_BGR2GRAY);

          var results = detector.detect(greymap);

          HashSet hashSet = new HashSet<>();

          for (var result : results) {
            count += 1;

            // points that will be used to draw a square around the AprilTag
            point1.x = result.getCornerX(0);
            point2.x = result.getCornerX(1);
            point3.x = result.getCornerX(2);
            point4.x = result.getCornerX(3);

            point1.y = result.getCornerY(0);
            point2.y = result.getCornerY(1);
            point3.y = result.getCornerY(2);
            point4.y = result.getCornerY(3);

            centerPoint.x = result.getCenterX();
            centerPoint.y = result.getCenterY();

            hashSet.add(result.getId());

            // draw a square around the apriltag
            // this is a really fucking retarded way of drawing a square but it works
            Imgproc.line(mat, point1, point2, redScalar, 5);
            Imgproc.line(mat, point2, point3, redScalar, 5);
            Imgproc.line(mat, point3, point4, redScalar, 5);
            Imgproc.line(mat, point4, point1, redScalar, 5);

            Imgproc.circle(mat, centerPoint, 4, greenScalar);
            Imgproc.putText(mat, String.valueOf(result.getId()), point3, Imgproc.FONT_HERSHEY_SIMPLEX, 2, greenScalar, 7);


          };

          for (var id : hashSet) {
            System.out.println("Tag: " + String.valueOf(id));
          }

          if (timer.advanceIfElapsed(1.0)) {
            System.out.println("detections per second: " + String.valueOf(count));
            count = 0;
          }

          videoStream.putFrame(mat);
        }
        
        //detector.close();
        
      });
      visionThread.setDaemon(true);
      visionThread.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
