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
-- AccountBridge
-- GET ACCOUNT BRIDGE BY CUSTOMER
SELECT ab.* FROM AccountBridge ab
JOIN Account a ON a.accountID = ab.accountId
JOIN Customer c ON c.customerID = a.customerID 
WHERE c.customerID = 1;

-- ------------------------------------------------------------------------------------------------------
-- AccountTransaction
-- GET ACCOUNT TRANSACTIONS BY CUSTOMER
SELECT at.* FROM AccountTransaction at
JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2
JOIN Customer c ON c.customerID = a.customerID
WHERE c.customerID = 1;
 
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
SELECT b.* FROM Bank b
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

-- GET COMPANY BY STOCK 
SELECT * FROM Company where companyID = 1;

SELECT  c.* FROM Company c 
JOIN Stock s on s.companyID = c.companyID 
WHERE s.stockID = 2;
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
SELECT c.* FROM Customer c
JOIN Account a ON a.customerID = c.customerID
JOIN AccountBridge ba ON ba.accountID = a.accountID
JOIN BankAccountType bat ON bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID
JOIN AccountType at ON at.accountTypeID = bat.accountTypeID
WHERE at.accountTypeID = 2;

-- GET CUSTOMER BY ACCOUNT 
SELECT c.* FROM Customer c 
JOIN Account a ON a.customerID = c.customerID 
WHERE a.accountID = 4;

-- GET CUSTOMER BY PORTFOLIO 
SELECT c.* FROM Customer c 
JOIN Portfolio p ON p.customerID = c.customerID 
WHERE p.portfolioID = 2;

-- GET CUSTOMER BY STOCK 
SELECT c.* FROM Customer c
JOIN Portfolio p ON p.customerID = c.customerID 
JOIN PortfolioStock ps ON ps.portfolioID = p.portfolioID
JOIN Stock s ON s.stockID = ps.stockID 
WHERE s.stockID = 1;

-- ------------------------------------------------------------------------------------------------------
-- Exchange Organization
-- GET EXCHANGE ORGANIZATIONS BY STOCK
SELECT eo.* FROM ExchangeOrganization eo
JOIN StockExchangeOrganization seo ON seo.exchangeOrganizationID = eo.exchangeOrganizationID
JOIN Stock s ON s.stockID = seo.stockID
WHERE s.stockID = 1;

-- GET EXCHANGE ORGANIZATION BY STOCK TRANSACTION
SELECT eo.* FROM ExchangeOrganization eo 
JOIN PortfolioBridge pb ON pb.exchangeOrganizationID = eo.exchangeOrganizationID 
JOIN StockTransaction st ON st.stockTransactionID = pb.stockTransactionID
WHERE st.stockTransactionID = 1;
-- ------------------------------------------------------------------------------------------------------
-- Portfolio
-- GET PORTFOLIO BY CUSTOMER
SELECT * FROM Portfolio WHERE customerID = 2;

-- GET PORTFOLIO BY PORTFOLIOSTOCK
SELECT p.* FROM Portfolio p 
JOIN PortfolioStock ps ON ps.portfolioID = p.portfolioID 
WHERE ps.portfolioStockID = 1;

-- GET PORTFOLIO BY STOCK TRANSACTION
SELECT p.* FROM Portfolio p 
JOIN PortfolioBridge pb ON pb.portfolioID = p.portfolioID 
JOIN StockTransaction st ON st.stockTransactionID = pb.stockTransactionID
WHERE st.stockTransactionID = 1;
-- ------------------------------------------------------------------------------------------------------
-- Portfolio Bridge
-- GET PORTFOLIO BRIDGE BY COMPANY
SELECT pb.* FROM PortfolioBridge pb
JOIN StockExchangeOrganization seo ON seo.stockID = pb.stockID AND seo.exchangeOrganizationID = pb.exchangeOrganizationID
JOIN Stock s ON seo.stockID = s.stockID
JOIN Company c ON c.companyID = s.companyID 
WHERE c.companyID = 1;

-- DELETE pb FROM PortfolioBridge pb
-- JOIN StockExchangeOrganization seo ON seo.stockID = pb.stockID AND seo.exchangeOrganizationID = pb.exchangeOrganizationID
-- JOIN Stock s ON seo.stockID = s.stockID
-- JOIN Company c ON c.companyID = s.companyID 
-- WHERE c.companyID = 1;

-- GET PORTFOLIO BRIDGE BY CUSTOMER
SELECT pb.* FROM PortfolioBridge pb
JOIN Portfolio p ON p.portfolioID = pb.portfolioID
JOIN Customer c ON c.customerID = p.customerID
WHERE c.customerID = 1;

DELETE pb FROM PortfolioBridge pb
JOIN Portfolio p ON p.portfolioID = pb.portfolioID
JOIN Customer c ON c.customerID = p.customerID
WHERE c.customerID = 1;

