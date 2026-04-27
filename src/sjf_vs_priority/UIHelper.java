package sjf_vs_priority;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class UIHelper {

    // ── Palette ──────────────────────────────────────────────────────────
    public static final Color BG         = new Color(245, 246, 250);
    public static final Color CARD       = Color.WHITE;
    public static final Color ACCENT     = new Color(29, 158, 117);
    public static final Color GREEN      = new Color(29, 158, 117);
    public static final Color DANGER     = new Color(226, 75, 74);
    public static final Color TEXT       = new Color(25, 30, 40);
    public static final Color TEXT_SEC   = new Color(100, 110, 130);
    public static final Color BORDER     = new Color(220, 224, 232);
    public static final Color GRAY_LIGHT = new Color(248, 249, 252);

    // ── Fonts ────────────────────────────────────────────────────────────
    public static final Font F_TITLE = new Font("SansSerif", Font.BOLD,  22);
    public static final Font F_H2    = new Font("SansSerif", Font.BOLD,  15);
    public static final Font F_H3    = new Font("SansSerif", Font.BOLD,  13);
    public static final Font F_BODY  = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font F_SMALL = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font F_BIG   = new Font("SansSerif", Font.BOLD,  22);

    // ── Label ────────────────────────────────────────────────────────────
    public static JLabel lbl(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JLabel htmlLbl(String html, Color color) {
        JLabel l = new JLabel("<html><body style='width:820px'>" + html + "</body></html>");
        l.setFont(F_BODY);
        l.setForeground(color);
        l.setBorder(new EmptyBorder(2, 0, 2, 0));
        l.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        return l;
    }

    public static JLabel bullet(String html) {
        return htmlLbl("• " + html, TEXT);
    }

    // ── Buttons ──────────────────────────────────────────────────────────
    public static JButton accentBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(F_BODY);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(7, 14, 7, 14));
        return b;
    }

    public static JButton outlineBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(F_BODY);
        b.setForeground(TEXT);
        b.setBackground(CARD);
        b.setBorder(new LineBorder(BORDER, 1, true));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ── Card panel ───────────────────────────────────────────────────────
    public static JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(12, 14, 12, 14)
        ));
        if (title != null && !title.isEmpty()) {
            JLabel l = lbl(title, F_H2, TEXT);
            l.setBorder(new EmptyBorder(0, 0, 6, 0));
            p.add(l, BorderLayout.NORTH);
        }
        p.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return p;
    }

    // ── Section card (for analysis) ──────────────────────────────────────
    public static JPanel sectionCard(String title) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));
        p.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel tl = lbl(title, F_H3, TEXT);
        tl.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        p.add(tl);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER);
        p.add(Box.createVerticalStrut(6));
        p.add(sep);
        p.add(Box.createVerticalStrut(8));
        return p;
    }

    // ── Metric box ───────────────────────────────────────────────────────
    public static JPanel metricBox(String name, JLabel val) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(GRAY_LIGHT);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JLabel nl = lbl(name, F_SMALL, TEXT_SEC);
        nl.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(nl, BorderLayout.NORTH);
        p.add(val, BorderLayout.CENTER);
        return p;
    }

    // ── Table ────────────────────────────────────────────────────────────
    public static JTable styledTable(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setFont(F_BODY);
        t.setRowHeight(28);
        t.getTableHeader().setFont(F_BODY);
        t.getTableHeader().setBackground(new Color(240, 242, 248));
        t.setGridColor(BORDER);
        t.setShowGrid(true);
        t.setSelectionBackground(new Color(220, 235, 255));
        t.setIntercellSpacing(new Dimension(1, 1));
        DefaultTableCellRenderer cen = new DefaultTableCellRenderer();
        cen.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < m.getColumnCount(); i++)
            t.getColumnModel().getColumn(i).setCellRenderer(cen);
        return t;
    }

    public static DefaultTableModel roModel(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    public static JScrollPane resScroll(DefaultTableModel m) {
        JTable t = styledTable(m);
        t.setRowHeight(26);
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(new LineBorder(BORDER));
        sp.setPreferredSize(new Dimension(400, 180));
        return sp;
    }

    // ── Layout helpers ───────────────────────────────────────────────────
    public static JPanel vBox() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(16, 18, 16, 18));
        return p;
    }

    public static JScrollPane scroll(JPanel p) {
        JScrollPane sp = new JScrollPane(p);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        sp.setBackground(BG);
        sp.getViewport().setBackground(BG);
        return sp;
    }
}