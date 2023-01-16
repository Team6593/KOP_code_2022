// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.vision;

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
  public CamRIO() {}// do not add any subsystem dependencies

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
        var camera = CameraServer.startAutomaticCapture();

              var cameraWidth = 640;
              var cameraHeight = 480;

              camera.setResolution(cameraWidth, cameraHeight);

              var cvSink = CameraServer.getVideo();
              var outputStream = CameraServer.putVideo("RioApriltags", cameraWidth, cameraHeight);

              // Mats are memory expensive, its better to re-use one but this is also ok
              var mat = new Mat();
              var grayMat = new Mat();

              // points that will be used to draw square around tag
              var pt0 = new Point();
              var pt1 = new Point();
              var pt2 = new Point();
              var pt3 = new Point();
              var center = new Point();
              var red = new Scalar(0, 0, 255);
              var green = new Scalar(0, 255, 0);

              var aprilTagDetector = new AprilTagDetector();

              var config = aprilTagDetector.getConfig();
              config.quadSigma = 0.8f;
              aprilTagDetector.setConfig(config);

              var quadThreshParams = aprilTagDetector.getQuadThresholdParameters();
              quadThreshParams.minClusterPixels = 250;
              quadThreshParams.criticalAngle *= 5; // default is 10
              quadThreshParams.maxLineFitMSE *= 1.5;
              aprilTagDetector.setQuadThresholdParameters(quadThreshParams);

              // the AprilTag detector can only detect one family at a time
              // to detect multiple tag fam's we must use multiple AprilTagDetectors
              // this leads worse performance as AprilTagDetectors are expensive to setup
              aprilTagDetector.addFamily("tag16h5");

              var timer = new Timer();
              timer.start();
              var count = 0;

              // this can never be true the robot must be off for this to be true
              while (!Thread.interrupted()) {
                if (cvSink.grabFrame(mat) == 0) {
                  outputStream.notifyError(cvSink.getError());
                  continue;
                }
                
                // convert image to grayscale
                Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_RGB2GRAY);

                var results = aprilTagDetector.detect(grayMat);

                var set = new HashSet<>();

                for (var result: results) {
                  count += 1;
                  pt0.x = result.getCornerX(0);
                  pt1.x = result.getCornerX(1);
                  pt2.x = result.getCornerX(2);
                  pt3.x = result.getCornerX(3);

                  pt0.y = result.getCornerY(0);
                  pt1.y = result.getCornerY(1);
                  pt2.y = result.getCornerY(2);
                  pt3.y = result.getCornerY(3);

                  center.x = result.getCenterX();
                  center.y = result.getCenterY();

                  set.add(result.getId());

                  // Imgproc doesn't have a square/rectangle member function
                  // so we must use this awkward ass way of drawing squares around the tag
                  Imgproc.line(mat, pt0, pt1, red, 5);
                  Imgproc.line(mat, pt1, pt2, red, 5);
                  Imgproc.line(mat, pt2, pt3, red, 5);
                  Imgproc.line(mat, pt3, pt0, red, 5);

                  Imgproc.circle(mat, center, 4, green);
                  // print id (number) of the tag
                  Imgproc.putText(mat, String.valueOf(result.getId()), pt2, Imgproc.FONT_HERSHEY_SIMPLEX, 2, green, 7);

                };

                for (var id : set){
                  System.out.println("Tag: " + String.valueOf(id));
                }

                if (timer.advanceIfElapsed(1.0)){
                  System.out.println("detections per second: " + String.valueOf(count));
                  count = 0;
                }

                outputStream.putFrame(mat);
              }
              // if you do not close the detector, it will cause a memory leak
              aprilTagDetector.close();
            });
    //TODO: create a handwritten protocol for syncing tag detection with autonomous driving
    // (or just use a pre-existing one like NetworkTables)
    visionThread.setDaemon(true);
    visionThread.start();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
