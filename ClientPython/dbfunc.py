import sqlite3
import sys
from sqlite3 import Connection

DB_NAME = 'database/expense.db'
DEFAULT_STATUS = 'pending'
LOGFILE = 'logging/log.txt'



def initialize():

    user_query = """
            CREATE TABLE IF NOT EXISTS USER (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(255) NOT NULL
            );
            """
    expense_query ="""
            CREATE TABLE IF NOT EXISTS EXPENSE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount REAL NOT NULL,
                description VARCHAR(255) NOT NULL,
                date VARCHAR(255) NOT NULL,
                user_id INTEGER,
                FOREIGN KEY(user_id) REFERENCES USER(id) ON DELETE CASCADE
            );
            """
    approvals_query ="""
        CREATE TABLE IF NOT EXISTS APPROVALS(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            status VARCHAR(255) NOT NULL,
            reviewer INT,
            comment VARCHAR(255),
            review_date VARCHAR(255),
            expense_id INTEGER,
            FOREIGN KEY(expense_id) REFERENCES EXPENSE(id) ON DELETE CASCADE
        );
        """
    drop1 = """ DROP TABLE EXPENSE"""
    drop2 = """ DROP TABLE APPROVALS"""
    conn = get_connection()
    cursor = conn.cursor()
    # cursor.execute(drop1)
    # cursor.execute(drop2)
    cursor.execute(user_query)
    conn.commit()
    cursor.execute(expense_query)
    conn.commit()
    cursor.execute(approvals_query)
    conn.commit()
"""

    As an employee, I want to log in with my credentials so that I can securely access my expense reports.
    As an employee, I want to submit a new expense with details about amount and description so that I can request reimbursement or track spending.
    As an employee, I want to view the status of my submitted expenses so that I know whether they are pending, approved, or denied.
    As an employee, I want to edit or delete expenses that are still pending so that I can correct mistakes before they are reviewed.
    As an employee, I want to view a history of all my approved and denied expenses so that I can track my financial activity over time.

"""

#Employees can log in with credentials to securely access expense reports
def login(username: str, password: str, conn):
    log_query = "SELECT * FROM USER WHERE USERNAME = '%s' AND PASSWORD = '%s'"%(username, password)
    cursor = conn.cursor()
    cursor.execute(log_query)
    result = cursor.fetchone()
    #result may return None
    #use hidden id to identify user after returning
    return result

#Users should be able to create an account
def new_usr(username: str, password: str, conn):
    add_query = "INSERT INTO USER (username, password, role) VALUES ('%s','%s', '%s')"%(username, password, DEFAULT_STATUS)
    cursor = conn.cursor()
    cursor.execute(add_query)
    conn.commit()



#Employees can submit an expense validated by hidden id to add to the pending list
def submit_expense(conn: Connection, userid: int, amount: float, description: str, date: str):
    submit_query = ("INSERT INTO EXPENSE (user_id, amount, description, date) "
                    "VALUES (%d, %f, '%s', '%s') RETURNING id") %(userid, amount, description, date)

    #need to get approvals to work by using expense id

    cursor = conn.cursor()
    try:
        cursor.execute(submit_query)
        eid = cursor.lastrowid
        approvals_query = (("INSERT INTO APPROVALS ( expense_id, status ) "
                            "SELECT EXPENSE.id, '%s' FROM EXPENSE WHERE EXPENSE.id = %f")
                           % (DEFAULT_STATUS, eid))
        cursor.execute(approvals_query)
        conn.commit()
    except Exception as e:
        #todo: log error
        print(e)

#Employees can submit a request validated by hidden id to see all expenses and their status
def view_submissions(conn: Connection, userid: int):
    cursor = conn.cursor()

    view_query = (
    "SELECT EXPENSE.id, EXPENSE.amount, EXPENSE.description, EXPENSE.date, APPROVALS.status"
    " FROM EXPENSE JOIN APPROVALS ON APPROVALS.expense_id=EXPENSE.id "
    "WHERE EXPENSE.user_id=%d" %(userid))
    # view_query = ("SELECT * FROM APPROVALS")
    cursor.execute(view_query)
    return cursor.fetchall()

def get_by_id(conn: Connection, userid: int, expenseid: int):
    cursor = conn.cursor()

    view_query = (
            "SELECT EXPENSE.id, EXPENSE.amount, EXPENSE.description, EXPENSE.date"
            " FROM EXPENSE JOIN APPROVALS ON APPROVALS.expense_id=EXPENSE.id "
            "WHERE EXPENSE.user_id=%d AND EXPENSE.id=%d AND NOT APPROVALS.status='%s'"
            % (userid, expenseid, DEFAULT_STATUS))
    # view_query = ("SELECT * FROM APPROVALS")
    cursor.execute(view_query)
    return cursor.fetchone()


#Employees validated by hidden id can edit or delete pending expenses
#todo: and data passed in as string needs its own single quotes
def edit_submission(conn: Connection, userid: int, expenseid: int, column: str, data: str):
    #submit expense id to change, validate pending in approvals
    edit_query = (("UPDATE EXPENSE SET %s = %s "
                   "FROM EXPENSE AS t1 INNER JOIN APPROVALS as t2 "
                  "WHERE t2.status = '%s' AND t1.user_id = %d AND t1.id = %d")
                  %(column, data, DEFAULT_STATUS, userid, expenseid))
    cursor = conn.cursor()
    cursor.execute(edit_query)
    conn.commit()



def delete_submission(conn: Connection, userid: int, expenseid: int):
    #submit expense id to change, validate pending in approvals
    delete_query = ("DELETE FROM EXPENSE WHERE id=%d AND user_id=%d"%(expenseid, userid))
    cursor = conn.cursor()
    cursor.execute(delete_query)
    conn.commit()
    return None

#Employees validated by hidden id can view all non pending expenses on request to track financial history
def view_non_pending(conn: Connection, userid: int):
    cursor = conn.cursor()

    view_query = (
    "SELECT * FROM EXPENSE INNER JOIN APPROVALS ON APPROVALS.expense_id=EXPENSE.id "
    "WHERE EXPENSE.user_id=%d AND NOT APPROVALS.status='%s'" %(userid, DEFAULT_STATUS))
    #todo: get expense ids based on user ids and feed them all into approvals
    # return both description from expense and status from approvals
    cursor.execute(view_query)
    return cursor.fetchall()




def get_connection():
    try:
        conn = sqlite3.connect(DB_NAME)
    except Exception:
        #todo: log error
        print("Something went wrong")
        sys.exit()
    return conn


if __name__ == "__main__":
    initialize()