package com.revature.p0.service;

import com.revature.p0.dao.ExpenseDAO;
import com.revature.p0.dao.ExpenseImp;
import com.revature.p0.model.Expense;

import java.util.List;

public class ExpenseService {
    public static List<Expense> getExpenses(){
        ExpenseDAO ED = new ExpenseImp();
        return ED.retrieveAll();
    }

    public static List<Expense> getPending(){
        ExpenseDAO ED = new ExpenseImp();
        return ED.retrievePending();
    }

    public static List<Expense> getOneExpense(int id){
        ExpenseDAO ED = new ExpenseImp();
        return ED.retrieveValue(id);
    }

    public static List<Expense> getByCategory(String cat){
        ExpenseDAO ED = new ExpenseImp();
        return ED.getCategory(cat);
    }
}
