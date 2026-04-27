/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sjf_vs_priority;

import java.util.List;

public class SimulationResult {
    public final List<GanttBlock> gantt;
    public final List<Process> completed;

    public SimulationResult(List<GanttBlock> gantt, List<Process> completed) {
        this.gantt = gantt;
        this.completed = completed;
    }

    public double avgWT()  { return completed.stream().mapToInt(p->p.waitingTime).average().orElse(0); }
    public double avgTAT() { return completed.stream().mapToInt(p->p.turnaroundTime).average().orElse(0); }
    public double avgRT()  { return completed.stream().mapToInt(p->p.responseTime).average().orElse(0); }
    public int    maxWT()  { return completed.stream().mapToInt(p->p.waitingTime).max().orElse(0); }
}