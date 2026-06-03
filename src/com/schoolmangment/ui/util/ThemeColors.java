package com.schoolmangment.ui.util;

import javax.swing.*;
import java.awt.*;

public class ThemeColors {
    // Exact Colors from your Login Page
    public static final Color GRADIENT_START = new Color(255, 182, 193); // Light Pink
    public static final Color GRADIENT_END = new Color(173, 216, 230);   // Light Blue
    public static final Color LOGIN_BUTTON_COLOR = new Color(102, 205, 170); // Mint Green
    public static final Color SIGNUP_BUTTON_COLOR = new Color(255, 105, 180); // Hot Pink
    public static final Color TEXT_DARK = Color.DARK_GRAY;

    // Table Header and Sidebar Highlight Colors
    public static final Color SIDEBAR_BG = new Color(240, 244, 248);
    public static final Color TABLE_HEADER_BG = new Color(140, 180, 210);

    /**
     * Ek custom JPanel banata hai jisme aapke login page jisa automatic gradient background hota hai.
     */
    public static JPanel createGradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Smooth rendering
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_START,
                        getWidth(), getHeight(), GRADIENT_END);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    /**
     * Buttons ko theme ke mutabiq style karne ke liye helper method
     */
    public static void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }
}