-- ------------------------------------------------------------------------------------------------------
-- Portfolio Stock
-- GET PORTFOLIO STOCKS BY PORTFOLIO 
SELECT * FROM PortfolioStock WHERE portfolioID = 1;

-- GET PORTFOLIO STOCKS BY COMPANY
SELECT ps.* FROM PortfolioStock ps 
JOIN Stock s ON s.stockID = ps.stockID
JOIN Company c ON c.companyID = s.companyID 
WHERE c.companyID = 2;

-- DELETE PORTFOLIO STOCKS BY COMPANY
-- DELETE ps FROM PortfolioStock ps 
-- JOIN Stock s ON s.stockID = ps.stockID
-- JOIN Company c ON c.companyID = s.companyID 
-- WHERE c.companyID = 2;

-- GET PORTFOLIO STOCKS BY CUSTOMER 
SELECT ps.* FROM PortfolioStock ps
JOIN Portfolio p ON p.portfolioID = ps.portfolioID 
JOIN Customer c ON c.customerID = p.customerID
WHERE c.customerID = 2;

-- DELETE ps FROM PortfolioStock ps
-- JOIN Portfolio p ON p.portfolioID = ps.portfolioID 
-- JOIN Customer c ON c.customerID = p.customerID
-- WHERE c.customerID = 2;

SELECT * FROM FinancialManagementSystem.PortfolioBridge;
-- GET PORTFOLIO STOCK BY PORTFOLIO STOCK AND EO
SELECT ps.* FROM PortfolioStock ps 
JOIN Portfolio p ON p.portfolioID = ps.portfolioID
JOIN PortfolioBridge pb ON pb.portfolioID = p.portfolioID 
WHERE pb.stockID = 2 AND pb.exchangeOrganizationID = 2 AND pb.portfolioID = 2;


-- ------------------------------------------------------------------------------------------------------
-- Stock
-- GET STOCK BY COMPANY
SELECT * FROM Stock WHERE companyID = 2;

-- GET STOCKS BY EXCHANGE ORGANIZATION 
SELECT s.* FROM Stock s
JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID
JOIN ExchangeOrganization eo ON eo.exchangeOrganizationID = seo.exchangeOrganizationID
WHERE eo.exchangeOrganizationID = 1;

-- GET STOCKS BY PORTFOLIO
SELECT DISTINCT s.* FROM Stock s 
JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID 
JOIN PortfolioBridge pb ON pb.stockID = seo.stockID 
JOIN Portfolio p ON p.portfolioID = pb.portfolioID 
WHERE p.portfolioID = 1;

-- GET STOCK BY PORTFOLIO STOCK 
SELECT s.* FROM Stock s
JOIN PortfolioStock ps ON ps.stockID = s.stockID 
WHERE ps.portfolioStockID = 2;

-- GET STOCK BY STOCK TRANSACTION
SELECT s.* FROM Stock s
JOIN PortfolioBridge pb ON pb.stockID = s.stockID
JOIN StockTransaction st ON st.stockTransactionID = pb.stockTransactionID
WHERE st.stockTransactionID = 1;

-- ------------------------------------------------------------------------------------------------------
-- StockTransaction
-- GET STOCK TRANSACTIONS BY PORTFOLIO
SELECT st.*, pb.stockID AS Stock, pb.exchangeOrganizationID AS EO 
FROM StockTransaction st 
JOIN PortfolioBridge pb ON pb.stockTransactionID = st.stockTransactionID 
JOIN Portfolio p ON p.portfolioID = pb.portfolioID
WHERE p.portfolioID = 1;

-- GET STOCK TRANSACTIONS BY PORTFOLIOSTOCK 
SELECT st.* FROM stocktransaction st 
JOIN PortfolioBridge pb ON st.stockTransactionID = pb.stockTransactionID
JOIN PortfolioStock ps ON pb.portfolioID = ps.portfolioID AND pb.stockID = ps.stockID
WHERE ps.portfoliostockID = 1 ;   

-- GET STOCK TRANSACTIONS BY STOCK 
SELECT st.* FROM stocktransaction st 
JOIN PortfolioBridge pb ON st.stockTransactionID = pb.stockTransactionID
JOIN Stock s ON pb.StockID = s.StockID
WHERE s.stockID = 1 ;

-- GET STOCK TRANSACTIONS BY DESC DATETIME 
SELECT * FROM StockTransaction ORDER BY dateTime DESC;
-- ------------------------------------------------------------------------------------------------------
-- Transaction
-- GET TRANSACTIONS BY ACCOUNT 
SELECT t.* FROM Transaction t
JOIN AccountTransaction at ON at.transactionID = t.transactionID 
JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2
WHERE a.accountID = 1;


