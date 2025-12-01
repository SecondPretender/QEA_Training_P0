package com.revature.p0.model;

public class Approval {
    /*

            id INTEGER PRIMARY KEY AUTOINCREMENT,
            status VARCHAR(255) NOT NULL,
            reviewer INT,
            comment VARCHAR(255),
            review_date VARCHAR(255),
            expense_id INTEGER,
            FOREIGN KEY(expense_id) REFERENCES EXPENSE(id) ON DELETE CASCADE

     */
    private int id;
    private String status;
    private int reviewer;
    private String comment;
    private String reviewDate;
    private int expenseId;
    private String category;
    public Approval(){

    }
    public Approval(int id, String status, int expenseId){
        this.id = id;
        this.status = status;
        this.expenseId = expenseId;
    }
    public Approval(int id, String status, int reviewer, String comment, String reviewDate, int expenseId, String category) {
        this.id = id;
        this.status = status;
        this.reviewer = reviewer;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.expenseId = expenseId;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReviewer() {
        return reviewer;
    }

    public void setReviewer(int reviewer) {
        this.reviewer = reviewer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
