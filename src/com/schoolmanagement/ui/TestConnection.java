package com.schoolmanagement.ui;

import com.schoolmanagement.ui.db.DatabaseConnection;

public class TestConnection {
    public static void main(String[] args) {
        DatabaseConnection.getConnection();
    }
}
