package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Account {
    private int accountID;
    private Date openingDate;
    private BigDecimal depositBalance;
    private BigDecimal interestBalance;
    private BigDecimal totalBalance;
    private AccountType accountType;
    private List<AccountTransaction> accountTransactions;

}
