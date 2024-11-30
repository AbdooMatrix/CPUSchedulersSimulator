package models;

import java.awt.Color;

public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private String color;
    private int completionTime;

    public Process(String name, int arrivalTime, int burstTime, int priority, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.color = color;
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
}