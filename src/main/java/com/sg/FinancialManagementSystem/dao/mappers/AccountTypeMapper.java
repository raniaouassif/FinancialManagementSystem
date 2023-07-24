package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.BankAccountType;
import com.sg.FinancialManagementSystem.dto.CompoundRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
public class AccountTypeMapper implements RowMapper<AccountType> {

    @Override
    public AccountType mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountType accountType = new AccountType();
        accountType.setAccountTypeID(rs.getInt("accountTypeID"));
        accountType.setType(BankAccountType.valueOf(rs.getString("type")));
        accountType.setMinimumStartDeposit(rs.getBigDecimal("minimumStartDeposit"));
        accountType.setInterestRate(rs.getBigDecimal("interestRate"));
        accountType.setCompoundRate(CompoundRate.valueOf(rs.getString("compoundRate")));
        return accountType;
    }

}
