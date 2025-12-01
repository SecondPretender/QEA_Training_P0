package com.revature.p0.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    static Connection connection=null;
    public static Connection dbConnection(){
        try {
            connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ExpenseManager","root","password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
