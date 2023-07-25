USE FinancialManagementSystem;
-- DAO QUERIES V2

-- ------------------------------------------------------------------------------------------------------
-- Account 
-- GET ACCOUNTS BY BANK ID 
SELECT
	a.* 
FROM Account a
JOIN AccountBridge ba ON ba.accountID = a.accountID
JOIN BankAccountType bat on bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 1;

-- GET ACCOUNTS BY ACCOUNT TYPE 
SELECT 
	a.* 
FROM Account a
JOIN AccountBridge ba ON ba.accountID = a.accountID
JOIN BankAccountType bat ON bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID
JOIN AccountType at ON at.accountTypeID = bat.accountTypeID
WHERE at.accountTypeID = 5;

-- GET ACCOUNTS BY TRANSACTION: 
SELECT 
	a.* 
FROM Account a 
JOIN AccountTransaction at ON at.accountID1 = a.accountID 
JOIN Transaction t ON t.transactionID = at.transactionID
WHERE t.transactionID = 3;

-- UPDATE ACCOUNT TO CLOSE 
UPDATE Account SET depositBalance = 0.00, interestBalance = 0.00, totalBalance = 0.00, status = 'CLOSED', closingDate = '2023-01-01', closingReason = 'Switched to another bank'
WHERE accountID = 5;

-- GET ACCOUNTS BY CUSTOMER
SELECT a.* FROM Account a 
JOIN Customer c ON c.customerID = a.customerID 
WHERE c.customerID = 1 ;

-- GET OPEN ACCOUNTS BY CUSTOMER
SELECT a.* FROM Account a 
JOIN Customer c ON c.customerID = a.customerID 
WHERE a.status = 'OPEN' AND c.customerID = 1;

-- ------------------------------------------------------------------------------------------------------
-- AccountType 
-- GET ACCOUNT TYPES BY BANK 
SELECT at.* FROM AccountType at 
JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID 
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 1;

-- GET ACCOUNT TYPES BY CUSTOMER 
SELECT at.* FROM AccountType at 
JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID 
JOIN AccountBridge ba ON ba.accountTypeID = bat.accountTypeID AND ba.bankID = bat.bankID
JOIN Account a ON a.accountID = ba.accountID
JOIN Customer c ON c.customerID = a.customerID
WHERE c.customerID = 1;

-- GET ACCOUNT TYPE BY ACCOUNT
SELECT at.* FROM AccountType at 
JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID 
JOIN AccountBridge ba ON ba.accountTypeID = bat.accountTypeID AND ba.bankID = bat.bankID
JOIN Account a ON a.accountID = ba.accountID
WHERE a.accountID = 3;

-- ------------------------------------------------------------------------------------------------------
-- Bank
-- GET BANKS BY CUSTOMER
SELECT b.* FROM Bank b 
JOIN BankAccountType bat ON bat.bankID = b.bankID 
JOIN AccountBridge ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID
JOIN Account a ON a.accountID = ba.accountID 
JOIN Customer c ON c.customerID = a.customerID 
WHERE c.customerID = 1;

-- GET BANK BY ACCOUNT 
SELECT
	b.*
FROM Bank b
JOIN BankAccountType bat ON bat.bankID = b.bankID 
JOIN AccountBridge ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID
JOIN Account a ON a.accountID = ba.accountID
WHERE a.accountID = 1;
-- ------------------------------------------------------------------------------------------------------
-- Company 
-- GET PUBLIC COMPANIES
SELECT * FROM Company WHERE status='PUBLIC';

-- GET PRIVATE COMPANIES
SELECT * FROM Company WHERE status='PRIVATE';
-- ------------------------------------------------------------------------------------------------------
-- Customer 
-- GET CUSTOMERS BY BANK 
SELECT 
	c.*
FROM Customer c
JOIN Account a ON a.customerID = c.customerID
JOIN AccountBridge ba ON ba.accountID = a.accountID
JOIN BankAccountType bat ON bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID
JOIN Bank b ON b.bankID = bat.bankID
WHERE b.bankID = 4;

-- GET CUSTOMERS BY ACCOUNT TYPE
SELECT 
	c.*
FROM Customer c
JOIN Account a ON a.customerID = c.customerID
JOIN AccountBridge ba ON ba.accountID = a.accountID
JOIN BankAccountType bat ON bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID
JOIN AccountType at ON at.accountTypeID = bat.accountTypeID
WHERE at.accountTypeID = 2;

-- GET CUSTOMER BY ACCOUNT 
SELECT c.* FROM Customer c 
JOIN Account a ON a.customerID = c.customerID 
WHERE a.accountID = 4;
-- ------------------------------------------------------------------------------------------------------
-- Exchange Organization
-- GET EXCHANGE ORGANIZATIONS BY STOCK
SELECT eo.* FROM ExchangeOrganization eo
JOIN StockExchangeOrganization seo ON seo.exchangeOrganizationID = eo.exchangeOrganizationID
JOIN Stock s ON s.stockID = seo.stockID
WHERE s.stockID = 1;

-- ------------------------------------------------------------------------------------------------------
-- Portfolio
-- GET PORTFOLIO BY CUSTOMER
SELECT * FROM Portfolio WHERE customerID = 2;

-- ------------------------------------------------------------------------------------------------------
-- Stock
-- GET STOCK BY COMPANY
SELECT * FROM Stock WHERE companyID = 1;

-- GET STOCKS BY EXCHANGE ORGANIZATION 
SELECT s.* FROM Stock s
JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID
JOIN ExchangeOrganization eo ON eo.exchangeOrganizationID = seo.exchangeOrganizationID
WHERE eo.exchangeOrganizationID = 1;
-- ------------------------------------------------------------------------------------------------------
-- Transaction
-- GET TRANSACTIONS BY ACCOUNT 
SELECT t.* FROM Transaction t
JOIN AccountTransaction at ON at.transactionID = t.transactionID 
JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2
WHERE a.accountID = 1;


