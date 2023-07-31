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

    
INSERT INTO Customer (firstName, lastName, phoneNumber) VALUES 
	('Laura', 'Robertson', '+1 (514) 123-1233'),
	('Jane', 'Smith', '+1 (234) 567-8901'),
    ('Michael', 'Johnson', '+1 (678) 901-2345'),
    ('Emily', 'Williams', '+1 (456) 789-0123'),
    ('William', 'Brown', '+1 (123) 123-1233'),
    ('Sophia', 'Jones', '+1 (890) 123-4567'),
    ('James', 'Lee', '+1 (345) 678-9012'),
    ('Olivia', 'Miller', '+1 (678) 901-2345'),
    ('Robert', 'Miller', '+1 (890) 123-4567'),
    ('Emma', 'Anderson', '+1 (234) 567-8901');

INSERT INTO AccountType (type, minimumStartDeposit, interestRate, compoundRate) VALUES
    ('CHECKING', 100.00, 0.00, 'NA'),
	('CHECKING', 20.00, 0.00, 'NA'),
	('CHECKING', 50.00, 0.00, 'NA'),
    ('HIGH_INTEREST_SAVINGS', 50.00, 1.0, 'MONTHLY'),
	('HIGH_INTEREST_SAVINGS', 100.00, 3.5, 'MONTHLY'),
	('HIGH_INTEREST_SAVINGS', 100.00, 1.75, 'MONTHLY'),
    ('LOW_INTEREST_SAVINGS', 45.00, 3.00, 'ANNUALLY'),
	('LOW_INTEREST_SAVINGS', 500.00, 1.25, 'SEMI_ANNUALLY'),
	('LOW_INTEREST_SAVINGS', 250.00, 1.0, 'QUARTERLY'),
	('LOW_INTEREST_SAVINGS', 100.00, 0.75, 'QUARTERLY');
			
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

