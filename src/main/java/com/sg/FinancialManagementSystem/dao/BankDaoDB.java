package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.*;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
@Repository
public class BankDaoDB implements BankDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Bank getBankByID(int bankID) {
        try {
            final String GET_BANK_BY_ID = "SELECT * FROM Bank WHERE bankID = ? ";
            return jdbcTemplate.queryForObject(GET_BANK_BY_ID, new BankMapper(), bankID);
        } catch (DataAccessException e) {
            System.out.println("BankDaoDB : Get Bank by ID failed.");
            return null;
        }
    }

    @Override
    public List<Bank> getAllBanks() {
        final String GET_ALL_BANKS = "SELECT * FROM Bank";
        return jdbcTemplate.query(GET_ALL_BANKS, new BankMapper());
    }

    @Override
    public Bank addBank(Bank bank) {

        final String ADD_BANK = "INSERT INTO Bank(name, location) VALUES (?, ?);";
        jdbcTemplate.update(ADD_BANK,
                bank.getName(),
                bank.getLocation());

        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        bank.setBankID(newID);

        // insert bank account types into the BankAccountType bridge table
        insertBankAccountTypes(bank);

        return bank;
    }

    @Override
    public void updateBank(Bank bank) {
        final String UPDATE_BANK = "UPDATE Bank SET name = ?, location = ? WHERE bankID = ?";

        jdbcTemplate.update(
                UPDATE_BANK,
                bank.getName(),
                bank.getLocation()
        );
        //First delete the bank account types
        deleteBankAccountTypesByBankID(bank.getBankID());

        //Then reset the bank account types
        insertBankAccountTypes(bank);
    }

    @Override
    public void deleteBankByID(int bankID) {
        String bankName = getBankByID(bankID).getName();

        //First retrieve the Account Transactions linked to the bank
        List<Account> accounts = getAccountsByBankID(bankID);

        // For all the accounts, set the status to closed
        final String UPDATE_ACCOUNTS_TO_CLOSED = "UPDATE Account SET "
                + "depositBalance = 0, "
                + "interestBalance = 0, "
                + "totalBalance = 0, "
                + "closingDate = ?, "
                + "status = 'CLOSED', "
                + "closingReason = ? "
                + "WHERE accountID = ?;";

        String closingReason = "Bank " + bankName + " has been disolved.";
        LocalDate closingDate = LocalDate.now();
        for(Account account : accounts) {
            jdbcTemplate.update(UPDATE_ACCOUNTS_TO_CLOSED,
                    closingDate,
                    closingReason,
                    account.getAccountID());
        }

        // Delete from the bankAccount bridge table
        String DELETE_BANK_ACCOUNT_BY_BANK_ID = "DELETE FROM BankAccount WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_BY_BANK_ID, bankID);

        //Delete from the bankAccountType bridge table
        String DELETE_BANK_ACCOUNT_TYPE_BY_BANK_ID = "DELETE FROM BankAccountType WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_TYPE_BY_BANK_ID, bankID);

        //Finally Delete the bank
        String DELETE_BANK_BY_ID = "DELETE FROM Bank WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_BY_ID, bankID);
    }

    @Override
    public List<Bank> getBanksByCustomer(Customer customer) {
        final String SELECT_BANKS_BY_CUSTOMER = "SELECT b.* FROM Bank b " +
                "JOIN BankAccountType bat ON bat.bankID = b.bankID " +
                "JOIN BankAccount ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID " +
                "JOIN Account a ON a.accountID = ba.accountID " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";

        List<Bank> retrievedBanks = jdbcTemplate.query(
                SELECT_BANKS_BY_CUSTOMER,
                new BankMapper(),
                customer.getCustomerID()
        );
        return retrievedBanks.size() == 0 ? new ArrayList<>() : retrievedBanks;
    }

    @Override
    public Bank getBankByAccount(Account account) {
        try {
            final String GET_BANK_BY_ACCOUNT = "SELECT b.* FROM Bank b "
                    + "JOIN BankAccountType bat ON bat.bankID = b.bankID "
                    + "JOIN BankAccount ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID "
                    + "JOIN Account a ON a.accountID = ba.accountID "
                    + "WHERE a.accountID = ?";
            Bank retrievedBank = jdbcTemplate.queryForObject(
                    GET_BANK_BY_ACCOUNT,
                    new BankMapper(),
                    account.getAccountID()
            );

            return retrievedBank;
        } catch(DataAccessException e) {
            System.out.println("BankDaoDB: GetBankByAccount() failed.");
            return null;
        }
    }

    //PRIVATE HELPER FUNCTIONS
    private void insertBankAccountTypes(Bank bank) {
        if(bank.getAccountTypes() != null) {
            final String INSERT_BANK_ACCOUNT_TYPES = "INSERT INTO "
                    + "BankAccountType (bankID, accountTypeID) VALUES (?,?)";

            int bankID = bank.getBankID();
            for(AccountType accountType : bank.getAccountTypes()) {
                int accountTypeID = accountType.getAccountTypeID();
                jdbcTemplate.update(
                        INSERT_BANK_ACCOUNT_TYPES,
                        bankID,
                        accountTypeID);
            }
        }
    }

    private List<Account> getAccountsByBankID(int bankID) {
        final String GET_ACCOUNTS_BY_BANK_ID = "SELECT a.* " +
                "FROM Account a " +
                "JOIN BankAccount ba ON ba.accountID = a.accountID " +
                "JOIN BankAccountType bat on bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID " +
                "JOIN Bank b ON b.bankID = bat.bankID " +
                "WHERE b.bankID = ?;";

        List<Account> retrievedAccounts = jdbcTemplate.query(
                GET_ACCOUNTS_BY_BANK_ID,
                new AccountMapper(),
                bankID
        );

        return retrievedAccounts.size() == 0 ? null : retrievedAccounts;
    }

    private void deleteBankAccountTypesByBankID(int bankID) {
        final String DELETE_BANK_ACCOUNT_TYPES_BY_BAND_ID = "DELETE FROM BankAccountType WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_TYPES_BY_BAND_ID, bankID);
    }
}
