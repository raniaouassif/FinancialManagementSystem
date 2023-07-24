-- GET STOCKS FOR EXCHANGE ORGANIZATION
SELECT 
	s.* 
FROM Stock s 
JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID 
JOIN ExchangeOrganization eo ON eo.exchangeOrganizationID = seo.exchangeOrganizationID 
WHERE eo.exchangeOrganizationID = 1;

-- GET COMPANY BY STOCK 
SELECT c.* FROM Company c
JOIN Stock s ON s.companyID = c.companyID 
WHERE s.stockID = 2;

-- GET ACCOUNTS BY CUSTOMER 
SELECT * FROM Account where customerID = 2;

-- GET CUSTOMER BY ACCOUNT
SELECT 
	c.* 
FROM Customer c 
JOIN Account a ON a.customerID = c.customerID 
WHERE a.accountID = 4;

-- GET ACCOUNTS BY ACCOUNT TYPE 
SELECT * FROM Account WHERE accountTypeID = 1;


-- GET ACCOUNT TYPE BY ACCOUNT
SELECT 
	at.* 
FROM AccountType at 
JOIN Account a ON a.accountTypeID = at.accountTypeID 
WHERE a.accountID = 2;

-- GET ACCOUNT TYPES BY BANK : 
SELECT 
	at.* 
FROM AccountType at 
JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID 
JOIN Bank b ON b.bankID = bat.bankID 
WHERE b.bankID = 10;


-- GET ALL TRANSACTIONS FROM A BANK ID
SELECT 
	at.* , b.bankID
FROM AccountTransaction at
JOIN Account a ON a.accountID = at.accountID 
JOIN BankAccountType bat ON bat.accountID = a.accountID
JOIN Bank b ON b.bankID = bat.bankID 
WHERE b.bankID = 6;

-- DELETE ALL TRANSACTION FROM A BANK ID 
DELETE at
FROM AccountTransaction at
JOIN Account a ON a.accountID = at.accountID
JOIN BankAccountType bat ON bat.accountID = a.accountID
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 6;

-- GET ALL ACCOUNTS FROM A BANK ID 
SELECT 
	a.*, b.bankID
FROM Account a
JOIN BankAccountType bat ON bat.accountID = a.accountID
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 1;

-- GET ALL ACCOUNTTYPES BY BANK ID
SELECT DISTINCT at.* FROM AccountType at
JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 1;

-- DELETE ALL BankAccountType FROM A BANK ID 
DELETE FROM BankAccountType WHERE bankID = 6;

-- DELETE ALL ACCOUNTS FROM A BANK ID
DELETE a
FROM Account a
JOIN BankAccountType bat ON bat.accountID = a.accountID
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 6;