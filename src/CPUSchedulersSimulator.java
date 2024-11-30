import algorithms.PriorityScheduler;
import models.Process;

import java.util.ArrayList;
import java.util.Scanner;

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
            System.out.print("Color: ");
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

        if (choice == 4) {
            System.out.print("Enter Round Robin Quantum for FCAI: ");
            int quantum = scanner.nextInt();
            // Call FCAI Scheduler with quantum and contextSwitchingTime
        }

        switch (choice) {
            case 1:
                // Call Priority Scheduler
                PriorityScheduler scheduler = new PriorityScheduler();
                scheduler.schedule(processes);
                break;
            case 2:
                // Call SJF Scheduler
                break;
            case 3:
                // Call SRTF Scheduler
                break;
            case 4:
                // Call FCAI Scheduler
                break;
            default:
                System.out.println("Invalid choice.");
        }

        // Display outputs: Execution order, Waiting Time, Turnaround Time, Averages
    }
}
