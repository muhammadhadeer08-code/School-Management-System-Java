package com.schoolmangment.ui.auth; // Agar aapka package name different hai toh is line ko apne mutabiq set rkhna

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField emailField, phoneField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, showHideButton;
    private JLabel welcomeLabel, subtitleLabel, brandingTitle, brandingSub;

    public LoginPage() {
        setTitle("School Management System - Login");
        // Split-screen ke liye wide layout perfect lagta hai
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout ko 1 row aur 2 columns mein divide kiya
        setLayout(new GridLayout(1, 2));

        // ----------------------------------------------------
        // LEFT PANEL: Branding & Visuals (Solid Deep Theme)
        // ----------------------------------------------------
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Premium Slate Blue Color
                g2d.setPaint(new Color(34, 45, 65));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.insets = new Insets(10, 20, 10, 20);
        gbcLeft.fill = GridBagConstraints.CENTER;

        // Big Icon Placeholder
        JLabel logoLabel = new JLabel("");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 70));
        gbcLeft.gridy = 0;
        leftPanel.add(logoLabel, gbcLeft);

        // Project Title
        brandingTitle = new JLabel("School Management System");
        brandingTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandingTitle.setForeground(Color.WHITE);
        gbcLeft.gridy = 1;
        leftPanel.add(brandingTitle, gbcLeft);

        // Tagline
        brandingSub = new JLabel("Empowering Education with Smart Tech");
        brandingSub.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        brandingSub.setForeground(new Color(200, 210, 225));
        gbcLeft.gridy = 2;
        leftPanel.add(brandingSub, gbcLeft);


        // ----------------------------------------------------
        // RIGHT PANEL: Login Form (Gradient + Fixed Layout)
        // ----------------------------------------------------
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Aapka original pyara gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 182, 193),
                        getWidth(), getHeight(), new Color(173, 216, 230));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // GridBagLayout components ko exact screen ke center mein auto-align rakhega
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Header
        welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(45, 55, 72));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        rightPanel.add(welcomeLabel, gbc);

        subtitleLabel = new JLabel("Please enter your account details");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(90, 100, 120));
        gbc.gridy = 1; gbc.gridwidth = 3;
        rightPanel.add(subtitleLabel, gbc);

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.gridx = 0;
        rightPanel.add(emailLabel, gbc);

        emailField = new JTextField(18);
        gbc.gridx = 1; gbc.gridwidth = 2;
        rightPanel.add(emailField, gbc);

        // Phone Field
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridx = 0;
        rightPanel.add(phoneLabel, gbc);

        phoneField = new JTextField(18);
        gbc.gridx = 1; gbc.gridwidth = 2;
        rightPanel.add(phoneField, gbc);

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 4; gbc.gridwidth = 1; gbc.gridx = 0;
        rightPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(12);
        gbc.gridx = 1; gbc.gridwidth = 1;
        rightPanel.add(passwordField, gbc);

        // Show/Hide Button
        showHideButton = new JButton("Show");
        gbc.gridx = 2; gbc.gridwidth = 1;
        rightPanel.add(showHideButton, gbc);

        showHideButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == '\u2022') {
                passwordField.setEchoChar((char) 0);
                showHideButton.setText("Hide");
            } else {
                passwordField.setEchoChar('\u2022');
                showHideButton.setText("Show");
            }
        });

        // Action Buttons Sub-Panel (Side by Side layout)
        JPanel buttonSubPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonSubPanel.setOpaque(false);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(102, 205, 170));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        buttonSubPanel.add(loginButton);

        signupButton = new JButton("Sign Up");
        signupButton.setBackground(new Color(255, 105, 180));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        buttonSubPanel.add(signupButton);

        gbc.gridy = 5; gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        rightPanel.add(buttonSubPanel, gbc);

        // Signature (Yahan error fixed hai!)
        JLabel madeByLabel = new JLabel(" Created by Muhammad Hadeer ");
        madeByLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        madeByLabel.setForeground(new Color(0, 102, 204));
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER; // Correct alignment fix
        gbc.insets = new Insets(30, 10, 0, 10);
        rightPanel.add(madeByLabel, gbc);

        // Frame mein dono split panels add kiye
        add(leftPanel);
        add(rightPanel);

        // Button Click Listeners
        loginButton.addActionListener(e -> loginUser());
        signupButton.addActionListener(e -> signupUser());
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email format! Must contain @");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/testdb", "root", "hadeer123rameez@")) {
            String query = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new com.schoolmangment.ui.MainFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Wrong details! Try again.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void signupUser() {
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Invalid email format! Must contain @");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/testdb", "root", "hadeer123rameez@")) {

                String query = "INSERT INTO users (email, password, phone) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, phone);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Account created successfully!");
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate entry")) {
                JOptionPane.showMessageDialog(this, "Email already registered!");
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "MySQL Driver not found!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}