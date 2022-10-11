// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    // Sort out ID's by classes
    // MAKE SURE THE CLASSES ARE DECLARED STATIC
    // ALSO MAKE SURE TO IMPORT INNER-CLASSES STATICALLY

    public static final class Motors {
        // put motor ID's here
        // how to define consts: subsystem + master or slave + orientation (left, right, up, down) + "ID"
        // change these nums later
        public final int DT_MASTER_RIGHT_ID = 1;
        public final int DT_MASTER_LEFT_ID = 2;
        public final int DT_SLAVE_RIGHT_ID = 3;
        public final int DT_SLAVE_LEFT_ID = 4;
        
    }




}
