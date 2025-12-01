package com.revature.p0.service;

import com.revature.p0.dao.ExpenseDAO;
import com.revature.p0.dao.ExpenseImp;
import com.revature.p0.model.Approval;
import com.revature.p0.model.Expense;
import tech.tablesaw.api.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserLoop {




    public static int inputLoop(int manid){
        Scanner sc = new Scanner(System.in);
        String val = "";
        //todo: allow manager to select for expenses by category
        System.out.println("Enter:\n" +
                "-V to View Pending Expenses\n-X to Approve and Deny Expenses" +
                "\n-G to generate reports on Expense\n-Q to quit");
        val = sc.next();

        if(val.equalsIgnoreCase("Q")){
            return -1;

        } else if (val.equalsIgnoreCase("V")) {
            //ENTER VIEW LOOP
            //TODO: use tablesaw to generate a table and print
            List<Expense> expList = ExpenseService.getPending();
            Table t = UserUtils.TableFromExpense(expList);
            System.out.println(t);
        }
        else if (val.equalsIgnoreCase("X")){
            List<Expense> expList = null;
            Approval tA = new Approval();
            //ENTER EDIT LOOP
            System.out.println("Enter Expense ID to be considered");
            //TODO: display
            try{
                int id = Integer.parseInt(sc.next());
                expList = ExpenseService.getOneExpense(id);
                if(expList.size() <= 0){
                    //todo: replace with custom exception
                    throw new IllegalArgumentException("ID not found");
                }
//                System.out.println(expList.get(0));
                System.out.println(UserUtils.TableFromExpense(expList));
                tA = ApprovalService.getOneApproval(expList.get(0).getId()).get(0);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                return 0;
            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
                return 0;
            }


            System.out.println("Enter A to Approve or D to Deny");
            val = sc.next();
            if(val.equalsIgnoreCase("A")){
                tA.setStatus("Approved");
            }
            else if(val.equalsIgnoreCase("D")){
                tA.setStatus("Denied");
            }
            else{
                System.out.println("Invalid case");
                return 0;
            }
            System.out.println("Add a category for the expense:");
            val = sc.next();
            if (val.equalsIgnoreCase("")) {
                System.out.println("Cannot allow no category");
                return 0;
            }
            tA.setCategory(val);
            System.out.println("Add a comment on your decision(optional):");
            val = sc.nextLine();
            val = sc.next();
            if(val.isBlank()){
                val = null;
            }
            tA.setComment(val);
            //TODO: add date and thingy
            tA.setReviewer(manid);
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
            tA.setReviewDate(now.format(formatter));
            ApprovalService.submitApproval(tA);
            //

        }
        else if (val.equalsIgnoreCase("G")){
            //ENTER REPORT LOOP
            //vall retrieve all expenses
            //TODO: use tablesaw to generate reports

            //

        }
        else{
            return -1;
        }
        return 1;

    }
}
