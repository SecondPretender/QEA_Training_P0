package com.revature.p0.dao;
import com.revature.p0.model.Expense;
import com.revature.p0.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseImp implements ExpenseDAO{
    Connection conn = null;


    @Override
    public List<Expense> retrieveValue(int id) {
        conn = ConnectionUtil.dbConnection();
        String selAll = "Select e.id, e.amount, e.description, e.date, u.username from Expense as e join User as u ON e.user_id = u.id WHERE e.id = ?";
        Expense exp = null;
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(selAll);
            preparedStatement.setInt(1, id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                exp = new Expense(
                        resultSet.getInt(1),resultSet.getDouble(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Expense> expList = new ArrayList<>();
        expList.add(exp);
        return expList;
    }

    @Override
    public List<Expense> retrieveAll() {
        conn = ConnectionUtil.dbConnection();
        String selAll = "Select e.id, e.amount, e.description, e.date, u.username from Expense as e join User as u ON e.user_id = u.id";
        Expense exp;
//        selAll = "Select * from Expense";
        List<Expense> expList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(selAll);
            ResultSet resultSet=preparedStatement.executeQuery();
//            System.out.println(resultSet);
            while(resultSet.next()){
                //id amount description date userId
//                System.out.println(resultSet.getInt(1));
                exp = new Expense(
                        resultSet.getInt(1),resultSet.getDouble(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5));
                expList.add(exp);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return expList;
    }

    @Override
    public List<Expense> retrievePending() {
        conn = ConnectionUtil.dbConnection();
        String selAll = "Select e.id, e.amount, e.description, e.date, u.username " +
                "from Expense as e join User as u On e.user_id = u.id join Approvals as a " +
                "On a.expense_id = e.id Where a.status LIKE 'pending'";
        Expense exp;
        List<Expense> expList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(selAll);
            ResultSet resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                //id amount description date userId
                exp = new Expense(
                        resultSet.getInt(1),resultSet.getDouble(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getString(5));
                expList.add(exp);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return expList;
    }

    @Override
    public void insertData(Expense e) {
        conn = ConnectionUtil.dbConnection();

    }

    @Override
    public void editData(int id, Expense e) {
        conn = ConnectionUtil.dbConnection();

    }
}
