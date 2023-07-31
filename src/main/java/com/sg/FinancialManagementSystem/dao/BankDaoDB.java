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
            Bank bank = jdbcTemplate.queryForObject(GET_BANK_BY_ID, new BankMapper(), bankID);
            bank.setAccountTypes(getAccountTypesByBank(bankID));
            return bank;
        } catch (DataAccessException e) {
            System.out.println("BankDaoDB : Get Bank by ID failed.");
            return null;
        }
    }

    @Override
    public List<Bank> getAllBanks() {
        final String GET_ALL_BANKS = "SELECT * FROM Bank";

        List<Bank> banks = jdbcTemplate.query(GET_ALL_BANKS, new BankMapper());

        setAccountTypesForBankList(banks);

        return banks;
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
                bank.getLocation(),
                bank.getBankID()
        );

        //Then reset the bank account types
        insertBankAccountTypes(bank);
    }

    @Override
    public void deleteBankByID(int bankID) {

        //Get the accounts to delete before deleting the bridge tables
        String GET_ACCOUNTS = "SELECT a.* FROM Account a  " +
                "JOIN AccountBridge ab ON ab.accountID = a.accountID " +
                "JOIN BankAccountType bat ON bat.accountTypeID = ab.accountTypeID " +
                "JOIN Bank b ON b.bankID = bat.bankID " +
                "WHERE b.bankID = ?";

        List<Account> accountsToDelete = jdbcTemplate.query(GET_ACCOUNTS, new AccountMapper(), bankID);

        // Delete account transactions
        String DELETE_ACCOUNT_TRANSACTIONS = "DELETE at.* FROM AccountTransaction at " +
                "JOIN Account a ON a.accountID = at.accountID1 OR a.accountID = at.accountID2 " +
                "JOIN AccountBridge ab ON ab.accountID = a.accountID " +
                "JOIN BankAccountType bat ON bat.accountTypeID = ab.accountTypeID " +
                "JOIN Bank b ON b.bankID = bat.bankID " +
                "WHERE b.bankID = ?";
        jdbcTemplate.update(DELETE_ACCOUNT_TRANSACTIONS, bankID);

        // Delete from the account bridge table
        String DELETE_BANK_ACCOUNT_BY_BANK_ID = "DELETE FROM AccountBridge WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_BY_BANK_ID, bankID);

        // DELETE ALL THE ACCOUNTS
        String DELETE_ACCOUNT_TO_DELETE = "DELETE FROM Account WHERE accountID = ?";
        for(Account account : accountsToDelete) {
            jdbcTemplate.update(DELETE_ACCOUNT_TO_DELETE, account.getAccountID());
        }

        //Delete from the bankAccountType bridge table
        String DELETE_BANK_ACCOUNT_TYPE_ACCOUNT_TYPE_ID = "DELETE FROM BankAccountType WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_TYPE_ACCOUNT_TYPE_ID, bankID);

        //Finally Delete the bank
        String DELETE_BANK_BY_ID = "DELETE FROM Bank WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_BY_ID, bankID);
    }

    @Override
    public List<Bank> getBanksByCustomer(Customer customer) {
        final String SELECT_BANKS_BY_CUSTOMER = "SELECT b.* FROM Bank b " +
                "JOIN BankAccountType bat ON bat.bankID = b.bankID " +
                "JOIN AccountBridge ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID " +
                "JOIN Account a ON a.accountID = ba.accountID " +
                "JOIN Customer c ON c.customerID = a.customerID " +
                "WHERE c.customerID = ?";

        List<Bank> retrievedBanks = jdbcTemplate.query(
                SELECT_BANKS_BY_CUSTOMER,
                new BankMapper(),
                customer.getCustomerID()
        );
        setAccountTypesForBankList(retrievedBanks);

        return retrievedBanks.size() == 0 ? new ArrayList<>() : retrievedBanks;
    }

    @Override
    public Bank getBankByAccount(Account account) {
        try {
            final String GET_BANK_BY_ACCOUNT = "SELECT b.* FROM Bank b "
                    + "JOIN BankAccountType bat ON bat.bankID = b.bankID "
                    + "JOIN AccountBridge ba ON ba.bankID = bat.bankID AND ba.accountTypeID = bat.accountTypeID "
                    + "JOIN Account a ON a.accountID = ba.accountID "
                    + "WHERE a.accountID = ?";
            Bank retrievedBank = jdbcTemplate.queryForObject(
                    GET_BANK_BY_ACCOUNT,
                    new BankMapper(),
                    account.getAccountID()
            );

            retrievedBank.setAccountTypes(getAccountTypesByBank(retrievedBank.getBankID()));

            return retrievedBank;
        } catch(DataAccessException e) {
            System.out.println("BankDaoDB: GetBankByAccount() failed.");
            return null;
        }
    }

    //PRIVATE HELPER FUNCTIONS

    //PRIVATE HELPER FUNCTIONS
    private List<AccountType> getAccountTypesByBank(int bankID) {
        final String GET_ACCOUNT_TYPES_BY_BANK = "SELECT at.* FROM AccountType at "
                +"JOIN BankAccountType bat ON bat.accountTypeID = at.accountTypeID "
                +"JOIN Bank b ON b.bankID = bat.bankID "
                +"WHERE b.bankID = ?";

        List<AccountType> retrievedAccountTypes = jdbcTemplate.query(
                GET_ACCOUNT_TYPES_BY_BANK,
                new AccountTypeMapper(),
                bankID
        );
        return retrievedAccountTypes.size() == 0 ? new ArrayList<>() : retrievedAccountTypes;
    }

    private void setAccountTypesForBankList(List<Bank> banks) {
        for(Bank bank : banks) {
            int bankID = bank.getBankID();
            bank.setAccountTypes(getAccountTypesByBank(bankID));
        }
    }
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
                "JOIN AccountBridge ba ON ba.accountID = a.accountID " +
                "JOIN BankAccountType bat on bat.bankID = ba.bankID AND bat.accountTypeID = ba.accountTypeID " +
                "JOIN Bank b ON b.bankID = bat.bankID " +
                "WHERE b.bankID = ?;";

        List<Account> retrievedAccounts = jdbcTemplate.query(
                GET_ACCOUNTS_BY_BANK_ID,
                new AccountMapper(),
                bankID
        );

        return retrievedAccounts.size() == 0 ? new ArrayList<>() : retrievedAccounts;
    }

    private void deleteBankAccountTypesByBankID(int bankID) {
        final String DELETE_BANK_ACCOUNT_TYPES_BY_BAND_ID = "DELETE FROM BankAccountType WHERE bankID = ?";
        jdbcTemplate.update(DELETE_BANK_ACCOUNT_TYPES_BY_BAND_ID, bankID);
    }
}
