// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Motors;

public class DriveTrain extends SubsystemBase {
  
  // refrence constants to get motor ID's
  public static Constants.Motors motors = new Constants.Motors();

  // We might not be using Sparks so this is subject to change
  private Spark masterRight = new Spark(motors.MasterRight);
  private Spark masterLeft = new Spark(motors.MasterLeft);
  private Spark slaveLeft = new Spark(motors.SlaveLeft);
  private Spark slaveRight = new Spark(motors.SlaveRight);

  private final MotorControllerGroup DtLeft = new MotorControllerGroup(masterLeft, slaveLeft);
  private final MotorControllerGroup DtRight = new MotorControllerGroup(masterRight, slaveRight);

  private final DifferentialDrive Drive = new DifferentialDrive(DtLeft, DtRight);

  private AHRS gyro; //import kauaiLabs_NavX_FRC vendor library

  private double P = 0.0;// might have to change number later


  /** Creates a new DriveTrain. */
  public DriveTrain() {
  //NavX Gyro setup
  try {
    gyro = new AHRS(SPI.Port.kMXP);
  } catch (RuntimeException rex) {
    DriverStation.reportError("An error occured with NavX mxp, most likely and error with installing NavX - MansourQ" + rex.getMessage(), true);
  }

}

  public void driveStraight(double motorspeed) {
    double err = -gyro.getAngle(); //target angle is zero
    double turnSpeed = P * err;
    Drive.arcadeDrive(motorspeed, turnSpeed);
  }

  public void resetGyro() {
    gyro.reset();
  }


  public void stopAllMotors() {
    DtLeft.stopMotor();
    DtRight.stopMotor();
  }

  public void arcadeDrive(double xSpd, double zRot) {
    Drive.arcadeDrive(xSpd, zRot, false);
  }

  public void dtInit() {

    //Ensure motor output is nuetral during initialization
    masterLeft.set(0);
    masterRight.set(0);
    slaveLeft.set(0);
    slaveRight.set(0);


    //Typically the right side of a drivetrain must be inverted
    DtRight.setInverted(true);    
    
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
