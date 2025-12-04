package com.revature.p0.service;

import com.revature.p0.model.Expense;
import tech.tablesaw.api.*;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class UserUtils {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");

    public static Table TableFromExpense(List<Expense> e){
        Table t = Table.create("Expenses");
        t.addColumns(IntColumn.create("id"), DoubleColumn.create("amount"),
                StringColumn.create("description"), StringColumn.create("Date"),
                StringColumn.create("employee"));
        for(Expense i: e){
            Row newRow = t.appendRow();
            newRow.setInt("id", i.getId());
            newRow.setDouble("amount", i.getAmount());
            newRow.setString("description", i.getDescription());
            newRow.setString("date", i.getDate());
            newRow.setString("employee", i.getName());

        }
        return t;
    }

    public static boolean DateValid(String s){
        try {
            formatter.parse(s);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
    public static String returnNow(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
        return now.format(formatter);
    }


    public static double AvgExpense(List<Expense> expList){

        double avg = 0.0;
        int count = 0;

        for(Expense exp:expList){
            avg += exp.getAmount();
            count += 1;
        }
        if(count <= 0){
            return 0;
        }
        avg = avg/count;

        return avg;

    }

//    public static Table TableFromApproval(List<Approval> a){
//        Table t = Table.create("Expenses");
//        t.addColumns(IntColumn.create("id"), DoubleColumn.create("amount"),
//                StringColumn.create("description"), StringColumn.create("Date"),
//                StringColumn.create("name"));
//        for(Approval i: a){
//            Row newRow = new Row(t);
//            newRow.setInt(0, i.getId());
//            newRow.setDouble(1, i.getAmount());
//            newRow.setString(2, i.getDescription());
//            newRow.setString(3, i.getDate());
//        }
//        return t;
//    }
}
