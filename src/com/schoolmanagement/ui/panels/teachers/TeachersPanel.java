package com.schoolmanagement.ui.panels.teachers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;

public class TeachersPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    // ── Colours ────────────────────────────────────────────────────────────
    private static final Color BG           = new Color(245, 247, 252);
    private static final Color CARD_WHITE   = Color.WHITE;
    private static final Color ACCENT_TEAL  = new Color(56,  189, 150);
    private static final Color ACCENT_BLUE  = new Color(99,  130, 210);
    private static final Color ACCENT_ORG   = new Color(255, 160, 60);
    private static final Color ACCENT_RED   = new Color(220, 75,  90);
    private static final Color ACCENT_GREEN = new Color(80,  190, 120);
    private static final Color HEADER_BG    = new Color(22,  33,  55);
    private static final Color TEXT_DARK    = new Color(25,  35,  60);
    private static final Color TEXT_GRAY    = new Color(130, 140, 160);

    // Stat card labels
    private JLabel totalVal, presentVal, absentVal, attendanceVal;

    public TeachersPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        add(buildTopSection(),   BorderLayout.NORTH);
        add(buildTableSection(), BorderLayout.CENTER);
        add(buildButtonBar(),    BorderLayout.SOUTH);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TOP — title + stat cards
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTopSection() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel title = new JLabel("  Teachers Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);

        totalVal      = new JLabel("0",   SwingConstants.LEFT);
        presentVal    = new JLabel("0",   SwingConstants.LEFT);
        absentVal     = new JLabel("0",   SwingConstants.LEFT);
        attendanceVal = new JLabel("0%",  SwingConstants.LEFT);

        cards.add(buildStatCard("Total Teachers",    totalVal,      ACCENT_BLUE));
        cards.add(buildStatCard("Present Today",     presentVal,    ACCENT_GREEN));
        cards.add(buildStatCard("Absent Today",      absentVal,     ACCENT_RED));
        cards.add(buildStatCard("Attendance Rate",   attendanceVal, ACCENT_ORG));

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
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
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

        String[] columns = {"ID", "Name", "Subject", "Experience", "Lecture Topic", "Date", "Status"};
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

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(160);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        // Color-coded rows: Present=green, Absent=red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    String status = model.getRowCount() > row
                            ? String.valueOf(model.getValueAt(row, 6)) : "";
                    if ("Present".equalsIgnoreCase(status)) {
                        setBackground(new Color(230, 255, 240));
                    } else if ("Absent".equalsIgnoreCase(status)) {
                        setBackground(new Color(255, 235, 238));
                    } else {
                        setBackground(row % 2 == 0 ? CARD_WHITE : new Color(248, 250, 253));
                    }
                    // Bold colored status column
                    if (col == 6) {
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                        setForeground("Present".equalsIgnoreCase(status)
                                ? new Color(30, 140, 80) : ACCENT_RED);
                    } else {
                        setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        setForeground(TEXT_DARK);
                    }
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
    //  BOTTOM — buttons
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JButton addBtn      = buildBtn("  Add Teacher",    ACCENT_TEAL);
        JButton editBtn     = buildBtn("  Edit Teacher",   ACCENT_BLUE);
        JButton lectureBtn  = buildBtn("  Set Lecture",    new Color(120, 90, 200));
        JButton presentBtn  = buildBtn("  Mark Present",   ACCENT_GREEN);
        JButton absentBtn   = buildBtn("  Mark Absent",    ACCENT_ORG);
        JButton deleteBtn   = buildBtn("  Delete",         ACCENT_RED);
        JButton reportBtn   = buildBtn("  Show Report",    HEADER_BG);

        bar.add(addBtn);
        bar.add(editBtn);
        bar.add(lectureBtn);
        bar.add(presentBtn);
        bar.add(absentBtn);
        bar.add(deleteBtn);
        bar.add(reportBtn);

        addBtn.addActionListener(e     -> addTeacher());
        editBtn.addActionListener(e    -> editTeacher());
        lectureBtn.addActionListener(e -> setLecture());
        presentBtn.addActionListener(e -> updateStatus("Present"));
        absentBtn.addActionListener(e  -> updateStatus("Absent"));
        deleteBtn.addActionListener(e  -> deleteTeacher());
        reportBtn.addActionListener(e  -> showReport());

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
        btn.setPreferredSize(new Dimension(138, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color darker = color.darker();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(darker); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color);  btn.repaint(); }
        });
        return btn;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ACTIONS
    // ══════════════════════════════════════════════════════════════════════
    private void addTeacher() {
        JTextField idField     = new JTextField();
        JTextField nameField   = new JTextField();
        JTextField subjectField= new JTextField();
        JTextField expField    = new JTextField();

        Object[] fields = {
                "Teacher ID:",            idField,
                "Teacher Name:",          nameField,
                "Subject:",               subjectField,
                "Experience (e.g. 5 yrs):", expField
        };

        int res = JOptionPane.showConfirmDialog(this, fields,
                "Add Teacher", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String id      = idField.getText().trim();
        String name    = nameField.getText().trim();
        String subject = subjectField.getText().trim();
        String exp     = expField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || subject.isEmpty() || exp.isEmpty()) {
            JOptionPane.showMessageDialog(this, " All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.addRow(new Object[]{id, name, subject, exp, "—", LocalDate.now().toString(), "Absent"});
        refreshStats();
        JOptionPane.showMessageDialog(this, " Teacher \"" + name + "\" added successfully!");
    }

    private void editTeacher() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a teacher to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField nameField    = new JTextField(String.valueOf(model.getValueAt(row, 1)));
        JTextField subjectField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
        JTextField expField     = new JTextField(String.valueOf(model.getValueAt(row, 3)));

        Object[] fields = {
                "Teacher Name:", nameField,
                "Subject:",      subjectField,
                "Experience:",   expField
        };

        int res = JOptionPane.showConfirmDialog(this, fields,
                "Edit Teacher", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String name    = nameField.getText().trim();
        String subject = subjectField.getText().trim();
        String exp     = expField.getText().trim();

        if (name.isEmpty() || subject.isEmpty() || exp.isEmpty()) {
            JOptionPane.showMessageDialog(this, " Fields cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setValueAt(name,    row, 1);
        model.setValueAt(subject, row, 2);
        model.setValueAt(exp,     row, 3);
        JOptionPane.showMessageDialog(this, " Teacher updated successfully!");
    }

    private void deleteTeacher() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a teacher to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = String.valueOf(model.getValueAt(row, 1));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete teacher \"" + name + "\"? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            refreshStats();
            JOptionPane.showMessageDialog(this, "🗑 Teacher deleted.");
        }
    }

    private void setLecture() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a teacher first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String lecture = JOptionPane.showInputDialog(this,
                "Enter lecture topic for \"" + model.getValueAt(row, 1) + "\":");
        if (lecture != null && !lecture.trim().isEmpty()) {
            model.setValueAt(lecture.trim(),              row, 4);
            model.setValueAt(LocalDate.now().toString(),  row, 5);
            JOptionPane.showMessageDialog(this, " Lecture set successfully!");
        }
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a teacher first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        model.setValueAt(LocalDate.now().toString(), row, 5);
        model.setValueAt(status,                     row, 6);
        refreshStats();
        table.repaint();
    }

    private void refreshStats() {
        int total = model.getRowCount();
        int present = 0, absent = 0;
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(model.getValueAt(i, 6));
            if ("Present".equalsIgnoreCase(s))      present++;
            else if ("Absent".equalsIgnoreCase(s))  absent++;
        }
        double pct = total == 0 ? 0 : (present * 100.0) / total;
        totalVal.setText(String.valueOf(total));
        presentVal.setText(String.valueOf(present));
        absentVal.setText(String.valueOf(absent));
        attendanceVal.setText(String.format("%.1f%%", pct));
    }

    private void showReport() {
        int total = model.getRowCount();
        if (total == 0) {
            JOptionPane.showMessageDialog(this, "No teachers added yet.", "Empty Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int present = 0, absent = 0;
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(model.getValueAt(i, 6));
            if ("Present".equalsIgnoreCase(s))     present++;
            else if ("Absent".equalsIgnoreCase(s)) absent++;
        }
        double pct  = (present * 100.0) / total;
        String perf = pct >= 75 ? "Excellent " : pct >= 50 ? "Average " : "Needs Improvement ❌";

        JOptionPane.showMessageDialog(this,
                "🎓  Teachers Summary Report\n" +
                        "──────────────────────────────\n" +
                        "Total Teachers     :  " + total   + "\n" +
                        "Present Today      :  " + present + "\n" +
                        "Absent Today       :  " + absent  + "\n" +
                        "Attendance Rate    :  " + String.format("%.1f%%", pct) + "\n" +
                        "Performance        :  " + perf,
                "Teachers Report", JOptionPane.INFORMATION_MESSAGE);
    }
}