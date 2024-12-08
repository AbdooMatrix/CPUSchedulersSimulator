package models;

public class FCAIProcess extends Process {
    private double fcaiFactor; // Specific to FCAI Scheduler
    private int updatedQuantum;

    // Constructor that calls the parent Process constructor and initializes FCAI-specific fields
    public FCAIProcess(String name, int arrivalTime, int burstTime, int priority, String color, int pid) {
        super(name, arrivalTime, burstTime, priority, color, pid);
        this.fcaiFactor = 0.0;
        this.updatedQuantum = 0;
    }

    // Getter and Setter for FCAI Factor
    public double getFcaiFactor() {
        return fcaiFactor;
    }

    public void setFcaiFactor(double fcaiFactor) {
        this.fcaiFactor = fcaiFactor;
    }

    // Getter and Setter for Updated Quantum
    public int getUpdatedQuantum() {
        return updatedQuantum;
    }

    public void setUpdatedQuantum(int updatedQuantum) {
        this.updatedQuantum = updatedQuantum;
    }

    // Method to calculate the FCAI Factor
    public void calculateFcaiFactor(double v1, double v2) {
        // FCAI Formula: (10 - Priority) + ArrivalTime / V1 + RemainingBurstTime / V2
        this.fcaiFactor = (10 - getPriority()) + (getArrivalTime() / v1) + (getBurstTime() / v2);
    }

    // Override toString() for debugging purposes
    @Override
    public String toString() {
        return super.toString() +
                ", FCAI Factor=" + fcaiFactor +
                ", Updated Quantum=" + updatedQuantum;
    }
}
