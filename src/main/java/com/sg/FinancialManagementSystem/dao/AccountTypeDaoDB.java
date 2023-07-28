package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.AccountMapper;
import com.sg.FinancialManagementSystem.dao.mappers.AccountTypeMapper;
import com.sg.FinancialManagementSystem.dto.Account;
import com.sg.FinancialManagementSystem.dto.AccountType;
import com.sg.FinancialManagementSystem.dto.Bank;
import com.sg.FinancialManagementSystem.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-24
 */
@Repository
public class AccountTypeDaoDB implements AccountTypeDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public AccountType getAccountTypeByID(int accountTypeID) {
        try{
            final String GET_ACCOUNT_TYPE_BY_ID = "SELECT * FROM AccountType WHERE accountTypeID = ?";

            return jdbcTemplate.queryForObject(GET_ACCOUNT_TYPE_BY_ID, new AccountTypeMapper(), accountTypeID) ;
        } catch (DataAccessException e){
            System.out.println("AccountTypeDaoDB :getAccountTypeByID() failed");
            return null;
        }
    }

    @Override
    public List<AccountType> getAllAccountTypes() {
        final String GET_ALL_ACCOUNT_TYPES = "SELECT * FROM AccountType ORDER BY type DESC";

        return jdbcTemplate.query(GET_ALL_ACCOUNT_TYPES, new AccountTypeMapper());
    }

    @Override
    public AccountType addAccountType(AccountType accountType) {
        final String ADD_ACCOUNT_TYPE = "INSERT INTO AccountType "
                + "(type, minimumStartDeposit, interestRate, compoundRate) VALUES "
                + "(?,?,?,?)";

        jdbcTemplate.update(
                ADD_ACCOUNT_TYPE,
                accountType.getType().toString(),
                accountType.getMinimumStartDeposit(),
                accountType.getCompoundRate() == null ? null : accountType.getInterestRate(),
                accountType.getCompoundRate() == null ? null : accountType.getCompoundRate().toString()
        );

        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        accountType.setAccountTypeID(newID);

        return accountType;
    }

    @Override
    public void updateAccountType(AccountType accountType) {
        final String UPDATE_ACCOUNT_TYPE = "UPDATE AccountType SET "
                + "type = ?, minimumStartDeposit = ?, interestRate = ?, compoundRate = ? "
                + "WHERE accountTypeID = ?";

        jdbcTemplate.update(
                UPDATE_ACCOUNT_TYPE,
                accountType.getType().toString(),
                accountType.getMinimumStartDeposit(),
                accountType.getInterestRate(),
                accountType.getCompoundRate().toString(),
                accountType.getAccountTypeID()
        );
    }

    @Override
    public void deleteAccountTypeByID(int accountTypeID) {
        // Get all accounts of this type
        final String GET_ACCOUNT_BY_ACCOUNT_TYPE = "SELECT a.* FROM Account a " +
                "JOIN AccountBridge ba ON ba.accountID = a.accountID " +
                "JOIN BankAccountType bat ON bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID " +
                "JOIN AccountType at ON at.accountTypeID = bat.accountTypeID " +
                "WHERE at.accountTypeID = ?;";

        List<Account> accounts = jdbcTemplate.query(GET_ACCOUNT_BY_ACCOUNT_TYPE, new AccountMapper(), accountTypeID);

        // For all the accounts, set the status to closed
        final String UPDATE_ACCOUNTS_TO_CLOSED = "UPDATE Account SET "
                + "depositBalance = 0.00, "
                + "interestBalance = 0.00, "
                + "totalBalance = 0.00, "
                + "closingDate = ?, "
                + "status = 'CLOSED', "
                + "closingReason = ? "
                + "WHERE accountID = ?;";

        AccountType accountType = getAccountTypeByID(accountTypeID);
        String closingReason = "Account type " + accountType.getAccountTypeID() + " - " + accountType.getType() + " has been dissolved.";
        LocalDate closingDate = LocalDate.now();
        for(Account account : accounts) {
            jdbcTemplate.update(UPDATE_ACCOUNTS_TO_CLOSED,
                    closingDate,
                    closingReason,
                    account.getAccountID());
        }

        // Delete from the bankAccount bridge table
        String DELETE_BANK_ACCOUNT_BY_ACCOUNT_TYPE_ID = "DELETE FROM AccountBridge WHERE accountTypeID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_BY_ACCOUNT_TYPE_ID, accountTypeID);

        //Delete from the bankAccountType bridge table
        String DELETE_BANK_ACCOUNT_TYPE_ACCOUNT_TYPE_ID = "DELETE FROM BankAccountType WHERE accountTypeID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_TYPE_ACCOUNT_TYPE_ID, accountTypeID);

        //Finally Delete the ACCOUNT Type
        String DELETE_ACCOUNT_TYPE_BY_ID = "DELETE FROM AccountType WHERE accountTypeID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT_TYPE_BY_ID, accountTypeID);
    }

    @Override
    public List<AccountType> getAccountTypesByBank(Bank bank) {
        final String GET_ACCOUNT_TYPES_BY_BANK = "SELECT at.* FROM AccountType at " +
                "JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID " +
                "JOIN Bank b ON b.bankID = bat.bankID " +
                "WHERE b.bankID = ?";

        List<AccountType> accountTypes = jdbcTemplate.query(GET_ACCOUNT_TYPES_BY_BANK, new AccountTypeMapper(), bank.getBankID());

        return accountTypes.size() == 0 ? new ArrayList<>() : accountTypes;
    }

    @Override
    public List<AccountType> getAccountTypesByCustomer(Customer customer) {
        final String GET_ACCOUNT_TYPES_BY_CUSTOMER = "SELECT at.* FROM AccountType at " +
                "JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID " +
                "JOIN AccountBridge ba ON ba.accountTypeID = bat.accountTypeID AND ba.bankID = bat.bankID " +
                "JOIN Account a ON a.accountID = ba.accountID " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";

        List<AccountType> accountTypes = jdbcTemplate.query(GET_ACCOUNT_TYPES_BY_CUSTOMER, new AccountTypeMapper(), customer.getCustomerID());
        return accountTypes.size() == 0 ? new ArrayList<>() : accountTypes;
    }

    @Override
    public AccountType getAccountTypeByAccount(Account account) {
        try {
            final String GET_ACCOUNT_TYPE_BY_ACCOUNT ="SELECT at.* FROM AccountType at " +
                    "JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID " +
                    "JOIN AccountBridge ba ON ba.accountTypeID = bat.accountTypeID AND ba.bankID = bat.bankID " +
                    "JOIN Account a ON a.accountID = ba.accountID " +
                    "WHERE a.accountID = ?";

            return jdbcTemplate.queryForObject(GET_ACCOUNT_TYPE_BY_ACCOUNT, new AccountTypeMapper(), account.getAccountID());
        } catch (DataAccessException e) {
            System.out.println("AccountTypeDaoDB: getAccountTypeByAccount() failed.");
            return null;

        }
    }

    @Override
    public List<AccountType> getAccountTypeByType(AccountType type) {
        final String GET_ACCOUNT_TYPES_BY_TYPE = "SELECT * FROM AccountType WHERE type = ? ";
        List<AccountType> accountTypes = jdbcTemplate.query(GET_ACCOUNT_TYPES_BY_TYPE, new AccountTypeMapper(), type.toString());
        return accountTypes.size() == 0 ? new ArrayList<>() : accountTypes;
    }
}
