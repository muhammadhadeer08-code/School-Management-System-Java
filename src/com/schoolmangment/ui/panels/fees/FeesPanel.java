package com.schoolmangment.ui.panels.fees;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FeesPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JButton addBtn, editBtn, deleteBtn, markPaidBtn, markUnpaidBtn, showReportBtn;

    // Matching Login Page Color Palette
    private final Color GRADIENT_START = new Color(255, 182, 193);     // Light Pink
    private final Color GRADIENT_END = new Color(173, 216, 230);       // Light Blue
    private final Color MINT_GREEN = new Color(102, 205, 170);         // Mint Green Button
    private final Color TEXT_DARK = Color.DARK_GRAY;

    public FeesPanel() {
        setLayout(new BorderLayout());

        // Title Setup with Segoe UI
        JLabel title = new JLabel("Fees Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // Table Setup
        String[] columns = {"Student ID", "Name", "Class", "Fee Amount", "Due Date", "Fee Status"};
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

        // Buttons panel setup
        JPanel buttons = new JPanel();
        buttons.setOpaque(false); // Transparent to overlay on background gradient

        addBtn = new JButton("Add Fee Record");
        editBtn = new JButton("Edit Fee Record");
        deleteBtn = new JButton("Delete Fee Record");
        markPaidBtn = new JButton("Mark Paid");
        markUnpaidBtn = new JButton("Mark Unpaid");
        showReportBtn = new JButton("Show Report");

        // Dynamically style all buttons and fix Windows background color bug
        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, markPaidBtn, markUnpaidBtn, showReportBtn}) {
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
        addBtn.addActionListener(e -> addFeeRecord());
        editBtn.addActionListener(e -> editFeeRecord());
        deleteBtn.addActionListener(e -> deleteFeeRecord());
        markPaidBtn.addActionListener(e -> updateStatus("Paid"));
        markUnpaidBtn.addActionListener(e -> updateStatus("Unpaid"));
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

    private void addFeeRecord() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID:");
        String name = JOptionPane.showInputDialog(this, "Enter Student Name:");
        String cls = JOptionPane.showInputDialog(this, "Enter Class:");
        String amount = JOptionPane.showInputDialog(this, "Enter Fee Amount:");
        String dueDate = JOptionPane.showInputDialog(this, "Enter Due Date (YYYY-MM-DD):");

        if (id != null && name != null && cls != null && amount != null && dueDate != null &&
                !id.isEmpty() && !name.isEmpty() && !cls.isEmpty() && !amount.isEmpty() && !dueDate.isEmpty()) {
            model.addRow(new Object[]{id, name, cls, amount, dueDate, "Unpaid"});
            JOptionPane.showMessageDialog(this, "Fee record added successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "All fields are required!");
        }
    }

    private void editFeeRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a fee record to edit!");
            return;
        }

        String newAmount = JOptionPane.showInputDialog(this, "Enter new fee amount:", model.getValueAt(selectedRow, 3));
        String newDueDate = JOptionPane.showInputDialog(this, "Enter new due date:", model.getValueAt(selectedRow, 4));

        if (newAmount != null && newDueDate != null) {
            model.setValueAt(newAmount, selectedRow, 3);
            model.setValueAt(newDueDate, selectedRow, 4);
            JOptionPane.showMessageDialog(this, "Fee record updated successfully!");
        }
    }

    private void deleteFeeRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a fee record to delete!");
            return;
        }
        model.removeRow(selectedRow);
        JOptionPane.showMessageDialog(this, "Fee record deleted successfully!");
    }

    private void updateStatus(String status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update fee status!");
            return;
        }
        model.setValueAt(status, selectedRow, 5);
    }

    private void showReport() {
        int totalRecords = model.getRowCount();
        int paidCount = 0;
        int unpaidCount = 0;

        for (int i = 0; i < totalRecords; i++) {
            String status = (String) model.getValueAt(i, 5);
            if ("Paid".equalsIgnoreCase(status)) paidCount++;
            else unpaidCount++;
        }

        String performance;
        double percentage = (totalRecords == 0) ? 0 : (paidCount * 100.0) / totalRecords;
        if (percentage >= 75) performance = "Excellent";
        else if (percentage >= 50) performance = "Average";
        else performance = "Needs Improvement";

        JOptionPane.showMessageDialog(this,
                "Fees Report\n\n" +
                        "Total Records: " + totalRecords +
                        "\nPaid: " + paidCount +
                        "\nUnpaid: " + unpaidCount +
                        "\nPayment Percentage: " + String.format("%.2f", percentage) + "%" +
                        "\nPerformance: " + performance,
                "Report Summary",
                JOptionPane.INFORMATION_MESSAGE);
    }
}