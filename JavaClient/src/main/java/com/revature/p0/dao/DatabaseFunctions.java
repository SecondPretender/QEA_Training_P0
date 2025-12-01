package com.revature.p0.dao;


import java.sql.*;

public class DatabaseFunctions {
    final String MANAGER_ROLE = "Manager";
    /**
     *
     As a manager, I want to log in securely so that I can access and manage employee expense reports.
     As a manager, I want to view a list of all pending expenses so that I can review them efficiently.
     As a manager, I want to approve or deny submitted expenses so that I can manage reimbursements appropriately.
     As a manager, I want to add comments to expense decisions so that employees understand the reasoning behind approvals or denials.
     As a manager, I want to generate reports by employee, category, or date so that I can analyze spending trends and make informed decisions.

     */
    public static void login(Connection connection, String uname, String pword){
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, uname); // usernameInput is a user-provided string
            pstmt.setString(2, pword); // passwordInput is a user-provided string
            try (ResultSet rs = pstmt.executeQuery()) {

            }
        } catch (SQLException e) {
            // Handle exceptions
        }

    }

    public static void newManager(Connection conn) throws SQLException {
        String query = "INSERT INTO USER (username, password, role) VALUES (? ? ?)";


        Statement stmt = conn.createStatement(); // For Statement
        PreparedStatement pstmt = conn.prepareStatement(query);

        //TODO: set up values
        pstmt.setString(1, "");
        pstmt.setString(2, "");
        pstmt.setString(3, "Manager");

        pstmt.executeUpdate();


    }


    public static void editPending(){
        String query = "UPDATE EXPENSE WHERE ";

    }
    public static void viewPending(){
        String query = "";

    }
    public static void approvePending(){
        String query = "";

    }
    public static void denyPending(){
        String query = "";

    }
    //todo: could overload approve or deny with comment

    public static void report(){
        String query = "";
    }


}
