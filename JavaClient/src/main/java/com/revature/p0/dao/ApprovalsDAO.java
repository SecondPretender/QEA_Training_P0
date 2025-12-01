package com.revature.p0.dao;

import com.revature.p0.model.Approval;

import java.util.List;

public interface ApprovalsDAO {
    public List<Approval> retrieveValue(int id);

    public List<Approval> retrieveAll();

    public void insertData(Approval e);

    public void editData(int id, Approval e);

}
