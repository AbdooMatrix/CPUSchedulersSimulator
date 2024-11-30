package algorithms;

import models.Process;
import java.util.ArrayList;
import java.util.List;
import models.ProcessExecution;

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
            currentTime += process.getBurstTime() + contextSwitchingTime;
        }

        return executionOrder;
    }
}
