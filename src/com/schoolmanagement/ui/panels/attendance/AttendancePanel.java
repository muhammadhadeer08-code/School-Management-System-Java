package com.schoolmanagement.ui.panels.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AttendancePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> classSelector;
    private Map<String, DefaultTableModel> classData;

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
    private JLabel totalVal, presentVal, absentVal, rateVal;

    public AttendancePanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        classData = new HashMap<>();

        add(buildTopSection(),   BorderLayout.NORTH);
        add(buildTableSection(), BorderLayout.CENTER);
        add(buildButtonBar(),    BorderLayout.SOUTH);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TOP — title + stat cards + class selector
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTopSection() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        // Title row
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel title = new JLabel("✓  Attendance Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);

        // Class selector on the right side of title row
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        selectorPanel.setOpaque(false);

        JLabel classLabel = new JLabel("Select Class:");
        classLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        classLabel.setForeground(TEXT_DARK);

        classSelector = new JComboBox<>();
        classSelector.addItem("-- Select Class --");
        classSelector.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        classSelector.setBackground(CARD_WHITE);
        classSelector.setPreferredSize(new Dimension(160, 32));

        selectorPanel.add(classLabel);
        selectorPanel.add(classSelector);

        titleRow.add(title,         BorderLayout.WEST);
        titleRow.add(selectorPanel, BorderLayout.EAST);

        // Stat cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);

        totalVal   = new JLabel("0",  SwingConstants.LEFT);
        presentVal = new JLabel("0",  SwingConstants.LEFT);
        absentVal  = new JLabel("0",  SwingConstants.LEFT);
        rateVal    = new JLabel("0%", SwingConstants.LEFT);

        cards.add(buildStatCard("Total Students", totalVal,   ACCENT_BLUE));
        cards.add(buildStatCard("Present Today",  presentVal, ACCENT_GREEN));
        cards.add(buildStatCard("Absent Today",   absentVal,  ACCENT_RED));
        cards.add(buildStatCard("Attendance Rate",rateVal,    ACCENT_ORG));

        top.add(titleRow, BorderLayout.NORTH);
        top.add(cards,    BorderLayout.CENTER);

        // Class selector listener
        classSelector.addActionListener(e -> loadClassData());

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

        String[] columns = {"Student ID", "Name", "Date", "Status"};
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
        table.getColumnModel().getColumn(0).setPreferredWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

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
                            ? String.valueOf(model.getValueAt(row, 3)) : "";
                    if ("Present".equalsIgnoreCase(status)) {
                        setBackground(new Color(230, 255, 240));
                    } else if ("Absent".equalsIgnoreCase(status)) {
                        setBackground(new Color(255, 235, 238));
                    } else {
                        setBackground(row % 2 == 0 ? CARD_WHITE : new Color(248, 250, 253));
                    }
                    if (col == 3) {
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

        JButton addClassBtn    = buildBtn("  Add Class",     ACCENT_BLUE);
        JButton deleteClassBtn = buildBtn(" Delete Class",  ACCENT_RED);
        JButton addStudentBtn  = buildBtn("  Add Student",   ACCENT_TEAL);
        JButton presentBtn     = buildBtn("  Mark Present",  ACCENT_GREEN);
        JButton absentBtn      = buildBtn("  Mark Absent",   ACCENT_ORG);
        JButton reportBtn      = buildBtn("  Show Report",   new Color(120, 90, 200));

        bar.add(addClassBtn);
        bar.add(deleteClassBtn);
        bar.add(addStudentBtn);
        bar.add(presentBtn);
        bar.add(absentBtn);
        bar.add(reportBtn);

        addClassBtn.addActionListener(e    -> addClass());
        deleteClassBtn.addActionListener(e -> deleteClass());
        addStudentBtn.addActionListener(e  -> addStudent());
        presentBtn.addActionListener(e     -> updateStatus("Present"));
        absentBtn.addActionListener(e      -> updateStatus("Absent"));
        reportBtn.addActionListener(e      -> showReport());

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
        btn.setPreferredSize(new Dimension(145, 36));
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
    private void addClass() {
        String name = JOptionPane.showInputDialog(this, "Enter Class Name:");
        if (name == null || name.trim().isEmpty()) return;
        name = name.trim();
        if (classData.containsKey(name)) {
            JOptionPane.showMessageDialog(this, " Class \"" + name + "\" already exists.", "Duplicate", JOptionPane.WARNING_MESSAGE);
            return;
        }
        classData.put(name, new DefaultTableModel(new Object[]{"Student ID", "Name", "Date", "Status"}, 0));
        classSelector.addItem(name);
        classSelector.setSelectedItem(name);
        JOptionPane.showMessageDialog(this, "Class \"" + name + "\" added!");
    }

    private void deleteClass() {
        String cls = (String) classSelector.getSelectedItem();
        if (cls == null || cls.startsWith("--")) {
            JOptionPane.showMessageDialog(this, " Select a class to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete class \"" + cls + "\" and all its students? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            classData.remove(cls);
            classSelector.removeItem(cls);
            model.setRowCount(0);
            refreshStats();
        }
    }

    private void addStudent() {
        String cls = (String) classSelector.getSelectedItem();
        if (cls == null || cls.startsWith("--")) {
            JOptionPane.showMessageDialog(this, " Select a class first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField idField   = new JTextField();
        JTextField nameField = new JTextField();
        Object[] fields = {"Student ID:", idField, "Student Name:", nameField};

        int res = JOptionPane.showConfirmDialog(this, fields,
                "Add Student to " + cls, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String id   = idField.getText().trim();
        String name = nameField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, " Both fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel classModel = classData.get(cls);
        classModel.addRow(new Object[]{id, name, LocalDate.now().toString(), "Absent"});
        loadClassData();
        JOptionPane.showMessageDialog(this, " Student \"" + name + "\" added to " + cls + "!");
    }

    private void updateStatus(String status) {
        String cls = (String) classSelector.getSelectedItem();
        if (cls == null || cls.startsWith("--")) {
            JOptionPane.showMessageDialog(this, " Select a class first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to mark attendance.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update both display model and stored classData model
        model.setValueAt(LocalDate.now().toString(), row, 2);
        model.setValueAt(status, row, 3);

        DefaultTableModel classModel = classData.get(cls);
        if (classModel != null && row < classModel.getRowCount()) {
            classModel.setValueAt(LocalDate.now().toString(), row, 2);
            classModel.setValueAt(status, row, 3);
        }
        refreshStats();
        table.repaint();
    }

    private void loadClassData() {
        String cls = (String) classSelector.getSelectedItem();
        model.setRowCount(0);
        if (cls == null || cls.startsWith("--") || !classData.containsKey(cls)) {
            refreshStats();
            return;
        }
        DefaultTableModel classModel = classData.get(cls);
        for (int i = 0; i < classModel.getRowCount(); i++) {
            Object[] row = new Object[classModel.getColumnCount()];
            for (int j = 0; j < classModel.getColumnCount(); j++) {
                row[j] = classModel.getValueAt(i, j);
            }
            model.addRow(row);
        }
        refreshStats();
    }

    private void refreshStats() {
        int total = model.getRowCount();
        int present = 0, absent = 0;
        for (int i = 0; i < total; i++) {
            String s = String.valueOf(model.getValueAt(i, 3));
            if ("Present".equalsIgnoreCase(s))     present++;
            else if ("Absent".equalsIgnoreCase(s)) absent++;
        }
        double rate = total == 0 ? 0 : (present * 100.0) / total;
        totalVal.setText(String.valueOf(total));
        presentVal.setText(String.valueOf(present));
        absentVal.setText(String.valueOf(absent));
        rateVal.setText(String.format("%.1f%%", rate));
    }

    private void showReport() {
        String cls = (String) classSelector.getSelectedItem();
        if (cls == null || cls.startsWith("--")) {
            JOptionPane.showMessageDialog(this, " Select a class first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DefaultTableModel classModel = classData.get(cls);
        int total = classModel.getRowCount();
        if (total == 0) {
            JOptionPane.showMessageDialog(this, "No students in this class yet.", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int present = 0;
        for (int i = 0; i < total; i++) {
            if ("Present".equalsIgnoreCase(String.valueOf(classModel.getValueAt(i, 3)))) present++;
        }
        double pct  = (present * 100.0) / total;
        String grade = pct >= 75 ? "High " : pct >= 50 ? "Medium " : "Low ";

        JOptionPane.showMessageDialog(this,
                "✓  Attendance Report — " + cls + "\n" +
                        "──────────────────────────────\n" +
                        "Total Students   :  " + total   + "\n" +
                        "Present Today    :  " + present + "\n" +
                        "Absent Today     :  " + (total - present) + "\n" +
                        "Attendance Rate  :  " + String.format("%.1f%%", pct) + "\n" +
                        "Grade            :  " + grade,
                "Attendance Report", JOptionPane.INFORMATION_MESSAGE);
    }
}