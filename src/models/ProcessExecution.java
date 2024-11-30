package models;

import java.awt.*;

public class ProcessExecution {
    public String processName;
    public int duration;
    public Color color;
    public int pid;
    public int priority;

    // Constructor with PID and Priority
    public ProcessExecution(String processName, int duration, Color color, int pid, int priority) {
        this.processName = processName;
        this.duration = duration;
        this.color = color;
        this.pid = pid;
        this.priority = priority;
    }

    // Constructor without PID and Priority for backward compatibility
    public ProcessExecution(String processName, int duration, Color color) {
        this(processName, duration, color, 0, 0); // Default PID and priority
    }
}
