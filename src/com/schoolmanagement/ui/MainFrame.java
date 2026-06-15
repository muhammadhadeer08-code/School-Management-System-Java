package com.schoolmanagement.ui;

import com.schoolmanagement.ui.reports.ReportsPanel;
import com.schoolmanagement.ui.panels.fees.FeesPanel;
import com.schoolmanagement.ui.panels.classes.ClassesPanel;
import com.schoolmanagement.ui.panels.teachers.TeachersPanel;
import com.schoolmanagement.ui.panels.students.StudentsPanel;
import com.schoolmanagement.ui.panels.attendance.AttendancePanel;
import com.schoolmanagement.ui.auth.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {

    // ── Layout panels ──────────────────────────────────────────────────────────
    private JPanel sidebar;
    private JPanel content;
    private JLabel timeLabel, dateLabel;
    private JLabel headerPageTitle;

    // Track which button is currently active
    private JButton activeButton = null;

    // ── Colour palette ─────────────────────────────────────────────────────────
    private static final Color SIDEBAR_BG      = new Color(22, 33, 55);      // Deep navy
    private static final Color ACTIVE_BG       = new Color(56, 189, 150);    // Mint-teal
    private static final Color HOVER_BG        = new Color(40, 55, 85);      // Lighter navy
    private static final Color LOGOUT_RED      = new Color(220, 75, 90);
    private static final Color HEADER_BG       = new Color(255, 255, 255);
    private static final Color HEADER_BORDER   = new Color(230, 234, 240);
    private static final Color TEXT_LIGHT      = new Color(200, 210, 230);
    private static final Color TEXT_WHITE      = Color.WHITE;
    private static final Color CLOCK_ACCENT    = new Color(56, 189, 150);

    // ── Nav items: {label, icon emoji} ────────────────────────────────────────
    private static final String[][] NAV_ITEMS = {
            {"Dashboard", "⊞"},
            {"Students",  "👤"},
            {"Attendance","✓"},
            {"Teachers",  "🎓"},
            {"Classes",   "📚"},
            {"Fees",      "💳"},
            {"Reports",   "📊"}
    };

    // ══════════════════════════════════════════════════════════════════════════
    public MainFrame() {
        setTitle("School Management System");
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 580));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildSidebar();
        buildContentArea();

        // Show Dashboard first
        showPage("Dashboard");
        setActiveButton(getNavButton("Dashboard"));

        setLayout(new BorderLayout());
        add(sidebar, BorderLayout.WEST);

        // Right side = header + content stacked
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(buildHeader(), BorderLayout.NORTH);
        rightPanel.add(content, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SIDEBAR
    // ══════════════════════════════════════════════════════════════════════════
    private void buildSidebar() {
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(195, getHeight()));

        // ── Logo area ──────────────────────────────────────────────────────
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(28, 16, 20, 16));

        JLabel logoIcon = new JLabel("🏫", SwingConstants.CENTER);
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoText = new JLabel("SchoolMS", SwingConstants.CENTER);
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 17));
        logoText.setForeground(TEXT_WHITE);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoSub = new JLabel("Management System", SwingConstants.CENTER);
        logoSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        logoSub.setForeground(TEXT_LIGHT);
        logoSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        logoPanel.add(logoText);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        logoPanel.add(logoSub);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(45, 60, 90));
        sep.setBackground(new Color(45, 60, 90));

        // ── Nav buttons ────────────────────────────────────────────────────
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));

        for (String[] item : NAV_ITEMS) {
            JButton btn = buildNavButton(item[0], item[1]);
            navPanel.add(btn);
            navPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        // ── Clock widget ───────────────────────────────────────────────────
        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        clockPanel.setOpaque(false);
        clockPanel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        // Clock card background
        JPanel clockCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(35, 50, 78));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        clockCard.setLayout(new BoxLayout(clockCard, BoxLayout.Y_AXIS));
        clockCard.setOpaque(false);
        clockCard.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));

        timeLabel = new JLabel("00:00:00 AM", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        timeLabel.setForeground(CLOCK_ACCENT);
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dateLabel = new JLabel("01 Jan 2026", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dateLabel.setForeground(TEXT_LIGHT);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        clockCard.add(timeLabel);
        clockCard.add(Box.createRigidArea(new Dimension(0, 4)));
        clockCard.add(dateLabel);
        clockPanel.add(clockCard);

        startClock();

        // ── Logout button ──────────────────────────────────────────────────
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(4, 12, 18, 12));

        JButton logoutBtn = new JButton("⏻  Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoutBtn.setForeground(TEXT_WHITE);
        logoutBtn.setBackground(LOGOUT_RED);
        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(true);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setPreferredSize(new Dimension(160, 36));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { logoutBtn.setBackground(new Color(190, 50, 65)); }
            public void mouseExited(MouseEvent e)  { logoutBtn.setBackground(LOGOUT_RED); }
        });
        logoutBtn.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (res == JOptionPane.YES_OPTION) {
                dispose();
                new LoginPage().setVisible(true);
            }
        });
        bottomPanel.add(logoutBtn);

        // ── Assemble sidebar ───────────────────────────────────────────────
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);
        topSection.add(logoPanel);
        topSection.add(sep);
        topSection.add(navPanel);

        JPanel bottomSection = new JPanel();
        bottomSection.setLayout(new BoxLayout(bottomSection, BoxLayout.Y_AXIS));
        bottomSection.setOpaque(false);
        bottomSection.add(clockPanel);
        bottomSection.add(bottomPanel);

        sidebar.add(topSection, BorderLayout.NORTH);
        sidebar.add(bottomSection, BorderLayout.SOUTH);
    }

    private JButton buildNavButton(String label, String icon) {
        JButton btn = new JButton(icon + "  " + label) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getBackground().equals(ACTIVE_BG)) {
                    g2.setColor(ACTIVE_BG);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                } else if (!getBackground().equals(SIDEBAR_BG)) {
                    g2.setColor(getBackground());
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(SIDEBAR_BG);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(175, 40));
        btn.setPreferredSize(new Dimension(175, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(HOVER_BG); btn.repaint(); }
            }
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) { btn.setBackground(SIDEBAR_BG); btn.repaint(); }
            }
        });
        btn.addActionListener(e -> {
            showPage(label);
            setActiveButton(btn);
            if (headerPageTitle != null) headerPageTitle.setText(label);
        });
        btn.putClientProperty("label", label);
        return btn;
    }

    private void setActiveButton(JButton btn) {
        if (activeButton != null) {
            activeButton.setBackground(SIDEBAR_BG);
            activeButton.setForeground(TEXT_LIGHT);
            activeButton.repaint();
        }
        activeButton = btn;
        if (btn != null) {
            btn.setBackground(ACTIVE_BG);
            btn.setForeground(TEXT_WHITE);
            btn.repaint();
        }
    }

    private JButton getNavButton(String label) {
        // Find button by label from sidebar navPanel
        return findButtonInSidebar(sidebar, label);
    }

    private JButton findButtonInSidebar(Container container, String label) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton) {
                Object lbl = ((JButton) c).getClientProperty("label");
                if (label.equals(lbl)) return (JButton) c;
            } else if (c instanceof Container) {
                JButton found = findButtonInSidebar((Container) c, label);
                if (found != null) return found;
            }
        }
        return null;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  TOP HEADER BAR
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setPreferredSize(new Dimension(getWidth(), 58));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, HEADER_BORDER));

        headerPageTitle = new JLabel("Dashboard");
        headerPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPageTitle.setForeground(new Color(30, 40, 65));
        headerPageTitle.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 0));

        JLabel adminLabel = new JLabel("👤  Admin");
        adminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        adminLabel.setForeground(new Color(100, 110, 130));
        adminLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        header.add(headerPageTitle, BorderLayout.WEST);
        header.add(adminLabel, BorderLayout.EAST);
        return header;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CONTENT AREA (CardLayout)
    // ══════════════════════════════════════════════════════════════════════════
    private void buildContentArea() {
        content = new JPanel(new CardLayout());
        content.setBackground(new Color(245, 247, 252));

        content.add(buildDashboardPanel(), "Dashboard");
        content.add(new StudentsPanel(),   "Students");
        content.add(new AttendancePanel(), "Attendance");
        content.add(new TeachersPanel(),   "Teachers");
        content.add(new ClassesPanel(),    "Classes");
        content.add(new FeesPanel(),       "Fees");
        content.add(new ReportsPanel(),    "Reports");
    }

    private void showPage(String pageName) {
        CardLayout cl = (CardLayout) content.getLayout();
        cl.show(content, pageName);
        if (headerPageTitle != null) headerPageTitle.setText(pageName);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DASHBOARD PANEL  (Statistics + Quick Info)
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildDashboardPanel() {
        JPanel dash = new JPanel(new BorderLayout());
        dash.setBackground(new Color(245, 247, 252));
        dash.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // ── Welcome banner ─────────────────────────────────────────────────
        JPanel banner = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(22, 33, 55),
                        getWidth(), 0, new Color(40, 120, 100));
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        banner.setOpaque(false);
        banner.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        banner.setPreferredSize(new Dimension(0, 90));

        JLabel welcomeTitle = new JLabel("Welcome back, Admin! 👋");
        welcomeTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeTitle.setForeground(Color.WHITE);

        JLabel welcomeSub = new JLabel("Here's what's happening in your school today.");
        welcomeSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        welcomeSub.setForeground(new Color(180, 220, 210));

        JPanel bannerText = new JPanel();
        bannerText.setLayout(new BoxLayout(bannerText, BoxLayout.Y_AXIS));
        bannerText.setOpaque(false);
        bannerText.add(welcomeTitle);
        bannerText.add(Box.createRigidArea(new Dimension(0, 5)));
        bannerText.add(welcomeSub);
        banner.add(bannerText, BorderLayout.CENTER);

        // ── Stats cards row ────────────────────────────────────────────────
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 16, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        statsRow.add(buildStatCard("Total Students", "245", "👤", new Color(56, 189, 150)));
        statsRow.add(buildStatCard("Total Teachers",  "18",  "🎓", new Color(99, 130, 210)));
        statsRow.add(buildStatCard("Fees Collected",  "Rs. 45,000", "💳", new Color(255, 160, 60)));
        statsRow.add(buildStatCard("Attendance Today","87%", "✓",  new Color(80, 190, 120)));

        // ── Quick action buttons ───────────────────────────────────────────
        JLabel quickLabel = new JLabel("Quick Actions");
        quickLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        quickLabel.setForeground(new Color(40, 50, 75));
        quickLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JPanel quickActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        quickActions.setOpaque(false);

        quickActions.add(buildQuickBtn("➕  Add Student",  "Students",  new Color(56, 189, 150)));
        quickActions.add(buildQuickBtn("➕  Add Teacher",  "Teachers",  new Color(99, 130, 210)));
        quickActions.add(buildQuickBtn("📋  Attendance",   "Attendance",new Color(80, 190, 120)));
        quickActions.add(buildQuickBtn("📊  View Reports", "Reports",   new Color(160, 100, 200)));

        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.setOpaque(false);
        bottomSection.add(quickLabel, BorderLayout.NORTH);
        bottomSection.add(quickActions, BorderLayout.CENTER);

        dash.add(banner,        BorderLayout.NORTH);
        dash.add(statsRow,      BorderLayout.CENTER);
        dash.add(bottomSection, BorderLayout.SOUTH);

        return dash;
    }

    /** One colourful statistics card */
    private JPanel buildStatCard(String title, String value, String icon, Color accent) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                // Left accent strip
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Float(0, 0, 6, getHeight(), 6, 6));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 14));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(new Color(25, 35, 60));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(130, 140, 160));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 3)));
        card.add(titleLabel);

        // Subtle shadow effect via border
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 8, 4),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 234, 242), 1),
                        BorderFactory.createEmptyBorder(18, 20, 18, 14)
                )
        ));
        return card;
    }

    /** Quick action button on dashboard */
    private JButton buildQuickBtn(String text, String target, Color color) {
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
        btn.setPreferredSize(new Dimension(150, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color darker = color.darker();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(darker); btn.repaint(); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color);  btn.repaint(); }
        });
        btn.addActionListener(e -> {
            showPage(target);
            setActiveButton(getNavButton(target));
        });
        return btn;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  LIVE CLOCK (runs on Swing Timer — safe for EDT)
    // ══════════════════════════════════════════════════════════════════════════
    private void startClock() {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("hh:mm:ss a");
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            timeLabel.setText(now.format(timeFmt));
            dateLabel.setText(now.format(dateFmt));
        });
        timer.start();
    }
}