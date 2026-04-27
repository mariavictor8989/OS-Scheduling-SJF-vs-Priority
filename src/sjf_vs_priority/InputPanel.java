package sjf_vs_priority;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class InputPanel {

    private DefaultTableModel inputModel;
    private JLabel errorLabel;
    private final Runnable onRun;

    public InputPanel(Runnable onRun) {
        this.onRun = onRun;
    }

    public JScrollPane build() {
        JPanel root = UIHelper.vBox();

        // ── Scenario loader ───────────────────────────────────────────────
        JPanel sc = UIHelper.card("Load a Test Scenario");
        JPanel scRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        scRow.setOpaque(false);
        String[][] scens = {
            {"A", "Scenario A — Basic mixed workload"},
            {"B", "Scenario B — Burst vs Priority conflict"},
            {"C", "Scenario C — Starvation sensitive"},
            {"D", "Scenario D — Validation case"}
        };
        for (String[] s : scens) {
            JButton b = UIHelper.outlineBtn(s[1]);
            final String code = s[0];
            b.addActionListener(e -> loadScenario(code));
            scRow.add(b);
        }
        sc.add(scRow, BorderLayout.CENTER);
        root.add(sc);
        root.add(Box.createVerticalStrut(10));

        // ── Process table ─────────────────────────────────────────────────
        JPanel tc = UIHelper.card("Process Input Table  (PID ≤ 8 chars | Arrival ≥ 0 | Burst > 0 | Priority ≥ 0)");

        String[] cols = {"PID", "Arrival Time", "Burst Time", "Priority"};
        inputModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return true; }
        };
        JTable tbl = UIHelper.styledTable(inputModel);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(80);

        JScrollPane tsp = new JScrollPane(tbl);
        tsp.setPreferredSize(new Dimension(700, 200));
        tsp.setBorder(new LineBorder(UIHelper.BORDER));

        errorLabel = UIHelper.lbl(" ", UIHelper.F_SMALL, UIHelper.DANGER);
        errorLabel.setBorder(new EmptyBorder(0, 4, 4, 0));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        btnRow.setOpaque(false);

        JButton addBtn   = UIHelper.accentBtn("＋ Add Process",    UIHelper.ACCENT);
        JButton delBtn   = UIHelper.accentBtn("― Remove Selected", UIHelper.DANGER);
        JButton clearBtn = UIHelper.outlineBtn("Clear All");
        JButton runBtn   = UIHelper.accentBtn("▶  Run Simulation", UIHelper.GREEN);
        runBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        addBtn.addActionListener(e   -> inputModel.addRow(new Object[]{"P" + (inputModel.getRowCount() + 1), "0", "1", "1"}));
        delBtn.addActionListener(e   -> { int r = tbl.getSelectedRow(); if (r >= 0) inputModel.removeRow(r); });
        clearBtn.addActionListener(e -> { while (inputModel.getRowCount() > 0) inputModel.removeRow(0); });
        runBtn.addActionListener(e   -> onRun.run());

        btnRow.add(addBtn); btnRow.add(delBtn); btnRow.add(clearBtn);
        btnRow.add(Box.createHorizontalStrut(20));
        btnRow.add(runBtn);

        JPanel inner = new JPanel(new BorderLayout(0, 6));
        inner.setOpaque(false);
        inner.add(errorLabel, BorderLayout.NORTH);
        inner.add(tsp,        BorderLayout.CENTER);
        inner.add(btnRow,     BorderLayout.SOUTH);
        tc.add(inner, BorderLayout.CENTER);
        root.add(tc);

        loadScenario("A");
        return UIHelper.scroll(root);
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public List<Process> collectProcesses() {
        if (inputModel.getRowCount() < 2)
            throw new IllegalArgumentException("Add at least 2 processes.");

        Set<String> pids = new HashSet<>();
        List<Process> list = new ArrayList<>();

        for (int r = 0; r < inputModel.getRowCount(); r++) {
            String pid = cell(r, 0);
            if (pid.isEmpty())     throw new IllegalArgumentException("Row " + (r+1) + ": PID cannot be empty.");
            if (pid.length() > 8)  throw new IllegalArgumentException("Row " + (r+1) + ": PID must be ≤ 8 characters.");
            if (!pids.add(pid))    throw new IllegalArgumentException("Duplicate PID: \"" + pid + "\".");

            int at  = parseCell(r, 1, "Arrival Time", pid, false);
            int bt  = parseCell(r, 2, "Burst Time",   pid, true);
            int pri = parseCell(r, 3, "Priority",      pid, false);
            list.add(new Process(pid, at, bt, pri));
        }
        return list;
    }

    public void showError(String msg) {
        errorLabel.setText(msg.isEmpty() ? " " : "⚠  " + msg);
    }

    // ── Scenario loader ───────────────────────────────────────────────────
    public void loadScenario(String s) {
        while (inputModel.getRowCount() > 0) inputModel.removeRow(0);
        errorLabel.setText(" ");

        Object[][] data;
        switch (s) {
            case "A": data = new Object[][]{{"P1",0,8,2},{"P2",1,4,1},{"P3",2,9,3},{"P4",3,5,4},{"P5",4,2,5}}; break;
            case "B": data = new Object[][]{{"P1",0,10,1},{"P2",1,2,5},{"P3",2,6,3},{"P4",3,1,4}}; break;
            case "C": data = new Object[][]{{"P1",0,2,4},{"P2",0,6,2},{"P3",0,1,3},{"P4",0,8,1},{"P5",0,3,5}}; break;
            case "D":
                data = new Object[][]{{"P1",0,5,2},{"P2",-1,3,1},{"P3",2,"abc",3},{"P4",3,4,1}};
                errorLabel.setText("⚠  Scenario D has invalid inputs — click 'Run Simulation' to trigger validation.");
                break;
            default: data = new Object[0][]; break;
        }
        for (Object[] row : data) inputModel.addRow(row);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private String cell(int r, int c) {
        Object v = inputModel.getValueAt(r, c);
        return v == null ? "" : v.toString().trim();
    }

    private int parseCell(int r, int c, String field, String pid, boolean positive) {
        String s = cell(r, c);
        int v;
        try { v = Integer.parseInt(s); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Process " + pid + ": " + field + " must be an integer (got \"" + s + "\").");
        }
        if (positive  && v <= 0) throw new IllegalArgumentException("Process " + pid + ": " + field + " must be > 0.");
        if (!positive && v <  0) throw new IllegalArgumentException("Process " + pid + ": " + field + " must be ≥ 0.");
        return v;
    }
}