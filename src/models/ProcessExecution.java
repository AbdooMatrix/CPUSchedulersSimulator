package models;

import java.awt.*;

public class ProcessExecution {
    public String processName;
    public int duration;
    public Color color;
    public int pid;
    public int priority;
    public int startTime; // Added to track when execution starts

    // Updated Constructor
    public ProcessExecution(String processName, int duration, Color color, int pid, int priority, int startTime) {
        this.processName = processName;
        this.duration = duration;
        this.color = color;
        this.pid = pid;
        this.priority = priority;
        this.startTime = startTime;
    }
}
