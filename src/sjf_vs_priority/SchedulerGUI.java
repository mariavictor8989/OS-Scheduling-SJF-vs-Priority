package sjf_vs_priority;

import javax.swing.*;
import java.awt.*;

public class SchedulerGUI extends JFrame {
    private InputPanel inputPanel;
    private GanttTab ganttTab;
    private ResultsTab resultsTab;

    public SchedulerGUI() {
        setTitle("SJF vs Priority Scheduling Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. تعريف الأجزاء (الـ Panels اللي انتِ بعتيها)
        inputPanel = new InputPanel(this::runSimulation);
        ganttTab = new GanttTab();
        resultsTab = new ResultsTab();

        // 2. تجميعهم في Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UIHelper.F_BODY);
        tabs.addTab("⚙ Input", inputPanel.build());
        tabs.addTab("📊 Gantt Charts", ganttTab.build());
        tabs.addTab("📋 Results", resultsTab.build());

        // 3. إضافة الـ Header والـ Tabs للفريم
        add(buildHeader(), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIHelper.ACCENT); // هيستخدم الأخضر الغامق اللي في UIHelper
        p.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel title = UIHelper.lbl("SJF vs Priority Comparison", UIHelper.F_TITLE, Color.WHITE);
        p.add(title, BorderLayout.WEST);
        return p;
    }

    private void runSimulation() {
        try {
            var processes = inputPanel.collectProcesses();
            
            // هنا بنادي على الـ Logic الخاص بالخوارزميات
            SimulationResult sjfR = new SJFscheduler(processes).run();
            // بافتراض إن Lower Number = Higher Priority
            SimulationResult priR = new PriorityScheduler(processes, true).run();

            // تحديث شاشات النتائج بالبيانات الجديدة
            ganttTab.update(sjfR.gantt, priR.gantt);
            resultsTab.update(sjfR, priR);
            
            inputPanel.showError(""); 
        } catch (Exception ex) {
            inputPanel.showError(ex.getMessage());
        }
    }
}