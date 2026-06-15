package com.schoolmanagement.ui.auth;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.*;

public class LoginPage extends JFrame {

    private JTextField emailField, phoneField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, showHideButton;

    // ── DB config ──────────────────────────────────────────────────────────
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "hadeer123rameez@";

    // ── Colours ────────────────────────────────────────────────────────────
    private static final Color LEFT_BG      = new Color(22, 33, 55);
    private static final Color ACCENT_TEAL  = new Color(56, 189, 150);
    private static final Color ACCENT_PINK  = new Color(220, 75, 120);
    private static final Color RIGHT_BG     = new Color(245, 247, 252);
    private static final Color FIELD_BG     = Color.WHITE;
    private static final Color FIELD_BORDER = new Color(210, 215, 225);
    private static final Color TEXT_DARK    = new Color(25, 35, 60);
    private static final Color TEXT_GRAY    = new Color(120, 130, 150);

    public LoginPage() {
        setTitle("School Management System — Login");
        setSize(860, 520);
        setMinimumSize(new Dimension(760, 460));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        add(buildLeftPanel());
        add(buildRightPanel());
    }

    // ══════════════════════════════════════════════════════════════════════
    //  LEFT — branding panel
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildLeftPanel() {
        JPanel left = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient: deep navy → dark teal
                GradientPaint gp = new GradientPaint(
                        0, 0, LEFT_BG,
                        0, getHeight(), new Color(20, 70, 65));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative circles
                g2.setColor(new Color(56, 189, 150, 30));
                g2.fillOval(-60, -60, 260, 260);
                g2.setColor(new Color(56, 189, 150, 20));
                g2.fillOval(getWidth() - 160, getHeight() - 160, 260, 260);
                g2.dispose();
            }
        };
        left.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo icon
        JLabel logo = new JLabel("🏫");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        gbc.gridy = 0;
        left.add(logo, gbc);

        // Title
        JLabel title = new JLabel("School Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(16, 30, 4, 30);
        left.add(title, gbc);

        JLabel titleLine2 = new JLabel("System");
        titleLine2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLine2.setForeground(ACCENT_TEAL);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 30, 10, 30);
        left.add(titleLine2, gbc);

        // Tagline
        JLabel tagline = new JLabel("Empowering Education");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        tagline.setForeground(new Color(180, 210, 200));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 30, 4, 30);
        left.add(tagline, gbc);

        JLabel tagline2 = new JLabel("with Smart Technology");
        tagline2.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        tagline2.setForeground(new Color(180, 210, 200));
        gbc.gridy = 4;
        left.add(tagline2, gbc);

        // Divider dots
        JLabel dots = new JLabel("• • •");
        dots.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dots.setForeground(ACCENT_TEAL);
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 30, 10, 30);
        left.add(dots, gbc);

        // Credit
        JLabel credit = new JLabel("by Muhammad Hadeer");
        credit.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        credit.setForeground(new Color(140, 170, 160));
        gbc.gridy = 6;
        gbc.insets = new Insets(4, 30, 10, 30);
        left.add(credit, gbc);

        return left;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RIGHT — login form
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildRightPanel() {
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(RIGHT_BG);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 40, 6, 40);

        // Heading
        JLabel heading = new JLabel("Welcome Back! ");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(TEXT_DARK);
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 40, 2, 40);
        right.add(heading, gbc);

        JLabel sub = new JLabel("Please sign in to your account");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(TEXT_GRAY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 40, 20, 40);
        right.add(sub, gbc);

        // Email
        gbc.insets = new Insets(5, 40, 2, 40);
        gbc.gridy = 2;
        right.add(fieldLabel("Email Address"), gbc);
        emailField = styledField("you@example.com");
        gbc.gridy = 3;
        right.add(emailField, gbc);

        // Phone
        gbc.gridy = 4;
        right.add(fieldLabel("Phone Number"), gbc);
        phoneField = styledField("03XX-XXXXXXX");
        gbc.gridy = 5;
        right.add(phoneField, gbc);

        // Password row
        gbc.gridy = 6;
        right.add(fieldLabel("Password"), gbc);

        JPanel passRow = new JPanel(new BorderLayout(6, 0));
        passRow.setOpaque(false);
        passwordField = new JPasswordField();
        styleComponent(passwordField);
        passwordField.setEchoChar('•');

        showHideButton = new JButton("Show");
        showHideButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        showHideButton.setForeground(ACCENT_TEAL);
        showHideButton.setBackground(FIELD_BG);
        showHideButton.setBorder(BorderFactory.createLineBorder(FIELD_BORDER));
        showHideButton.setFocusPainted(false);
        showHideButton.setPreferredSize(new Dimension(58, 36));
        showHideButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHideButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == '•') {
                passwordField.setEchoChar((char) 0);
                showHideButton.setText("Hide");
            } else {
                passwordField.setEchoChar('•');
                showHideButton.setText("Show");
            }
        });
        passRow.add(passwordField,  BorderLayout.CENTER);
        passRow.add(showHideButton, BorderLayout.EAST);

        gbc.gridy = 7;
        right.add(passRow, gbc);

        // Buttons
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 12, 0));
        btnRow.setOpaque(false);

        loginButton  = roundedButton("Login",   ACCENT_TEAL);
        signupButton = roundedButton("Sign Up", ACCENT_PINK);
        btnRow.add(loginButton);
        btnRow.add(signupButton);

        gbc.gridy = 8;
        gbc.insets = new Insets(18, 40, 10, 40);
        right.add(btnRow, gbc);

        loginButton.addActionListener(e  -> loginUser());
        signupButton.addActionListener(e -> signupUser());

        return right;
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(180, 185, 195));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 10, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        styleComponent(f);
        return f;
    }

    private void styleComponent(JComponent c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        c.setBackground(FIELD_BG);
        c.setForeground(TEXT_DARK);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        c.setPreferredSize(new Dimension(0, 36));
    }

    private JButton roundedButton(String text, Color color) {
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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(0, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color darker = color.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(darker); btn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(color);  btn.repaint(); }
        });
        return btn;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DB ACTIONS
    // ══════════════════════════════════════════════════════════════════════
    private void loginUser() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, " Email and password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, " Login successful! Welcome back.");
                dispose();
                new com.schoolmanagement.ui.MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, " Wrong email or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signupUser() {
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Validation
        if (email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required for sign up.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, " Password must be at least 6 characters.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String query = "INSERT INTO users (email, password, phone) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, phone);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, " Account created successfully! You can now login.");
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, " This email is already registered.", "Signup Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, " MySQL Driver not found!", "Driver Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}