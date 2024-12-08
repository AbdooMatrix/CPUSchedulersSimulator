package algorithms;

import models.Process;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ShortestRemainingTimeFirstScheduler {

    public List<Process> schedule(List<Process> processes, int contextSwitchingTime) {
        // Sort processes by arrival time initially
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        PriorityQueue<Process> queue = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()));
        List<Process> executionOrder = new ArrayList<>();

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

            currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            currentTime++;

            if (currentProcess.getBurstTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                executionOrder.add(currentProcess);
                completed++;
            } else {
                queue.add(currentProcess);
            }

            lastProcess = currentProcess;
        }

        return executionOrder;

}

    public void printResults(List<Process> executionOrder) {
        System.out.println("Process Execution Order:");
        executionOrder.forEach(p -> System.out.println(p.getName()));

        // Print individual process results (waiting time, turnaround time)
        for (Process p : executionOrder) {
            int waitTime = p.getWaitingTimeSRTF(p.getCompletionTime());
            int turnaroundTime = p.getTurnaroundTime(p.getCompletionTime());
            System.out.println("Process: " + p.getName());
            System.out.println("Waiting Time: " + waitTime);
            System.out.println("Turnaround Time: " + turnaroundTime + '\n');
        }

        double avgWait = executionOrder.stream()
                .mapToInt(p -> p.getWaitingTimeSRTF(p.getCompletionTime()))
                .average()
                .orElse(0);

        double avgTurnaround = executionOrder.stream()
                .mapToInt(p -> p.getTurnaroundTime(p.getCompletionTime()))
                .average()
                .orElse(0);

        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average Turnaround Time: " + avgTurnaround);
    }
}





