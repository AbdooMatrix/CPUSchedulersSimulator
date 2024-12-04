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
        setBackground(Color.DARK_GRAY); // Revert background to original color
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int xOffset = 100;
        int yOffset = 80;
        int barHeight = 30;
        int barSpacing = 40;
        int timeUnitWidth = 40;

        // Title
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 30)); // Bigger and bolder font
        g2d.drawString("CPU Scheduling Graph", getWidth() / 3, 40);

        int maxTime = 0;
        for (ProcessExecution exec : schedule) {
            maxTime = Math.max(maxTime, exec.startTime + exec.duration);
        }

        // Draw Grid Lines
        g2d.setColor(new Color(100, 100, 100)); // Original grid line color
        for (int i = 0; i <= maxTime; i++) {
            int xPosition = xOffset + i * timeUnitWidth;
            g2d.drawLine(xPosition, yOffset - 20, xPosition, yOffset + schedule.size() * barSpacing + 20);
        }

        // Draw Process Bars and Labels
        for (int i = 0; i < schedule.size(); i++) {
            ProcessExecution exec = schedule.get(i);
            int barStartX = xOffset + (exec.startTime - 1) * timeUnitWidth;
            int yPosition = yOffset + i * barSpacing;

            // Rounded process bar
            g2d.setColor(exec.color);
            g2d.fill(new RoundRectangle2D.Double(barStartX, yPosition, exec.duration * timeUnitWidth, barHeight, 10, 10));

            // Process Label in front of the lane
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16)); // Bigger and bolder font
            g2d.drawString("Process: " + exec.processName, xOffset - 90, yPosition + barHeight / 2 + 5);

            // Process Label on the bar
            g2d.setColor(Color.BLACK);
            g2d.drawString(exec.processName, barStartX + 5, yPosition + barHeight / 2 + 5);
        }

        // Time Axis
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16)); // Bigger and bolder font
        for (int i = 0; i <= maxTime; i++) {
            int xPosition = xOffset + i * timeUnitWidth;
            g2d.drawLine(xPosition, yOffset - 10, xPosition, yOffset + schedule.size() * barSpacing);
            g2d.drawString(String.valueOf(i), xPosition - 5, yOffset + schedule.size() * barSpacing + 20);
        }
    }

    public static void createAndShowGUI(List<ProcessExecution> schedule, String scheduleName, double awt, double ata) {
        JFrame frame = new JFrame("Scheduling Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);

        // Create the grid panel
        SchedulingGraph gridPanel = new SchedulingGraph(schedule, scheduleName, awt, ata);
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(1200, 600));

        // Create the legend panel
        JPanel legendPanel = new JPanel();
        legendPanel.setBackground(Color.DARK_GRAY);
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20)); // Add space above and after the legend panel

        JLabel titleLabel = new JLabel("Processes Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Bigger and bolder font
        titleLabel.setForeground(Color.RED);
        legendPanel.add(titleLabel);

        for (ProcessExecution exec : schedule) {
            JLabel processLabel = new JLabel("Name: " + exec.processName + " | PID: " + exec.pid + " | Priority: " + exec.priority);
            processLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Bigger and bolder font
            processLabel.setForeground(Color.WHITE);
            legendPanel.add(processLabel);
        }

        // Create the statistics panel
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.DARK_GRAY);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add space around the statistics panel

        JLabel statsLabel = new JLabel("Statistics");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Bigger and bolder font
        statsLabel.setForeground(Color.RED);
        statsPanel.add(statsLabel);

        JLabel scheduleNameLabel = new JLabel("Schedule Name: " + scheduleName);
        scheduleNameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Bigger and bolder font
        scheduleNameLabel.setForeground(Color.WHITE);
        statsPanel.add(scheduleNameLabel);

        JLabel awtLabel = new JLabel("Average Waiting Time: " + String.format("%.2f", awt)); // Expanded abbreviation
        awtLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Bigger and bolder font
        awtLabel.setForeground(Color.WHITE);
        statsPanel.add(awtLabel);

        JLabel ataLabel = new JLabel("Average Turnaround Time: " + String.format("%.2f", ata)); // Expanded abbreviation
        ataLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Bigger and bolder font
        ataLabel.setForeground(Color.WHITE);
        statsPanel.add(ataLabel);

        // Add components to the main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        mainPanel.add(legendPanel, BorderLayout.EAST);

        frame.add(mainPanel);
        frame.setSize(1400, 900);
        frame.setVisible(true);
    }
}
