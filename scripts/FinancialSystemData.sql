USE FinancialManagementSystem;

INSERT INTO Bank (name, location) VALUES
    ('Toronto-Dominion Bank', 'Canada'), 
    ('Scotiabank', 'Canada'), 
    ('Bank of Montreal', 'Canada'), 
    ('Royal Bank Of Canada', 'Canada'), 
    ('Citigroup Inc.', 'United States'), 
	('JPMorgan Chase & Co.', 'United States'),
	('HSBC Holdings plc', 'United Kingdom'),
    ('Goldman Sachs Group Inc.', 'United States'),
	('Bank of America', 'United States'),
	('Wells Fargo & Co.', 'United States');

INSERT INTO Company (name, industry, status, revenue, profit, grossMargin, cashFlow ) VALUES 
	('Amazon', 'Multinational Technology Company', 'PUBLIC', 514.22, 225.152, 41.8, 25.18), 
	('Apple Inc.', 'Technology', 'PUBLIC', 365.98, 74.46, 39.7, 81.43),
    ('Microsoft Corporation', 'Technology', 'PUBLIC', 168.09, 45.03, 67.0, 60.67),
    ('Saudi Aramco', 'Energy', 'PUBLIC', 229.67, 49.89, 65.0, 47.53),
    ('Alphabet Inc. (Google)', 'Technology', 'PUBLIC', 182.53, 40.27, 56.7, 52.11),
    ('Visa Inc.', 'Financial Services', 'PUBLIC', 23.02, 12.08, 67.0, 14.75),
	('Nike', 'Apparel and Footwear', 'PUBLIC', 44.54, 2.54, 43.8, 4.86),
    ('Deloitte', 'Professional Services', 'PRIVATE', 47.6, 2.03, 28.4, 3.95),
	('Cargill', 'Agriculture', 'PRIVATE', 114.7, 3.1, 11.4, 3.57),
	('Mars, Incorporated', 'Food and Beverage', 'PRIVATE', 94.0, 2.6, 40.8, 1.02);

INSERT INTO ExchangeOrganization (tickerCode, name) VALUES 
    ('NYSE', 'New York Stock Exchange'),
    ('NASDAQ', 'NASDAQ Stock Market'),
    ('LSE', 'London Stock Exchange'),
    ('TSE', 'Tokyo Stock Exchange'),
    ('HKEX', 'Hong Kong Stock Exchange'),
    ('SSE', 'Shanghai Stock Exchange'),
    ('Euronext', 'Euronext Stock Exchange'),
    ('SIX', 'SIX Swiss Exchange'),
    ('BSE', 'Bombay Stock Exchange');
    
INSERT INTO Customer (firstName, lastName, phoneNumber) VALUES 
	('Laura', 'Robertson', '+1 (514) 123-1233'),
	('Jane', 'Smith', '+1 (234) 567-8901'),
    ('Michael', 'Johnson', '+1 (678) 901-2345'),
    ('Emily', 'Williams', '+1 (456) 789-0123'),
    ('William', 'Brown', '+1 (123) 123-1233'),
    ('Sophia', 'Jones', '+1 (890) 123-4567'),
    ('James', 'Lee', '+1 (345) 678-9012'),
    ('Olivia', 'Miller', '+1 (678) 901-2345'),
    ('Robert', 'Taylor', '+1 (890) 123-4567'),
    ('Emma', 'Anderson', '+1 (234) 567-8901');

INSERT INTO Stock (tickerCode, sharePrice, status, numberOfOutstandingShares, marketCap, dailyVolume, companyID) VALUES 
    ('AMZN',  131.56, 'LISTED', 2000000000, 6940240000000.00, 1000000, 1),
    ('AAPL',  147.37, 'LISTED',5000000000, 7368500000000.00, 1500000, 2),
    ('MSFT',  289.71,'LISTED', 3000000000, 8691300000000.00, 1200000, 3),
    ('ARAMCO',  18.45, 'LISTED',4000000000, 73800000000.00, 800000, 4),
    ('GOOGL', 131.82,'LISTED', 1500000000, 4215375000000.00, 900000, 5),
    ('V', 234.80, 'LISTED',1000000000, 234800000000.00, 500000, 6),
    ('NKE',  155.90,'LISTED', 800000000, 124720000000.00, 700000, 7);

INSERT INTO AccountType (type, minimumStartDeposit, interestRate, compoundRate) VALUES
    ('CHECKING', 100.00, 0.00, 'NA'),
	('CHECKING', 20.00, 0.00, 'NA'),
	('CHECKING', 50.00, 0.00, 'NA'),
    ('HIGH_INTEREST_SAVINGS', 100.00, 1.5, 'MONTHLY'),
    ('HIGH_INTEREST_SAVINGS', 1000.00, 10.0, 'MONTHLY'),
    ('HIGH_INTEREST_SAVINGS', 25.00, 1.75, 'MONTHLY'),
    ('LOW_INTEREST_SAVINGS', 50.00, 1.25, 'SEMI_ANNUALLY'),
    ('LOW_INTEREST_SAVINGS', 50.00, 1.0, 'QUARTERLY'),
    ('LOW_INTEREST_SAVINGS', 25.00, 0.75, 'QUARTERLY');


