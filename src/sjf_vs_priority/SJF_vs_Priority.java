/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sjf_vs_priority;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SJF_vs_Priority {

    /**
     * @param args the command line arguments
     * 
     */
   


    public static void main(String[] args) {
        try { 
            // توحيد شكل البرنامج مع نظام التشغيل
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            // بننادي على الكلاس اللي بيجمع الـ Tabs كلها
            SchedulerGUI frame = new SchedulerGUI();
            frame.setVisible(true);
        });
    
    }}



