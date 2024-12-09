package algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import models.Process;
import models.ProcessExecution;

public class FCAIScheduler {

    private List<Process> processList; // List of all processes
    private List<String> timeline;    // Execution timeline for reporting
    private LinkedList<Process> readyQueue; // Ready queue for processes
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
            if (process.getArrivalTime() <= currentTime && !readyQueue.contains(process)) {
                readyQueue.add(process);
                iterator.remove(); // Remove from processList once added to readyQueue
            }
        }
        calculateFcaiFactor();
    }

    private void calculateFcaiFactor() {
        for (Process process : readyQueue) {
            process.calculateFcaiFactor(v1, v2);
        }
    }

    private Process getBestProcessFromQueue() {
        Process bestProcess = null;
        for (Process process : readyQueue) {
            if (bestProcess == null ||
                    process.getFcaiFactor() < bestProcess.getFcaiFactor() ||
                    (process.getFcaiFactor() == bestProcess.getFcaiFactor() &&
                            process.getArrivalTime() < bestProcess.getArrivalTime())) {
                bestProcess = process;
            }
        }
        return bestProcess;
    }

    public List<ProcessExecution> schedule(int contextSwitchingTime) {
        int currentTime = 0;
        int choice = 2;
        Process currentProcess = null;
        List<ProcessExecution> executionOrder = new ArrayList<>();

        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            updateReadyQueueState(currentTime);

            while (readyQueue.isEmpty() && !processList.isEmpty()) {
                currentTime++;
                updateReadyQueueState(currentTime);
            }

            if (readyQueue.isEmpty()) break;

            int start = currentTime;

            if (choice == 1) {
                currentProcess = getBestProcessFromQueue();
                readyQueue.remove(currentProcess);
            } else {
                currentProcess = readyQueue.poll();
            }

            int quantum = currentProcess.getUpdatedQuantum();
            int nonPreemptiveTime = (int) Math.ceil(quantum * 0.4);
            int executionTime = Math.min(nonPreemptiveTime, currentProcess.getBurstTime());

            // Execute the process non-preemptively
            currentTime += executionTime;
            currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);
            int remainingQuantum = quantum - executionTime;

            while (currentProcess.getBurstTime() > 0 && remainingQuantum > 0) {
                if (getBestProcessFromQueue() != null && getBestProcessFromQueue().getFcaiFactor() < currentProcess.getFcaiFactor()) {
                    break;
                }
                currentTime++;
                remainingQuantum--;
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                updateReadyQueueState(currentTime);
            }

            // Track execution
            executionOrder.add(new ProcessExecution(
                    currentProcess.getName(),
                    currentTime - start,
                    currentProcess.getColor(),
                    currentProcess.getPid(),
                    currentProcess.getPriority(),
                    start
            ));

            if (currentProcess.getBurstTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
                timeline.add("Process " + currentProcess.getName() + ": from " + start + " to " + currentTime + " --> completed");
                choice = 2;
            } else if (remainingQuantum == 0) {
                currentProcess.setUpdatedQuantum(quantum + 2);
                timeline.add("Process " + currentProcess.getName() + ": from " + start + " to " + currentTime +
                        ", Quantum: " + quantum + " --> " + currentProcess.getUpdatedQuantum());
                readyQueue.add(currentProcess);
                choice = 2;
            } else {
                currentProcess.setUpdatedQuantum(quantum + remainingQuantum);
                timeline.add("Process " + currentProcess.getName() + ": from " + start + " to " + currentTime +
                        ", Quantum: " + quantum + " --> " + currentProcess.getUpdatedQuantum());
                readyQueue.add(currentProcess);
                choice = 1;
            }

            currentTime += contextSwitchingTime;
        }
        return executionOrder;
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

    public void printResults(List<Process> processes, List<ProcessExecution> executionOrder) {
        timeline.forEach(System.out::println);

        // Print individual process results (waiting time, turnaround time)
        for (Process p : processes) {
            int waitTime = p.getWaitingTimeFcai(p.getOriginalBurstTime());
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
            totalWaitingTime += p.getWaitingTimeFcai(p.getOriginalBurstTime());
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
