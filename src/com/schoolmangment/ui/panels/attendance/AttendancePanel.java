package com.schoolmangment.ui.panels.attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AttendancePanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> classSelector;
    private JButton addClassBtn, deleteClassBtn, addStudentBtn, markPresentBtn, markAbsentBtn, showReportBtn;
    private Map<String, DefaultTableModel> classData;
    private String selectedClass;

    // Matching Login Page Color Palette
    private final Color GRADIENT_START = new Color(255, 182, 193);     // Light Pink
    private final Color GRADIENT_END = new Color(173, 216, 230);       // Light Blue
    private final Color MINT_GREEN = new Color(102, 205, 170);         // Mint Green Button
    private final Color TEXT_DARK = Color.DARK_GRAY;

    public AttendancePanel() {
        setLayout(new BorderLayout());

        // Top Wrapper Panel to hold Title and Class Selector together smoothly
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Title Setup with Segoe UI
        JLabel title = new JLabel("Attendance Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        headerPanel.add(title);

        classData = new HashMap<>();

        // Class selector panel customization
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setOpaque(false); // Transparent to flow with background gradient

        JLabel classLabel = new JLabel("Class: ");
        classLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        classLabel.setForeground(TEXT_DARK);

        classSelector = new JComboBox<>();
        classSelector.addItem("Select Class");
        classSelector.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        classSelector.setBackground(Color.WHITE);

        topPanel.add(classLabel);
        topPanel.add(classSelector);
        headerPanel.add(topPanel);

        add(headerPanel, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Student ID", "Name", "Date", "Status"};
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
        addStudentBtn = new JButton("Add Student");
        markPresentBtn = new JButton("Mark Present");
        markAbsentBtn = new JButton("Mark Absent");
        showReportBtn = new JButton("Show Report");

        // Dynamically style all buttons and fix Windows background color bug
        for (JButton btn : new JButton[]{addClassBtn, deleteClassBtn, addStudentBtn, markPresentBtn, markAbsentBtn, showReportBtn}) {
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
        addStudentBtn.addActionListener(e -> addStudent());
        markPresentBtn.addActionListener(e -> updateStatus("Present"));
        markAbsentBtn.addActionListener(e -> updateStatus("Absent"));
        showReportBtn.addActionListener(e -> showReport());

        // Class selector listener
        classSelector.addActionListener(e -> {
            selectedClass = (String) classSelector.getSelectedItem();
            if (selectedClass != null && classData.containsKey(selectedClass)) {
                DefaultTableModel classModel = classData.get(selectedClass);
                // Refresh table data
                model.setRowCount(0);
                for (int i = 0; i < classModel.getRowCount(); i++) {
                    Object[] row = new Object[classModel.getColumnCount()];
                    for (int j = 0; j < classModel.getColumnCount(); j++) {
                        row[j] = classModel.getValueAt(i, j);
                    }
                    model.addRow(row);
                }
            }
        });
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
            classSelector.addItem(className);
            classData.put(className, new DefaultTableModel(getColumnNames(), 0));
            JOptionPane.showMessageDialog(this, "Class '" + className + "' added successfully!");
        }
    }

    private void deleteClass() {
        String selectedClass = (String) classSelector.getSelectedItem();
        if (selectedClass == null || selectedClass.equals("Select Class")) {
            JOptionPane.showMessageDialog(this, "Please select a class to delete!");
            return;
        }
        classData.remove(selectedClass);
        classSelector.removeItem(selectedClass);
        model.setRowCount(0);
        JOptionPane.showMessageDialog(this, "Class '" + selectedClass + "' deleted successfully!");
    }

    private void addStudent() {
        String selectedClass = (String) classSelector.getSelectedItem();
        if (selectedClass == null || selectedClass.equals("Select Class")) {
            JOptionPane.showMessageDialog(this, "Please select a class first!");
            return;
        }

        String id = JOptionPane.showInputDialog(this, "Enter Student ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Student Name:");

        if (id != null && name != null && !id.isEmpty() && !name.isEmpty()) {
            DefaultTableModel classModel = classData.get(selectedClass);
            classModel.addRow(new Object[]{id, name, LocalDate.now().toString(), "Absent"});
            // Refresh table safely
            model.setRowCount(0);
            for (int i = 0; i < classModel.getRowCount(); i++) {
                Object[] row = new Object[classModel.getColumnCount()];
                for (int j = 0; j < classModel.getColumnCount(); j++) {
                    row[j] = classModel.getValueAt(i, j);
                }
                model.addRow(row);
            }
            JOptionPane.showMessageDialog(this, "Student added to " + selectedClass + "!");
        }
    }

    private void updateStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update attendance!");
            return;
        }
        model.setValueAt(LocalDate.now().toString(), selectedRow, 2);
        model.setValueAt(status, selectedRow, 3);
    }

    private void showReport() {
        String selectedClass = (String) classSelector.getSelectedItem();
        if (selectedClass == null || selectedClass.equals("Select Class")) {
            JOptionPane.showMessageDialog(this, "Please select a class first!");
            return;
        }

        DefaultTableModel classModel = classData.get(selectedClass);
        int total = classModel.getRowCount();
        int presentCount = 0;

        for (int i = 0; i < total; i++) {
            String status = (String) classModel.getValueAt(i, 3);
            if ("Present".equalsIgnoreCase(status)) presentCount++;
        }

        double percentage = (total == 0) ? 0 : (presentCount * 100.0) / total;
        String grade;
        if (percentage >= 75) grade = "High";
        else if (percentage >= 50) grade = "Medium";
        else grade = "Low";

        JOptionPane.showMessageDialog(this,
                "Class Report: " + selectedClass + "\n\n" +
                        "Total Students: " + total +
                        "\nPresent Today: " + presentCount +
                        "\nAttendance Percentage: " + String.format("%.2f", percentage) + "%" +
                        "\nGrade: " + grade,
                "Attendance Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private Object[] getColumnNames() {
        return new Object[]{"Student ID", "Name", "Date", "Status"};
    }
}