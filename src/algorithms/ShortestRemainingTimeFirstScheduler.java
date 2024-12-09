package algorithms;

import models.Process;
import models.ProcessExecution;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;


public class ShortestRemainingTimeFirstScheduler {
    public static int MAX_WAIT_TIME=20;
    // Scheduling method: Handles only scheduling logic
    public List<Process> executionOrder2 = new ArrayList<>();
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
                if (process.getWaitingTime(currentTime) > MAX_WAIT_TIME && process.getBurstTime() > 0) {
                    // Immediate execution for the starved process
                    System.out.println("Process " + process.getName() + " starved! Executing immediately.");
                    currentTime += contextSwitchingTime;
                    currentTime += process.getBurstTime();
                    process.setBurstTime(0);
                    process.setCompletionTime(currentTime);
                    executionOrder.add(new ProcessExecution(process.getName(), 1,process.getColor(),process.getPid(),process.getPriority(),currentTime));
                    executionOrder2.add(process);
                    completed++;
                    queue.remove(process);

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
            executionOrder.add(new ProcessExecution(
                    currentProcess.getName(),
                    1, // Each unit of execution is 1
                    currentProcess.getColor(),
                    currentProcess.getPid(),
                    currentProcess.getPriority(),
                    currentTime
            ));

            // Add ProcessExecution entry for current process

            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            currentTime++;

            if (currentProcess.getBurstTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                completed++;
                executionOrder2.add(currentProcess);

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
        executionOrder.forEach(pe -> System.out.println(pe.processName));

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

