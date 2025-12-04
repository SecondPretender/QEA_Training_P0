package com.revature.p0.service;

import com.revature.p0.ExpenseManager;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UserLoop {


    private final static Logger LOGGER = Logger.getLogger(UserLoop.class.getName());


    public static int inputLoop(int manid){
        Scanner sc = new Scanner(System.in);
        String val = "";
        //todo: allow manager to select for expenses by category
        System.out.println("Enter:\n" +
                "-V to View Pending Expenses\n-X to Approve and Deny Expenses" +
                "\n-G to generate reports on Expense\n-Q to quit");
        val = sc.next();

        if(val.equalsIgnoreCase("Q")){
            LOGGER.log(Level.INFO, "Quitting");
            return -1;

        } else if (val.equalsIgnoreCase("V")) {
            //ENTER VIEW LOOP
            LOGGER.log(Level.INFO, "Viewing");
            //TODO: use tablesaw to generate a table and print
            List<Expense> expList = ExpenseService.getPending();
            Table t = UserUtils.TableFromExpense(expList);
            System.out.println(t);
        }
        else if (val.equalsIgnoreCase("X")){
            LOGGER.log(Level.INFO, "Approving");
            List<Expense> expList = null;
            Approval tA = new Approval();
            //ENTER EDIT LOOP
            System.out.println("Enter Expense ID to be considered");
            try{
                int id = Integer.parseInt(sc.next());
                expList = ExpenseService.getOneExpense(id);
                if(expList.size() <= 0){
                    //todo: replace with custom exception\
                    System.out.println("No Expense found");

                    return 0;

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
            tA.setReviewer(manid);
            tA.setReviewDate(UserUtils.returnNow());
            ApprovalService.submitApproval(tA);
            //

        }
        else if (val.equalsIgnoreCase("G")){
            //ENTER REPORT LOOP
            //call retrieve all expenses
            //allow the user to query to take modifications of th preloaded data
            //As a manager, I want to generate reports by employee,
            //category, or date so that I can analyze spending trends and make informed decisions.
            List<Expense> expList;
            LOGGER.log(Level.INFO, "Reporting");
            System.out.println("Preparing to Generate Report");


            System.out.println("Enter:\n-E to view by Employee\n-C to view by Category\n-D to view by Date");
            //Grab all columns, also report sum of expenses, max expene, and min expense
            val = sc.next();
            if(val.equalsIgnoreCase("E")){
                try{
                    expList = ExpenseService.getExpenses();
                }
                catch(Exception e){
                    System.out.println("Preparation failed, returning");
                    return 0;
                }
                //employee
                System.out.println("Enter Employee username to sort by");
                val = sc.next();
                String finalVal = val;
                List<Expense> filtered = expList.stream()
                        .filter(s -> s.getName().equals(finalVal))
                        .collect(Collectors.toList());

                System.out.println(UserUtils.TableFromExpense(filtered));
                System.out.println("Average: " + UserUtils.AvgExpense(filtered));


            }
            else if(val.equalsIgnoreCase("C")){
                System.out.println("Enter Category to sort by");
                val = sc.next();
                try{
                    expList = ExpenseService.getByCategory(val);
                }
                catch(Exception e){
                    System.out.println("Preparation failed, returning");
                    return 0;
                }
                //TODO: need this shit


                System.out.println(UserUtils.TableFromExpense(expList));
                System.out.println("Average: " + UserUtils.AvgExpense(expList));

            }
            else if(val.equalsIgnoreCase("D")){
                try{
                    expList = ExpenseService.getExpenses();
                }
                catch(Exception e){
                    System.out.println("Preparation failed, returning");
                    return 0;
                }
                System.out.println("Enter Date to sort by");
                val = sc.next();
                if(!UserUtils.DateValid(val)){
                    System.out.println("Invalid date");
                    return 0;
                }
                String finalVal = val;
                List<Expense> filtered = expList.stream()
                        .filter(s -> s.getDate().equals(finalVal))
                        .collect(Collectors.toList());
                System.out.println(UserUtils.TableFromExpense(filtered));
                System.out.println("Average: " + UserUtils.AvgExpense(filtered));
            }
            else{
                return 0;
            }

        }
        else{
            return -1;
        }
        return 1;

    }
}
