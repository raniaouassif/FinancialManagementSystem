package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author raniaouassif on 2023-07-23
 */
public class AccountMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setAccountID(rs.getInt("accountID"));
        account.setOpeningDate(rs.getDate("openingDate").toLocalDate());
        account.setDepositBalance(rs.getBigDecimal("depositBalance"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));

        // Handle the optional fields
        if(rs.getBigDecimal("interestBalance") != null ) {
            account.setInterestBalance(rs.getBigDecimal("interestBalance"));
        }

        if(rs.getBigDecimal("totalBalance") != null ) {
            account.setTotalBalance(rs.getBigDecimal("totalBalance"));
        }

        if(rs.getDate("closingDate") != null ) {
            account.setClosingDate(rs.getDate("closingDate").toLocalDate());
        }
        if(rs.getString("closingReason") != null ) {
            account.setClosingReason(rs.getString("closingReason"));
        }

        return account;
    }


}