INSERT INTO Account (openingDate, depositBalance, interestBalance, totalBalance, status, customerID) VALUES 
	('2022-01-01',	1230.12,0.00,	1230.12,'OPEN',			1),
	('2022-01-01',	40.00,	0.00,	40.00,	'OPEN',			1),
	('2018-01-01',	0,		0,		0,		'OPEN',			1),
	('2020-05-05',	1000,	123.11,	123.12,	'OPEN',			3),
	('2018-10-12',	923.65,	0.00,	923.65,	'OPEN',			4),
	('2019-12-12',	20.12,	131.33,	1.22,   'OPEN',			5),
	('2018-07-19',	55.22,	5431.22,5431.22,'OPEN',			6);
			
INSERT INTO BankAccountType (bankID, accountTypeID ) VALUES 
	(1,1),
    (1,2),
    (1,3),
    (2,2),
	(2,3),
    (4,5),
    (4,4),
    (6,8),
    (8,7);

INSERT INTO AccountBridge (bankID, accountTypeID, accountID) VALUES 
	(1,1,1),
    (2,2,2),
    (4,5,3),
    (4,5,4),
    (1,2,5),
    (6,8,6),
    (8,7,7);


INSERT INTO StockExchangeOrganization (stockID, exchangeOrganizationID) VALUES 
    (1, 1),
    (1, 5),
    (2, 2),
	(2, 4),
    (2, 5),
    (2, 6),
    (3, 1),
    (3, 5),
    (4, 4),
    (5, 5),
    (6, 7),
    (7, 7);

INSERT INTO Portfolio (customerID, balance) VALUES 
	(1, 1100.00),
    (2, 200.00),
    (3, 10000.00),
    (4, 400000.00),
    (5, 10000.50),
    (6, 300.12),
    (7, 5000.53),
    (8, 6321.31),
    (9, 123.32),
    (10, 4115.78);
    
INSERT INTO PortfolioStock (numberOfShares, marketValue, bookValue, averagePrice, totalReturn, percentageReturn, portfolioID, stockID) VALUES
    -- Customer 1 Portfolio
    (10, 34701.20, 34701.20, 3470.12, 0.00, 0.00, 1, 1),    -- 10 shares of AMZN
    (50, 7368.50, 7368.50, 147.37, 0.00, 0.00, 1, 2),      -- 50 shares of AAPL
    -- Customer 2 Portfolio
	(10, 34701.20, 34701.20, 3470.12, 0.00, 0.00, 2, 1),    -- 10 shares of AMZN
    (20, 5794.20, 5794.20, 289.71, 0.00, 0.00, 2, 3),      -- 20 shares of MSFT
    -- Customer 3 Portfolio
    (2000, 36900.00, 36900.00, 18.45, 0.00, 0.00, 3, 4),   -- 2000 shares of ARAMCO
    (5, 14051.25, 14051.25, 2810.25, 0.00, 0.00, 3, 5),    -- 5 shares of GOOGL
    -- Customer 4 Portfolio
    (1700, 398660.00, 398660.00, 234.80, 0.00, 0.00, 4, 6),-- 1700 shares of V
    -- Customer 5 Portfolio
    (60, 9354.00, 9354.00, 155.90, 0.00, 0.00, 5, 7);      -- 60 shares of NKE
    
INSERT INTO StockTransaction (dateTime, transactionType, numberOfShares, transactionCost) VALUES 
	("2022-09-01 10:00:00", "BUY", 4, 200.00),
	("2023-01-01 11:00:00", "BUY", 4, 500.00),
	("2022-02-12 13:00:00", "BUY", 4, 50.00),
	("2012-05-16 01:00:00", "BUY", 4, 150.00),
	("2020-09-19 13:00:00", "SELL",4, 2000.00),
	("2021-11-07 13:00:00", "SELL", 4, 10000.00),
	("2021-12-27 13:00:00", "SELL", 4, 125.00),
	("2022-12-21 10:30:00", "SELL", 2, 1000.00),
	("2022-11-30 04:00:00", "SELL", 4, 25.00);
    
INSERT INTO PortfolioBridge (portfolioID, stockID, exchangeOrganizationID, stockTransactionID) VALUES 
	(1,1, 1,1),
    (1,1, 1,2),
    (2,2, 2,3),
    (2,5, 5,4),
    (2,7, 7,5),
    (3,3, 1,6),
    (3,2, 6,7),
    (4,5, 5,8),
    (4,1, 1,9);
    
INSERT INTO Transaction(dateTime, transactionType, amount) VALUES 
	("2018-01-02 10:00:00", "DEPOSIT", 100.00),
	("2019-01-02 11:00:00", "TRANSFER", 100.00),
	("2021-01-01 13:00:00", "DEPOSIT", 50.00),
	("2012-05-16 01:00:00", "DEPOSIT", 150.00),
	("2020-09-19 13:00:00", "DEPOSIT", 2000.00),
	("2021-11-07 13:00:00", "DEPOSIT", 10000.00),
	("2021-12-27 13:00:00", "DEPOSIT", 125.00),
	("2022-12-21 10:30:00", "WITHDRAW", 1000.00),
	("2022-11-30 04:00:00", "WITHDRAW", 25.00);

INSERT INTO AccountTransaction (transactionID, accountID1, accountID2) VALUES
	(1,3,3),
    (2,1,3),
    (9,3,3);

