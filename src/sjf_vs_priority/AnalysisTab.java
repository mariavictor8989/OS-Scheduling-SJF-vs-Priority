package sjf_vs_priority;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AnalysisTab {

    private JPanel anaPanel;

    public JScrollPane build() {
        JPanel root = UIHelper.vBox();

        JLabel title = UIHelper.lbl("Comparative Scheduling Analysis", UIHelper.F_H2, UIHelper.ACCENT);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        root.add(title);

        anaPanel = new JPanel();
        anaPanel.setLayout(new BoxLayout(anaPanel, BoxLayout.Y_AXIS));
        anaPanel.setBackground(UIHelper.BG);
        anaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel waiting = UIHelper.lbl("Run the simulation to see the analysis.", UIHelper.F_BODY, UIHelper.TEXT_SEC);
        waiting.setAlignmentX(Component.LEFT_ALIGNMENT);
        anaPanel.add(waiting);

        root.add(anaPanel);
        return UIHelper.scroll(root);
    }

    public void update(SimulationResult s, SimulationResult p, boolean lowerIsBetter) {
        anaPanel.removeAll();

        double sW = s.avgWT(), sT = s.avgTAT(), sR = s.avgRT();
        double pW = p.avgWT(), pT = p.avgTAT(), pR = p.avgRT();
        boolean wW = sW <= pW, tW = sT <= pT, rW = sR <= pR;
        boolean sjfBetter = wW && tW;
        boolean priBetter = !wW && !tW;
        String rule = lowerIsBetter ? "lower number = higher priority" : "higher number = higher priority";

        // ── Section 1: Waiting & Response Time ───────────────────────────
        JPanel sec1 = UIHelper.sectionCard("⏱  Waiting & Response Time");
        sec1.add(UIHelper.bullet("SJF average waiting time: <b>" + f(sW) + "</b>  |  Priority: <b>" + f(pW) + "</b>  →  Winner: <b>" + (wW ? "SJF" : "Priority") + "</b>"));
        sec1.add(UIHelper.bullet(wW
            ? "SJF achieved the <b>lowest average waiting time</b> by always selecting the shortest burst."
            : "Priority achieved the <b>lowest average waiting time</b> — urgent processes ran early."));
        sec1.add(UIHelper.bullet("SJF average response time: <b>" + f(sR) + "</b>  |  Priority: <b>" + f(pR) + "</b>  →  Winner: <b>" + (rW ? "SJF" : "Priority") + "</b>"));
        anaPanel.add(sec1);
        anaPanel.add(Box.createVerticalStrut(10));

        // ── Section 2: Turnaround Time ───────────────────────────────────
        JPanel sec2 = UIHelper.sectionCard("📊  Turnaround Time");
        sec2.add(UIHelper.bullet("SJF average TAT: <b>" + f(sT) + "</b>  |  Priority: <b>" + f(pT) + "</b>  →  Winner: <b>" + (tW ? "SJF" : "Priority") + "</b>"));
        sec2.add(UIHelper.bullet(tW
            ? "SJF provided <b>shorter turnaround time</b> — short jobs finish and leave quickly."
            : "Priority provided <b>shorter turnaround time</b> on this workload."));
        anaPanel.add(sec2);
        anaPanel.add(Box.createVerticalStrut(10));

        // ── Section 3: Fairness & Starvation ────────────────────────────
        JPanel sec3 = UIHelper.sectionCard("⚖  Fairness & Starvation Risk");
        sec3.add(UIHelper.bullet("SJF max single-process wait: <b>" + s.maxWT() + " units</b>  |  Priority: <b>" + p.maxWT() + " units</b>"));
        sec3.add(UIHelper.bullet(s.maxWT() > 20
            ? "⚠ SJF: long-burst processes waited &gt; 20 units — <b>starvation risk</b> exists."
            : "✓ SJF: no starvation risk detected for this workload."));
        sec3.add(UIHelper.bullet(p.maxWT() > 20
            ? "⚠ Priority: low-priority processes waited &gt; 20 units — <b>starvation risk</b> without aging."
            : "✓ Priority: no starvation risk detected for this workload."));
        anaPanel.add(sec3);
        anaPanel.add(Box.createVerticalStrut(10));

        // ── Section 4: Algorithm Behavior ───────────────────────────────
        JPanel sec4 = UIHelper.sectionCard("🔬  Algorithm Behavior");
        sec4.add(UIHelper.bullet("SJF favors <b>short-burst</b> processes regardless of priority — burst length is everything."));
        sec4.add(UIHelper.bullet("Priority (" + rule + ") favors <b>urgent</b> processes regardless of burst length."));
        sec4.add(UIHelper.bullet("In Scenario B: SJF runs the short low-priority job first; Priority runs the long high-priority job first."));
        anaPanel.add(sec4);
        anaPanel.add(Box.createVerticalStrut(10));

        // ── Final Verdict ────────────────────────────────────────────────
        anaPanel.add(buildVerdict(sjfBetter, priBetter, sW, sT, pW, pT));

        anaPanel.revalidate();
        anaPanel.repaint();
    }

    // ── Verdict card ─────────────────────────────────────────────────────
    private JPanel buildVerdict(boolean sjfBetter, boolean priBetter,
                                double sW, double sT, double pW, double pT) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(240, 247, 255));
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIHelper.ACCENT, 1, true),
            new EmptyBorder(14, 16, 14, 16)
        ));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Title
        JLabel title = UIHelper.lbl("Final Verdict", UIHelper.F_H3, UIHelper.ACCENT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(8));

        // Winner row
        String winner = sjfBetter ? "SJF" : priBetter ? "Priority" : "Mixed";
        Color winColor = sjfBetter ? UIHelper.GREEN : priBetter ? UIHelper.ACCENT : UIHelper.TEXT_SEC;
        JPanel wrow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        wrow.setOpaque(false);
        wrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrow.add(UIHelper.lbl("• Performance Winner: ", UIHelper.F_BODY, UIHelper.TEXT));
        wrow.add(UIHelper.lbl(winner, new Font("SansSerif", Font.BOLD, 13), winColor));
        p.add(wrow);
        p.add(Box.createVerticalStrut(6));

        // Recommendation
        String rec;
        if (sjfBetter)
            rec = "Use <b>SJF</b> for this workload to maximize throughput and minimize average wait time.";
        else if (priBetter)
            rec = "Use <b>Priority Scheduling</b> for this workload — it served processes more efficiently.";
        else
            rec = String.format("Mixed result — SJF WT=%.2f TAT=%.2f  |  Priority WT=%.2f TAT=%.2f. "
                + "Choose <b>SJF</b> for throughput, <b>Priority</b> for urgency.", sW, sT, pW, pT);

        JLabel rlbl = UIHelper.htmlLbl("• Recommendation: " + rec, UIHelper.ACCENT);
        rlbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(rlbl);

        // Separator
        p.add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(UIHelper.BORDER);
        p.add(sep);
        p.add(Box.createVerticalStrut(8));

        // Trade-off
        JLabel tradeTitle = UIHelper.lbl("Trade-off — Efficiency vs Urgency:", UIHelper.F_H3, UIHelper.TEXT);
        tradeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(tradeTitle);
        p.add(Box.createVerticalStrut(4));
        p.add(UIHelper.htmlLbl("• SJF minimises average wait/turnaround but <b>ignores urgency</b> — a critical long job waits behind trivial short ones.", UIHelper.TEXT));
        p.add(Box.createVerticalStrut(3));
        p.add(UIHelper.htmlLbl("• Priority respects urgency but may <b>increase average metrics</b> when high-priority jobs have large burst times.", UIHelper.TEXT));
        p.add(Box.createVerticalStrut(3));
        p.add(UIHelper.htmlLbl("• SJF is fairer in average wait; Priority is fairer to urgent processes. Neither prevents starvation without aging.", UIHelper.TEXT_SEC));

        return p;
    }

    private String f(double v) { return String.format("%.2f", v); }
}