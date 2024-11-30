import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.ProcessExecution;

class GanttChart extends JPanel {
    private final List<ProcessExecution> schedule;

    public GanttChart(List<ProcessExecution> schedule) {
        this.schedule = schedule;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50; // Starting point of the timeline
        int y = 50; // Vertical position of the chart
        int height = 50; // Height of each process bar

        for (ProcessExecution exec : schedule) {
            g.setColor(exec.color); // Set the process color
            int width = exec.duration * 20; // Scale duration for better visibility
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height); // Draw the outline of the bar
            g.drawString(exec.processName, x + 5, y + height - 10); // Label the process
            x += width; // Move to the next time slot
        }

        g.setColor(Color.BLACK);
        g.drawString("Timeline", 50, y + height + 20);
    }
}
