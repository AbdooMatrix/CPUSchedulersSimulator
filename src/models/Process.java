package models;

import java.awt.Color;

public class Process {
    // Common Attributes for all schedulers
    private String name;             // Name of the process
    private int arrivalTime;         // Time the process arrives in the ready queue
    private int burstTime;           // Total CPU burst time required by the process
    private int priority;            // Priority level of the process (lower value = higher priority)
    private String color;            // Visual representation (e.g., for Gantt charts)
    private int pid;                 // Unique Process ID
    private int completionTime;      // Time the process finishes execution
    private int originalBurstTime = 0; // To store the initial burst time for specific algorithms

    // FCAI-Specific Attributes
    private double fcaiFactor;       // FCAI factor used to determine scheduling order
    private int updatedQuantum;      // Dynamic quantum used for FCAI scheduling

    // Constructor
    public Process(String name, int arrivalTime, int burstTime, int priority, String color, int pid) {
        // Initializing all common attributes
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
        this.pid = pid;
    }

    // Getters and Setters for Common Attributes
    public String getName() {
        return name; // Returns the process name
    }

    public int getArrivalTime() {
        return arrivalTime; // Returns the time the process arrives
    }

    public int getBurstTime() {
        return burstTime; // Returns the remaining burst time
    }

    public void setBurstTime(int burstTime) {
        // Updates the remaining burst time, e.g., after partial execution
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority; // Returns the priority of the process
    }

    public Color getColor() {
        return Color.decode(color); // Converts the hex string color to a Color object
    }

    public int getPid() {
        return pid; // Returns the process ID
    }

    public int getCompletionTime() {
        return completionTime; // Returns the completion time
    }

    public void setCompletionTime(int completionTime) {
        // Updates the completion time when the process finishes
        this.completionTime = completionTime;
    }

    public int getOriginalBurstTime() {
        return originalBurstTime; // Returns the initial burst time
    }

    public void setOriginalBurstTime(int originalBurstTime) {
        // Sets the initial burst time (used in algorithms like SRTF)
        this.originalBurstTime = originalBurstTime;
    }

    // Common Calculations for Schedulers
    public int getWaitingTime(int completionTime) {
        // Waiting Time = Completion Time - Arrival Time - Burst Time
        return completionTime - arrivalTime - burstTime;
    }

    public int getTurnaroundTime(int completionTime) {
        // Turnaround Time = Completion Time - Arrival Time
        return completionTime - arrivalTime;
    }

    public int getWaitingTimeSRTF(int completionTime) {
        // SRTF-Specific Waiting Time = Completion Time - Arrival Time - Original Burst Time
        return completionTime - arrivalTime - originalBurstTime;
    }

    // FCAI-Specific Methods
    public double getFcaiFactor() {
        return fcaiFactor; // Returns the FCAI factor for scheduling order
    }

    public void setFcaiFactor(double fcaiFactor) {
        // Updates the FCAI factor dynamically
        this.fcaiFactor = fcaiFactor;
    }

    public int getUpdatedQuantum() {
        return updatedQuantum; // Returns the current quantum used in FCAI scheduling
    }

    public void setUpdatedQuantum(int updatedQuantum) {
        // Dynamically adjusts the quantum during FCAI scheduling
        this.updatedQuantum = updatedQuantum;
    }

    public void calculateFcaiFactor(double v1, double v2) {

        // v1 and v2 are scaling factors for arrival time and burst time
        this.fcaiFactor = (10 - priority) + Math.ceil(arrivalTime / v1) + Math.ceil(burstTime / v2);
    }

    // Debugging and Visualization
    @Override
    public String toString() {
        // Provides a readable representation of the process
        return "Process{" +
                "name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", color='" + color + '\'' +
                ", pid=" + pid +
                ", completionTime=" + completionTime +
                ", originalBurstTime=" + originalBurstTime +
                ", fcaiFactor=" + fcaiFactor +
                ", updatedQuantum=" + updatedQuantum +
                '}';
    }
}
