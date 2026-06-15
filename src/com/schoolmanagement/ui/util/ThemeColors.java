package com.schoolmanagement.ui.util;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Central theme file — sab panels yahan se colors aur styles lete hain.
 * Koi bhi color change karna ho to sirf yahan karo, poora project update ho jayega.
 */
public class ThemeColors {

    // ── Main Palette ───────────────────────────────────────────────────────
    public static final Color BG            = new Color(245, 247, 252); // Page background
    public static final Color SIDEBAR_BG    = new Color(22,  33,  55);  // Deep navy sidebar
    public static final Color CARD_WHITE    = Color.WHITE;              // Card/table bg
    public static final Color HEADER_BG     = new Color(22,  33,  55);  // Table header

    // ── Accent Colors ──────────────────────────────────────────────────────
    public static final Color ACCENT_TEAL   = new Color(56,  189, 150); // Primary green-teal
    public static final Color ACCENT_BLUE   = new Color(99,  130, 210); // Secondary blue
    public static final Color ACCENT_ORANGE = new Color(255, 160, 60);  // Warning / fees
    public static final Color ACCENT_RED    = new Color(220, 75,  90);  // Danger / delete
    public static final Color ACCENT_GREEN  = new Color(80,  190, 120); // Success / paid
    public static final Color ACCENT_PURPLE = new Color(120, 90,  200); // Reports / summary

    // ── Text Colors ────────────────────────────────────────────────────────
    public static final Color TEXT_DARK     = new Color(25,  35,  60);  // Primary text
    public static final Color TEXT_GRAY     = new Color(130, 140, 160); // Secondary/label text
    public static final Color TEXT_LIGHT    = new Color(200, 210, 230); // Sidebar text
    public static final Color TEXT_WHITE    = Color.WHITE;

    // ── Status Colors ──────────────────────────────────────────────────────
    public static final Color STATUS_PAID_BG   = new Color(230, 255, 240); // Green row bg
    public static final Color STATUS_UNPAID_BG = new Color(255, 235, 238); // Red row bg
    public static final Color STATUS_WARN_BG   = new Color(255, 248, 225); // Orange row bg

    // ── Border Colors ──────────────────────────────────────────────────────
    public static final Color BORDER_LIGHT  = new Color(225, 229, 238);
    public static final Color FIELD_BORDER  = new Color(210, 215, 225);

    // ══════════════════════════════════════════════════════════════════════
    //  FACTORY METHODS
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Standard page panel with light background
     */
    public static JPanel createPagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        return panel;
    }

    /**
     * White card panel with rounded corners and accent left strip
     */
    public static JPanel createCard(Color accent) {
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
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(14, 18, 14, 14)
        ));
        return card;
    }

    /**
     * Rounded action button with hover effect
     */
    public static JButton createButton(String text, Color color) {
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
        btn.setForeground(TEXT_WHITE);
        btn.setBackground(color);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(145, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color darker = color.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(darker); btn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(color);  btn.repaint(); }
        });
        return btn;
    }

    /**
     * Styled JTable with dark header and alternating rows
     */
    public static void styleTable(JTable table) {
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(210, 240, 255));
        table.setSelectionForeground(TEXT_DARK);
        table.setBackground(CARD_WHITE);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(HEADER_BG);
        header.setForeground(TEXT_WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        header.setReorderingAllowed(false);
    }

    /**
     * Styled input field with border and padding
     */
    public static void styleField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(CARD_WHITE);
        field.setForeground(TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(0, 36));
    }

    /**
     * Section title label
     */
    public static JLabel createTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setForeground(TEXT_DARK);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        return lbl;
    }

    /**
     * Field label (bold small)
     */
    public static JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }
}