import sqlite3
import sys
from sqlite3 import Connection
import logging

DB_NAME = 'database/expense.db'
DEFAULT_STATUS = 'pending'
DEFAULT_ROLE = "user"
LOGFILE = 'logdir/log.txt'
server ="127.0.0.1"
port = "3306"
user = "root"
password = "password"
database = "ExpenseManager"

def initialize():

    user_query = """
            CREATE TABLE IF NOT EXISTS User (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(255) NOT NULL
            );
            """
    expense_query ="""
            CREATE TABLE IF NOT EXISTS Expense (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                amount REAL NOT NULL,
                description VARCHAR(255) NOT NULL,
                date VARCHAR(255) NOT NULL,
                user_id INTEGER,
                FOREIGN KEY(user_id) REFERENCES User(id) ON DELETE CASCADE
            );
            """
    approvals_query ="""
        CREATE TABLE IF NOT EXISTS Approvals(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            status VARCHAR(255) NOT NULL,
            reviewer INT,
            comment VARCHAR(255),
            review_date VARCHAR(255),
            expense_id INTEGER,
            FOREIGN KEY(expense_id) REFERENCES Expense(id) ON DELETE CASCADE
        );
        """
    drop1 = """ DROP TABLE Expense"""
    drop2 = """ DROP TABLE Approvals"""
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
    log_query = "SELECT * FROM User WHERE UserNAME = '%s' AND PASSWORD = '%s'"%(username, password)
    cursor = conn.cursor()
    cursor.execute(log_query)
    result = cursor.fetchone()
    #result may return None
    #use hidden id to identify user after returning
    return result

#Users should be able to create an account
def new_usr(username: str, password: str, conn):
    add_query = "INSERT INTO User (username, password, role) VALUES ('%s','%s', '%s')"%(username, password, DEFAULT_ROLE)
    cursor = conn.cursor()
    cursor.execute(add_query)
    conn.commit()



#Employees can submit an expense validated by hidden id to add to the pending list
def submit_expense(conn: Connection, userid: int, amount: float, description: str, date: str):
    submit_query = ("INSERT INTO Expense (user_id, amount, description, date) "
                    "VALUES (%d, %f, '%s', '%s') RETURNING id") %(userid, amount, description, date)

    #need to get approvals to work by using expense id
    try:
        cursor = conn.cursor()
        cursor.execute(submit_query)
        eid = cursor.lastrowid
        approvals_query = (("INSERT INTO Approvals ( expense_id, status ) "
                            "SELECT Expense.id, '%s' FROM Expense WHERE Expense.id = %f")
                           % (DEFAULT_STATUS, eid))
        cursor.execute(approvals_query)
        conn.commit()
    except Exception as e:
        logger = logging.getLogger(__name__)
        logger.error("Data submission to sql db failed")

#Employees can submit a request validated by hidden id to see all expenses and their status
def view_submissions(conn: Connection, userid: int):
    cursor = conn.cursor()

    view_query = (
    "SELECT Expense.id, Expense.amount, Expense.description, Expense.date, Approvals.status"
    " FROM Expense JOIN Approvals ON Approvals.expense_id=EXPENSE.id "
    "WHERE Expense.user_id=%d" %(userid))
    # view_query = ("SELECT * FROM Approvals")
    cursor.execute(view_query)
    return cursor.fetchall()

def get_by_id(conn: Connection, userid: int, expenseid: int):
    cursor = conn.cursor()

    view_query = (
            "SELECT Expense.id, Expense.amount, Expense.description, Expense.date"
            " FROM Expense JOIN Approvals ON Approvals.expense_id=EXPENSE.id "
            "WHERE Expense.user_id=%d AND Expense.id=%d AND Approvals.status='%s'"
            % (userid, expenseid, DEFAULT_STATUS))
    # view_query = ("SELECT * FROM Approvals")
    cursor.execute(view_query)
    return cursor.fetchone()


def edit_submission(conn: Connection, userid: int, expenseid: int, column: str, data: str):
    #submit expense id to change, validate pending in approvals
    edit_query = (("UPDATE Expense SET %s = %s "
                   "FROM Expense AS t1 JOIN Approvals as t2 ON t2.expense_id = t1.id "
                  "WHERE t2.status = '%s' AND t1.user_id = %d AND t1.id = %d "
                   "RETURNING Expense.id")
                  %(column, data, DEFAULT_STATUS, userid, expenseid))
    cursor = conn.cursor()
    cursor.execute(edit_query)
    row = cursor.fetchall()
    if not row:
        logger = logging.getLogger(__name__)
        logger.warning("No update entry found")
        raise KeyError("Valid entry not found, update not passed")
    else:
        for i in row:
            print(i[0])
    conn.commit()



def delete_submission(conn: Connection, userid: int, expenseid: int):
    delete_query = (("DELETE FROM Expense WHERE Expense.id IN("
                     "SELECT t1.id FROM Expense "
                     "AS t1 LEFT JOIN Approvals as t2 ON (t2.expense_id = t1.id) "
                     "WHERE t2.status = '%s' AND t1.user_id = %d AND t1.id = %d )RETURNING *; ")
                    % (DEFAULT_STATUS, userid, expenseid))
    cursor = conn.cursor()
    cursor.execute(delete_query)
    row = cursor.fetchall()
    if not row:
        logger = logging.getLogger(__name__)
        logger.warning("No deletion entry found")
        raise KeyError("Valid entry not found, nothing deleted")
    conn.commit()
    return None

#Employees validated by hidden id can view all non pending expenses on request to track financial history
def view_non_pending(conn: Connection, userid: int):
    cursor = conn.cursor()

    view_query = (
    "SELECT * FROM Expense INNER JOIN Approvals ON Approvals.expense_id=EXPENSE.id "
    "WHERE Expense.user_id=%d AND NOT Approvals.status='%s'" %(userid, DEFAULT_STATUS))
    cursor.execute(view_query)
    return cursor.fetchall()




def get_connection():
    try:
        conn = sqlite3.connect(DB_NAME)
    except Exception as e:
        #todo: log error
        logger = logging.getLogger(__name__)
        logger.critical(f"SQLite connection failed: {e}")
        print("Something went wrong")
        sys.exit()
    return conn


if __name__ == "__main__":
    initialize()