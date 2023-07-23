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
    name VARCHAR(50) NOT NULL, 
    sharePrice DECIMAL(10,2) NOT NULL,
    numberOfShares INT NOT NULL,
    marketCap DECIMAL(10,2) NOT NULL,
    dailyVolume INT, 
    companyID INT NOT NULL,
    FOREIGN KEY (companyID) REFERENCES Company(companyID)
);

CREATE TABLE AccountType (
	accountTypeID INT AUTO_INCREMENT PRIMARY KEY, 
    type VARCHAR(50) NOT NULL, 
    interestRate DECIMAL(5,2),
    compoundRate VARCHAR(50),
    bankID INT NOT NULL, 
    FOREIGN KEY (bankID) REFERENCES Bank(bankID)
);

CREATE TABLE Account (
	accountID INT AUTO_INCREMENT PRIMARY KEY, 
    openingDate DATE NOT NULL,
	depositBalance DECIMAL(10,2) NOT NULL, 
    interestBalance DECIMAL(10,2),
    totalBalance DECIMAL(10,2),
    customerID INT NOT NULL, 
    accountTypeID INT NOT NULL, 
    FOREIGN KEY (customerID) REFERENCES Customer(customerID),
    FOREIGN KEY (accountTypeID) REFERENCES AccountType(accountTypeID)
);

CREATE TABLE AccountTransaction (
	accountTransactionID INT AUTO_INCREMENT PRIMARY KEY, 
    dateTime DATETIME NOT NULL, 
    transactionType VARCHAR(20) NOT NULL, -- deposit or withdrawal
    amount DECIMAL(10,2) NOT NULL, 
    accountID INT NOT NULL, 
    FOREIGN KEY (accountID) REFERENCES Account(accountID)
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

CREATE TABLE Portfolio (
	portfolioID INT AUTO_INCREMENT PRIMARY KEY, 
    balance DECIMAL(10,2),
    customerID INT NOT NULL, 
    FOREIGN KEY (customerID) REFERENCES Customer(customerID)
);

-- BRIDGE TABLE BETWEEN CUSTOMER AND STOCK 
CREATE TABLE StockPortfolio (
	portfolioID INT, 
    stockID INT,
	numberOfShares INT NOT NULL,
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
