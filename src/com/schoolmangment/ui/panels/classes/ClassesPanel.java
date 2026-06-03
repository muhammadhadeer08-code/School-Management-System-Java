package com.schoolmangment.ui.panels.classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClassesPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn, editBtn, deleteBtn, setPerformanceBtn, showReportBtn;

    // Login Page Colors Integration
    private final Color GRADIENT_START = new Color(255, 182, 193);     // Light Pink
    private final Color GRADIENT_END = new Color(173, 216, 230);       // Light Blue
    private final Color MINT_GREEN = new Color(102, 205, 170);         // Login Button Green
    private final Color TEXT_DARK = Color.DARK_GRAY;

    public ClassesPanel() {
        setLayout(new BorderLayout());

        // Title Setup
        JLabel title = new JLabel("Classes Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Class ID", "Class Name", "Teacher", "Total Students", "Performance"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Table Header Styling to match the theme
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(MINT_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);

        // Making the scroll pane transparent so the background gradient shows through smoothly
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel Setup
        JPanel buttons = new JPanel();
        buttons.setOpaque(false); // Transparent to show gradient

        addBtn = new JButton("Add Class");
        editBtn = new JButton("Edit Class");
        deleteBtn = new JButton("Delete Class");
        setPerformanceBtn = new JButton("Set Performance");
        showReportBtn = new JButton("Show Report");

        // Styling and adding buttons dynamically
        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, setPerformanceBtn, showReportBtn}) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBackground(MINT_GREEN);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
            buttons.add(btn);
        }
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(buttons, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addClass());
        editBtn.addActionListener(e -> editClass());
        deleteBtn.addActionListener(e -> deleteClass());
        setPerformanceBtn.addActionListener(e -> setPerformance());
        showReportBtn.addActionListener(e -> showReport());
    }

    /**
     * Overriding to paint the exact same background gradient as your LoginPage
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Create the top-left to bottom-right beautiful pink-blue layout
        GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START,
                getWidth(), getHeight(), GRADIENT_END);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void addClass() {
        String id = JOptionPane.showInputDialog(this, "Enter Class ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Class Name:");
        String teacher = JOptionPane.showInputDialog(this, "Enter Teacher Name:");
        String totalStudents = JOptionPane.showInputDialog(this, "Enter Total Students:");

        if (id != null && name != null && teacher != null && totalStudents != null &&
                !id.isEmpty() && !name.isEmpty() && !teacher.isEmpty() && !totalStudents.isEmpty()) {
            model.addRow(new Object[]{id, name, teacher, totalStudents, "Not Set"});
            JOptionPane.showMessageDialog(this, "Class added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "All fields are required!");
        }
    }

    private void editClass() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a class to edit!");
            return;
        }

        String newName = JOptionPane.showInputDialog(this, "Enter new class name:", model.getValueAt(selectedRow, 1));
        String newTeacher = JOptionPane.showInputDialog(this, "Enter new teacher name:", model.getValueAt(selectedRow, 2));
        String newTotal = JOptionPane.showInputDialog(this, "Enter new total students:", model.getValueAt(selectedRow, 3));

        if (newName != null && newTeacher != null && newTotal != null) {
            model.setValueAt(newName, selectedRow, 1);
            model.setValueAt(newTeacher, selectedRow, 2);
            model.setValueAt(newTotal, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Class updated successfully!");
        }
    }

    private void deleteClass() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a class to delete!");
            return;
        }
        model.removeRow(selectedRow);
        JOptionPane.showMessageDialog(this, "Class deleted successfully!");
    }

    private void setPerformance() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a class to set performance!");
            return;
        }

        String performance = JOptionPane.showInputDialog(this, "Enter performance level (High / Medium / Low):");
        if (performance != null && !performance.isEmpty()) {
            model.setValueAt(performance, selectedRow, 4);
            JOptionPane.showMessageDialog(this, "Performance set successfully!");
        }
    }

    private void showReport() {
        int totalClasses = model.getRowCount();
        int highCount = 0, mediumCount = 0, lowCount = 0;

        for (int i = 0; i < totalClasses; i++) {
            String perf = (String) model.getValueAt(i, 4);
            if ("High".equalsIgnoreCase(perf)) highCount++;
            else if ("Medium".equalsIgnoreCase(perf)) mediumCount++;
            else if ("Low".equalsIgnoreCase(perf)) lowCount++;
        }

        JOptionPane.showMessageDialog(this,
                "Classes Report\n\n" +
                        "Total Classes: " + totalClasses +
                        "\nHigh Performance: " + highCount +
                        "\nMedium Performance: " + mediumCount +
                        "\nLow Performance: " + lowCount,
                "Report Summary",
                JOptionPane.INFORMATION_MESSAGE);
    }
}