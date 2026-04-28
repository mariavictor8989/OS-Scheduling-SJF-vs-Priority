package sjf_vs_priority;
import javax.swing.*;
import java.awt.*;

public class SchedulerGUI extends JFrame {
    
    private InputPanel  inputPanel;
    private GanttTab    ganttTab;
    private ResultsTab  resultsTab;
    private AnalysisTab analysisTab;  
    private JTabbedPane tabs;

    public SchedulerGUI() {
        setTitle("SJF vs Priority Scheduling Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inputPanel  = new InputPanel(this::runSimulation);
        ganttTab    = new GanttTab();
        resultsTab  = new ResultsTab();
        analysisTab = new AnalysisTab(); 

        tabs = new JTabbedPane();
        tabs.setFont(UIHelper.F_BODY);
        tabs.addTab("⚙ Input",                 inputPanel.build());
        tabs.addTab("📊 Gantt Charts",          ganttTab.build());
        tabs.addTab("📋 Results",               resultsTab.build());
        tabs.addTab("🔍 Analysis & Conclusion", analysisTab.build());

        add(buildHeader(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIHelper.ACCENT);
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel title = UIHelper.lbl("SJF vs Priority Comparison", UIHelper.F_TITLE, Color.WHITE);
        p.add(title, BorderLayout.WEST);
        return p;
    }

    private void runSimulation() {
        try {
            var processes = inputPanel.collectProcesses();

            // SJF — Preemptive أو Non-Preemptive حسب اختيار المستخدم
            SimulationResult sjfR = inputPanel.isSJFPreemptive()
                ? new SJFPreemptiveScheduler(processes).run()
                : new SJFscheduler(processes).run();  // ← S كبيرة

            // Priority — Preemptive أو Non-Preemptive حسب اختيار المستخدم
            SimulationResult priR = inputPanel.isPriPreemptive()
                ? new PriorityPreemptiveScheduler(processes, true).run()
                : new PriorityScheduler(processes, true).run();

            // ← أضف isSJFPreemptive و isPriPreemptive للـ GanttTab
            ganttTab.update(sjfR.gantt, priR.gantt,
                inputPanel.isSJFPreemptive(),
                inputPanel.isPriPreemptive());

            resultsTab.update(sjfR, priR);
            analysisTab.update(sjfR, priR, true);

            inputPanel.showError("");
        } catch (Exception ex) {
            inputPanel.showError(ex.getMessage());
        }
    }
}