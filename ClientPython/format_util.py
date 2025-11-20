from datetime import datetime
import logging
import dbfunc

colSet = {"DATE", "AMOUNT", "DESCRIPTION"}
dformat = "%Y:%m:%d"
#Recieves a string and validates that it matches the date format set above
def validate_date(date_string):
    try:
        datetime.strptime(date_string, dformat)
        return True
    except ValueError:
        logger = logging.getLogger(__name__)
        logger.warning("User entered invalid date")
        return False
def view_transactions(tr_ls: []):
    for i in tr_ls:
        view_transaction(i)
def view_transaction(tr_ls):
    i = tr_ls

    ret_str = (f"ID: {i[0]}\nAmount: {i[1]}\nDescription: {i[2]}\n Date:{i[3]}\n")
    if len(i) >= 4:
        ret_str += f"Status:{i[4]}\n"
    print(ret_str)

def to_float(input):
    try:
        ret = float(input)
    except Exception as e:
        logger = logging.getLogger(__name__)
        logger.warning("Invalid floating point cast caught")
        print("Float cast failed")
        return None
    return ret
