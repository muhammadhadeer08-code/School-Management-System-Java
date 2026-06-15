package com.schoolmanagement.ui.panels.classes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ClassesPanel extends JPanel {

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

    // Stat card value labels
    private JLabel totalClassesVal, highVal, mediumVal, lowVal;

    public ClassesPanel() {
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

        JLabel title = new JLabel("  Classes Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);

        totalClassesVal = new JLabel("0", SwingConstants.LEFT);
        highVal         = new JLabel("0", SwingConstants.LEFT);
        mediumVal       = new JLabel("0", SwingConstants.LEFT);
        lowVal          = new JLabel("0", SwingConstants.LEFT);

        cards.add(buildStatCard("Total Classes",      totalClassesVal, ACCENT_BLUE));
        cards.add(buildStatCard("High Performance",   highVal,         ACCENT_GREEN));
        cards.add(buildStatCard("Medium Performance", mediumVal,       ACCENT_ORG));
        cards.add(buildStatCard("Low Performance",    lowVal,          ACCENT_RED));

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

        String[] columns = {"Class ID", "Class Name", "Teacher", "Total Students", "Performance"};
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        // Color-coded performance rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    String perf = model.getRowCount() > row
                            ? String.valueOf(model.getValueAt(row, 4)) : "";
                    if ("High".equalsIgnoreCase(perf)) {
                        setBackground(new Color(230, 255, 240));
                    } else if ("Medium".equalsIgnoreCase(perf)) {
                        setBackground(new Color(255, 248, 225));
                    } else if ("Low".equalsIgnoreCase(perf)) {
                        setBackground(new Color(255, 235, 238));
                    } else {
                        setBackground(row % 2 == 0 ? CARD_WHITE : new Color(248, 250, 253));
                    }
                    // Bold colored performance column
                    if (col == 4) {
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                        if ("High".equalsIgnoreCase(perf))        setForeground(new Color(30, 140, 80));
                        else if ("Medium".equalsIgnoreCase(perf)) setForeground(new Color(180, 110, 20));
                        else if ("Low".equalsIgnoreCase(perf))    setForeground(ACCENT_RED);
                        else setForeground(TEXT_GRAY);
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

        JButton addBtn         = buildBtn("  Add Class",       ACCENT_TEAL);
        JButton editBtn        = buildBtn("  Edit Class",      ACCENT_BLUE);
        JButton perfBtn        = buildBtn("  Set Performance", ACCENT_ORG);
        JButton deleteBtn      = buildBtn("  Delete Class",    ACCENT_RED);
        JButton reportBtn      = buildBtn("  Show Report",     new Color(120, 90, 200));

        bar.add(addBtn);
        bar.add(editBtn);
        bar.add(perfBtn);
        bar.add(deleteBtn);
        bar.add(reportBtn);

        addBtn.addActionListener(e    -> addClass());
        editBtn.addActionListener(e   -> editClass());
        perfBtn.addActionListener(e   -> setPerformance());
        deleteBtn.addActionListener(e -> deleteClass());
        reportBtn.addActionListener(e -> showReport());

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
        btn.setPreferredSize(new Dimension(150, 36));
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
        JTextField idField      = new JTextField();
        JTextField nameField    = new JTextField();
        JTextField teacherField = new JTextField();
        JTextField studField    = new JTextField();

        Object[] fields = {
                "Class ID:",        idField,
                "Class Name:",      nameField,
                "Teacher Name:",    teacherField,
                "Total Students:",  studField
        };

        int res = JOptionPane.showConfirmDialog(this, fields,
                "Add Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String id      = idField.getText().trim();
        String name    = nameField.getText().trim();
        String teacher = teacherField.getText().trim();
        String stud    = studField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || teacher.isEmpty() || stud.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try { Integer.parseInt(stud); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, " Total Students must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.addRow(new Object[]{id, name, teacher, stud, "Not Set"});
        refreshStats();
        JOptionPane.showMessageDialog(this, " Class \"" + name + "\" added successfully!");
    }

    private void editClass() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a class to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField nameField    = new JTextField(String.valueOf(model.getValueAt(row, 1)));
        JTextField teacherField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
        JTextField studField    = new JTextField(String.valueOf(model.getValueAt(row, 3)));

        Object[] fields = {
                "Class Name:",     nameField,
                "Teacher Name:",   teacherField,
                "Total Students:", studField
        };

        int res = JOptionPane.showConfirmDialog(this, fields,
                "Edit Class", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String name    = nameField.getText().trim();
        String teacher = teacherField.getText().trim();
        String stud    = studField.getText().trim();

        if (name.isEmpty() || teacher.isEmpty() || stud.isEmpty()) {
            JOptionPane.showMessageDialog(this, " Fields cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try { Integer.parseInt(stud); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, " Total Students must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.setValueAt(name,    row, 1);
        model.setValueAt(teacher, row, 2);
        model.setValueAt(stud,    row, 3);
        JOptionPane.showMessageDialog(this, " Class updated successfully!");
    }

    private void deleteClass() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a class to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = String.valueOf(model.getValueAt(row, 1));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete class \"" + name + "\"? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            refreshStats();
            JOptionPane.showMessageDialog(this, "🗑 Class deleted.");
        }
    }

    private void setPerformance() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a class first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = {"High ⭐⭐⭐", "Medium ⭐⭐", "Low ⭐"};
        String choice = (String) JOptionPane.showInputDialog(this,
                "Select performance level for class \"" + model.getValueAt(row, 1) + "\":",
                "Set Performance", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice != null) {
            String perf = choice.startsWith("High") ? "High"
                    : choice.startsWith("Medium") ? "Medium" : "Low";
            model.setValueAt(perf, row, 4);
            refreshStats();
            table.repaint();
        }
    }

    private void refreshStats() {
        int total = model.getRowCount();
        int high = 0, medium = 0, low = 0;
        for (int i = 0; i < total; i++) {
            String p = String.valueOf(model.getValueAt(i, 4));
            if ("High".equalsIgnoreCase(p))        high++;
            else if ("Medium".equalsIgnoreCase(p)) medium++;
            else if ("Low".equalsIgnoreCase(p))    low++;
        }
        totalClassesVal.setText(String.valueOf(total));
        highVal.setText(String.valueOf(high));
        mediumVal.setText(String.valueOf(medium));
        lowVal.setText(String.valueOf(low));
    }

    private void showReport() {
        int total = model.getRowCount();
        if (total == 0) {
            JOptionPane.showMessageDialog(this, "No classes available yet.", "Empty Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int high = 0, medium = 0, low = 0, notSet = 0;
        for (int i = 0; i < total; i++) {
            String p = String.valueOf(model.getValueAt(i, 4));
            if      ("High".equalsIgnoreCase(p))   high++;
            else if ("Medium".equalsIgnoreCase(p)) medium++;
            else if ("Low".equalsIgnoreCase(p))    low++;
            else                                   notSet++;
        }
        JOptionPane.showMessageDialog(this,
                " Classes Summary Report\n" +
                        "──────────────────────────────\n" +
                        "Total Classes      :  " + total  + "\n" +
                        "High Performance   :  " + high   + " ⭐⭐⭐\n" +
                        "Medium Performance :  " + medium + " ⭐⭐\n" +
                        "Low Performance    :  " + low    + " ⭐\n" +
                        "Not Evaluated      :  " + notSet,
                "Classes Report", JOptionPane.INFORMATION_MESSAGE);
    }
}