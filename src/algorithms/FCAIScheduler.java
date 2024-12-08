package algorithms;

import models.FCAIProcess;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCAIScheduler {

    private List<FCAIProcess> processList; // List of all processes
    private List<String> timeline;        // Execution timeline for reporting
    private double v1 ;              // Scaling factor for Arrival Time
    private double v2 ;              // Scaling factor for Remaining Burst Time

    public FCAIScheduler(List<FCAIProcess> processes) {
        this.processList = new ArrayList<>(processes);
        this.timeline = new ArrayList<>();
        calculateV1();
        calculateV2();
    }

    public void schedule() {
        int currentTime = 0;
        List<FCAIProcess> readyQueue = new ArrayList<>();

        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to ready queue that have arrived
            for (int i = 0; i < processList.size(); i++) {
                FCAIProcess process = processList.get(i);
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    processList.remove(i--);
                }
            }

            // Recalculate FCAI Factors for ready processes
            for (FCAIProcess process : readyQueue) {
                process.calculateFcaiFactor(v1, v2);
            }

            // Sort ready queue by FCAI Factor
            readyQueue.sort(Comparator.comparingDouble(FCAIProcess::getFcaiFactor));

            // Execute the process with the lowest FCAI Factor
            if (!readyQueue.isEmpty()) {
                FCAIProcess currentProcess = readyQueue.get(0);
                int executionTime = Math.min(currentProcess.getUpdatedQuantum(), currentProcess.getBurstTime());

                // Update process state
                currentProcess.setBurstTime(currentProcess.getBurstTime() - executionTime);
                currentTime += executionTime;

                // Add execution details to timeline
                timeline.add("Time " + (currentTime - executionTime) + " to " + currentTime + ": " + currentProcess.getName());

                // Remove process if completed
                if (currentProcess.getBurstTime() == 0) {
                    readyQueue.remove(currentProcess);
                } else {
                    // Update quantum dynamically (as per the algorithm)
                    currentProcess.setUpdatedQuantum(currentProcess.getUpdatedQuantum() + 1); // Example adjustment
                }
            } else {
                // If no process is ready, increment time
                currentTime++;
            }
        }
    }

    void calculateV1(){
        double lastArriveTime = Double.MIN_VALUE ;
        for(FCAIProcess process : processList){
            lastArriveTime = Math.max(lastArriveTime, process.getArrivalTime());
        }
        v1 = lastArriveTime / 10.0 ;
    }

    void calculateV2(){
        double maxBurstTime = Double.MIN_VALUE ;
        for(FCAIProcess process : processList){
            maxBurstTime = Math.max(maxBurstTime, process.getBurstTime());
        }
        v2 = maxBurstTime / 10.0 ;
    }

    public void printTimeline() {
        for (String event : timeline) {
            System.out.println(event);
        }
    }

}
