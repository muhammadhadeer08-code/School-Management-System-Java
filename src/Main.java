package com.schoolmangment.ui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Application start point — open LoginPage first
        SwingUtilities.invokeLater(() -> {
            new com.schoolmangment.ui.auth.LoginPage().setVisible(true);
        });
    }
}
