package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.AccountDao;
import com.sg.FinancialManagementSystem.dao.BankDao;
import com.sg.FinancialManagementSystem.dao.CustomerDao;
import com.sg.FinancialManagementSystem.dao.TransactionDao;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author raniaouassif on 2023-07-27
 */
@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountDao accountDao;

    @Autowired
    CustomerDao customerDao;

    @Autowired
    TransactionDao transactionDao;
    @Autowired
    BankDao bankDao;
    @Override
    public Account getAccountByID(int accountID) {
        Account account = accountDao.getAccountByID(accountID);
        calculateAndSetBalances(account);
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountDao.getAllAccounts();
        calculateAndSetBalancesForAccountList(accounts);

        return accounts;
    }

    @Override
    public Account addAccount(Account account) {
        BigDecimal minDeposit = account.getAccountType().getMinimumStartDeposit();

        boolean validBankAccountType = bankDao.getBankByID(account.getBank().getBankID()).getAccountTypes().contains(account.getAccountType());

        BigDecimal initialDeposit = account.getDepositBalance();

        account.setInterestBalance(BigDecimal.ZERO);
        account.setTotalBalance(initialDeposit);
        account.setStatus(AccountStatus.OPEN);
        account = accountDao.addAccount(account);


        //Add the initial transaction
        Transaction transaction = new Transaction();
        transaction.setDateTime(account.getOpeningDate().atTime(LocalTime.now().withNano(0)));
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setFrom(account);
        transaction.setTo(account);
        transaction.setAmount(initialDeposit);
        transaction = transactionDao.addTransaction(transaction); // Add the transaction to the dao

        return account;
    }

    @Override
    public void updateAccount(Account account) {
        accountDao.updateAccount(account);
    }

    @Override
    public void deleteAccountByID(int accountID) {
        accountDao.deleteAccountByID(accountID);
    }

    @Override
    public List<Account> getAccountsByCustomer(Customer customer) {
        List<Account> accounts = accountDao.getAccountsByCustomer(customer);
        calculateAndSetBalancesForAccountList(accounts);

        return accounts.size() == 0 ? new ArrayList<>() : accounts;
    }

    @Override
    public List<Account> getOpenAccountsByCustomer(Customer customer) {
        List<Account> accounts = accountDao.getOpenAccountsByCustomer(customer);
        calculateAndSetBalancesForAccountList(accounts);

        return accounts.size() == 0 ? new ArrayList<>() : accounts;
    }

    @Override
    public List<Account> getClosedAccountsByCustomer(Customer customer) {
        List<Account> accounts = accountDao.getClosedAccountsByCustomer(customer);
        calculateAndSetBalancesForAccountList(accounts);

        return accounts.size() == 0 ? new ArrayList<>() : accounts;
    }

    // PRIVATE HELPER FUNCTIONS

    /*
     * Calculate & set the balance based on today's date
     */
    private void calculateAndSetBalances(Account account) {
        if(account.getAccountType() != null) {
            //No interest is applied on checking accounts
            if(account.getAccountType().getType().toString().equals("CHECKING")) {
                BigDecimal interestBalance = BigDecimal.ZERO;
                BigDecimal depositBalance = BigDecimal.ZERO;
                BigDecimal totalBalance = BigDecimal.ZERO;
                List<Transaction> accountTransactions = transactionDao.getASCTransactionsByAccount(account.getAccountID());

                for(Transaction accountTransaction : accountTransactions) {
                    TransactionType transactionType = accountTransaction.getTransactionType();
                    if(transactionType.equals(TransactionType.DEPOSIT)
                            || (transactionType.equals(TransactionType.TRANSFER) && accountTransaction.getTo().getAccountID() == account.getAccountID()))
                    {
                        depositBalance = depositBalance.add(accountTransaction.getAmount());
                        totalBalance = totalBalance.add(accountTransaction.getAmount());
                    } else if(transactionType.equals(TransactionType.WITHDRAW) || (transactionType.equals(TransactionType.TRANSFER) && accountTransaction.getFrom().getAccountID() == account.getAccountID()))
                    {
                        depositBalance = depositBalance.subtract(accountTransaction.getAmount());
                        totalBalance = totalBalance.subtract(accountTransaction.getAmount());
                    }
                }
                account.setDepositBalance(depositBalance.setScale(2, RoundingMode.HALF_UP));
                account.setInterestBalance(interestBalance.setScale(2, RoundingMode.HALF_UP));
                account.setTotalBalance(totalBalance.setScale(2, RoundingMode.HALF_UP));
            } else {
                AccountType accountType = account.getAccountType();
                BigDecimal interestRate = accountType.getInterestRate().divide(new BigDecimal(100)); //30%, 0.3

                List<Transaction> accountTransactions = transactionDao.getASCTransactionsByAccount(account.getAccountID());
                LocalDate today = LocalDate.now();
                LocalDate processedUpTo = account.getOpeningDate();

                BigDecimal depositBalance = BigDecimal.ZERO;
                BigDecimal interestBalance = BigDecimal.ZERO;
                BigDecimal totalBalance = BigDecimal.ZERO;

                int monthsPeriod = compoundDurationInMonths.get(accountType.getCompoundRate());

                // 1 transaction
                for(Transaction accountTransaction : accountTransactions) {
                    LocalDate transactionDate = accountTransaction.getDateTime().toLocalDate();
                    TransactionType transactionType = accountTransaction.getTransactionType();
                    LocalDate compoundDate = processedUpTo.plusMonths(monthsPeriod);
                    // If the transaction is after the compound limit date
                    if(transactionDate.isAfter(compoundDate)){
                        long monthsBetween = ChronoUnit.MONTHS.between(processedUpTo, transactionDate);
                        BigDecimal interest =
                                totalBalance
                                        .multiply(BigDecimal.ONE.add(interestRate).pow(
                                                (int) (monthsBetween/monthsPeriod)
                                        ))
                                        .subtract(totalBalance) ;

                        interestBalance = interestBalance.add(interest);
                        totalBalance = totalBalance.add(interest);

                        processedUpTo = processedUpTo.plusMonths( monthsBetween/monthsPeriod* monthsPeriod);
                    }

                    if(transactionType.equals(TransactionType.DEPOSIT) ||
                            (transactionType.equals(TransactionType.TRANSFER) && accountTransaction.getTo().getAccountID() == account.getAccountID())) {
                        depositBalance = depositBalance.add(accountTransaction.getAmount());
                        totalBalance = totalBalance.add(accountTransaction.getAmount());

                    } else if(transactionType.equals(TransactionType.WITHDRAW) ||
                            (transactionType.equals(TransactionType.TRANSFER) && accountTransaction.getFrom().getAccountID() == account.getAccountID())){
                        depositBalance = depositBalance.subtract(accountTransaction.getAmount());
                        totalBalance = totalBalance.subtract(accountTransaction.getAmount());
                    }
                }

                //Calculate the last interest applied for the last transaction
                long monthsBetween =
                        ChronoUnit.MONTHS.between(processedUpTo,
                                // Only apply up to closing date, or today if account open
                                account.getStatus().equals(AccountStatus.CLOSED) ? account.getClosingDate() : today);
                BigDecimal interest =
                        totalBalance
                                .multiply(BigDecimal.ONE.add(interestRate).pow(
                                        (int) (monthsBetween/monthsPeriod)
                                ))
                                .subtract(totalBalance) ;

                interestBalance = interestBalance.add(interest);
                totalBalance = totalBalance.add(interest);
                //Set the properties
                account.setDepositBalance(depositBalance.setScale(2, RoundingMode.HALF_UP));
                account.setInterestBalance(interestBalance.setScale(2, RoundingMode.HALF_UP));
                account.setTotalBalance(totalBalance.setScale(2, RoundingMode.HALF_UP));

            }
            accountDao.updateAccount(account);
        }

    }

    private Map<CompoundRate, Integer> compoundDurationInMonths = Map.of(
            CompoundRate.QUARTERLY, 4,
            CompoundRate.MONTHLY, 1,
            CompoundRate.ANNUALLY,12,
            CompoundRate.SEMI_ANNUALLY, 6
    );

    private void calculateAndSetBalancesForAccountList(List<Account> accounts) {
        for(Account account : accounts) {
            calculateAndSetBalances(account);
        }
    }




}
