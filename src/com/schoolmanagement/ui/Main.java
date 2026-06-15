package com.schoolmanagement.ui;

import com.schoolmanagement.ui.auth.LoginPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Application start point — open LoginPage first
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}
