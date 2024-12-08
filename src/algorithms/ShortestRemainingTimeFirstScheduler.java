package algorithms;

import models.Process;
import models.ProcessExecution;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestRemainingTimeFirstScheduler {

    // Scheduling method: Handles only scheduling logic
    public List<ProcessExecution> schedule(List<Process> processes, int contextSwitchingTime) {
        // Sort processes by arrival time initially
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        PriorityQueue<Process> queue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()));
        List<ProcessExecution> executionOrder = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        Process lastProcess = null;

        while (completed < processes.size()) {
            for (Process process : processes) {
                if (!queue.contains(process) && process.getArrivalTime() <= currentTime && process.getBurstTime() > 0) {
                    if (process.getOriginalBurstTime() == 0) {
                        process.setOriginalBurstTime(process.getBurstTime());
                    }
                    queue.add(process);
                }
            }

            if (queue.isEmpty()) {
                currentTime++;
                lastProcess = null;
                continue;
            }

            Process currentProcess = queue.poll();

            if (lastProcess != null && !lastProcess.equals(currentProcess)) {
                currentTime += contextSwitchingTime;
            }

            // Add ProcessExecution entry for current process
            executionOrder.add(new ProcessExecution(
                    currentProcess.getName(),
                    1, // Each unit of execution is 1
                    currentProcess.getColor(),
                    currentProcess.getPid(),
                    currentProcess.getPriority(),
                    currentTime
            ));

            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            currentTime++;

            if (currentProcess.getBurstTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                completed++;
            } else {
                queue.add(currentProcess);
            }

            lastProcess = currentProcess;
        }

        return executionOrder;
    }

    // Printing results and calling average calculation methods
    public void printResults(List<Process> processes, List<ProcessExecution> executionOrder) {
        System.out.println("Process Execution Order:");
        executionOrder.forEach(pe -> System.out.println(pe.getProcessName()));

        System.out.println("\nProcess Details:");
        for (Process p : processes) {
            int waitTime = p.getWaitingTimeSRTF(p.getCompletionTime());
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

    // Calculate average waiting time
    public double calculateAverageWaitingTime(List<Process> processes) {
        int totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.getWaitingTimeSRTF(p.getCompletionTime());
        }
        return (double) totalWaitingTime / processes.size();
    }

    // Calculate average turnaround time
    public double calculateAverageTurnaroundTime(List<Process> processes) {
        int totalTurnaroundTime = 0;
        for (Process p : processes) {
            totalTurnaroundTime += p.getTurnaroundTime(p.getCompletionTime());
        }
        return (double) totalTurnaroundTime / processes.size();
    }
}
