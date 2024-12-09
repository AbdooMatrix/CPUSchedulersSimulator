package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import models.Process;
import models.ProcessExecution;

public class FCAIScheduler {

    private List<Process> processList; // List of all processes
    private List<String> timeline;    // Execution timeline for reporting
    private LinkedList<Process> readyQueue; // Changed to LinkedList for efficient manipulation
    private double v1;                // Scaling factor for Arrival Time
    private double v2;                // Scaling factor for Remaining Burst Time

    public FCAIScheduler(List<Process> processes) {
        this.processList = new ArrayList<>(processes);
        this.timeline = new ArrayList<>();
        this.readyQueue = new LinkedList<>();
        calculateV1();
        calculateV2();
    }

    private void updateReadyQueueState(int currentTime) {
        Iterator<Process> iterator = processList.iterator();
        while (iterator.hasNext()) {
            Process process = iterator.next();
            if (process.getArrivalTime() <= currentTime) {
                if (!readyQueue.contains(process)) { // Avoid duplicates
                    readyQueue.add(process);
                }
                iterator.remove(); // Remove process from processList once added to readyQueue
            }
        }
        calculateFcaiFactor();
        sortReadyQueue();
    }

    private void calculateFcaiFactor() {
        for (Process process : readyQueue) {
            process.calculateFcaiFactor(v1, v2);
        }
    }

    private void sortReadyQueue() {
        readyQueue.sort(Comparator
                .comparingDouble(Process::getFcaiFactor)
                .thenComparingInt(Process::getArrivalTime));
    }

    public void schedule() {
        int currentTime = 0;
        Process currentProcess = null;

        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            updateReadyQueueState(currentTime);

            // Handle idle time
            while (readyQueue.isEmpty()) {
                currentTime++;
                updateReadyQueueState(currentTime);
            }

            int start = currentTime;

            calculateFcaiFactor();
            sortReadyQueue();

            if (currentProcess == null) {
                currentProcess = readyQueue.poll(); // Poll removes and retrieves the first element
            }

            int quantum = currentProcess.getUpdatedQuantum();
            int nonPreemptiveTime = (int) Math.ceil(quantum * 0.4);
            int executionTime = Math.min(nonPreemptiveTime, currentProcess.getBurstTime());

            // Non-preemptive execution
            currentTime += executionTime;
            currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);
            int remainingQuantum = quantum - executionTime;

            while (currentProcess.getBurstTime() > 0 && readyQueue.isEmpty() && remainingQuantum > 0) {
                currentTime++;
                remainingQuantum--;
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                updateReadyQueueState(currentTime);
            }

            if (currentProcess.getBurstTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                timeline.add("Time " + start + " to " + currentTime + ": " + currentProcess.getName() + " --> completed");
                currentProcess = null;
            }
            else if (remainingQuantum == 0) {
                currentProcess.setUpdatedQuantum(quantum + 2);
                timeline.add("Time " + start + " to " + currentTime + ": " + currentProcess.getName()
                        + " , quantum :  " + quantum + " --> " + currentProcess.getUpdatedQuantum());
                readyQueue.add(currentProcess);
                currentProcess = null ;
            }
            else // ready queue became non-empty
            {
                // Preemptive execution
                calculateFcaiFactor();
                while (remainingQuantum > 0 && currentProcess.getBurstTime() > 0) {
                    updateReadyQueueState(currentTime);
                    sortReadyQueue();
                    if (!readyQueue.isEmpty() &&
                            readyQueue.peek().getFcaiFactor() < currentProcess.getFcaiFactor()) {
                        break;
                    }
                    currentTime++;
                    remainingQuantum--;
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                }

                if (currentProcess.getBurstTime() == 0) {
                    currentProcess.setCompletionTime(currentTime);
                    timeline.add("Time " + start + " to " + currentTime + ": " + currentProcess.getName() + " --> completed");
                    currentProcess = null;
                } else if (remainingQuantum == 0) {
                    currentProcess.setUpdatedQuantum(quantum + 2);
                    timeline.add("Time " + start + " to " + currentTime + ": " + currentProcess.getName()
                            + " , quantum :  " + quantum + " --> " + currentProcess.getUpdatedQuantum());
                    readyQueue.add(currentProcess);
                    currentProcess = null ;
                }
                else // process has preempted the current process
                {
                    currentProcess.setUpdatedQuantum(quantum + remainingQuantum);
                    timeline.add("Time " + start + " to " + currentTime + ": " + currentProcess.getName()
                            + " , quantum :  " + quantum + " --> " + currentProcess.getUpdatedQuantum());
                    readyQueue.add(currentProcess);
                    currentProcess = null ;
                }
                currentProcess = readyQueue.poll();
            }
        }
    }

    private void calculateV1() {
        double lastArriveTime = processList.stream()
                .mapToDouble(Process::getArrivalTime)
                .max()
                .orElse(0.0);
        v1 = lastArriveTime / 10.0;
    }

    private void calculateV2() {
        double maxBurstTime = processList.stream()
                .mapToDouble(Process::getBurstTime)
                .max()
                .orElse(0.0);
        v2 = maxBurstTime / 10.0;
    }


    public void printResults(List<Process> processes) {

        timeline.forEach(System.out::println);

        // Print individual process results (waiting time, turnaround time)
        for(Process p : processes){
            System.out.println("prcocess " + p.getName()
                    + " : turnaroundTime = " + p.getTurnaroundTime(p.getCompletionTime())
                    + " , waitingTime = " + p.getWaitingTimeFcai(p.getBurstTime())) ;
        }

        double avgWait = calculateAverageWaitingTime(processes);
        double avgTurnaround = calculateAverageTurnaroundTime(processes);

        System.out.println("Average Waiting Time: " + avgWait);
        System.out.println("Average Turnaround Time: " + avgTurnaround);
    }

    public double calculateAverageWaitingTime(List<Process> processes) {
        int totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.getWaitingTimeFcai(p.getOriginalBurstTime());
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
