package com.revature.p0.dao;

import com.revature.p0.model.Expense;
import com.revature.p0.model.Manager;
import com.revature.p0.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/*

Manager need to login
Need to be able to create a new manager and add to db
If necessary, be able to update Manager: less important though
be able to delete manager



 */

public class ManagerImp implements ManagerDAO {
    //Login
    Connection conn = null;
    @Override
    public Manager login(String username, String password) {
        conn = ConnectionUtil.dbConnection();
        Manager mana = null;

        String query = "SELECT id, username, password FROM User WHERE username = ? AND " +
                "password = ? AND role = 'Manager'";
        try {
            PreparedStatement prep=conn.prepareStatement(query);
            prep.setString(1, username);
            prep.setString(2, password);
            ResultSet resultSet=prep.executeQuery();

            if(resultSet.next()){
                mana = new Manager(resultSet.getInt(1),
                        resultSet.getString(2), resultSet.getString(3));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mana;
    }

    //Create a new user
    @Override
    public void insertData(Manager m) {
        conn = ConnectionUtil.dbConnection();
        String query = "INSERT INTO User (username, password, role)  VALUES(?, ?, 'Manager')";

        try {
            PreparedStatement prep=conn.prepareStatement(query);
            prep.setString(1, m.getUsername());
            prep.setString(2, m.getPassword());
            prep.execute();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editData(int id, Manager m) {
        conn = ConnectionUtil.dbConnection();


        String query = "";
        try {
            PreparedStatement prep=conn.prepareStatement(query);
            //prep.setString(1, "egg");
            ResultSet resultSet=prep.executeQuery();

            while(resultSet.next()){
//                //id amount description date userId
//                Object o = new Object(
//                        resultSet.getInt(1),resultSet.getDouble(2),
//                        resultSet.getString(3), resultSet.getString(4),
//                        resultSet.getInt(5));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
