// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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

    public final int XboxController_Port = 0;

    public static final class CameraVision {
        public final String cameraName = "camera";
        public final int ViewportWidth = 640;
        public final int ViewportHeight = 480;
    }

    public static final class InputMap {
    
        public static final class xBox{
            public final int Abutton = 1;
            public final int Bbutton = 2;
            public final int Xbutton = 3;
            public final int Ybutton = 4;
            public final int LeftTrigger = 5;
            public final int RightTrigger = 6;
            public final int WindowButton = 7;
            public final int MenuButton = 8;
            public final int LeftButtonClick = 9;
            public final int RightButtonClick = 10;
        }
    }

    public static final class Motors {
        // put motor ID's here
        // how to define consts: subsystem + master or slave + orientation (left, right, up, down) + "ID"
        // change these nums later
        // TO ELECTRICAL AND MECHANICAL, PLEASE STOP FUCKING WITH THE PORTS ON THE ROBORIO
        public final int MasterRight = 1;
        public final int MasterLeft = 2;
        public final int SlaveRight = 3;
        public final int SlaveLeft = 4;
    }




}
