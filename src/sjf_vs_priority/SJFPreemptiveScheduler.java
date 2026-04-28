package sjf_vs_priority;

import java.awt.Color;
import java.util.*;

public class SJFPreemptiveScheduler {

    private static final Color[] COLORS = {
        new Color(55,138,221), new Color(29,158,117), new Color(216,90,48),
        new Color(212,83,126), new Color(99,153,34),  new Color(186,117,23),
        new Color(127,119,221),new Color(226,75,74),  new Color(15,110,86),
        new Color(153,53,86)
    };

    private final List<Process> original;

    public SJFPreemptiveScheduler(List<Process> processes) {
        this.original = processes;
    }

    public SimulationResult run() {
        int n = original.size();
        int[] remaining = new int[n];
        int[] arrival   = new int[n];
        boolean[] started = new boolean[n];
        boolean[] done    = new boolean[n];

        for (int i = 0; i < n; i++) {
            remaining[i] = original.get(i).burstTime;
            arrival[i]   = original.get(i).arrivalTime;
        }

        List<GanttBlock> gantt     = new ArrayList<>();
        List<Process>    completed = new ArrayList<>();

        int time = 0, finishedCount = 0;
        int lastPid = -1;
        int blockStart = 0;

        while (finishedCount < n) {
            // pick process with shortest remaining time among arrived
            int chosen = -1;
            int minRemaining = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!done[i] && arrival[i] <= time && remaining[i] < minRemaining) {
                    minRemaining = remaining[i];
                    chosen = i;
                }
            }

            if (chosen == -1) {
                // CPU idle
                if (lastPid != -2) {
                    if (lastPid >= 0)
                        gantt.add(new GanttBlock(original.get(lastPid).pid, blockStart, time, COLORS[lastPid % COLORS.length]));
                    blockStart = time;
                    lastPid = -2;
                }
                time++;
                continue;
            }

            // first time running -> record response time
            if (!started[chosen]) {
                started[chosen] = true;
                original.get(chosen).responseTime = time - arrival[chosen];
            }

            // if different process -> close old Gantt block
            if (chosen != lastPid) {
                if (lastPid == -2)
                    gantt.add(new GanttBlock("IDLE", blockStart, time, new Color(220,220,220)));
                else if (lastPid >= 0)
                    gantt.add(new GanttBlock(original.get(lastPid).pid, blockStart, time, COLORS[lastPid % COLORS.length]));
                blockStart = time;
                lastPid = chosen;
            }

            remaining[chosen]--;
            time++;

            if (remaining[chosen] == 0) {
                done[chosen] = true;
                finishedCount++;
                Process p = original.get(chosen).copy();
                p.finishTime     = time;
                p.turnaroundTime = time - arrival[chosen];
                p.waitingTime    = p.turnaroundTime - original.get(chosen).burstTime;
                p.responseTime   = original.get(chosen).responseTime;
                completed.add(p);

                gantt.add(new GanttBlock(original.get(chosen).pid, blockStart, time, COLORS[chosen % COLORS.length]));
                lastPid = -1;
                blockStart = time;
            }
        }

        return new SimulationResult(gantt, completed);
    }
}