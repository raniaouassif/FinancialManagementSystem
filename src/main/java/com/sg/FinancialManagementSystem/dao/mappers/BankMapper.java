package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
@Component
public class BankMapper implements RowMapper<Bank> {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Bank mapRow(ResultSet rs, int rowNum) throws SQLException {
        Bank bank = new Bank();
        bank.setBankID(rs.getInt("bankID"));
        bank.setName(rs.getString("name"));
        bank.setLocation(rs.getString("location"));
        bank.setAccountTypes(getAccountTypesByBank(bank));
        return bank;
    }

    //PRIVATE HELPER FUNCTIONS
    private List<AccountType> getAccountTypesByBank(Bank bank) {
        final String GET_ACCOUNT_TYPES_BY_BANK = "SELECT at.* FROM AccountType at "
                +"JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID "
                +"JOIN Bank b ON b.bankID = bat.bankID "
                +"WHERE b.bankID = ?";

        List<AccountType> retrievedAccountTypes = jdbcTemplate.query(
                GET_ACCOUNT_TYPES_BY_BANK,
                new AccountTypeMapper(),
                bank.getBankID()
        );

        return retrievedAccountTypes.size() == 0 ? new ArrayList<>() : retrievedAccountTypes;
    }
}
