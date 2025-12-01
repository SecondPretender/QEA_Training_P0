from datetime import datetime
import logging
from prettytable import PrettyTable
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
    from prettytable import PrettyTable
    t = PrettyTable(['ID', 'Amount', 'Description', 'Date', 'Status'])
    #print header
    for i in tr_ls:
        t.add_row(i)
    print(t)
def view_transaction(tr_ls):
    #do in one line
    t = PrettyTable(['ID', 'Amount', 'Description', 'Date', 'Status'])

    if len(tr_ls) < 5:
        tr_ls = tr_ls + (None,)
    t.add_row(tr_ls)
    print(t)
def to_float(input):
    try:
        ret = float(input)
    except Exception as e:
        logger = logging.getLogger(__name__)
        logger.warning("Invalid floating point cast caught")
        print("Float cast failed")
        return None
    return ret

def validate_amount(input):
    ret = to_float(input)
    if not ret == None:
        if ret < 0:
            logger = logging.getLogger(__name__)
            logger.warning("Invalid amount entered, below 0")
            print("Invalid amount: below 0")
            return None
        if ret > 1000:
            logger = logging.getLogger(__name__)
            logger.warning("Invalid amount entered, above 1000")
            print("Invalid amount: above 1000")
            return None
    return ret
