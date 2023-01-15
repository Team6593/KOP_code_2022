// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Utils;

/** singleton class for Memory monitoriing and analysing memory leaks/performance etc
 * This class can be accessed anywhere without creating multiple instances because
 * it is in a singleton pattern
 */
public class MemoryMonitor {

    public static MemoryMonitor memoryMonitorInstance = new MemoryMonitor();

    public MemoryMonitor() {}
    public static MemoryMonitor getInstance() { return memoryMonitorInstance;}

    /**
     * You can call this function to print the current memory usage.
     * The values are in bytes, the function convert it to MB for readability
     * Note that, this method returns the amount of memory that is currently in use by the JVM,
     * not the amount of memory that the operating system has available. 
     * 
     * You can use the class by creating an object of it and call the printMemoryUsage method on it.
     * Or, you can call the method directly because it's static:
     * MemoryMonitor.getInstance().printMemoryUsage();
     * 
     */
    public void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalRAM = runtime.totalMemory();
        long freeRAM = runtime.freeMemory();
        long usedRAM = totalRAM - freeRAM;

        long megabyte = (1024 * 1024);

        System.out.println("Used memory: " + usedRAM / megabyte);
        System.out.println("Free memory: " + freeRAM / megabyte);
        System.out.println("Total memory: " + totalRAM / megabyte);
    }

}
