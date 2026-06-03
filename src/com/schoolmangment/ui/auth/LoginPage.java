package com.schoolmangment.ui.auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private JTextField emailField, phoneField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, showHideButton;
    private JLabel titleLabel, madeByLabel;

    public LoginPage() {
        setTitle("School Management System - Login");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Gradient background panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(255, 182, 193),
                        getWidth(), getHeight(), new Color(173, 216, 230));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);

        // Title
        titleLabel = new JLabel("Welcome to School Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setBounds(100, 40, 400, 30);
        panel.add(titleLabel);

        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(80, 100, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(160, 100, 220, 25);
        panel.add(emailField);

        // Phone field
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(80, 140, 80, 25);
        panel.add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(160, 140, 220, 25);
        panel.add(phoneField);

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 180, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 180, 220, 25);
        panel.add(passwordField);

        // Show/Hide button
        showHideButton = new JButton("Show");
        showHideButton.setBounds(390, 180, 80, 25);
        panel.add(showHideButton);

        showHideButton.addActionListener(e -> {
            if (passwordField.getEchoChar() == '\u2022') {
                passwordField.setEchoChar((char) 0);
                showHideButton.setText("Hide");
            } else {
                passwordField.setEchoChar('\u2022');
                showHideButton.setText("Show");
            }
        });

        // Buttons
        loginButton = new JButton("Login");
        loginButton.setBounds(160, 230, 100, 30);
        loginButton.setBackground(new Color(102, 205, 170));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton);

        signupButton = new JButton("Sign Up");
        signupButton.setBounds(280, 230, 100, 30);
        signupButton.setBackground(new Color(255, 105, 180));
        signupButton.setForeground(Color.WHITE);
        panel.add(signupButton);

        // Signature
        madeByLabel = new JLabel("✨ Created by Muhammad Hadeer ✨");
        madeByLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        madeByLabel.setForeground(new Color(0, 102, 204));
        madeByLabel.setBounds(180, 300, 250, 25);
        panel.add(madeByLabel);

        add(panel);

        // Button actions
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
            // Load MySQL driver (optional but recommended)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Correct connection line
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/testdb", "root", "hadeer123rameez@");

            // ✅ Correct query with all columns
            String query = "INSERT INTO users (email, password, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, phone);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created successfully!");
            conn.close();

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
