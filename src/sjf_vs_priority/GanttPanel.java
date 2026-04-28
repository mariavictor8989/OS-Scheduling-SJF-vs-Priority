/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sjf_vs_priority;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GanttPanel extends JPanel {

    private List<GanttBlock> blocks;
    private int totalTime = 1;
    private static final int BAR_HEIGHT  = 46;
    private static final int TICK_HEIGHT = 18;
    private static final int PADDING     = 14;

    public GanttPanel() {
        setPreferredSize(new Dimension(800, BAR_HEIGHT + TICK_HEIGHT + PADDING * 2));
        setBackground(Color.WHITE);
    }

    public void setBlocks(List<GanttBlock> blocks) {
        this.blocks = blocks;
        if (blocks != null && !blocks.isEmpty()) {
            totalTime = blocks.get(blocks.size() - 1).end;
            totalTime = Math.max(totalTime, 1);
        }
        int minWidth = Math.max(800, totalTime * 36 + PADDING * 2);
        setPreferredSize(new Dimension(minWidth, BAR_HEIGHT + TICK_HEIGHT + PADDING * 2));
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (blocks == null || blocks.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int   availW = getWidth() - PADDING * 2;
        float scale  = (float) availW / totalTime;
        int   y      = PADDING;

        for (GanttBlock b : blocks) {
            int x = PADDING + Math.round(b.start * scale);
            int w = Math.round((b.end - b.start) * scale);
            if (w < 1) w = 1;

            // Fill
            g2.setColor(b.color);
            g2.fillRoundRect(x, y, w, BAR_HEIGHT, 6, 6);

            // Border
            g2.setColor(b.color.darker());
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(x, y, w, BAR_HEIGHT, 6, 6);

            // Label
            g2.setColor(b.pid.equals("IDLE") ? new Color(100, 100, 100) : Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            String label = b.pid;
            int lw = fm.stringWidth(label);
            if (lw + 4 <= w) {
                g2.drawString(label, x + (w - lw) / 2, y + BAR_HEIGHT / 2 + fm.getAscent() / 2 - 2);
            }
        }

        // Time ticks
        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        java.util.Set<Integer> drawn = new java.util.HashSet<>();

        for (GanttBlock b : blocks) {
            for (int t : new int[]{b.start, b.end}) {
                if (drawn.contains(t)) continue;
                drawn.add(t);

                int tx = PADDING + Math.round(t * scale);

                // tick line
                g2.setColor(new Color(180, 180, 180));
                g2.drawLine(tx, y + BAR_HEIGHT, tx, y + BAR_HEIGHT + 4);

                // number
                g2.setColor(new Color(80, 80, 80));
                String ts = String.valueOf(t);
                int tw = g2.getFontMetrics().stringWidth(ts);
                g2.drawString(ts, tx - tw / 2, y + BAR_HEIGHT + TICK_HEIGHT);
            }
        }
    }
}