DROP DATABASE IF EXISTS FinancialManagementSystem;
CREATE DATABASE FinancialManagementSystem;
USE FinancialManagementSystem;

CREATE TABLE Bank (
	bankID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    location VARCHAR(50) NOT NULL
);

CREATE TABLE Company (
	companyID INT AUTO_INCREMENT PRIMARY KEY, 
    name VARCHAR(50) NOT NULL,
    industry VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL -- public or private 
);

CREATE TABLE ExchangeOrganization (
	exchangeOrganizationID INT AUTO_INCREMENT PRIMARY KEY, 
    tickerCode VARCHAR(10) NOT NULL, 
    name VARCHAR(50) NOT NULL
);

CREATE TABLE Customer (
	customerID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL, 
    lastName VARCHAR(50) NOT NULL,
    phoneNumber CHAR(12) NOT NULL
);

CREATE TABLE Stock (
	stockID INT AUTO_INCREMENT PRIMARY KEY, 
    tickerCode VARCHAR(10) NOT NULL, 
    sharePrice DECIMAL(10,2) NOT NULL,
    numberOfOutstandingShares BIGINT NOT NULL,
    marketCap DECIMAL(18, 2) NOT NULL,
    dailyVolume INT, 
    companyID INT NOT NULL,
    FOREIGN KEY (companyID) REFERENCES Company(companyID)
);

CREATE TABLE AccountType (
	accountTypeID INT AUTO_INCREMENT PRIMARY KEY, 
    type VARCHAR(50) NOT NULL, 
	minimumStartDeposit DECIMAL(10,2) NOT NULL,
    interestRate DECIMAL(5,2),
    compoundRate VARCHAR(50)
);

CREATE TABLE Account (
	accountID INT AUTO_INCREMENT PRIMARY KEY, 
    openingDate DATE NOT NULL,
	depositBalance DECIMAL(10,2) NOT NULL,
    interestBalance DECIMAL(10,2),
    totalBalance DECIMAL(10,2),
    status VARCHAR(10) NOT NULL,
    closingDate DATE,
    closingReason VARCHAR(255),
    customerID INT NOT NULL, 
    FOREIGN KEY (customerID) REFERENCES Customer(customerID)
);


-- BRIDGE BETWEEN Bank AND AccountType
CREATE TABLE BankAccountType (
	bankID INT, 
    accountTypeID INT, 
    CONSTRAINT pk_BankAccountType
		PRIMARY KEY(bankID, accountTypeID),
	CONSTRAINT fk_pk_BankAccountType_Bank
		FOREIGN KEY (bankID)
        REFERENCES Bank(bankID),
	CONSTRAINT fk_pk_BankAccountType_AccountType
		FOREIGN KEY (accountTypeID)
        REFERENCES AccountType(accountTypeID)
);

-- BRIDGE BETWEEN Account AND BankAccountType
CREATE TABLE BankAccount (
	accountID INT, 
    bankID INT, 
    accountTypeID INT, 
    CONSTRAINT pk_BankAccount
		PRIMARY KEY(accountID, bankID, accountTypeID),
	CONSTRAINT fk_pk_BankAccount_Account
		FOREIGN KEY (accountID)
        REFERENCES Account(accountID),
	CONSTRAINT fk_pk_BankAccount_BankAccountType
		FOREIGN KEY (bankID, accountTypeID)
        REFERENCES BankAccountType(bankID, accountTypeID)
);


-- BRIDGE TABLE BETWEEN STOCK & EXCHANGE ORGANIZATION
CREATE TABLE StockExchangeOrganization ( 
	stockID INT, 
    exchangeOrganizationID INT, 
    CONSTRAINT pk_StockExchangeOrganization
		PRIMARY KEY (stockID, exchangeOrganizationID),
	CONSTRAINT fk_pk_StockExchangeOrganization_Stock
		FOREIGN KEY (stockID)
		REFERENCES Stock(stockID),
	CONSTRAINT fk_pk_StockExchangeOrganization_ExchangeOrganization
		FOREIGN KEY (exchangeOrganizationID)
        REFERENCES ExchangeOrganization(exchangeOrganizationID)
);

CREATE TABLE Transaction (
	transactionID INT AUTO_INCREMENT PRIMARY KEY,
	dateTime DATETIME NOT NULL, 
    transactionType VARCHAR(20) NOT NULL, -- deposit or withdrawal
    amount DECIMAL(10,2) NOT NULL
);

CREATE TABLE AccountTransaction (
    accountID1 INT NOT NULL,
    accountID2 INT NOT NULL,
    CONSTRAINT pk_AccountTransaction
        PRIMARY KEY (accountID1, accountID2),
    CONSTRAINT fk_AccountTransaction_Account1
        FOREIGN KEY (accountID1)
        REFERENCES Account(accountID),
    CONSTRAINT fk_AccountTransaction_Account2
        FOREIGN KEY (accountID2)
        REFERENCES Account(accountID)
);

-- BRIDGE BETWEEN ACCOUNT AND TRANSACTION
CREATE TABLE TransactionBridge (
    transactionID INT NOT NULL,
    accountID1 INT NOT NULL, 
    accountID2 INT NOT NULL, 
    CONSTRAINT pk_AccountTransaction
        PRIMARY KEY (transactionID, accountID1, accountID2),
    CONSTRAINT fk_pk_TransactionBridge_Transaction
        FOREIGN KEY (transactionID)
        REFERENCES Transaction(transactionID),
    CONSTRAINT fk_pk_TransactionBridge_AccountTransaction
        FOREIGN KEY (accountID1, accountID2)
        REFERENCES AccountTransaction(accountID1, accountID2)
);

CREATE TABLE Portfolio (
	portfolioID INT AUTO_INCREMENT PRIMARY KEY, 
    balance DECIMAL(10,2),
    customerID INT NOT NULL, 
    FOREIGN KEY (customerID) REFERENCES Customer(customerID)
);

-- BRIDGE TABLE BETWEEN PORTFOLIO AND STOCK 
CREATE TABLE StockPortfolio (
	portfolioID INT, 
    stockID INT,
	numberOfShares BIGINT NOT NULL,
	sharesValue DECIMAL(10,2) NOT NULL, 
    CONSTRAINT pk_StockPortfolio
		PRIMARY KEY (portfolioID, stockID),
	CONSTRAINT fk_pk_StockPortfolio_Portfolio
		FOREIGN KEY (portfolioID)
        REFERENCES Portfolio(portfolioID),
	CONSTRAINT fk_pk_StockPortfolio_Stock
		FOREIGN KEY (stockID)
        REFERENCES Stock(stockID)
);

CREATE TABLE StockPortfolioTransaction (
	stockPortfolioTransactionID INT AUTO_INCREMENT PRIMARY KEY, 
	dateTime DATETIME NOT NULL, 
    transactionType VARCHAR(20) NOT NULL, -- buy or sell
	numberOfShares INT NOT NULL,
    transactionCost DECIMAL(10,2) NOT NULL,
	portfolioID INT NOT NULL,
    stockID INT NOT NULL, 
	FOREIGN KEY (portfolioID, stockID) REFERENCES StockPortfolio (portfolioID, stockID)
);
