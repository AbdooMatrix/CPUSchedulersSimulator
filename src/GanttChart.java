import models.ProcessExecution;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;

public class GanttChart extends JPanel {
    private final List<ProcessExecution> schedule;
    private final String scheduleName;
    private final double averageWaitingTime;
    private final double averageTurnaroundTime;

    public GanttChart(List<ProcessExecution> schedule, String scheduleName, double awt, double ata) {
        this.schedule = schedule;
        this.scheduleName = scheduleName;
        this.averageWaitingTime = awt;
        this.averageTurnaroundTime = ata;
        setBackground(Color.DARK_GRAY);
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
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("CPU Scheduling Graph", xOffset, 40);

        // Calculate max execution time
        int maxTime = 0;
        for (ProcessExecution exec : schedule) {
            maxTime = Math.max(maxTime, exec.startTime + exec.duration);
        }

        // Extract unique processes
        Set<String> uniqueProcesses = new HashSet<>();
        for (ProcessExecution exec : schedule) {
            uniqueProcesses.add(exec.processName);
        }
        int processCount = uniqueProcesses.size();

        // Adjust panel size
        int panelWidth = xOffset + (maxTime + 1) * timeUnitWidth + 100;
        int panelHeight = yOffset + processCount * barSpacing + 200;
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        // Draw gridlines and time labels
        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        for (int i = 0; i <= maxTime; i++) {
            int xPosition = xOffset + i * timeUnitWidth;

            // Draw gridline
            g2d.drawLine(xPosition, yOffset - 20, xPosition, yOffset + processCount * barSpacing + 20);

            // Draw centered time label
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(i), xPosition - 5, yOffset + processCount * barSpacing + 30);
        }

        // Draw process bars and labels
        int processIndex = 0;
        for (String processName : uniqueProcesses) {
            for (ProcessExecution exec : schedule) {
                if (exec.processName.equals(processName)) {
                    int barStartX = xOffset + exec.startTime * timeUnitWidth;
                    int yPosition = yOffset + processIndex * barSpacing;

                    // Draw rounded process bar
                    g2d.setColor(exec.color);
                    g2d.fill(new RoundRectangle2D.Double(barStartX, yPosition, exec.duration * timeUnitWidth, barHeight, 10, 10));

                    // Draw process name inside the bar
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(exec.processName, barStartX + 5, yPosition + barHeight / 2 + 5);
                }
            }

            // Draw process label to the left of the lane
            g2d.setColor(Color.WHITE);
            g2d.drawString("Process: " + processName, xOffset - 90, yOffset + processIndex * barSpacing + barHeight / 2 + 5);

            processIndex++;
        }
    }

    public static void createAndShowGUI(List<ProcessExecution> schedule, String scheduleName, double awt, double ata) {
        JFrame frame = new JFrame("Scheduling Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GanttChart gridPanel = new GanttChart(schedule, scheduleName, awt, ata);
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(1200, 800));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel statsPanel = createStatsPanel(scheduleName, awt, ata);
        JPanel legendPanel = createLegendPanel(schedule);

        mainPanel.add(statsPanel, BorderLayout.SOUTH);
        mainPanel.add(legendPanel, BorderLayout.EAST);

        frame.add(mainPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private static JPanel createStatsPanel(String scheduleName, double awt, double ata) {
        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.DARK_GRAY);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel statsLabel = new JLabel("Statistics");
        statsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statsLabel.setForeground(Color.RED);
        statsPanel.add(statsLabel);

        statsPanel.add(createLabel("Schedule Name: " + scheduleName));
        statsPanel.add(createLabel("Average Waiting Time: " + String.format("%.2f", awt)));
        statsPanel.add(createLabel("Average Turnaround Time: " + String.format("%.2f", ata)));

        return statsPanel;
    }

    private static JPanel createLegendPanel(List<ProcessExecution> schedule) {
        JPanel legendPanel = new JPanel(new BorderLayout());
        legendPanel.setBackground(Color.DARK_GRAY);
        legendPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        JLabel titleLabel = new JLabel("Processes Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.RED);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        legendPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel processListPanel = new JPanel();
        processListPanel.setBackground(Color.DARK_GRAY);
        processListPanel.setLayout(new BoxLayout(processListPanel, BoxLayout.Y_AXIS));

        Set<String> uniqueProcesses = new HashSet<>();
        for (ProcessExecution exec : schedule) {
            if (uniqueProcesses.add(exec.processName)) {
                JPanel processInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                processInfoPanel.setBackground(Color.DARK_GRAY);

                JLabel colorBox = new JLabel();
                colorBox.setOpaque(true);
                colorBox.setBackground(exec.color);
                colorBox.setPreferredSize(new Dimension(20, 20));

                JLabel processLabel = new JLabel(String.format(" Name: %s | PID: %s | Priority: %s",
                        exec.processName, exec.pid, exec.priority));
                processLabel.setFont(new Font("Arial", Font.BOLD, 16));
                processLabel.setForeground(Color.WHITE);

                processInfoPanel.add(colorBox);
                processInfoPanel.add(processLabel);
                processListPanel.add(processInfoPanel);
            }
        }

        legendPanel.add(new JScrollPane(processListPanel), BorderLayout.CENTER);
        return legendPanel;
    }

    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }
}
