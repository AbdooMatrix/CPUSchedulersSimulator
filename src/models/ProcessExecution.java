package models;

import java.awt.*;

public class ProcessExecution {
    public String processName;
    public int duration;
    public Color color;

    public ProcessExecution(String processName, int duration, Color color) {
        this.processName = processName;
        this.duration = duration;
        this.color = color;
    }
}
