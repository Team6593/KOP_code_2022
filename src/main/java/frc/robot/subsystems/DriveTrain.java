// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

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
  private Spark masterRight = new Spark(motors.DT_MASTER_RIGHT_ID);
  private Spark masterLeft = new Spark(motors.DT_MASTER_LEFT_ID);
  private Spark slaveLeft = new Spark(motors.DT_SLAVE_LEFT_ID);
  private Spark slaveRight = new Spark(motors.DT_SLAVE_RIGHT_ID);

  private final MotorControllerGroup DtLeft = new MotorControllerGroup(masterLeft, slaveLeft);
  private final MotorControllerGroup DtRight = new MotorControllerGroup(masterRight, slaveRight);

  private final DifferentialDrive Drive = new DifferentialDrive(DtLeft, DtRight);

  /** Creates a new DriveTrain. */
  public DriveTrain() {
  

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
