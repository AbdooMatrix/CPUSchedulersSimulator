package algorithms;

import models.Process;
import java.util.List;

public class PriorityScheduler {
    public void schedule(List<Process> processes) {
        processes.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

        int currentTime = 0;
        for (Process process : processes) {
            if (process.getArrivalTime() > currentTime) {
                currentTime = process.getArrivalTime();
            }
            process.setWaitingTime(currentTime - process.getArrivalTime());
            process.setTurnaroundTime(process.getWaitingTime() + process.getBurstTime());
            currentTime += process.getBurstTime();
        }

        printResults(processes);
    }

    private void printResults(List<Process> processes) {
        System.out.println("Process Execution Order:");
        processes.forEach(p -> System.out.println(p.getName()));
        double avgWait = processes.stream().mapToInt(Process::getWaitingTime).average().orElse(0);
        double avgTurnaround = processes.stream().mapToInt(Process::getTurnaroundTime).average().orElse(0);
        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average Turnaround Time: " + avgTurnaround);
    }
}
