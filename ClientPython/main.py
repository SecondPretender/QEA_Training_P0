import sqlite3
import sys
from datetime import datetime
import logging
import dbfunc
import format_util
import mysql.connector

"""


X    As an employee, I want to log in with my credentials so that I can securely access my expense reports.
X    As an employee, I want to submit a new expense with details about amount and description so that I can request reimbursement or track spending.
X    As an employee, I want to view the status of my submitted expenses so that I know whether they are pending, approved, or denied.
    As an employee, I want to edit or delete expenses that are still pending so that I can correct mistakes before they are reviewed.
    As an employee, I want to view a history of all my approved and denied expenses so that I can track my financial activity over time.


"""
# colSet = {"DATE", "AMOUNT", "DESCRIPTION"}
# dformat = "%Y:%m:%d"
# #Recieves a string and validates that it matches the date format set above
# def validate_date(date_string):
#     try:
#         datetime.strptime(date_string, dformat)
#         return True
#     except ValueError:
#         logger.warning("User entered invalid date")
#         return False
# def view_transactions(tr_ls: []):
#     for i in tr_ls:
#         view_transaction(i)
# def view_transaction(tr_ls):
#     i = tr_ls
#
#     ret_str = (f"ID: {i[0]}\nAmount: {i[1]}\nDescription: {i[2]}\n Date:{i[3]}\n")
#     if len(i) >= 4:
#         ret_str += f"Status:{i[4]}\n"
#     print(ret_str)
#
# def to_float(input):
#     try:
#         ret = float(input)
#     except Exception as e:
#         logger.warning("Invalid floating point cast caught")
#         print("Float cast failed")
#         return None
#     return ret



#TODO:
# update file structure to be less insane:
#   util file, split up db file, split up main text loop
# add data validations:
#   no negative values(or 0-1000), no future date, descriptions nonempty
# tabular output/table view

if __name__ == "__main__":
    # conn = sqlite3.connect(dbfunc.DB_NAME)
    conn = mysql.connector.connect(host=dbfunc.server, user=dbfunc.user,
                           password=dbfunc.password, database=dbfunc.database,
                           port=dbfunc.port)
    logger = logging.getLogger(__name__)
    logging.basicConfig(format='%(asctime)s - %(levelname)s - %(message)s',filename=dbfunc.LOGFILE, level=logging.INFO)


    print("Welcome to the Employee Expenses Portal")
    ui_inp = " "
    loginsuccess = False
    cuser = -1
    curr_id = -1
    while not loginsuccess:
        ui_inp = input("Enter L to Login or C to create a new account\n")
        if(ui_inp.upper() == "L"):
            username = input("Enter Username: ")
            pword = input("Enter Password: ")
            result = dbfunc.login(username, pword, conn)
            if result == None:
                logger.info(f"User failed login attempt with username {username}")
                print("Login failed")
            else:
                logger.info("Successful login")
                print("Login was successful")
                curr_id = result[0]
                print("Welcome", result[1])
                loginsuccess = True

        elif (ui_inp.upper() == "C"):
            username = input("Enter new Username: ")
            pword = input("Enter new Password: ")
            dbfunc.new_usr(username, pword, conn)
            logger.info(f"new account created: {username}")
        elif ui_inp.upper() == "Q":
            print("Thank you for using the Expense Portal")
            sys.exit()
        else:
            print("Invalid input, try again")
    while loginsuccess:

        ui_inp = input("Inputs:\n"
                       "--V to View your Expenses\n--E to enter a new Expense\n"
                       "--D to Delete a pending Expense\n--X to change a pending expense\n"
                       "--A to view approved and denied requests\n"
                       "--Q to quit\n")
        if ui_inp.upper() == "V":
            exp_view = dbfunc.view_submissions(conn, curr_id)
            logger.info("User ran the view method")
            format_util.view_transactions(exp_view)
        elif ui_inp.upper() == "E":

            date = input("Enter the date in YYYY:MM:DD format\n")
            if not (format_util.validate_date(date)):
                print("Invalid date")
                continue
            amount = input("Enter the amount of the expense\n")
            #todo: add error catch to cast
            amount = format_util.validate_amount(amount)
            if (amount == None):
                continue
            category = input("Enter the description\n")
            logger.info("User adding a new entry")
            dbfunc.submit_expense(conn, curr_id, amount, category, date)
        elif ui_inp.upper() == "D":
            del_id = int(input("Enter the id of the expense to be removed: "))
            try:
                dbfunc.delete_submission(conn, curr_id, del_id)
                logger.info("User ran the deletion method")
            except KeyError as e:
                print(e)
        elif ui_inp.upper() == "A":
            exp_view = dbfunc.view_non_pending(conn, curr_id)
            format_util.view_transactions(exp_view)
        elif ui_inp.upper() == "X":
            logger.info("User entered the editing function")
            try:
                target_id = int(input("Enter the target id to edit\n"))
            except ValueError:
                logger.debug("User entered an invalid id to edit")
                print("invalid input")
                continue
            result = dbfunc.get_by_id(conn, curr_id, target_id)
            if not result == None:
                format_util.view_transaction(result)
            else:
                print("HUH")
            column = input("Which column should be edited\n")
            if column.upper() not in format_util.colSet:
                print("Invalid column")
                continue
            changed = input("Enter New value\n")
            if column.upper() == "DATE":
                if not format_util.validate_date(changed):
                    print("Invalid date")
                    continue
            if column.upper() == "AMOUNT":
                changed = format_util.validate_amount(changed)
                if (changed == None):
                    continue
            else:
                changed = "'" + changed + "'"
            try:
                logger.info("User ran the edit method")
                dbfunc.edit_submission(conn, curr_id, target_id, column, changed)
            except KeyError as e:
                print(e)
        elif ui_inp.upper() == "Q":
            logger.info("User quit the program")
            sys.exit()



