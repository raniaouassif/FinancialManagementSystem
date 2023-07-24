package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-23
 */
@Component
public class AccountMapper implements RowMapper<Account> {



    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setAccountID(rs.getInt("accountID"));
        account.setOpeningDate(rs.getDate("openingDate").toLocalDate());
        account.setDepositBalance(rs.getBigDecimal("depositBalance"));
        account.setInterestBalance(rs.getBigDecimal("interestBalance"));
        account.setTotalBalance(rs.getBigDecimal("totalBalance"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        account.setClosingDate(rs.getDate("closingDate").toLocalDate());
        account.setClosingReason(rs.getString("closingReason"));
//        account.setCustomer(getCustomerByAccount(account));
//        account.setAccountType(getAccountTypeByAccount(account));
//        account.setAccountTransactions(getAccountTransactionsByAccount(account));
        return account;
    }


}
