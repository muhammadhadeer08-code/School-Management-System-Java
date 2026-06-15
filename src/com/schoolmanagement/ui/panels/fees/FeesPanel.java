package com.schoolmanagement.ui.panels.fees;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class FeesPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    // ── Colour palette ─────────────────────────────────────────────────────
    private static final Color BG          = new Color(245, 247, 252);
    private static final Color CARD_WHITE  = Color.WHITE;
    private static final Color ACCENT_TEAL = new Color(56, 189, 150);
    private static final Color ACCENT_BLUE = new Color(99, 130, 210);
    private static final Color ACCENT_ORG  = new Color(255, 160, 60);
    private static final Color ACCENT_RED  = new Color(220, 75, 90);
    private static final Color HEADER_BG   = new Color(22, 33, 55);
    private static final Color TEXT_DARK   = new Color(25, 35, 60);
    private static final Color TEXT_GRAY   = new Color(130, 140, 160);
    private static final Color PAID_BG     = new Color(230, 255, 240);
    private static final Color UNPAID_BG   = new Color(255, 235, 238);

    // Stat card value labels (refreshed on every change)
    private JLabel totalRecordsVal, paidCountVal, unpaidCountVal, collectedVal;

    public FeesPanel() {
        setLayout(new BorderLayout());
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        add(buildTopSection(),  BorderLayout.NORTH);
        add(buildTableSection(), BorderLayout.CENTER);
        add(buildButtonBar(),   BorderLayout.SOUTH);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TOP — title + 4 stat cards
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildTopSection() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel title = new JLabel("  Fees Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel cards = new JPanel(new GridLayout(1, 4, 14, 0));
        cards.setOpaque(false);

        totalRecordsVal = new JLabel("0", SwingConstants.LEFT);
        paidCountVal    = new JLabel("0", SwingConstants.LEFT);
        unpaidCountVal  = new JLabel("0", SwingConstants.LEFT);
        collectedVal    = new JLabel("Rs. 0", SwingConstants.LEFT);

        cards.add(buildStatCard("Total Records",   totalRecordsVal, ACCENT_BLUE));
        cards.add(buildStatCard("Paid",            paidCountVal,    ACCENT_TEAL));
        cards.add(buildStatCard("Unpaid",          unpaidCountVal,  ACCENT_RED));
        cards.add(buildStatCard("Total Collected", collectedVal,    ACCENT_ORG));

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

        String[] columns = {"Student ID", "Name", "Class", "Fee Amount (Rs.)", "Due Date", "Status"};
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
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);

        // Color-coded rows: Paid = green, Unpaid = red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                if (!sel) {
                    String status = model.getRowCount() > row
                            ? String.valueOf(model.getValueAt(row, 5)) : "";
                    if ("Paid".equalsIgnoreCase(status)) {
                        setBackground(PAID_BG);
                    } else if ("Unpaid".equalsIgnoreCase(status)) {
                        setBackground(UNPAID_BG);
                    } else {
                        setBackground(row % 2 == 0 ? CARD_WHITE : new Color(248, 250, 253));
                    }
                    // Bold + colored status column
                    if (col == 5) {
                        setFont(new Font("Segoe UI", Font.BOLD, 13));
                        setForeground("Paid".equalsIgnoreCase(status)
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

        JButton addBtn      = buildBtn("  Add Record",    ACCENT_TEAL);
        JButton editBtn     = buildBtn("  Edit Record",   ACCENT_BLUE);
        JButton paidBtn     = buildBtn("  Mark Paid",     new Color(50, 170, 100));
        JButton unpaidBtn   = buildBtn("  Mark Unpaid",   ACCENT_ORG);
        JButton deleteBtn   = buildBtn("  Delete",        ACCENT_RED);
        JButton reportBtn   = buildBtn("  Show Report",   new Color(120, 90, 200));

        bar.add(addBtn);
        bar.add(editBtn);
        bar.add(paidBtn);
        bar.add(unpaidBtn);
        bar.add(deleteBtn);
        bar.add(reportBtn);

        addBtn.addActionListener(e    -> addFeeRecord());
        editBtn.addActionListener(e   -> editFeeRecord());
        paidBtn.addActionListener(e   -> updateStatus("Paid"));
        unpaidBtn.addActionListener(e -> updateStatus("Unpaid"));
        deleteBtn.addActionListener(e -> deleteFeeRecord());
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
        btn.setPreferredSize(new Dimension(140, 36));
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
    private void addFeeRecord() {
        JTextField idField     = new JTextField();
        JTextField nameField   = new JTextField();
        JTextField classField  = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField   = new JTextField("2026-12-31");

        Object[] fields = {
                "Student ID:",   idField,
                "Student Name:", nameField,
                "Class:",        classField,
                "Fee Amount (Rs.):", amountField,
                "Due Date (YYYY-MM-DD):", dateField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Add Fee Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String id     = idField.getText().trim();
        String name   = nameField.getText().trim();
        String cls    = classField.getText().trim();
        String amount = amountField.getText().trim();
        String date   = dateField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || cls.isEmpty() || amount.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, " All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Validate amount is a number
        try { Double.parseDouble(amount); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, " Fee Amount must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.addRow(new Object[]{id, name, cls, amount, date, "Unpaid"});
        refreshStats();
        JOptionPane.showMessageDialog(this, " Fee record added successfully!");
    }

    private void editFeeRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField amountField = new JTextField(String.valueOf(model.getValueAt(row, 3)));
        JTextField dateField   = new JTextField(String.valueOf(model.getValueAt(row, 4)));

        Object[] fields = {
                "Fee Amount (Rs.):", amountField,
                "Due Date:",         dateField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Edit Fee Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String newAmount = amountField.getText().trim();
        String newDate   = dateField.getText().trim();

        if (newAmount.isEmpty() || newDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, " Fields cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try { Double.parseDouble(newAmount); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, " Fee Amount must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.setValueAt(newAmount, row, 3);
        model.setValueAt(newDate,   row, 4);
        refreshStats();
        JOptionPane.showMessageDialog(this, " Record updated successfully!");
    }

    private void deleteFeeRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a record to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String name = String.valueOf(model.getValueAt(row, 1));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete fee record for \"" + name + "\"? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            refreshStats();
            JOptionPane.showMessageDialog(this, " Record deleted.");
        }
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Select a student record first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        model.setValueAt(status, row, 5);
        refreshStats();
        table.repaint();
    }

    private void refreshStats() {
        int total = model.getRowCount();
        int paid = 0;
        double collected = 0;

        for (int i = 0; i < total; i++) {
            String status = String.valueOf(model.getValueAt(i, 5));
            if ("Paid".equalsIgnoreCase(status)) {
                paid++;
                try { collected += Double.parseDouble(String.valueOf(model.getValueAt(i, 3))); }
                catch (NumberFormatException ignored) {}
            }
        }

        totalRecordsVal.setText(String.valueOf(total));
        paidCountVal.setText(String.valueOf(paid));
        unpaidCountVal.setText(String.valueOf(total - paid));
        collectedVal.setText("Rs. " + String.format("%.0f", collected));
    }

    private void showReport() {
        int total = model.getRowCount();
        if (total == 0) {
            JOptionPane.showMessageDialog(this, "No records available yet.", "Empty Report", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int paid = 0;
        double collected = 0, pending = 0;
        for (int i = 0; i < total; i++) {
            String status = String.valueOf(model.getValueAt(i, 5));
            double amt = 0;
            try { amt = Double.parseDouble(String.valueOf(model.getValueAt(i, 3))); } catch (Exception ignored) {}
            if ("Paid".equalsIgnoreCase(status)) { paid++; collected += amt; }
            else pending += amt;
        }
        double pct = (paid * 100.0) / total;
        String perf = pct >= 75 ? "Excellent " : pct >= 50 ? "Average " : "Needs Improvement ❌";

        JOptionPane.showMessageDialog(this,
                "  Fees Summary Report\n" +
                        "──────────────────────────────\n" +
                        "Total Records   :  " + total         + "\n" +
                        "Paid            :  " + paid           + "\n" +
                        "Unpaid          :  " + (total - paid) + "\n" +
                        "Collected       :  Rs. " + String.format("%.0f", collected) + "\n" +
                        "Pending         :  Rs. " + String.format("%.0f", pending)   + "\n" +
                        "Payment Rate    :  " + String.format("%.1f%%", pct)         + "\n" +
                        "Performance     :  " + perf,
                "Fees Report", JOptionPane.INFORMATION_MESSAGE);
    }
}