package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author raniaouassif on 2023-07-23
 */
public class AccountTransaction {
    private int accountTransactionID;
    private LocalDateTime dateTime;
    private AccountTransactionType transactionType;
    private BigDecimal amount;

}
