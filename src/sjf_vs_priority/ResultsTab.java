package sjf_vs_priority;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ResultsTab {

    private JLabel[] metSJF = new JLabel[3];
    private JLabel[] metPRI = new JLabel[3];
    private DefaultTableModel resSJF, resPRI;
    private JTextArea compArea;

    public JScrollPane build() {
        JPanel root = UIHelper.vBox();

        // ── Average metrics ───────────────────────────────────────────────
        JPanel avgRow = new JPanel(new GridLayout(1, 2, 14, 0));
        avgRow.setOpaque(false);
        avgRow.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        avgRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel sjfAvg = UIHelper.card("SJF — Average Metrics");
        JPanel priAvg = UIHelper.card("Priority — Average Metrics");
        JPanel sjfMet = new JPanel(new GridLayout(1, 3, 8, 0)); sjfMet.setOpaque(false);
        JPanel priMet = new JPanel(new GridLayout(1, 3, 8, 0)); priMet.setOpaque(false);

        String[] mNames = {"Avg WT", "Avg TAT", "Avg RT"};
        for (int i = 0; i < 3; i++) {
            metSJF[i] = new JLabel("—", SwingConstants.CENTER);
            metSJF[i].setFont(UIHelper.F_BIG);
            metSJF[i].setForeground(UIHelper.TEXT);

            metPRI[i] = new JLabel("—", SwingConstants.CENTER);
            metPRI[i].setFont(UIHelper.F_BIG);
            metPRI[i].setForeground(UIHelper.TEXT);

            sjfMet.add(UIHelper.metricBox(mNames[i], metSJF[i]));
            priMet.add(UIHelper.metricBox(mNames[i], metPRI[i]));
        }
        sjfAvg.add(sjfMet, BorderLayout.CENTER);
        priAvg.add(priMet, BorderLayout.CENTER);
        avgRow.add(sjfAvg);
        avgRow.add(priAvg);
        root.add(avgRow);
        root.add(Box.createVerticalStrut(12));

        // ── Per-process tables ────────────────────────────────────────────
        JPanel tabRow = new JPanel(new GridLayout(1, 2, 14, 0));
        tabRow.setOpaque(false);
        tabRow.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        tabRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        String[] rc = {"PID", "AT", "BT", "Priority", "FT", "WT", "TAT", "RT"};
        resSJF = UIHelper.roModel(rc);
        resPRI = UIHelper.roModel(rc);

        JPanel st = UIHelper.card("SJF — Per-Process Metrics");
        st.add(UIHelper.resScroll(resSJF), BorderLayout.CENTER);
        JPanel pt = UIHelper.card("Priority — Per-Process Metrics");
        pt.add(UIHelper.resScroll(resPRI), BorderLayout.CENTER);

        tabRow.add(st); tabRow.add(pt);
        root.add(tabRow);
        root.add(Box.createVerticalStrut(12));

        // ── Comparison summary ────────────────────────────────────────────
        JPanel comp = UIHelper.card("📌  Comparison Summary");
        compArea = new JTextArea(6, 60);
        compArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));
        compArea.setEditable(false);
        compArea.setLineWrap(true);
        compArea.setWrapStyleWord(true);
        compArea.setBackground(UIHelper.GRAY_LIGHT);
        compArea.setBorder(new EmptyBorder(8, 10, 8, 10));
        comp.add(new JScrollPane(compArea), BorderLayout.CENTER);
        root.add(comp);

        return UIHelper.scroll(root);
    }

    public void update(SimulationResult sjfR, SimulationResult priR) {
        // Averages
        double[] sv = {sjfR.avgWT(), sjfR.avgTAT(), sjfR.avgRT()};
        double[] pv = {priR.avgWT(), priR.avgTAT(), priR.avgRT()};
        for (int i = 0; i < 3; i++) {
            metSJF[i].setText(String.format("%.2f", sv[i]));
            metPRI[i].setText(String.format("%.2f", pv[i]));
        }

        // Tables
        fillTable(resSJF, sjfR.completed);
        fillTable(resPRI, priR.completed);

        // Comparison text
        compArea.setText(buildComparison(sjfR, priR));
    }

    private void fillTable(DefaultTableModel m, List<Process> list) {
        m.setRowCount(0);
        for (Process p : list)
            m.addRow(new Object[]{
                p.pid, p.arrivalTime, p.burstTime, p.priority,
                p.finishTime, p.waitingTime, p.turnaroundTime, p.responseTime
            });
    }

    private String buildComparison(SimulationResult s, SimulationResult p) {
        return String.format(
            "%-30s  SJF: %6.2f    Priority: %6.2f    Winner: %s%n" +
            "%-30s  SJF: %6.2f    Priority: %6.2f    Winner: %s%n" +
            "%-30s  SJF: %6.2f    Priority: %6.2f    Winner: %s%n%n" +
            "SJF max single-process wait      : %d units%n" +
            "Priority max single-process wait : %d units",
            "Average Waiting Time:",    s.avgWT(),  p.avgWT(),  s.avgWT()  <= p.avgWT()  ? "✔ SJF" : "✔ Priority",
            "Average Turnaround Time:", s.avgTAT(), p.avgTAT(), s.avgTAT() <= p.avgTAT() ? "✔ SJF" : "✔ Priority",
            "Average Response Time:",   s.avgRT(),  p.avgRT(),  s.avgRT()  <= p.avgRT()  ? "✔ SJF" : "✔ Priority",
            s.maxWT(), p.maxWT()
        );
    }
}