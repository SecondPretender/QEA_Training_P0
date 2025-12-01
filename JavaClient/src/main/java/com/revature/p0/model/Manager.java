package com.revature.p0.model;

public class Manager {
    /*

                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(255) NOT NULL

     */

    private int id;
    private String username;
    private String password;
    private final String role = "Manager";

    public Manager(String u, String p){
        this.username = u;
        this.password = p;
    }

    public Manager(int i, String u, String p){
        this.id = i;
        this.username = u;
        this.password = p;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
