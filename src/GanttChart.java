import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.ProcessExecution;

public class GanttChart extends JPanel {
    private final List<ProcessExecution> schedule;

    public GanttChart(List<ProcessExecution> schedule) {
        this.schedule = schedule;
        setPreferredSize(new Dimension(800, 800)); // Set the initial panel size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int xOffset = 100; // Horizontal start point
        int yOffset = 50;  // Vertical start point
        int barHeight = 30;
        int barSpacing = 10;

        // Title
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("CPU Scheduling Graph", xOffset, 30);

        // Draw process bars
        for (int i = 0; i < schedule.size(); i++) {
            ProcessExecution exec = schedule.get(i);
            g.setColor(exec.color); // Process color
            int width = exec.duration * 20; // Adjust scaling
            int yPosition = yOffset + i * (barHeight + barSpacing);

            g.fillRect(xOffset, yPosition, width, barHeight); // Fill bar
            g.setColor(Color.BLACK);
            g.drawRect(xOffset, yPosition, width, barHeight); // Outline bar
            g.drawString(exec.processName, xOffset + 5, yPosition + barHeight - 5); // Label

            g.setColor(Color.BLACK);
            g.drawString("Process" + i, 10, yPosition + barHeight - 10); // Label Process No.
        }

        // Legend drawing
        g.setColor(Color.RED);
        g.drawString("Processes Information", 650, yOffset);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        int legendY = yOffset + 20;
        for (int i = 0; i < schedule.size(); i++) {
            ProcessExecution exec = schedule.get(i);
            g.setColor(exec.color);
            g.fillRect(650, legendY, 20, 20);
            g.setColor(Color.BLACK);
            g.drawRect(650, legendY, 20, 20);
            g.drawString(exec.processName + " | PID: " + exec.pid + " | Priority: " + exec.priority,
                    680, legendY + 15);
            legendY += 30;
        }
    }

    public static void createAndShowGUI(List<ProcessExecution> schedule) {
        JFrame frame = new JFrame("Gantt Chart Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GanttChart chart = new GanttChart(schedule);
        JScrollPane scrollPane = new JScrollPane(chart);
        frame.add(scrollPane);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }
}
