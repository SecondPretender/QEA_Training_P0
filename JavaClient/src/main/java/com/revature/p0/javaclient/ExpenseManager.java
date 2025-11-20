package com.revature.p0.javaclient;

import java.sql.*;

public class ExpenseManager {

    public static void main(String[] args) {
        String DB_PATH = "jdbc:sqlite:../ClientPython/database/expense.db";
        try (Connection conn = DriverManager.getConnection(DB_PATH)) {
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");
                Statement statement = conn.createStatement();
                ResultSet res = statement
                        .executeQuery("SELECT * FROM EXPENSE");
                while(res.next()){

                }
                // Perform database operations here
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
