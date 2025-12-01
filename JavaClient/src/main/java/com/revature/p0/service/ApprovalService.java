package com.revature.p0.service;

import com.revature.p0.dao.ApprovalsDAO;
import com.revature.p0.dao.ApprovalsImp;
import com.revature.p0.dao.ExpenseDAO;
import com.revature.p0.dao.ExpenseImp;
import com.revature.p0.model.Approval;
import com.revature.p0.model.Expense;

import java.util.List;

public class ApprovalService {
    public static List<Approval> getApprovals(){
        ApprovalsDAO aD = new ApprovalsImp();
        return aD.retrieveAll();
    }

    public static List<Approval> getOneApproval(int id){
        ApprovalsDAO aD = new ApprovalsImp();
        return aD.retrieveValue(id);
    }

    public static void submitApproval(Approval a){
        ApprovalsDAO aD = new ApprovalsImp();
        aD.insertData(a);
    }
}
