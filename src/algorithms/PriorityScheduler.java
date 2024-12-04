package algorithms;

import models.Process;
import models.ProcessExecution;
import java.util.ArrayList;
import java.util.List;

public class PriorityScheduler {
    public List<ProcessExecution> schedule(List<Process> processes, int contextSwitchingTime) {
        // Sort by priority first, then by arrival time if priorities are equal
        processes.sort((p1, p2) -> {
            // First, compare priority
            int priorityCompare = Integer.compare(p1.getPriority(), p2.getPriority());
            if (priorityCompare != 0) {
                return priorityCompare;
            }

            // If priority is the same, compare arrival time
            int arrivalCompare = Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
            if (arrivalCompare != 0) {
                return arrivalCompare;
            }

            // If both priority and arrival time are the same, compare burst time (shortest first)
            return Integer.compare(p1.getBurstTime(), p2.getBurstTime());
        });

        List<ProcessExecution> executionOrder = new ArrayList<>();
        int currentTime = 0;

        for (Process process : processes) {
            // Wait for the process to arrive if necessary
            if (process.getArrivalTime() > currentTime) {
                currentTime = process.getArrivalTime();
            }

            // Add execution entry with correct start time and duration
            executionOrder.add(new ProcessExecution(
                    process.getName(),
                    process.getBurstTime(),
                    process.getColor(),
                    process.getPid(),
                    process.getPriority(),
                    currentTime // Add startTime here
            ));

            // Update process completion time
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);

            // Add context switching time (simulated after each process completes)
            currentTime += contextSwitchingTime;
        }

        return executionOrder;
    }

    public void printResults(List<Process> processes, List<ProcessExecution> executionOrder) {
        System.out.println("Process Execution Order:");

        // Print execution order
        for (ProcessExecution pe : executionOrder) {
            System.out.println(pe.getProcessName());
        }

        // Print individual process results (waiting time, turnaround time)
        for (Process p : processes) {
            int waitTime = p.getWaitingTime(p.getCompletionTime());
            int turnaroundTime = p.getTurnaroundTime(p.getCompletionTime());
            System.out.println("Process: " + p.getName());
            System.out.println("Waiting Time: " + waitTime);
            System.out.println("Turnaround Time: " + turnaroundTime + '\n');
        }

        double avgWait = calculateAverageWaitingTime(processes);
        double avgTurnaround = calculateAverageTurnaroundTime(processes);

        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average Turnaround Time: " + avgTurnaround);
    }

    public double calculateAverageWaitingTime(List<Process> processes) {
        int totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.getWaitingTime(p.getCompletionTime());
        }
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        return averageWaitingTime;
    }

    public double calculateAverageTurnaroundTime(List<Process> processes) {
        int totalTurnaroundTime = 0;
        for (Process p : processes) {
            totalTurnaroundTime += p.getTurnaroundTime(p.getCompletionTime());
        }
        return (double) totalTurnaroundTime / processes.size();
    }
}
