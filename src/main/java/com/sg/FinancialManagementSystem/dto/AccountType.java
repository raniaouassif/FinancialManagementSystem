package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class AccountType {
    private int accountTypeID;
    private BankAccountType type;
    private BigDecimal interestRate;
    private CompoundRate compoundRate;
    private List<Account> accounts;

}
