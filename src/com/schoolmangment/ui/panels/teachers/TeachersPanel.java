package com.schoolmangment.ui.panels.teachers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class TeachersPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn, editBtn, deleteBtn, setLectureBtn, markPresentBtn, markAbsentBtn, showReportBtn;

    // Matching Login Page Color Palette
    private final Color GRADIENT_START = new Color(255, 182, 193);     // Light Pink
    private final Color GRADIENT_END = new Color(173, 216, 230);       // Light Blue
    private final Color MINT_GREEN = new Color(102, 205, 170);         // Mint Green Button
    private final Color TEXT_DARK = Color.DARK_GRAY;

    public TeachersPanel() {
        setLayout(new BorderLayout());

        // Title Setup with Segoe UI
        JLabel title = new JLabel("Teachers Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"ID", "Name", "Subject", "Experience", "Lecture", "Date", "Status"};
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

        addBtn = new JButton("Add Teacher");
        editBtn = new JButton("Edit Teacher");
        deleteBtn = new JButton("Delete Teacher");
        setLectureBtn = new JButton("Set Lecture");
        markPresentBtn = new JButton("Mark Present");
        markAbsentBtn = new JButton("Mark Absent");
        showReportBtn = new JButton("Show Report");

        // Dynamically style all buttons and fix Windows background color bug
        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, setLectureBtn, markPresentBtn, markAbsentBtn, showReportBtn}) {
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
        addBtn.addActionListener(e -> addTeacher());
        editBtn.addActionListener(e -> editTeacher());
        deleteBtn.addActionListener(e -> deleteTeacher());
        setLectureBtn.addActionListener(e -> setLecture());
        markPresentBtn.addActionListener(e -> updateStatus("Present"));
        markAbsentBtn.addActionListener(e -> updateStatus("Absent"));
        showReportBtn.addActionListener(e -> showReport());
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

    private void addTeacher() {
        String id = JOptionPane.showInputDialog(this, "Enter Teacher ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Teacher Name:");
        String subject = JOptionPane.showInputDialog(this, "Enter Subject:");
        String experience = JOptionPane.showInputDialog(this, "Enter Experience (e.g., 5 years):");

        if (id != null && name != null && subject != null && experience != null &&
                !id.isEmpty() && !name.isEmpty() && !subject.isEmpty() && !experience.isEmpty()) {
            model.addRow(new Object[]{id, name, subject, experience, "", LocalDate.now().toString(), "Absent"});
            JOptionPane.showMessageDialog(this, "Teacher added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "All fields are required!");
        }
    }

    private void editTeacher() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a teacher to edit!");
            return;
        }

        String newName = JOptionPane.showInputDialog(this, "Enter new name:", model.getValueAt(selectedRow, 1));
        String newSubject = JOptionPane.showInputDialog(this, "Enter new subject:", model.getValueAt(selectedRow, 2));
        String newExperience = JOptionPane.showInputDialog(this, "Enter new experience:", model.getValueAt(selectedRow, 3));

        if (newName != null && newSubject != null && newExperience != null) {
            model.setValueAt(newName, selectedRow, 1);
            model.setValueAt(newSubject, selectedRow, 2);
            model.setValueAt(newExperience, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Teacher updated successfully!");
        }
    }

    private void deleteTeacher() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a teacher to delete!");
            return;
        }
        model.removeRow(selectedRow);
        JOptionPane.showMessageDialog(this, "Teacher deleted successfully!");
    }

    private void setLecture() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a teacher to set lecture!");
            return;
        }
        String lecture = JOptionPane.showInputDialog(this, "Enter lecture topic:");
        if (lecture != null && !lecture.isEmpty()) {
            model.setValueAt(lecture, selectedRow, 4);
            model.setValueAt(LocalDate.now().toString(), selectedRow, 5);
            JOptionPane.showMessageDialog(this, "Lecture set successfully!");
        }
    }

    private void updateStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a teacher to update attendance!");
            return;
        }
        model.setValueAt(LocalDate.now().toString(), selectedRow, 5);
        model.setValueAt(status, selectedRow, 6);
    }

    private void showReport() {
        int totalTeachers = model.getRowCount();
        int presentCount = 0;
        int absentCount = 0;

        for (int i = 0; i < totalTeachers; i++) {
            String status = (String) model.getValueAt(i, 6);
            if ("Present".equalsIgnoreCase(status)) presentCount++;
            else if ("Absent".equalsIgnoreCase(status)) absentCount++;
        }

        String performance;
        double percentage = (totalTeachers == 0) ? 0 : (presentCount * 100.0) / totalTeachers;
        if (percentage >= 75) performance = "Excellent";
        else if (percentage >= 50) performance = "Average";
        else performance = "Needs Improvement";

        JOptionPane.showMessageDialog(this,
                "Teachers Report\n\n" +
                        "Total Teachers: " + totalTeachers +
                        "\nPresent: " + presentCount +
                        "\nAbsent: " + absentCount +
                        "\nAttendance Percentage: " + String.format("%.2f", percentage) + "%" +
                        "\nPerformance: " + performance,
                "Report Summary",
                JOptionPane.INFORMATION_MESSAGE);
    }
}