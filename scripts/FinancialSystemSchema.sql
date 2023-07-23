DROP DATABASE IF EXISTS FinancialManagementSystem;
CREATE DATABASE FinancialManagementSystem;
USE FinancialManagementSystem;

CREATE TABLE Bank (
	bankID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
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
    dailyVolume INT NOT NULL, 
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
    amount DECIMAL(10,2) NOT NULL, 
    accountID INT NOT NULL, 
    FOREIGN KEY (accountID) REFERENCES Account(accountID)
);

-- Create the HeroOrganization table
CREATE TABLE HeroOrganization (
    heroID INT,
    organizationID INT,
    CONSTRAINT pk_HeroOrganization
    	PRIMARY KEY (heroID, organizationID),
    CONSTRAINT fk_pk_HeroOrganization_Hero
    	FOREIGN KEY (heroID)
    	REFERENCES Hero(heroID),
    CONSTRAINT fk_pk_HeroOrganization_Organization
    	FOREIGN KEY (organizationID)
    	REFERENCES Organization(organizationID)
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

-- BRIDGE TABLE BETWEEN CUSTOMER AND STOCK 
CREATE TABLE CustomerStock (
	customerID INT, 
    stockID INT,
    CONSTRAINT pk_CustomerStock
		PRIMARY KEY (customerID, stockID),
	CONSTRAINT fk_pk_CustomerStock_Customer
		FOREIGN KEY (customerID)
        REFERENCES Customer(customerID),
	CONSTRAINT fk_pk_CustomerStock_Stock
		FOREIGN KEY (stockID)
        REFERENCES Stock(stockID)
);

CREATE TABLE CustomerStockTransaction (
	customerStockTransactionID INT AUTO_INCREMENT PRIMARY KEY, 
	dateTime DATETIME NOT NULL, 
	numberOfShares INT NOT NULL,
    transactionCost DECIMAL(10,2) NOT NULL
	customerID INT NOT NULL,
    stockID INT NOT NULL, 
	FOREIGN KEY (customerID, stockID) REFERENCES CustomerStock (customerID, stockID)
);
