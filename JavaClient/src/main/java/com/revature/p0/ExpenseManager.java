package com.revature.p0;

import com.revature.p0.model.Manager;
import com.revature.p0.service.LoginLoop;
import com.revature.p0.service.UserLoop;
import com.revature.p0.utils.ConnectionUtil;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.*;
import java.util.logging.SimpleFormatter;


/*



    As a manager, I want to log in securely so that I can access and manage employee expense reports.
    As a manager, I want to view a list of all pending expenses so that I can review them efficiently.
    As a manager, I want to approve or deny submitted expenses so that I can manage reimbursements appropriately.
    As a manager, I want to add comments to expense decisions so that employees understand the reasoning behind approvals or denials.
    As a manager, I want to generate reports by employee, category, or date so that I can analyze spending trends and make informed decisions.


 */

public class ExpenseManager {


    static Logger logger = Logger.getLogger("MyLog");
    static FileHandler fh;

    public static void main(String[] args) {
//        String DB_PATH = "jdbc:sqlite:../ClientPython/database/expense.db";
        try{
            logger.setUseParentHandlers(false);
            fh = new FileHandler("my_application.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        }catch(Exception e){
            System.out.println("Logging issue, terminating");
            return;
        }




        try (Connection conn = ConnectionUtil.dbConnection()) {
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");

                Manager user = null;
                do{
                    user = LoginLoop.hi(conn);
                    if(user == null){
                        logger.log(Level.WARNING, "Login failure");
                        System.out.println("Login failed");
                    }

                }while(user == null);

                // Perform database ops loop here
                int result = 0;
                do{
                    result = UserLoop.inputLoop(user.getId());
                }while(result != -1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "SQL Failure");
            System.out.println(e.getMessage());
        }
    }
}
