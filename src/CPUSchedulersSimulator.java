import algorithms.PriorityScheduler;
import models.Process;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.ProcessExecution;

public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();

        System.out.print("Enter number of processes: ");
        int numProcesses = scanner.nextInt();

        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Process " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.next();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority: ");
            int priority = scanner.nextInt();
            System.out.print("Color (Hex code, e.g., #FF5733): ");
            String color = scanner.next();
            processes.add(new Process(name, arrivalTime, burstTime, priority, color));
        }

        System.out.println("Select the Scheduling Algorithm:");
        System.out.println("1. Non-preemptive Priority Scheduling");
        System.out.println("2. Non-preemptive Shortest Job First (SJF)");
        System.out.println("3. Shortest Remaining Time First (SRTF)");
        System.out.println("4. FCAI Scheduling");
        int choice = scanner.nextInt();

        System.out.print("Enter context switching time: ");
        int contextSwitchingTime = scanner.nextInt();

        List<ProcessExecution> schedule = null;

        switch (choice) {
            case 1:
                PriorityScheduler priorityScheduler = new PriorityScheduler();
                schedule = priorityScheduler.schedule(processes, contextSwitchingTime);
                priorityScheduler.printResults(processes);
                break;
            case 2:
                // Call SJF Scheduler and get execution order
                break;
            case 3:
                // Call SRTF Scheduler and get execution order
                break;
            case 4:
                System.out.print("Enter Round Robin Quantum for FCAI: ");
                int quantum = scanner.nextInt();
                // Call FCAI Scheduler and get execution order
                break;
            default:
                System.out.println("Invalid choice.");
        }

//        if (schedule != null) {
//            // Display the Gantt chart
//            JFrame frame = new JFrame("Graphical representation of Processes execution order");
//            GanttChart chart = new GanttChart(schedule);
//            frame.add(chart);
//            frame.setSize(800, 200);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setVisible(true);
//        }
    }
}
