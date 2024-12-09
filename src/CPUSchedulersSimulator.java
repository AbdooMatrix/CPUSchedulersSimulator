import algorithms.FCAIScheduler;
import algorithms.PriorityScheduler;
import algorithms.ShortestJobFirstScheduler;
import algorithms.ShortestRemainingTimeFirstScheduler;
import models.Process;
import models.ProcessExecution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class CPUSchedulersSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();
        Random random = new Random();

        System.out.print("Enter number of processes: ");
        int numProcesses = scanner.nextInt();

        // Input processes
        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Process " + (i + 1) + ":");
            System.out.print("  Name: ");
            String name = scanner.next();
            System.out.print("  Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("  Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("  Priority: ");
            int priority = scanner.nextInt();
            String colorHex = generateRandomColor(random);
            int pid = i + 1; // Generate unique PID
            processes.add(new Process(name, arrivalTime, burstTime, priority, colorHex, pid));
            System.out.println("-------------------------------------");
        }

        System.out.println("Select the Scheduling Algorithm:");
        System.out.println("1. Non-preemptive Priority Scheduling");
        System.out.println("2. Non-preemptive Shortest Job First (SJF)");
        System.out.println("3. Shortest Remaining Time First (SRTF)");
        System.out.println("4. FCAI Scheduling");
        System.out.print("Please enter your choice: ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > 4) {
            System.out.println("Invalid choice. Exiting program.");
            return;
        }

        System.out.print("Enter context switching time: ");
        int contextSwitchingTime = scanner.nextInt();

        List<ProcessExecution> schedule = null;
        String scheduleName = null;
        double averageWaitingTime = 0;
        double averageTurnaroundTime = 0;

        switch (choice) {
            case 1:
                // Use PriorityScheduler
                PriorityScheduler priorityScheduler = new PriorityScheduler();
                scheduleName = "Process Execution by Priority Scheduling";
                schedule = priorityScheduler.schedule(processes, contextSwitchingTime);
                averageWaitingTime = priorityScheduler.calculateAverageWaitingTime(processes);
                averageTurnaroundTime = priorityScheduler.calculateAverageTurnaroundTime(processes);

                // Print results (with execution order passed in)
                priorityScheduler.printResults(processes, schedule);
                break;

            case 2:
                // Use ShortestJobFirstScheduler
                ShortestJobFirstScheduler shortestJobFirstScheduler = new ShortestJobFirstScheduler(processes, contextSwitchingTime);
                scheduleName = "Process Execution by Shortest Job First Scheduling";
                schedule = shortestJobFirstScheduler.schedule();
                averageWaitingTime = shortestJobFirstScheduler.calculateAverageWaitingTime();
                averageTurnaroundTime = shortestJobFirstScheduler.calculateAverageTurnaroundTime();
                break;

            case 3:
                List<Process>exc_order=new ArrayList<>();
                ShortestRemainingTimeFirstScheduler shortestRemainingTimeFirstScheduler = new ShortestRemainingTimeFirstScheduler();
                scheduleName = "Process Execution by Shortest Remaining Time First Scheduling";
                schedule = shortestRemainingTimeFirstScheduler.schedule(processes, contextSwitchingTime);
                averageWaitingTime = shortestRemainingTimeFirstScheduler.calculateAverageWaitingTime(processes);
                averageTurnaroundTime = shortestRemainingTimeFirstScheduler.calculateAverageTurnaroundTime(processes);
                exc_order= shortestRemainingTimeFirstScheduler.executionOrder2;
                // Print results (with execution order passed in)
                shortestRemainingTimeFirstScheduler.printResults(exc_order, schedule);
                break;

            case 4:
                for (Process p : processes) {
                    System.out.print("Enter Round Robin Quantum for " + p.getName() + " : ");
                    int quantum = scanner.nextInt();
                    p.setUpdatedQuantum(quantum);
                }

                FCAIScheduler fcaiScheduler = new FCAIScheduler(processes);
                scheduleName = "Process Execution by FCAI Scheduling";
                schedule = fcaiScheduler.schedule(contextSwitchingTime);

                averageWaitingTime = fcaiScheduler.calculateAverageWaitingTime(processes);
                averageTurnaroundTime = fcaiScheduler.calculateAverageTurnaroundTime(processes);

                fcaiScheduler.printResults(processes, schedule);
                break;

        }

        // If there is a valid schedule, pass it to GUI creation
        if (schedule != null) {
            GanttChart.createAndShowGUI(
                    schedule,
                    scheduleName,
                    averageWaitingTime,
                    averageTurnaroundTime
            );
        }
    }

    // Method to generate random hex color
    private static String generateRandomColor(Random random) {
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }
}