package com.revature.p0.dao;

import com.revature.p0.model.Manager;

import java.util.List;

public interface ManagerDAO {

    public Manager login(String username, String password);

    public void insertData(Manager m);

    public void editData(int id, Manager m);







}
