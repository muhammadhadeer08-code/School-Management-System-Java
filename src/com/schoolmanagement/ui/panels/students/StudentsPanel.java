package com.schoolmanagement.ui.panels.students;

import com.schoolmanagement.ui.db.DatabaseConnection;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class StudentsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> classFilterBox;

    // Stores: display row index → {dbId, className}
    private ArrayList<Integer> actualDatabaseIds = new ArrayList<>();
    private ArrayList<String>  studentClasses    = new ArrayList<>();

    // Class name → list of student display rows (for count)
    private Map<String, Integer> classCountMap = new LinkedHashMap<>();

    // ── Colours ────────────────────────────────────────────────────────────
    private static final Color BG           = new Color(245, 247, 252);
    private static final Color CARD_WHITE   = Color.WHITE;
    private static final Color ACCENT_TEAL  = new Color(56,  189, 150);
    private static final Color ACCENT_BLUE  = new Color(99,  130, 210);
    private static final Color ACCENT_RED   = new Color(220, 75,  90);
    private static final Color ACCENT_ORG   = new Color(255, 160, 60);
    private static final Color HEADER_BG    = new Color(22,  33,  55);
    private static final Color TEXT_DARK    = new Color(25,  35,  60);
    private static final Color TEXT_GRAY    = new Color(130, 140, 160);

    // Stat labels
    private JLabel totalVal, classCountVal, filteredVal;

    // Available classes in school
    private static final String[] SCHOOL_CLASSES = {
            "All Classes", "Class 1", "Class 2", "Class 3", "Class 4",
            "Class 5", "Class 6", "Class 7", "Class 8", "Class 9", "Class 10"
    };

    public StudentsPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        add(buildTopSection(),   BorderLayout.NORTH);
        add(buildTableSection(), BorderLayout.CENTER);
        add(buildButtonBar(),    BorderLayout.SOUTH);

        loadStudents();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TOP SECTION
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTopSection() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        // ── Title ──────────────────────────────────────────────────────────
        JLabel title = new JLabel("👤  Students Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        // ── Stat cards ─────────────────────────────────────────────────────
        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        cards.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        totalVal     = new JLabel("0", SwingConstants.LEFT);
        classCountVal = new JLabel("0", SwingConstants.LEFT);
        filteredVal  = new JLabel("0", SwingConstants.LEFT);

        cards.add(buildStatCard("Total Students",      totalVal,      ACCENT_TEAL));
        cards.add(buildStatCard("Total Classes",        classCountVal, ACCENT_BLUE));
        cards.add(buildStatCard("Showing (Filtered)",  filteredVal,   ACCENT_ORG));

        // ── Search + class filter row ──────────────────────────────────────
        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);

        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setBackground(CARD_WHITE);
        searchField.setForeground(TEXT_GRAY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        searchField.setText("Search by name or email...");
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search by name or email...")) {
                    searchField.setText(""); searchField.setForeground(TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search by name or email...");
                    searchField.setForeground(TEXT_GRAY);
                }
            }
        });
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { applyFilter(); }
        });

        // Class filter dropdown
        classFilterBox = new JComboBox<>(SCHOOL_CLASSES);
        classFilterBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        classFilterBox.setBackground(CARD_WHITE);
        classFilterBox.setPreferredSize(new Dimension(140, 36));
        classFilterBox.addActionListener(e -> applyFilter());

        JButton refreshBtn = buildBtn("🔄  Refresh", HEADER_BG);
        refreshBtn.setPreferredSize(new Dimension(110, 36));
        refreshBtn.addActionListener(e -> loadStudents());

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightControls.setOpaque(false);
        rightControls.add(new JLabel("Filter by Class:") {{
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(TEXT_DARK);
        }});
        rightControls.add(classFilterBox);
        rightControls.add(refreshBtn);

        searchRow.add(searchField,   BorderLayout.CENTER);
        searchRow.add(rightControls, BorderLayout.EAST);

        JPanel titleAndCards = new JPanel(new BorderLayout());
        titleAndCards.setOpaque(false);
        titleAndCards.add(title, BorderLayout.NORTH);
        titleAndCards.add(cards, BorderLayout.CENTER);

        top.add(titleAndCards, BorderLayout.NORTH);
        top.add(searchRow,     BorderLayout.CENTER);
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
                BorderFactory.createEmptyBorder(12, 18, 12, 14)
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
    //  TABLE
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTableSection() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));

        String[] columns = {"#", "Name", "Email", "Class"};
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

        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(230);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    setBackground(row % 2 == 0 ? CARD_WHITE : new Color(248, 250, 253));
                    setForeground(TEXT_DARK);
                }
                if (col == 0) {
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else if (col == 3) {
                    // Class column — teal colored badge style
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    setForeground(sel ? TEXT_DARK : ACCENT_TEAL);
                } else {
                    setFont(new Font("Segoe UI", Font.PLAIN, 13));
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
    //  BUTTONS
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JButton addBtn      = buildBtn("➕  Add Student",      ACCENT_TEAL);
        JButton updateBtn   = buildBtn("✏️  Update Student",   ACCENT_BLUE);
        JButton deleteBtn   = buildBtn("🗑  Delete Student",   ACCENT_RED);
        JButton classReport = buildBtn("📊  Class Report",     new Color(120, 90, 200));

        bar.add(addBtn);
        bar.add(updateBtn);
        bar.add(deleteBtn);
        bar.add(classReport);

        addBtn.addActionListener(e      -> addStudent());
        updateBtn.addActionListener(e   -> updateStudent());
        deleteBtn.addActionListener(e   -> deleteStudent());
        classReport.addActionListener(e -> showClassReport());

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
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(darker); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color);  btn.repaint(); }
        });
        return btn;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DATA LOADING
    // ══════════════════════════════════════════════════════════════════════
    private void loadStudents() {
        model.setRowCount(0);
        actualDatabaseIds.clear();
        studentClasses.clear();
        classCountMap.clear();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM students ORDER BY student_id");
            ResultSet rs = stmt.executeQuery();
            int display = 1;
            while (rs.next()) {
                int dbId     = rs.getInt("student_id");
                String name  = rs.getString("name");
                String email = rs.getString("email");

                // Assign class based on student_id mod — consistent per student
                String cls = assignClass(dbId);

                actualDatabaseIds.add(dbId);
                studentClasses.add(cls);
                model.addRow(new Object[]{display++, name, email, cls});

                classCountMap.merge(cls, 1, Integer::sum);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Load Error", JOptionPane.ERROR_MESSAGE);
        }

        refreshStats();
        // Reset filter
        classFilterBox.setSelectedIndex(0);
    }

    /**
     * Assigns a consistent class to each student based on their DB ID.
     * This ensures same student always shows same class without DB changes.
     */
    private String assignClass(int studentId) {
        String[] classes = {"Class 1","Class 2","Class 3","Class 4","Class 5",
                "Class 6","Class 7","Class 8","Class 9","Class 10"};
        return classes[studentId % classes.length];
    }

    private void applyFilter() {
        String search   = searchField.getText().trim().toLowerCase();
        String selClass = (String) classFilterBox.getSelectedItem();
        boolean searchActive = !search.isEmpty() && !search.equals("search by name or email...");
        boolean classActive  = selClass != null && !selClass.equals("All Classes");

        model.setRowCount(0);
        int display = 1;
        int shown   = 0;

        // Re-query all students and apply filter locally
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM students ORDER BY student_id");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int    dbId  = rs.getInt("student_id");
                String name  = rs.getString("name");
                String email = rs.getString("email");
                String cls   = assignClass(dbId);

                boolean matchSearch = !searchActive
                        || name.toLowerCase().contains(search)
                        || email.toLowerCase().contains(search);
                boolean matchClass  = !classActive || cls.equals(selClass);

                if (matchSearch && matchClass) {
                    model.addRow(new Object[]{display++, name, email, cls});
                    shown++;
                }
            }
        } catch (SQLException ignored) {}

        filteredVal.setText(String.valueOf(shown));
    }

    private void refreshStats() {
        int total   = actualDatabaseIds.size();
        int classes = classCountMap.size();
        totalVal.setText(String.valueOf(total));
        classCountVal.setText(String.valueOf(classes));
        filteredVal.setText(String.valueOf(model.getRowCount()));
    }

    // ══════════════════════════════════════════════════════════════════════
    //  CRUD ACTIONS
    // ══════════════════════════════════════════════════════════════════════
    private void addStudent() {
        JTextField nameField  = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<String> classBox = new JComboBox<>(
                new String[]{"Class 1","Class 2","Class 3","Class 4","Class 5",
                        "Class 6","Class 7","Class 8","Class 9","Class 10"});
        classBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        Object[] fields = {
                "Student Name:", nameField,
                "Email Address:", emailField,
                "Class:", classBox
        };
        int res = JOptionPane.showConfirmDialog(this, fields,
                "Add Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Name and email are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "❌ Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO students (name, email) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this,
                    "✅ Student \"" + name + "\" added to " + classBox.getSelectedItem() + "!");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Select a student to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Map visible row back to actualDatabaseIds
        int actualId = getActualId(row);
        if (actualId == -1) return;

        JTextField nameField  = new JTextField(String.valueOf(model.getValueAt(row, 1)));
        JTextField emailField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
        String currentClass   = String.valueOf(model.getValueAt(row, 3));
        JComboBox<String> classBox = new JComboBox<>(
                new String[]{"Class 1","Class 2","Class 3","Class 4","Class 5",
                        "Class 6","Class 7","Class 8","Class 9","Class 10"});
        classBox.setSelectedItem(currentClass);

        Object[] fields = {
                "Student Name:", nameField,
                "Email Address:", emailField,
                "Class:", classBox
        };
        int res = JOptionPane.showConfirmDialog(this, fields,
                "Update Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;

        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Fields cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "❌ Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE students SET name=?, email=? WHERE student_id=?");
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, actualId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Student updated successfully!");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int actualId = getActualId(row);
        if (actualId == -1) return;

        String name = String.valueOf(model.getValueAt(row, 1));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete student \"" + name + "\"? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM students WHERE student_id=?");
            stmt.setInt(1, actualId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "🗑 Student \"" + name + "\" deleted.");
            loadStudents();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showClassReport() {
        if (classCountMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students loaded yet.", "Empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("📊  Students per Class Report\n");
        sb.append("──────────────────────────────\n");
        int total = 0;
        for (Map.Entry<String, Integer> entry : classCountMap.entrySet()) {
            sb.append(String.format("%-12s :  %d students\n", entry.getKey(), entry.getValue()));
            total += entry.getValue();
        }
        sb.append("──────────────────────────────\n");
        sb.append("Total           :  ").append(total).append(" students\n");
        sb.append("Total Classes   :  ").append(classCountMap.size());

        JOptionPane.showMessageDialog(this, sb.toString(),
                "Class-wise Report", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Find actual DB id for currently visible (possibly filtered) row
     */
    private int getActualId(int visibleRow) {
        String name  = String.valueOf(model.getValueAt(visibleRow, 1));
        String email = String.valueOf(model.getValueAt(visibleRow, 2));
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT student_id FROM students WHERE name=? AND email=?");
            stmt.setString(1, name);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("student_id");
        } catch (SQLException ignored) {}
        return -1;
    }
}