package algorithms;

import models.Process;
import models.ProcessExecution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShortestJobFirstScheduler {

    public List<ProcessExecution> schedule(List<Process> processes, int contextSwitchingTime) {
        // Sort processes by arrival time, then burst time, then priority
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getBurstTime)
                .thenComparingInt(Process::getPriority));

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
        return (double) totalWaitingTime / processes.size();
    }

    public double calculateAverageTurnaroundTime(List<Process> processes) {
        int totalTurnaroundTime = 0;
        for (Process p : processes) {
            totalTurnaroundTime += p.getTurnaroundTime(p.getCompletionTime());
        }
        return (double) totalTurnaroundTime / processes.size();
    }
}
