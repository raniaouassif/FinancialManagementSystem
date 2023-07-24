USE FinancialManagementSystem;

INSERT INTO Bank (name, location) VALUES
	('Industrial and Commercial Bank of China (ICBC)', 'China'),
	('China Construction Bank', 'China'),
	('Agricultural Bank of China', 'China'),
	('Bank of China', 'China'),
	('Mitsubishi UFJ Financial Group (MUFG)', 'Japan'),
	('JPMorgan Chase & Co.', 'United States'),
	('HSBC Holdings plc', 'United Kingdom'),
	('BNP Paribas', 'France'),
	('Bank of America', 'United States'),
	('Wells Fargo & Co.', 'United States');


INSERT INTO Company (name, industry, status) VALUES 
	('Amazon', 'Multinational Technology Company', 'PUBLIC'), 
	('Apple Inc.', 'Technology', 'PUBLIC'),
    ('Microsoft Corporation', 'Technology', 'PUBLIC'),
    ('Saudi Aramco', 'Energy', 'PUBLIC'),
    ('Alphabet Inc. (Google)', 'Technology', 'PUBLIC'),
    ('Visa Inc.', 'Financial Services', 'PUBLIC'),
	('Nike', 'Apparel and Footwear', 'PUBLIC'),
    ('Deloitte', 'Professional Services', 'PRIVATE'),
	('Cargill', 'Agriculture', 'PRIVATE'),
	('Mars, Incorporated', 'Food and Beverage', 'PRIVATE');

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
	('Rania', 'Ouassif', '123-123-1234'),
	('Jane', 'Smith', '555-5678'),
    ('Michael', 'Johnson', '555-9876'),
    ('Emily', 'Williams', '555-4321'),
    ('William', 'Brown', '555-8765'),
    ('Sophia', 'Jones', '555-3456'),
    ('James', 'Lee', '555-6543'),
    ('Olivia', 'Miller', '555-7890'),
    ('Robert', 'Taylor', '555-2109'),
    ('Emma', 'Anderson', '555-9012');

INSERT INTO Stock (tickerCode, sharePrice, numberOfOutstandingShares, marketCap, dailyVolume, companyID) VALUES 
    ('AMZN',  3470.12, 2000000000, 6940240000000.00, 1000000, 1),
    ('AAPL',  147.37, 5000000000, 7368500000000.00, 1500000, 2),
    ('MSFT',  289.71, 3000000000, 8691300000000.00, 1200000, 3),
    ('ARAMCO',  18.45, 4000000000, 73800000000.00, 800000, 4),
    ('GOOGL', 2810.25, 1500000000, 4215375000000.00, 900000, 5),
    ('V', 234.80, 1000000000, 234800000000.00, 500000, 6),
    ('NKE',  155.90, 800000000, 124720000000.00, 700000, 7);

INSERT INTO AccountType (type, minimumStartDeposit, interestRate, compoundRate) VALUES
    ('CHECKING', 100.00, NULL, NULL),
	('CHECKING', 20.00, NULL, NULL),
	('CHECKING', 50.00, NULL, NULL),
    ('HIGH_INTEREST_SAVINGS', 100.00, 1.5, 'Monthly'),
    ('HIGH_INTEREST_ANNUAL', 1000.00, 5.0, 'Annually'),
    ('HIGH_INTEREST_MONTHLY', 25.00, 1.75, 'Monthly'),
    ('LOW_INTEREST_SAVINGS', 50.00, 1.25, 'Semi-Annually'),
    ('LOW_INTEREST_QUARTERLY', 50.00, 1.0, 'Quarterly'),
    ('LOW_INTEREST_QUARTERLY', 25.00, 0.75, 'Quarterly');


INSERT INTO Account (openingDate, depositBalance, status, customerID) VALUES 
	('2022-01-01', 200.00, 'OPEN', 1),
	('2022-01-01', 40.00, 'OPEN', 1),
	('2023-11-28', 100.00, 'OPEN', 1),
	('2020-05-05', 1000.00, 'OPEN', 3),
	('2018-10-12', 500.00,' OPEN', 4),
	('2019-12-12', 20.00, 'OPEN',5),
	('2018-07-19', 55.00, 'OPEN',6);
			
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


INSERT INTO StockExchangeOrganization (exchangeOrganizationID, stockID) VALUES 
    (1, 1),
    (1, 7),
    (2, 2),
    (2, 6),
    (2, 5),
    (2, 4),
    (3, 3),
    (3, 1),
    (4, 4),
    (5, 5),
    (6, 6),
    (7, 7);

INSERT INTO Portfolio (customerID, balance) VALUES 
	(1, 1100.00),
    (2, 200.00),
    (3, 10000.00),
    (4, 400000.00),
    (5, 10000.50);
    
INSERT INTO StockPortfolio(portfolioID, stockID, numberOfShares, sharesValue) VALUES 
    (1, 2, 5, 736.85),    -- Portfolio 1: AAPL (Apple Inc.)
    (1, 4, 10, 184.50),   -- Portfolio 1: ARAMCO (Saudi Aramco)
    (2, 3, 15, 4345.65),  -- Portfolio 2: MSFT (Microsoft Corporation)
    (2, 7, 20, 3118.00),  -- Portfolio 2: NKE (Nike Inc.)
    (3, 1, 8, 27760.96),  -- Portfolio 3: AMZN (Amazon.com Inc.)
    (4, 5, 12, 33723.00), -- Portfolio 4: GOOGL (Alphabet Inc.)
    (5, 6, 30, 7044.00);  -- Portfolio 5: V (Visa Inc.)


INSERT INTO Transaction(dateTime, transactionType, amount) VALUES 
	("2022-09-01 10:00:00", "DEPOSIT", 100.00),
	("2023-01-01 11:00:00", "DEPOSIT", 200.00),
	("2022-02-12 13:00:00", "DEPOSIT", 50.00),
	("2012-05-16 01:00:00", "DEPOSIT", 150.00),
	("2020-09-19 13:00:00", "DEPOSIT", 2000.00),
	("2021-11-07 13:00:00", "DEPOSIT", 10000.00),
	("2021-12-27 13:00:00", "DEPOSIT", 125.00),
	("2022-12-21 10:30:00", "WITHDRAW", 1000.00),
	("2022-11-30 04:00:00", "WITHDRAW", 25.00);

INSERT INTO AccountTransaction (transactionID, accountID1, accountID2) VALUES
	(9,1,1),
    (8,6,6),
	(1,1,2),
    (2,1,3),
    (1,2,1),
    (4,4,4),
    (5,3,1);
