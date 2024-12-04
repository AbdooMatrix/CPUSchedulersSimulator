import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import models.ProcessExecution;

public class SchedulingGraph extends JPanel {
    private final List<ProcessExecution> schedule;
    private final String scheduleName;
    private final double averageWaitingTime;
    private final double averageTurnaroundTime;

    public SchedulingGraph(List<ProcessExecution> schedule, String scheduleName, double awt, double ata) {
        this.schedule = schedule;
        this.scheduleName = scheduleName;
        this.averageWaitingTime = awt;
        this.averageTurnaroundTime = ata;
        setPreferredSize(new Dimension(1400, 900));
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int xOffset = 80;
        int yOffset = 80;
        int barHeight = 30;
        int barSpacing = 60;
        int timeUnitWidth = 40;

        // Title
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 26));
        g2d.drawString("CPU Scheduling Graph", getWidth() / 3, 40);

        int maxTime = 0;
        for (ProcessExecution exec : schedule) {
            maxTime = Math.max(maxTime, exec.startTime + exec.duration);
        }

        // Draw Grid Lines
        g2d.setColor(new Color(100, 100, 100));
        for (int i = 0; i <= maxTime; i++) {
            int xPosition = xOffset + i * timeUnitWidth;
            g2d.drawLine(xPosition, yOffset - 20, xPosition, yOffset + schedule.size() * barSpacing + 20);
        }

        // Draw Process Bars
        for (int i = 0; i < schedule.size(); i++) {
            ProcessExecution exec = schedule.get(i);
            int barStartX = xOffset + exec.startTime * timeUnitWidth;
            int yPosition = yOffset + i * barSpacing;

            // Rounded process bar
            g2d.setColor(exec.color);
            g2d.fill(new RoundRectangle2D.Double(barStartX, yPosition, exec.duration * timeUnitWidth, barHeight, 10, 10));

            // Process Label
            g2d.setColor(Color.BLACK);
            g2d.drawString(exec.processName, barStartX + 5, yPosition + barHeight / 2 + 5);
        }

        // Time Axis
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        for (int i = 0; i <= maxTime; i++) {
            int xPosition = xOffset + i * timeUnitWidth;
            g2d.drawLine(xPosition, yOffset - 10, xPosition, yOffset + schedule.size() * barSpacing);
            g2d.drawString(String.valueOf(i), xPosition - 5, yOffset + schedule.size() * barSpacing + 20);
        }

        // Legend
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Processes Information", getWidth() - 380, yOffset);

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        int legendY = yOffset + 30;
        for (ProcessExecution exec : schedule) {
            g2d.setColor(exec.color);
            g2d.fillRect(getWidth() - 360, legendY, 30, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Name: " + exec.processName + " PID: " + exec.pid + " Priority: " + exec.priority, getWidth() - 320, legendY + 15);
            legendY += 30;
        }

        // Statistics Section
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Statistics", xOffset, yOffset + schedule.size() * barSpacing + 60);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.drawString("Schedule Name: " + scheduleName, xOffset, yOffset + schedule.size() * barSpacing + 90);
        g2d.drawString("AWT: " + String.format("%.2f", averageWaitingTime), xOffset, yOffset + schedule.size() * barSpacing + 120);
        g2d.drawString("ATA: " + String.format("%.2f", averageTurnaroundTime), xOffset, yOffset + schedule.size() * barSpacing + 150);
    }

    public static void createAndShowGUI(List<ProcessExecution> schedule, String scheduleName, double awt, double ata) {
        JFrame frame = new JFrame("Scheduling Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SchedulingGraph chart = new SchedulingGraph(schedule, scheduleName, awt, ata);
        frame.add(chart); // Add directly to the frame
        frame.setSize(1400, 900);
        frame.setVisible(true);
    }
}
