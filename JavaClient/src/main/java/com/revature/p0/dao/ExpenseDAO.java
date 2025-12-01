package com.revature.p0.dao;

import com.revature.p0.model.Expense;
import com.revature.p0.model.Manager;

import java.util.List;

public interface ExpenseDAO {
    public List<Expense> retrieveValue(int id);

    public List<Expense> retrieveAll();
    public List<Expense> retrievePending();

    public void insertData(Expense e);

    public void editData(int id, Expense e);


}
