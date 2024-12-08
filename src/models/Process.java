package models;

import java.awt.Color;

public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private String color;
    private int pid;
    private int completionTime;
    private int originalBurstTime = 0;
    private double fcaiFactor; // For FCAI Scheduler
    private int updatedQuantum;

    // Constructor
    public Process(String name, int arrivalTime, int burstTime, int priority, String color, int pid) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
        this.pid = pid;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public Color getColor() {
        return Color.decode(color);
    }

    public int getPid() {
        return pid;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getOriginalBurstTime() {
        return originalBurstTime;
    }

    public void setOriginalBurstTime(int originalBurstTime) {
        this.originalBurstTime = originalBurstTime;
    }

    public int getWaitingTime(int completionTime) {
        return completionTime - arrivalTime - burstTime;
    }

    public int getTurnaroundTime(int completionTime) {
        return completionTime - arrivalTime;
    }

    public int getWaitingTimeSRTF(int completionTime) {
        return completionTime - arrivalTime - originalBurstTime;
    }

    // FCAI-specific methods
    public double getFcaiFactor() {
        return fcaiFactor;
    }

    public void setFcaiFactor(double fcaiFactor) {
        this.fcaiFactor = fcaiFactor;
    }

    public int getUpdatedQuantum() {
        return updatedQuantum;
    }

    public void setUpdatedQuantum(int updatedQuantum) {
        this.updatedQuantum = updatedQuantum;
    }

    public void calculateFcaiFactor(double v1, double v2) {
        this.fcaiFactor = (10 - priority) + (arrivalTime / v1) + (burstTime / v2);
    }

    // Override toString() for debugging
    @Override
    public String toString() {
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
