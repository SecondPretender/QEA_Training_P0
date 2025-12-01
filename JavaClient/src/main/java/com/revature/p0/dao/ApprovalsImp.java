package com.revature.p0.dao;

import com.revature.p0.model.Approval;
import com.revature.p0.model.Expense;
import com.revature.p0.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApprovalsImp implements ApprovalsDAO{
    Connection conn = null;


    @Override
    public List<Approval> retrieveValue(int id) {
        conn = ConnectionUtil.dbConnection();
        String selAll = "Select * from Approvals where expense_id = ?";
        Approval app;
        List<Approval> appList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(selAll);
            preparedStatement.setInt(1, id);
            ResultSet resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){

                app = new Approval(
                        resultSet.getInt(1),resultSet.getString(2),
                        resultSet.getInt(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getInt(6),
                        resultSet.getString(7)
                );
                appList.add(app);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appList;
    }

    @Override
    public List<Approval> retrieveAll() {
        conn = ConnectionUtil.dbConnection();
        String selAll = "Select * from Approvals";
        Approval app;
        List<Approval> appList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=conn.prepareStatement(selAll);
            ResultSet resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                //id tatu reviewer comment reviewdate expenseid category
                app = new Approval(
                        resultSet.getInt(1),resultSet.getString(2),
                        resultSet.getInt(3), resultSet.getString(4),
                        resultSet.getString(5), resultSet.getInt(6),
                        resultSet.getString(7)
                );
                appList.add(app);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appList;
    }

    @Override
    public void insertData(Approval a) {
        conn = ConnectionUtil.dbConnection();
        //todo: only where value is not pending
        String insertApp = "Update Approvals SET status = ?, reviewer = ?, category = ?, " +
                "review_date = ?, comment = ? Where id = ? and status like 'pending'";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertApp);
            preparedStatement.setString(1, a.getStatus());
            preparedStatement.setInt(2, a.getReviewer());
            preparedStatement.setString(3, a.getCategory());
            preparedStatement.setString(4, a.getReviewDate());
            preparedStatement.setString(5, a.getComment());
            preparedStatement.setInt(6, a.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editData(int id, Approval e) {

    }
}
