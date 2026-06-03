package com.schoolmangment.ui.panels.reports;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addClassBtn, deleteClassBtn, addStudentsBtn, markAttendanceBtn, showSummaryBtn;

    // Matching Login Page Color Palette
    private final Color GRADIENT_START = new Color(255, 182, 193);     // Light Pink
    private final Color GRADIENT_END = new Color(173, 216, 230);       // Light Blue
    private final Color MINT_GREEN = new Color(102, 205, 170);         // Mint Green Button
    private final Color TEXT_DARK = Color.DARK_GRAY;

    public ReportsPanel() {
        setLayout(new BorderLayout());

        // Title Setup with Segoe UI
        JLabel title = new JLabel("Reports Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Class Name", "Total Students", "Present Today", "Attendance Grade"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Table Header Styling to match theme
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(MINT_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);

        // ScrollPane transparency logic for gradient flow
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel Setup
        JPanel buttons = new JPanel();
        buttons.setOpaque(false); // Transparent to overlay on background gradient

        addClassBtn = new JButton("Add Class");
        deleteClassBtn = new JButton("Delete Class");
        addStudentsBtn = new JButton("Add Students");
        markAttendanceBtn = new JButton("Mark Attendance");
        showSummaryBtn = new JButton("Show Summary");

        // Dynamically style all buttons and fix Windows background color bug
        for (JButton btn : new JButton[]{addClassBtn, deleteClassBtn, addStudentsBtn, markAttendanceBtn, showSummaryBtn}) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.setBackground(MINT_GREEN);
            btn.setForeground(Color.WHITE);
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
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
        addClassBtn.addActionListener(e -> addClass());
        deleteClassBtn.addActionListener(e -> deleteClass());
        addStudentsBtn.addActionListener(e -> addStudents());
        markAttendanceBtn.addActionListener(e -> markAttendance());
        showSummaryBtn.addActionListener(e -> showSummary());
    }

    /**
     * Custom painting to replicate the exact look and feel of the main LoginPage
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START,
                getWidth(), getHeight(), GRADIENT_END);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void addClass() {
        String className = JOptionPane.showInputDialog(this, "Enter Class Name:");
        if (className != null && !className.isEmpty()) {
            model.addRow(new Object[]{className, 0, 0, "No Data"});
            JOptionPane.showMessageDialog(this, "Class added successfully!");
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

    private void addStudents() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a class to add students!");
            return;
        }
        String total = JOptionPane.showInputDialog(this, "Enter total students:");
        if (total != null && !total.isEmpty()) {
            model.setValueAt(Integer.parseInt(total), selectedRow, 1);
            JOptionPane.showMessageDialog(this, "Students count updated!");
        }
    }

    private void markAttendance() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a class to mark attendance!");
            return;
        }
        String present = JOptionPane.showInputDialog(this, "Enter number of students present today:");
        if (present != null && !present.isEmpty()) {
            int total = (int) model.getValueAt(selectedRow, 1);
            int presentCount = Integer.parseInt(present);
            model.setValueAt(presentCount, selectedRow, 2);

            // Attendance grade calculation
            double percentage = (total == 0) ? 0 : (presentCount * 100.0) / total;
            String grade;
            if (percentage >= 75) {
                grade = "Good";
            } else if (percentage >= 50) {
                grade = "Medium";
            } else {
                grade = "Low";
            }
            model.setValueAt(grade, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Attendance updated successfully!");
        }
    }

    private void showSummary() {
        int totalClasses = model.getRowCount();
        int totalStudents = 0;
        int totalPresent = 0;

        for (int i = 0; i < totalClasses; i++) {
            totalStudents += (int) model.getValueAt(i, 1);
            totalPresent += (int) model.getValueAt(i, 2);
        }

        JOptionPane.showMessageDialog(this,
                "School Summary Report\n\n" +
                        "Total Classes: " + totalClasses +
                        "\nTotal Students: " + totalStudents +
                        "\nTotal Present Today: " + totalPresent +
                        "\nOverall Attendance: " +
                        (totalStudents == 0 ? "No Data" :
                                (totalPresent * 100 / totalStudents) + "%"),
                "Summary",
                JOptionPane.INFORMATION_MESSAGE);
    }
}