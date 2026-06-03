package com.schoolmangment.ui;

import com.schoolmangment.ui.panels.reports.ReportsPanel;
import com.schoolmangment.ui.panels.fees.FeesPanel;
import com.schoolmangment.ui.panels.classes.ClassesPanel;
import com.schoolmangment.ui.panels.teachers.TeachersPanel;
import com.schoolmangment.ui.panels.students.StudentsPanel;
import com.schoolmangment.ui.panels.attendance.AttendancePanel;
import com.schoolmangment.ui.auth.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    private JPanel sidebar;
    private JPanel content;
    private JLabel timeLabel, dateLabel;

    // Theme Palette to match Login & Panels
    private final Color SIDEBAR_BG = new Color(240, 244, 248); // Soft professional light grey/blue
    private final Color MINT_GREEN = new Color(102, 205, 170); // Theme color for active/buttons
    private final Color LOGOUT_RED = new Color(219, 68, 85);    // Soft Red for logout
    private final Color TEXT_DARK = Color.DARK_GRAY;

    public MainFrame() {
        setTitle("School Management System");
        setSize(1000, 650); // Slightly adjusted for better widescreen view
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sidebar uses BorderLayout to position Navigation (TOP), Clock (CENTER), and Logout (BOTTOM)
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(180, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));

        // --- 1. TOP PANEL: Navigation Buttons ---
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JButton studentsBtn = new JButton("Students");
        JButton attendanceBtn = new JButton("Attendance");
        JButton teachersBtn = new JButton("Teachers");
        JButton classesBtn = new JButton("Classes");
        JButton feesBtn = new JButton("Fees");
        JButton reportsBtn = new JButton("Reports");

        JButton[] navButtons = {studentsBtn, attendanceBtn, teachersBtn, classesBtn, feesBtn, reportsBtn};

        // Style all navigation buttons cleanly
        for (JButton btn : navButtons) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setForeground(TEXT_DARK);
            btn.setBackground(Color.WHITE);
            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setFocusPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(160, 40)); // Fixed neat size
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));

            // Hover effect trick using MouseListener
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(MINT_GREEN);
                    btn.setForeground(Color.WHITE);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(TEXT_DARK);
                }
            });

            navPanel.add(btn);
            navPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Gap between buttons
        }

        sidebar.add(navPanel, BorderLayout.NORTH);

        // --- 2. MIDDLE PANEL: Live Date & Time Widget (Fills the blank space!) ---
        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        clockPanel.setOpaque(false);
        clockPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        timeLabel = new JLabel("00:00:00 AM", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timeLabel.setForeground(new Color(70, 130, 180)); // Steel Blue accent
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel = new JLabel("01 Jan 2026", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        clockPanel.add(timeLabel);
        clockPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        clockPanel.add(dateLabel);

        sidebar.add(clockPanel, BorderLayout.CENTER);

        // Start Live Clock Thread
        startClock();

        // --- 3. BOTTOM PANEL: Logout Button ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setBackground(LOGOUT_RED);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setOpaque(true);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setPreferredSize(new Dimension(140, 35));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        logoutBtn.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                this.dispose(); // Close Main Dashboard
                new LoginPage().setVisible(true); // Re-open Login Screen
            }
        });

        bottomPanel.add(logoutBtn);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        // --- CONTENT PANEL (CardLayout Setup) ---
        content = new JPanel(new CardLayout());
        content.add(new StudentsPanel(), "Students");
        content.add(new AttendancePanel(), "Attendance");
        content.add(new TeachersPanel(), "Teachers");
        content.add(new ClassesPanel(), "Classes");
        content.add(new FeesPanel(), "Fees");
        content.add(new ReportsPanel(), "Reports");

        // Action Listeners for Page Switching
        studentsBtn.addActionListener(e -> showPage("Students"));
        attendanceBtn.addActionListener(e -> showPage("Attendance"));
        teachersBtn.addActionListener(e -> showPage("Teachers"));
        classesBtn.addActionListener(e -> showPage("Classes"));
        feesBtn.addActionListener(e -> showPage("Fees"));
        reportsBtn.addActionListener(e -> showPage("Reports"));

        // Add layouts to Frame
        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    private void showPage(String pageName) {
        CardLayout cl = (CardLayout) content.getLayout();
        cl.show(content, pageName);
    }

    // Background Thread to update Time and Date live without freezing the UI
    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");

            timeLabel.setText(now.format(timeFormatter));
            dateLabel.setText(now.format(dateFormatter));
        });
        timer.start();
    }
}