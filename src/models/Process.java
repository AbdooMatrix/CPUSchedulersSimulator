package models;

import java.awt.Color;

public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private String color;
    private int pid; // Added Process ID
    private int completionTime;

    private int originalBurstTime=0;

    // Updated Constructor to include PID
    public Process(String name, int arrivalTime, int burstTime, int priority, String color, int pid) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
        this.pid = pid;
    }


    public Process(String name, int arrivalTime, int burstTime, int priority, String color, int pid , double quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
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

    public int getWaitingTime(int completionTime) {
        return completionTime - arrivalTime - burstTime;
    }

    public int getTurnaroundTime(int completionTime) {
        return completionTime - arrivalTime;
    }



    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }
    public int getWaitingTime_srtf(int completionTime) {
        return completionTime - arrivalTime - originalBurstTime;
    }
    public void setOriginalBurstTime(int originalBurstTime) {
        this.originalBurstTime = originalBurstTime;
    }
    public int getoriginalBurstTime() {return this.originalBurstTime;}
}
