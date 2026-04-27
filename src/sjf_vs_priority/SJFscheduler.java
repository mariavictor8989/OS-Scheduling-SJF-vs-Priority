/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sjf_vs_priority;
 import java.awt.Color;
 import java.util.*;


public class SJFscheduler {
    
  
    private static final Color[] COLORS = {
        new Color(55,138,221), new Color(29,158,117), new Color(216,90,48),
        new Color(212,83,126), new Color(99,153,34),  new Color(186,117,23),
        new Color(127,119,221),new Color(226,75,74),  new Color(15,110,86),
        new Color(153,53,86)
    };

    private final List<Process> original;

    public SJFscheduler(List<Process> processes) {
        this.original = processes;
    }

    public SimulationResult run() {
        List<Process> copies = new ArrayList<>();
        for (Process p : original) copies.add(p.copy());

        List<GanttBlock> gantt = new ArrayList<>();
        List<Process> completed = new ArrayList<>();
        boolean[] done = new boolean[copies.size()];
        int time = 0;
        Map<String,Integer> pidIndex = new HashMap<>();
        for (int i = 0; i < original.size(); i++) pidIndex.put(original.get(i).pid, i);

        while (completed.size() < copies.size()) {
            List<Integer> ready = new ArrayList<>();
            for (int i = 0; i < copies.size(); i++)
                if (!done[i] && copies.get(i).arrivalTime <= time)
                    ready.add(i);

            if (ready.isEmpty()) {
                int next = copies.stream().filter(p -> !done[copies.indexOf(p)])
                    .mapToInt(p -> p.arrivalTime).min().orElse(time + 1);
                // find real next
                int nextTime = Integer.MAX_VALUE;
                for (int i = 0; i < copies.size(); i++)
                    if (!done[i]) nextTime = Math.min(nextTime, copies.get(i).arrivalTime);
                gantt.add(new GanttBlock("IDLE", time, nextTime, new Color(220,220,220)));
                time = nextTime;
                continue;
            }

            // shortest burst, tie: earliest arrival, tie: pid lex
            ready.sort(Comparator
                .comparingInt((Integer i) -> copies.get(i).burstTime)
                .thenComparingInt(i -> copies.get(i).arrivalTime)
                .thenComparing(i -> copies.get(i).pid));

            int idx = ready.get(0);
            Process p = copies.get(idx);
            int colorIdx = pidIndex.getOrDefault(p.pid, idx) % COLORS.length;
            gantt.add(new GanttBlock(p.pid, time, time + p.burstTime, COLORS[colorIdx]));

            p.responseTime   = time - p.arrivalTime;
            p.finishTime     = time + p.burstTime;
            p.turnaroundTime = p.finishTime - p.arrivalTime;
            p.waitingTime    = p.turnaroundTime - p.burstTime;

            time += p.burstTime;
            done[idx] = true;
            completed.add(p);
        }

        return new SimulationResult(gantt, completed);
    }
}


