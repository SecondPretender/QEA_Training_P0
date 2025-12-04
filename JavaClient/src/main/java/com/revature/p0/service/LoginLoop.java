package com.revature.p0.service;

import com.revature.p0.dao.ManagerDAO;
import com.revature.p0.dao.ManagerImp;
import com.revature.p0.model.Manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LoginLoop {

    /*

    Returns: userid of login or created user. -1 if not.

     */
    public static Manager hi(Connection conn){
        Scanner sc = new Scanner(System.in);
        String val = "";
        System.out.println("Enter L to Login or C to Create a new User");
        val = sc.next();
        Manager newm = null;

        if(val.equalsIgnoreCase("L")){
            System.out.print("Enter your username: ");
            String uname = sc.next();
            System.out.print("Enter your password: ");
            String pword = sc.next();

            ManagerDAO mDB = new ManagerImp();
            newm = mDB.login(uname, pword);
        }
        else if(val.equalsIgnoreCase("C")){
            System.out.print("Enter new username: ");
            String uname = sc.next();
            System.out.print("Enter new password: ");
            String pword = sc.next();
            System.out.print("Verify your password: ");
            if(!pword.equals(sc.next())){
                return null;
            }

            try {
                Statement statement = conn.createStatement();
                ManagerDAO mDB = new ManagerImp();
                mDB.insertData(new Manager(uname, pword));
                newm = mDB.login(uname, pword);
                //todo: call db create user, then login
            } catch (SQLException e) {
                return null;
            }catch (Exception e){
                System.out.println(e.getMessage());
                return null;
            }
        }
        else{
            System.out.println("Invalid Entry");
        }
        return newm;

    }
}
