package sjf_vs_priority;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class GanttTab {

    private GanttPanel ganttSJF, ganttPRI;
    private JLabel titleSJF, titlePRI;

    public JScrollPane build() {
        JPanel root = UIHelper.vBox();

        // ── SJF Gantt ─────────────────────────────────────────────────────
        JPanel c1 = UIHelper.card("");
        titleSJF = UIHelper.lbl("Gantt Chart — Shortest Job First  (SJF, Non-Preemptive)", UIHelper.F_H2, UIHelper.TEXT);
        titleSJF.setBorder(new EmptyBorder(0, 0, 6, 0));
        c1.add(titleSJF, BorderLayout.NORTH);

        ganttSJF = new GanttPanel();
        JScrollPane sp1 = new JScrollPane(ganttSJF,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp1.setPreferredSize(new Dimension(900, 95));
        sp1.setBorder(new LineBorder(UIHelper.BORDER));
        c1.add(sp1, BorderLayout.CENTER);
        root.add(c1);
        root.add(Box.createVerticalStrut(12));

        // ── Priority Gantt ────────────────────────────────────────────────
        JPanel c2 = UIHelper.card("");
        titlePRI = UIHelper.lbl("Gantt Chart — Priority Scheduling  (Non-Preemptive)", UIHelper.F_H2, UIHelper.TEXT);
        titlePRI.setBorder(new EmptyBorder(0, 0, 6, 0));
        c2.add(titlePRI, BorderLayout.NORTH);

        ganttPRI = new GanttPanel();
        JScrollPane sp2 = new JScrollPane(ganttPRI,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp2.setPreferredSize(new Dimension(900, 95));
        sp2.setBorder(new LineBorder(UIHelper.BORDER));
        c2.add(sp2, BorderLayout.CENTER);
        root.add(c2);

        return UIHelper.scroll(root);
    }

    public void update(List<GanttBlock> sjf, List<GanttBlock> pri,
                       boolean sjfPreemptive, boolean priPreemptive) {
        titleSJF.setText("Gantt Chart — Shortest Job First  (SJF, "
            + (sjfPreemptive ? "Preemptive - SRTF" : "Non-Preemptive") + ")");
        titlePRI.setText("Gantt Chart — Priority Scheduling  ("
            + (priPreemptive ? "Preemptive" : "Non-Preemptive") + ")");

        ganttSJF.setBlocks(sjf);
        ganttPRI.setBlocks(pri);
    }
}