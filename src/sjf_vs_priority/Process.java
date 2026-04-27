
package sjf_vs_priority;


public class Process {
    
    public String pid;
    public int arrivalTime;
    public int burstTime;
    public int priority;
 
    // Computed
    public int finishTime;
    public int waitingTime;
    public int turnaroundTime;
    public int responseTime;
 
    public Process(String pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
 
    public Process copy() {
        return new Process(pid, arrivalTime, burstTime, priority);
    }
}
 
    

