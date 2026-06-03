package com.schoolmangment.ui.panels.students;

import com.schoolmangment.ui.db.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentsPanel extends JPanel {
    private JButton loadButton, addButton, updateButton, deleteButton;
    private JTable table;
    private DefaultTableModel model;

    // 👇 Ek chupi hui list jo peeche se asli Database IDs ka hisab rakhegi
    private ArrayList<Integer> actualDatabaseIds = new ArrayList<>();

    private final Color GRADIENT_START = new Color(255, 182, 193);
    private final Color GRADIENT_END = new Color(173, 216, 230);
    private final Color MINT_GREEN = new Color(102, 205, 170);
    private final Color TEXT_DARK = Color.DARK_GRAY;

    public StudentsPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Students Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Email"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table double-click par edit na ho
            }
        };
        table = new JTable(model);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        table.setRowHeight(25);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(MINT_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        loadButton = new JButton("Load Students");
        addButton = new JButton("Add Student");
        updateButton = new JButton("Update Student");
        deleteButton = new JButton("Delete Student");

        for (JButton btn : new JButton[]{loadButton, addButton, updateButton, deleteButton}) {
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
            buttonPanel.add(btn);
        }
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(buttonPanel, BorderLayout.SOUTH);

        loadButton.addActionListener(e -> loadStudents());
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        // Auto load data on startup
        loadStudents();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START, getWidth(), getHeight(), GRADIENT_END);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    // 🔄 DATA LOAD FUNCTION (Displays 1,2,3 but remembers actual IDs behind the scenes)
    private void loadStudents() {
        model.setRowCount(0);
        actualDatabaseIds.clear(); // Purani list clear karein

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM students";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            int displayId = 1; // Display par hamesha 1, 2, 3... hi chalega
            while (rs.next()) {
                int dbId = rs.getInt("student_id"); // Asli ID (e.g., 6, 7, 8)
                String name = rs.getString("name");
                String email = rs.getString("email");

                model.addRow(new Object[]{displayId++, name, email});
                actualDatabaseIds.add(dbId); // Asli ID ko backend list mein bacha liya
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addStudent() {
        String name = JOptionPane.showInputDialog(this, "Enter student name:");
        String email = JOptionPane.showInputDialog(this, "Enter student email:");

        if (name != null && email != null && !name.isEmpty() && !email.isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO students (name, email) VALUES (?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                loadStudents();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // 📝 UPDATE FUNCTION
    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update!");
            return;
        }

        // 👇 Row index ke mutabiq background list se ASLI database ID uthayein
        int actualId = actualDatabaseIds.get(selectedRow);

        String newName = JOptionPane.showInputDialog(this, "Enter new name:", model.getValueAt(selectedRow, 1));
        String newEmail = JOptionPane.showInputDialog(this, "Enter new email:", model.getValueAt(selectedRow, 2));

        if (newName != null && newEmail != null && !newName.isEmpty() && !newEmail.isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE students SET name=?, email=? WHERE student_id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, newName);
                stmt.setString(2, newEmail);
                stmt.setInt(3, actualId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                loadStudents();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // ❌ DELETE FUNCTION
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to delete!");
            return;
        }

        // 👇 Row index ke mutabiq background list se ASLI database ID uthayein
        int actualId = actualDatabaseIds.get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM students WHERE student_id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, actualId); // SQL mein bilkul sahi record delete hoga!
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                loadStudents(); // Reload karne par counting automatically 1, 2, 3 reset ho jayegi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}