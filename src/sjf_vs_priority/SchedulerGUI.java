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
        tabs.addTab("⚙ Input",                inputPanel.build());
        tabs.addTab("📊 Gantt Charts",         ganttTab.build());
        tabs.addTab("📋 Results",              resultsTab.build());
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

            SimulationResult sjfR = new SJFscheduler(processes).run(); 
            SimulationResult priR = new PriorityScheduler(processes, true).run();

            ganttTab.update(sjfR.gantt, priR.gantt);
            resultsTab.update(sjfR, priR);
            analysisTab.update(sjfR, priR, true);  

            inputPanel.showError("");
        } catch (Exception ex) {
            inputPanel.showError(ex.getMessage());
        }
    }
}