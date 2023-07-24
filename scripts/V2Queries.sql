USE FinancialManagementSystem;
-- WORKING QUERIES V2

-- GET ACCOUNTS BY BANK ID 
SELECT
	a.* 
FROM Account a
JOIN BankAccount ba ON ba.accountID = a.accountID
JOIN BankAccountType bat on bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 1;

-- GET BANKS BY CUSTOMER
SELECT b.* FROM Bank b 
JOIN BankAccountType bat ON bat.bankID = b.bankID 
JOIN BankAccount ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID
JOIN Account a ON a.accountID = ba.accountID 
JOIN Customer c ON c.customerID = a.customerID 
WHERE c.customerID = 1;

-- GET ACCOUNT TYPES BY BANK 
SELECT at.* FROM AccountType at 
JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID 
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 1;

-- GET BANK BY BANK ACCOUNT 
SELECT b.* FROM Bank b 
