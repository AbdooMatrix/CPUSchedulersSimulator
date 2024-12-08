package algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import models.Process;

public class FCAIScheduler {

    private List<Process> processList; // List of all processes
    private List<String> timeline;    // Execution timeline for reporting
    private List<Process> readyQueue ;
    private double v1;                // Scaling factor for Arrival Time
    private double v2;                // Scaling factor for Remaining Burst Time

    public FCAIScheduler(List<Process> processes) {
        this.processList = new ArrayList<>(processes);
        this.timeline = new ArrayList<>();
        this.readyQueue = new ArrayList<>();
        calculateV1();
        calculateV2();
    }


    void handleReadyQueue(int currentTime){
        // Add processes to ready queue that have arrived
        for (int i = 0; i < processList.size(); i++) {
            Process process = processList.get(i);
            if (process.getArrivalTime() <= currentTime) {
                readyQueue.add(process);
                processList.remove(i--);
            }
        }
    }

    void recalculateFcaiFactor(){
        // Recalculate FCAI Factors for ready processes
        for (Process process : readyQueue) {
            process.calculateFcaiFactor(v1, v2);
        }
    }

    public void schedule() {
        int currentTime = 0;
        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            String processName = "";
            int start = currentTime  ;

            handleReadyQueue(currentTime); // add process to ready queue if exist
            recalculateFcaiFactor();

            // Sort ready queue by FCAI Factor (primary) and arrival time (secondary)
            readyQueue.sort(Comparator
                    .comparingDouble(Process::getFcaiFactor)
                    .thenComparingInt(Process::getArrivalTime));

            // Execute the process
            while(readyQueue.isEmpty()){
                currentTime++;
                handleReadyQueue(currentTime);
            }

            Process currentProcess = readyQueue.getFirst() ;
            readyQueue.removeFirst() ;

            processName = currentProcess.getName();

            int quantum = currentProcess.getUpdatedQuantum();
            int nonPreemptiveTime = (int) Math.ceil(quantum * 0.4); // First 40% non-preemptive
            int executionTime = Math.min(nonPreemptiveTime, currentProcess.getBurstTime());

            // Non-preemptive execution
            currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);
            currentTime += executionTime;

            int remainingQuantum = quantum - executionTime;

            if (!readyQueue.isEmpty() && currentProcess.getBurstTime() > 0 && remainingQuantum > 0) {
                handleReadyQueue(currentTime);
                recalculateFcaiFactor();

                // Preemptive execution for remaining quantum (if allowed)
                Process readyQueueFirst = readyQueue.getFirst() ; // first queue

                if(readyQueueFirst.getFcaiFactor() < currentProcess.getFcaiFactor()){
                    readyQueue.add(currentProcess);
                    currentProcess.setBurstTime(readyQueueFirst.getBurstTime() - executionTime);
                }
                else{
                    int currQuantum = currentProcess.getUpdatedQuantum() ;
                    while(readyQueueFirst.getFcaiFactor() >= currentProcess.getFcaiFactor() &&
                            currentProcess.getBurstTime() > 0 && currQuantum > 0){
                        currentTime++ ;
                        currQuantum-- ;
                        currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                        handleReadyQueue(currentTime);
                        recalculateFcaiFactor();
                    }
                }
                currentTime += executionTime;

                // Update quantum
                if (currentProcess.getBurstTime() > 0) {
                    currentProcess.setUpdatedQuantum(remainingQuantum > 0 ? remainingQuantum : quantum + 2);
                    readyQueue.add(currentProcess); // Re-add to queue
                }
            }
            else{
                while(readyQueue.isEmpty() && currentProcess.getBurstTime() > 0 && remainingQuantum > 0){
                    remainingQuantum-- ;
                    currentTime++ ;
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                    handleReadyQueue(currentTime);
                    recalculateFcaiFactor();
                }
            }
            // Update timeline
            timeline.add("Time " + start + " to " + currentTime + ": " + processName );

        }
    }

    private void calculateV1() {
        double lastArriveTime = Double.MIN_VALUE;
        for (Process process : processList) {
            lastArriveTime = Math.max(lastArriveTime, process.getArrivalTime());
        }
        v1 = lastArriveTime / 10.0;
    }

    private void calculateV2() {
        double maxBurstTime = Double.MIN_VALUE;
        for (Process process : processList) {
            maxBurstTime = Math.max(maxBurstTime, process.getBurstTime());
        }
        v2 = maxBurstTime / 10.0;
    }

    public void printTimeline() {
        for (String event : timeline) {
            System.out.println(event);
        }
    }
}