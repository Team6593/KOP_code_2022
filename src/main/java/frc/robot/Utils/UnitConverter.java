// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Utils;

/** Unit conversion class, converts between radians, degrees and units */
public class UnitConverter {

    /**
     * An ideal use-case for this is when you are programming sensors/encoders/autonomoue
     * Converts degrees into radians
     * @param degrees
     * @return radians
     */
    public double deg2rad(double degrees) {
        double radians = Math.toRadians(degrees);
        return radians;
    }

    /**
     * An ideal use-case for this is when you are programming sensors/encoders/autonomoue
     * Converts radians into degrees
     * @param radians
     * @return degrees
     */
    public double rad2deg(double radians) {
        double degrees = Math.toDegrees(radians);
        return degrees;
    }

    /**
     * An ideal use-case for this is when you are programming sensors/encoders/autonomoue
     * @param units
     * @return units per revolution/full rotation (360 degrees)
     */
    public double unitsPerFullRevolution(double units) {
        double unitsPerRev = units / 360;
        return unitsPerRev;
    }
    
    /**
     * An ideal use-case for this is when you are programming sensors/encoders/autonomoue
     * @param units
     * @return units per half-revolution/half of a full rotation (180 degrees)
     */
    public double unitsPerHalfRevolution(double units) {
        double unitsPerHalfRev = units / 180;
        return unitsPerHalfRev;
    }

    /**
     * An ideal use-case for this is when you are programming sensors/encoders/autonomoue
     * @param units
     * @param degrees
     * @return units per-degree (units/degree)
     */
    public double unitsPerDegree(double units, double degrees) {
        double unitsPerDegr = units / degrees;
        return unitsPerDegr;
    }

    /**
     * An ideal use-case for this is when you are programming sensors/encoders/autonomoue
     * @param degree
     * @param units
     * @return degree(s) per unit (degree/units)
     */
    public double degreePerUnits(double degree, double units) {
        double degPerUnit = degree / units;
        return degPerUnit;
    }
    
}
