package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountTransaction;
import com.sg.FinancialManagementSystem.dto.AccountType;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public interface AccountTransactionDao {
    AccountTransaction getAccountTransactionByID(int accountTransactionID);

    List<AccountTransaction> getAllAccountTransactions();

    AccountTransaction addAccountTransaction(AccountTransaction accountTransaction);

    void updateAccountTransaction(AccountTransaction accountTransaction);

    void deleteAccountTransactionByID(int accountTransactionID);

    List<AccountTransaction> getAccountTransactionsByAccount(Account account);

}
