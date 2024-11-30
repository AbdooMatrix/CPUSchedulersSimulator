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
                currentTime = process.getArrivalTime();
            }
            executionOrder.add(new ProcessExecution(process.getName(), process.getBurstTime(), process.getColor()));
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);
            currentTime += contextSwitchingTime;
        }

        return executionOrder;
    }

    public void printResults(List<Process> processes) {
        System.out.println("Process Execution Order:");
        processes.forEach(p -> System.out.println(p.getName()));
        double avgWait = processes.stream().mapToInt(p -> p.getWaitingTime(p.getCompletionTime())).average().orElse(0);
        double avgTurnaround = processes.stream().mapToInt(p -> p.getTurnaroundTime(p.getCompletionTime())).average().orElse(0);
        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average Turnaround Time: " + avgTurnaround);
    }
}