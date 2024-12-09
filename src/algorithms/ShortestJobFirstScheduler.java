package algorithms;

import models.Process;
import models.ProcessExecution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class ShortestJobFirstScheduler {

    private List<Process> processList;
    private int contextSwitchTime;
    private static final int MAX_WAIT_TIME = 20; // Define maximum wait time to avoid starvation

    public ShortestJobFirstScheduler(List<Process> processList, int contextSwitchTime) {
        this.processList = processList;
        this.contextSwitchTime = contextSwitchTime;
    }

    public List<ProcessExecution> schedule() {
        // Sort processes by arrival time first, then burst time
        processList.sort(Comparator.comparingInt(Process::getArrivalTime).thenComparingInt(Process::getBurstTime).thenComparing(Process::getPriority));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        List<Process> executionOrder = new ArrayList<>();
        List<ProcessExecution> executionOrder2 = new ArrayList<>();
          boolean isFirstProcess = true; // Add a flag to track the first process

        while (!processList.isEmpty()) {
            // Filter processes that have arrived by the current time
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : processList) {
                if (p.getArrivalTime() <= currentTime) {
                    availableProcesses.add(p);
                }
            }

            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            // Check for starvation in the available processes
            Process starvedProcess = null;
            for (Process process : availableProcesses) {
                if (process.getWaitingTime(currentTime) > MAX_WAIT_TIME && process.getBurstTime() > 0) {
                    starvedProcess = process;
                    break;
                }
            }

            Process selectedProcess;
            if (starvedProcess != null) {
                // Handle starvation: prioritize the starved process
                System.out.println("Process " + starvedProcess.getName() + " starved! Executing immediately.");
                selectedProcess = starvedProcess;
            }
            else {
                // Select the process with the shortest burst time
                selectedProcess = availableProcesses.stream()
                        .min(Comparator.comparingInt(Process::getBurstTime)
                                .thenComparingInt(Process::getArrivalTime)
                                .thenComparingInt(Process::getPriority))
                        .orElseThrow(() -> new NoSuchElementException("No process found in availableProcesses"));
            }

            processList.remove(selectedProcess);
            executionOrder.add(selectedProcess);
            executionOrder2.add(new ProcessExecution(
                    selectedProcess.getName(),
                    selectedProcess.getBurstTime(),
                    selectedProcess.getColor(),
                    selectedProcess.getPid(),
                    selectedProcess.getPriority(),
                    currentTime // Add startTime here
            ));

            // Simulate process execution and context switching
              int completionTime =  currentTime + selectedProcess.getBurstTime();

            selectedProcess.setCompletionTime(completionTime);

            // Update current time
            currentTime = completionTime;
           
                currentTime+=contextSwitchTime;
            
           
          

            // Calculate waiting and turnaround times
            int waitingTime = selectedProcess.getWaitingTime(completionTime);
            int turnaroundTime = selectedProcess.getTurnaroundTime(completionTime);

            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;

            // Print process execution
            System.out.println("Executed Process: " + selectedProcess.getName());
            System.out.println("Waiting Time: " + waitingTime);
            System.out.println("Turnaround Time: " + turnaroundTime);
            System.out.println("---------------------------");
        }

        // Print average waiting and turnaround times
        int numProcesses = executionOrder.size();
        double avgWaitingTime = (double) totalWaitingTime / numProcesses;
        double avgTurnaroundTime = (double) totalTurnaroundTime / numProcesses;

        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);

        // Print execution order
        System.out.println("Execution Order: ");
        executionOrder.forEach(p -> System.out.print(p.getName() + " -> "));
        System.out.println("End");
        return executionOrder2;

    }
}
