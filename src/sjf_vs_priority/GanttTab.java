package sjf_vs_priority;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

public class GanttTab {

    private GanttPanel ganttSJF, ganttPRI;

    public JScrollPane build() {
        JPanel root = UIHelper.vBox();

        JPanel c1 = UIHelper.card("Gantt Chart — Shortest Job First  (SJF, Non-Preemptive)");
        ganttSJF = new GanttPanel();
        JScrollPane sp1 = new JScrollPane(ganttSJF,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp1.setPreferredSize(new Dimension(900, 95));
        sp1.setBorder(new LineBorder(UIHelper.BORDER));
        c1.add(sp1, BorderLayout.CENTER);
        root.add(c1);
        root.add(Box.createVerticalStrut(12));

        JPanel c2 = UIHelper.card("Gantt Chart — Priority Scheduling  (Non-Preemptive)");
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

    public void update(List<GanttBlock> sjf, List<GanttBlock> pri) {
        ganttSJF.setBlocks(sjf);
        ganttPRI.setBlocks(pri);
    }
}