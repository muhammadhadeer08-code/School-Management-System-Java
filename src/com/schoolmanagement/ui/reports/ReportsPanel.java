package com.schoolmanagement.ui.reports;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ReportsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    // ── Colour palette (matches MainFrame dark theme) ──────────────────────
    private static final Color BG           = new Color(245, 247, 252);
    private static final Color CARD_WHITE   = Color.WHITE;
    private static final Color ACCENT_TEAL  = new Color(56, 189, 150);
    private static final Color ACCENT_BLUE  = new Color(99, 130, 210);
    private static final Color ACCENT_ORG   = new Color(255, 160, 60);
    private static final Color ACCENT_RED   = new Color(220, 75, 90);
    private static final Color HEADER_BG    = new Color(22, 33, 55);
    private static final Color TEXT_DARK    = new Color(25, 35, 60);
    private static final Color TEXT_GRAY    = new Color(130, 140, 160);

    // Stat label references so we can refresh them
    private JLabel totalClassesVal, totalStudentsVal, avgAttendanceVal;

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        add(buildTopSection(), BorderLayout.NORTH);
        add(buildTableSection(), BorderLayout.CENTER);
        add(buildButtonBar(), BorderLayout.SOUTH);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TOP — title + 3 stat cards
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTopSection() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        // Title
        JLabel title = new JLabel("  Reports & Attendance");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        // Stat cards row
        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);

        totalClassesVal   = new JLabel("0", SwingConstants.LEFT);
        totalStudentsVal  = new JLabel("0", SwingConstants.LEFT);
        avgAttendanceVal  = new JLabel("0%", SwingConstants.LEFT);

        cards.add(buildStatCard("Total Classes",   totalClassesVal,  ACCENT_TEAL));
        cards.add(buildStatCard("Total Students",  totalStudentsVal, ACCENT_BLUE));
        cards.add(buildStatCard("Avg Attendance",  avgAttendanceVal, ACCENT_ORG));

        top.add(title, BorderLayout.NORTH);
        top.add(cards, BorderLayout.CENTER);
        return top;
    }

    private JPanel buildStatCard(String label, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Float(0, 0, 6, getHeight(), 6, 6));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 234, 242), 1),
                BorderFactory.createEmptyBorder(14, 18, 14, 14)
        ));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(TEXT_DARK);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(lbl);
        return card;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  CENTER — table
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTableSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(18, 0, 14, 0));

        String[] columns = {"Class Name", "Total Students", "Present Today", "Attendance %", "Grade"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(210, 240, 255));
        table.setSelectionForeground(TEXT_DARK);
        table.setBackground(CARD_WHITE);
        table.setFocusable(false);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        // Color-coded rows based on Grade column
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    String grade = model.getRowCount() > row
                            ? (String) model.getValueAt(row, 4) : "";
                    if ("Good ".equals(grade))        setBackground(new Color(230, 255, 240));
                    else if ("Medium ".equals(grade)) setBackground(new Color(255, 248, 225));
                    else if ("Low ".equals(grade))    setBackground(new Color(255, 235, 238));
                    else setBackground(row % 2 == 0 ? CARD_WHITE : new Color(248, 250, 253));
                    setForeground(TEXT_DARK);
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(225, 229, 238), 1));
        scroll.getViewport().setBackground(CARD_WHITE);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BOTTOM — action buttons
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JButton addClassBtn      = buildBtn("  Add Class",       ACCENT_TEAL);
        JButton addStudentsBtn   = buildBtn("  Set Students",    ACCENT_BLUE);
        JButton markAttendBtn    = buildBtn("  Mark Attendance",  new Color(80, 190, 120));
        JButton deleteClassBtn   = buildBtn("  Delete Class",    ACCENT_RED);
        JButton showSummaryBtn   = buildBtn("  Show Summary",    new Color(120, 90, 200));

        bar.add(addClassBtn);
        bar.add(addStudentsBtn);
        bar.add(markAttendBtn);
        bar.add(deleteClassBtn);
        bar.add(showSummaryBtn);

        // Actions
        addClassBtn.addActionListener(e    -> addClass());
        addStudentsBtn.addActionListener(e -> addStudents());
        markAttendBtn.addActionListener(e  -> markAttendance());
        deleteClassBtn.addActionListener(e -> deleteClass());
        showSummaryBtn.addActionListener(e -> showSummary());

        return bar;
    }

    private JButton buildBtn(String text, Color color) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(155, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color darker = color.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(darker); btn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(color);  btn.repaint(); }
        });
        return btn;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ACTIONS
    // ══════════════════════════════════════════════════════════════════════
    private void addClass() {
        String name = JOptionPane.showInputDialog(this, "Enter Class Name:");
        if (name != null && !name.trim().isEmpty()) {
            model.addRow(new Object[]{name.trim(), 0, 0, "0%", "No Data"});
            refreshStats();
            JOptionPane.showMessageDialog(this, "Class \"" + name.trim() + "\" added!");
        }
    }

    private void deleteClass() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Please select a class to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String className = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete class \"" + className + "\"? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            refreshStats();
            JOptionPane.showMessageDialog(this, "🗑 Class deleted.");
        }
    }

    private void addStudents() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a class first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String input = JOptionPane.showInputDialog(this, "Enter total number of students:");
        if (input == null || input.trim().isEmpty()) return;
        try {
            int total = Integer.parseInt(input.trim());
            if (total < 0) throw new NumberFormatException();
            model.setValueAt(total, row, 1);
            recalcGrade(row);
            refreshStats();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number. Please enter a positive integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markAttendance() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a class first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int total = (int) model.getValueAt(row, 1);
        if (total == 0) {
            JOptionPane.showMessageDialog(this, " Set total students first before marking attendance.", "No Students", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String input = JOptionPane.showInputDialog(this, "Students present today (out of " + total + "):");
        if (input == null || input.trim().isEmpty()) return;
        try {
            int present = Integer.parseInt(input.trim());
            if (present < 0 || present > total)
                throw new NumberFormatException();
            model.setValueAt(present, row, 2);
            recalcGrade(row);
            refreshStats();
            JOptionPane.showMessageDialog(this, " Attendance marked successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, " Invalid number. Must be between 0 and " + total + ".", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Recalculate attendance % and grade for a row */
    private void recalcGrade(int row) {
        int total   = (int) model.getValueAt(row, 1);
        int present = (int) model.getValueAt(row, 2);
        if (total == 0) {
            model.setValueAt("0%",      row, 3);
            model.setValueAt("No Data", row, 4);
            return;
        }
        double pct = (present * 100.0) / total;
        model.setValueAt(String.format("%.1f%%", pct), row, 3);
        String grade;
        if      (pct >= 75) grade = "Good ";
        else if (pct >= 50) grade = "Medium ";
        else                grade = "Low ";
        model.setValueAt(grade, row, 4);
    }

    /** Refresh the top stat cards */
    private void refreshStats() {
        int classes  = model.getRowCount();
        int students = 0;
        double totalPct = 0;
        int gradedRows  = 0;

        for (int i = 0; i < classes; i++) {
            students += (int) model.getValueAt(i, 1);
            String pctStr = (String) model.getValueAt(i, 3);
            if (pctStr != null && pctStr.endsWith("%") && !pctStr.equals("0%")) {
                try {
                    totalPct += Double.parseDouble(pctStr.replace("%", ""));
                    gradedRows++;
                } catch (NumberFormatException ignored) {}
            }
        }

        totalClassesVal.setText(String.valueOf(classes));
        totalStudentsVal.setText(String.valueOf(students));
        avgAttendanceVal.setText(gradedRows == 0 ? "N/A"
                : String.format("%.1f%%", totalPct / gradedRows));
    }

    private void showSummary() {
        int classes  = model.getRowCount();
        if (classes == 0) {
            JOptionPane.showMessageDialog(this, "No data available yet. Add classes first.", "Empty Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int totalStudents = 0, totalPresent = 0;
        for (int i = 0; i < classes; i++) {
            totalStudents += (int) model.getValueAt(i, 1);
            totalPresent  += (int) model.getValueAt(i, 2);
        }
        double overall = totalStudents == 0 ? 0 : (totalPresent * 100.0) / totalStudents;
        String overallGrade = overall >= 75 ? "Good " : overall >= 50 ? "Medium " : "Low ";

        JOptionPane.showMessageDialog(this,
                "  School Summary Report\n" +
                        "─────────────────────────────\n" +
                        "Total Classes      :  " + classes       + "\n" +
                        "Total Students     :  " + totalStudents + "\n" +
                        "Present Today      :  " + totalPresent  + "\n" +
                        "Overall Attendance :  " + String.format("%.1f%%", overall) + "\n" +
                        "Overall Grade      :  " + overallGrade,
                "Summary Report", JOptionPane.INFORMATION_MESSAGE);
    }
}