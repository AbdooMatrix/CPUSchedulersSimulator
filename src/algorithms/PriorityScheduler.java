package algorithms;

import models.Process;
import models.ProcessExecution;
import java.util.ArrayList;
import java.util.List;

public class PriorityScheduler {
    public List<ProcessExecution> schedule(List<Process> processes, int contextSwitchingTime) {
        processes.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

        List<ProcessExecution> executionOrder = new ArrayList<>();
        int currentTime = 0;

        for (Process process : processes) {
            if (process.getArrivalTime() > currentTime) {
                currentTime = process.getArrivalTime(); // Waiting for the process to arrive
            }

            // Add execution entry to the schedule (name, burst time, color)
            executionOrder.add(new ProcessExecution(process.getName(), process.getBurstTime(), process.getColor()));

            // Set completion time for the process
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);

            // Add context switching time after each process execution
            currentTime += contextSwitchingTime;
        }

        return executionOrder;
    }

    public void printResults(List<Process> processes) {
        System.out.println("Process Execution Order:");
        processes.forEach(p -> System.out.println(p.getName()));

        // Print individual process results (waiting time, turnaround time)
        for (Process p : processes) {
            int waitTime = p.getWaitingTime(p.getCompletionTime());
            int turnaroundTime = p.getTurnaroundTime(p.getCompletionTime());
            System.out.println("Process: " + p.getName());
            System.out.println("Waiting Time: " + waitTime);
            System.out.println("Turnaround Time: " + turnaroundTime + '\n');
        }

        double avgWait = processes.stream()
                .mapToInt(p -> p.getWaitingTime(p.getCompletionTime()))
                .average()
                .orElse(0);

        double avgTurnaround = processes.stream()
                .mapToInt(p -> p.getTurnaroundTime(p.getCompletionTime()))
                .average()
                .orElse(0);

        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average Turnaround Time: " + avgTurnaround);
    }
}
