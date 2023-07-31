package com.sg.FinancialManagementSystem.controller;

import com.sg.FinancialManagementSystem.dto.*;
import com.sg.FinancialManagementSystem.service.*;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientFundsException;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientMinDepositException;
import com.sg.FinancialManagementSystem.service.Exceptions.InvalidBankAccountTypeException;
import com.sg.FinancialManagementSystem.service.Exceptions.InvalidDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author raniaouassif on 2023-07-26
 */

@Controller
public class ApplicationController {
    @Autowired
    CompanyService companyService;
    @Autowired
    CustomerService customerService;

    @Autowired
    ExchangeOrganizationService exchangeOrganizationService;
    @Autowired
    StockService stockService;

    @Autowired
    PortfolioService portfolioService;
    @Autowired
    BankService bankService;
    @Autowired
    AccountService accountService;

    @Autowired
    AccountTypeService accountTypeService;
    @Autowired
    StockTransactionService stockTransactionService;
    @Autowired
    TransactionService transactionService;

    @Autowired
    ValidationService validationService;


    @GetMapping("/")
    public String displayHomeView(Model model) {
        return "redirect:/latest-trades";
    }

    @GetMapping("/latest-trades")
    public String displayLatestTrades(Model model) {
        List<StockTransaction> stockTransactions = stockTransactionService.getAllStockTransactionsDescDatetime();
        model.addAttribute("stockTransactions",stockTransactions);
        return "latest-trades";
    }



    /*
     **********************************      ACCOUNT       ****************************************************************
     */


    @GetMapping("/account-add")
    public String addAccount(Integer customerID, Model model) {
        List<Bank> banks = bankService.getAllBanks();
        List<AccountType> accountTypes = accountTypeService.getAllAccountTypes();
        Customer customer = customerService.getCustomerByID(customerID);
        model.addAttribute("customer", customer);
        model.addAttribute("banks", banks);
        model.addAttribute("accountTypes", accountTypes);
        model.addAttribute("account", new Account());

        model.addAttribute("addError", "");// No error on landing page
        return "account-add";
    }

    @PostMapping("addCustomerAccount")
    public String performAddAccount(@Valid Account account, BindingResult result, Integer customerID, HttpServletRequest request, Model model) throws InsufficientMinDepositException, InvalidDateException, InvalidBankAccountTypeException {
        Customer customer = customerService.getCustomerByID(customerID);
        String bankID= request.getParameter("bankID");
        String accountTypeID = request.getParameter("accountTypeID");
        String depositBalance = request.getParameter("depositBalance");
        String openingDate = request.getParameter("openingDate");

        account.setOpeningDate(LocalDate.parse(openingDate));
        account.setDepositBalance(new BigDecimal(depositBalance));
        account.setBank(bankService.getBankByID(Integer.parseInt(bankID)));
        account.setAccountType(accountTypeService.getAccountTypeByID(Integer.parseInt(accountTypeID)));
        account.setCustomer(customer);

        //Validate the customer account
        String err = validationService.validateAddAccount(account);
        if(!err.isEmpty()){
            // Add the error
            model.addAttribute("addError", err);
            //And the other attributes
            model.addAttribute("customer", customer);
            model.addAttribute("banks",  bankService.getAllBanks());
            model.addAttribute("accountTypes", accountTypeService.getAllAccountTypes());
            model.addAttribute("account", account);
            return "account-add";
        }

        accountService.addAccount(account);
        return "redirect:/customer-detail?customerID="+customerID;
    }

    @GetMapping("/account-close")
    public String closeAccount(Integer accountID, Model model) {
        Account account = accountService.getAccountByID(accountID);

        model.addAttribute("account", account);
        return "account-close";
    }

    @PostMapping("closeCustomerAccount")
    public String performCloseAccount(@Valid Account account, BindingResult result, Integer customerID, HttpServletRequest request, Model model) {
        String closingReason = request.getParameter("closingReason");
        account = accountService.getAccountByID(account.getAccountID());
        account.setClosingReason(closingReason);
        account.setClosingDate(LocalDate.now());
        account.setStatus(AccountStatus.CLOSED);
        //Check for errors
        if (result.hasErrors()) {
            //Pass the modified account
            model.addAttribute("account", account);
            return "/account-close";
        }
        accountService.updateAccount(account);
        return "redirect:/customer-detail?customerID="+customerID;
    }

    @GetMapping("/account-delete")
    public String deleteAccount(Integer accountID) {
        Account account = accountService.getAccountByID(accountID);
        int customerID = account.getCustomer().getCustomerID();
        accountService.deleteAccountByID(accountID);
        return "redirect:/customer-detail?customerID="+customerID;
    }

    /*
     **********************************      ACCOUNT TYPES       ****************************************************************
     */
    @GetMapping("/account-types")
    public String displayAccountTypes(Model model) {
        List<AccountType> checkingAccountTypes = accountTypeService.getAllCheckingAccountTypes();
        List<AccountType> savingsAccountTypes = accountTypeService.getAllSavingsAccountTypes();

        //BankAccountType enum to list
        BankAccountType[] accountTypes = BankAccountType.values();
        List<BankAccountType> bankAccountTypeList = new ArrayList<>(Arrays.asList(accountTypes));
        bankAccountTypeList.remove(BankAccountType.CHECKING);


        //BankAccountType enum to list
        CompoundRate[] compoundRates = CompoundRate.values();
        List<CompoundRate> compoundRateList = new ArrayList<>(Arrays.asList(compoundRates));
        compoundRateList.remove(CompoundRate.NA);

        model.addAttribute("checkingAccountTypes", checkingAccountTypes);
        model.addAttribute("savingsAccountTypes", savingsAccountTypes);
        model.addAttribute("accountType", new AccountType());
        model.addAttribute("bankAccountTypes", bankAccountTypeList);
        model.addAttribute("compoundRates", compoundRateList);
        model.addAttribute("savingsError", "");
        model.addAttribute("checkingError", "");

        return "account-types";
    }

    @PostMapping("addCheckingAccountType")
    public String performAddCheckingAccountType(@Valid AccountType accountType, BindingResult result, HttpServletRequest request, Model model) {
        String minimumDeposit = request.getParameter("checkingMinimumStartDeposit");

        accountType.setType(BankAccountType.CHECKING);
        accountType.setMinimumStartDeposit(new BigDecimal(minimumDeposit));
        accountType.setInterestRate(BigDecimal.ZERO);
        accountType.setCompoundRate(CompoundRate.NA);

        String checkingError = validationService.validateAddCheckingAccountType(accountType);
        if(!checkingError.isEmpty()){
            //BankAccountType enum to list
            BankAccountType[] accountTypes = BankAccountType.values();
            List<BankAccountType> bankAccountTypeList = new ArrayList<>(Arrays.asList(accountTypes));
            bankAccountTypeList.remove(BankAccountType.CHECKING);


            //BankAccountType enum to list
            CompoundRate[] compoundRates = CompoundRate.values();
            List<CompoundRate> compoundRateList = new ArrayList<>(Arrays.asList(compoundRates));
            compoundRateList.remove(CompoundRate.NA);

            model.addAttribute("checkingAccountTypes", accountTypeService.getAllCheckingAccountTypes());
            model.addAttribute("savingsAccountTypes", accountTypeService.getAllSavingsAccountTypes());
            model.addAttribute("accountType", accountType);
            model.addAttribute("bankAccountTypes", bankAccountTypeList);
            model.addAttribute("compoundRates", compoundRateList);
            model.addAttribute("savingsError", "");
            model.addAttribute("checkingError", checkingError);

            return "account-types";
        }
        accountTypeService.addAccountType(accountType);
        return "redirect:/account-types";
    }

    @PostMapping("addSavingAccountType")
    public String performAddSavingsAccountType(@Valid AccountType accountType, BindingResult result, HttpServletRequest request, Model model) {
        String minimumDeposit = request.getParameter("savingMinimumStartDeposit");
        String interestRate = request.getParameter("savingInterestRate");
        String compoundRate = request.getParameter("compoundRate");
        String type = request.getParameter("bankAccountType");


        accountType.setType(BankAccountType.valueOf(type));
        accountType.setMinimumStartDeposit(new BigDecimal(minimumDeposit));
        accountType.setInterestRate(new BigDecimal(interestRate).setScale(2, RoundingMode.HALF_UP));
        accountType.setCompoundRate(CompoundRate.valueOf(compoundRate));

        String savingsError = validationService.validateAddSavingAccountType(accountType);
        if(!savingsError.isEmpty()){
            //BankAccountType enum to list
            BankAccountType[] accountTypes = BankAccountType.values();
            List<BankAccountType> bankAccountTypeList = new ArrayList<>(Arrays.asList(accountTypes));
            bankAccountTypeList.remove(BankAccountType.CHECKING);


            //BankAccountType enum to list
            CompoundRate[] compoundRates = CompoundRate.values();
            List<CompoundRate> compoundRateList = new ArrayList<>(Arrays.asList(compoundRates));
            compoundRateList.remove(CompoundRate.NA);

            model.addAttribute("checkingAccountTypes", accountTypeService.getAllCheckingAccountTypes());
            model.addAttribute("savingsAccountTypes", accountTypeService.getAllSavingsAccountTypes());
            model.addAttribute("accountType", accountType);
            model.addAttribute("bankAccountTypes", bankAccountTypeList);
            model.addAttribute("compoundRates", compoundRateList);
            model.addAttribute("savingsError", savingsError);
            model.addAttribute("checkingError", "");
            return "account-types";
        }
        accountTypeService.addAccountType(accountType);
        return "redirect:/account-types";
    }

    @GetMapping("/account-type-delete")
    public String deleteAccountType(Integer accountTypeID) {
        accountTypeService.deleteAccountTypeByID(accountTypeID);
        return "redirect:/account-types";
    }

    /*
     **********************************      BANK       ****************************************************************
     */

    @GetMapping("/bank-detail")
    public String bankDetail(Integer id, Model model) {
        Bank bank = bankService.getBankByID(id);
        List<AccountType> accountTypes = accountTypeService.getAccountTypesByBank(bank);
        List<Customer> customers = customerService.getCustomersByBank(bank);
        model.addAttribute("bank", bank);
        model.addAttribute("accountTypes", accountTypes);
        model.addAttribute("customers", customers);
        model.addAttribute("clientSinceDateMap", getClientSince(customers));
        model.addAttribute("customersNumAccounts", getAccountsForCustomers(customers, id));
        model.addAttribute("customersTransactions", getTransactionsForCustomers(customers, id));
        model.addAttribute("customersAggBalance", getABForCustomers(customers, id));
        return "bank-detail";
    }

    @GetMapping("/banks")
    public String displayBanks(Model model) {
        List<Bank> banks = bankService.getAllBanks();
        List<AccountType> accountTypes = accountTypeService.getAllAccountTypes();

        model.addAttribute("accountTypes", accountTypes);
        model.addAttribute("banks", banks);
        model.addAttribute("bank", new Bank());
        model.addAttribute("err", "");
        model.addAttribute("bankAccountTypesIDs", new ArrayList<>());

        return "banks";
    }

    @PostMapping("addBank")
    public String performAddBank(@Valid Bank bank, BindingResult result, HttpServletRequest request, Model model) {
        String[] accountTypesIDs = request.getParameterValues("accountTypeID");
        List<AccountType> reqAccountTypes = new ArrayList<>();
        for(String accountTypeID :accountTypesIDs ) {
            reqAccountTypes.add(accountTypeService.getAccountTypeByID(Integer.parseInt(accountTypeID)));
        }

        bank.setAccountTypes(reqAccountTypes.size() == 0 ? new ArrayList<>() : reqAccountTypes);

        String err = validationService.validateAddBank(bank);

        //Check for errors
        if (result.hasErrors() || !err.isEmpty()) {
            //Create a list of bank account types IDS for the HTML  select
            List<Integer> bankAccountTypesIDs = bank.getAccountTypes().stream()
                    .map(AccountType::getAccountTypeID) // Map each Organization to its ID
                    .collect(Collectors.toList()); // Collect the IDs into a new list
            model.addAttribute("bankAccountTypesIDs", bankAccountTypesIDs);

            model.addAttribute("accountTypes", accountTypeService.getAllAccountTypes());
            model.addAttribute("banks", bankService.getAllBanks());
            model.addAttribute("bank", bank);
            model.addAttribute("err", err);

            //Pass the modified customer
            model.addAttribute("bank", bank);
            return "/banks";
        }

        bankService.addBank(bank);
        return "redirect:/banks";
    }

    @GetMapping("/banks-edit")
    public String editBank(Integer bankID, Model model) {
        Bank bank = bankService.getBankByID(bankID);
        //Create a list of bank account types IDS for the HTML  select
        List<Integer> bankAccountTypesIDs = bank.getAccountTypes().stream()
                .map(AccountType::getAccountTypeID) // Map each Organization to its ID
                .collect(Collectors.toList()); // Collect the IDs into a new list
        model.addAttribute("bankAccountTypesIDs", bankAccountTypesIDs);

        model.addAttribute("bank", bank);
        List<AccountType> accountTypes = accountTypeService.getAllAccountTypes();
        model.addAttribute("accountTypes", accountTypes);
        model.addAttribute("err", "");
        return "banks-edit";
    }

    @PostMapping("editBank")
    public String performEditBank(@Valid Bank bank, BindingResult result, Integer bankID, HttpServletRequest request, Model model) {
        Bank bankPrev = bankService.getBankByID(bankID);
        //Create a list of bank account types IDS for the HTML  select
        List<Integer> bankPrevAccountTypesIDs = bankPrev.getAccountTypes().stream()
                .map(AccountType::getAccountTypeID) // Map each Organization to its ID
                .collect(Collectors.toList()); // Collect the IDs into a new list


        String[] accountTypesIDs = request.getParameterValues("accountTypeID");
        List<AccountType> reqAccountTypes = new ArrayList<>();
        for(String accountTypeID :accountTypesIDs ) {
            //Skip the already selected accounts, which cannot be removed
            if(bankPrevAccountTypesIDs.contains(Integer.parseInt(accountTypeID))){
                continue;
            }
            reqAccountTypes.add(accountTypeService.getAccountTypeByID(Integer.parseInt(accountTypeID)));
        }
        bank.setAccountTypes(reqAccountTypes.size() == 0 ? new ArrayList<>() : reqAccountTypes);
        //Check for errors
        if (result.hasErrors()) {
            //Pass the modified customer
            model.addAttribute("bank", bank);
            return "banks-edit";
        }
        bankService.updateBank(bank);
        return "redirect:/banks";
    }


    /*
     **********************************      COMPANIES    ***********************************************************
     */
    @GetMapping("/companies")
    public String displayCompanies(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        List<ExchangeOrganization> exchangeOrganizations = exchangeOrganizationService.getAllExchangeOrganizations();
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("companies", companies);
        model.addAttribute("stocks", stocks);
        model.addAttribute("exchangeOrganizations", exchangeOrganizations);
        return "companies";
    }

    /*
     **********************************      CUSTOMERS       ***********************************************************
     */
    @GetMapping("/customers")
    public String displayCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping("/customer-detail")
    public String displayCustomerDetails(Integer customerID, Model model) {
        Customer customer = customerService.getCustomerByID(customerID);
        Portfolio portfolio = portfolioService.getPortfolioByCustomer(customerID);

        List<Account> openAccounts = accountService.getOpenAccountsByCustomer(customer);
        List<Account> closedAccounts = accountService.getClosedAccountsByCustomer(customer);
        model.addAttribute("customer", customer);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("openAccounts", openAccounts);
        model.addAttribute("closedAccounts", closedAccounts);
        model.addAttribute("addError", "");
        return "customer-detail";
    }

    @GetMapping("/customers-add")
    public String addCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers-add";
    }

    @PostMapping("addCustomer")
    public String performAddCustomer(@Valid Customer customer, BindingResult result, HttpServletRequest request, Model model) {
        //Check for errors
        if (result.hasErrors()) {
            //Pass the modified customer
            model.addAttribute("customer", customer);
            return "/customers-add";
        }
        customerService.addCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/customers-edit")
    public String editCustomer(Integer customerID, Model model) {
        Customer customer = customerService.getCustomerByID(customerID);
        model.addAttribute("customer", customer);
        return "customers-edit";
    }

    @PostMapping("editCustomer")
    public String performEditCustomer(@Valid Customer customer, BindingResult result, HttpServletRequest request, Model model) {
        //Check for errors
        if (result.hasErrors()) {
            //Pass the modified customer
            model.addAttribute("customer", customer);
            return "/customers-edit";
        }
        customerService.updateCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/customers-delete")
    public String deleteCustomer(Integer customerID) {
        customerService.deleteCustomerByID(customerID);
        return "redirect:/customers";
    }

    /*
     **********************************      STOCK EXCHANGE  ****************************************************************
     */

    @GetMapping("/stock-exchanges")
    public String displayStockExchanges(Model model) {
        List<Company> companies = companyService.getAllCompanies();
        List<ExchangeOrganization> exchangeOrganizations = exchangeOrganizationService.getAllExchangeOrganizations();
        List<Stock> stocks = stockService.getAllStocks();

        model.addAttribute("companies", companies);
        model.addAttribute("stocks", stocks);
        model.addAttribute("exchangeOrganizations", exchangeOrganizations);
        return "stock-exchanges";
    }

    @GetMapping("/stock-exchanges-add")
    public String addStockExchange(Model model) {
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("exchangeOrganization", new ExchangeOrganization());
        model.addAttribute("stocks", stocks);
        return "stock-exchanges-add";
    }

    @PostMapping("addStockExchange")
    public String performAddStockExchange(@Valid ExchangeOrganization exchangeOrganization, BindingResult result, HttpServletRequest request, Model model) {
        //StockEx object already coming with set ticker and name
        //Only need to set stocks
        String[] stockIDs = request.getParameterValues("stockID");

        //Create a list of stock exchange stocks
        List<Stock> SEstocks = new ArrayList<>();
        if(stockIDs != null) {
            for(String stockID : stockIDs) {
                SEstocks.add(stockService.getStockByID(Integer.parseInt(stockID)));
            }
        }
        exchangeOrganization.setStocks(SEstocks);

        //Check for errors
        if(result.hasErrors()) {
            //Pass the modified stock ex
            model.addAttribute("exchangeOrganization", exchangeOrganization);
            model.addAttribute("stocks", stockService.getAllStocks());
            return "/stock-exchanges-add";
        }
        exchangeOrganizationService.addExchangeOrganization(exchangeOrganization);
        return "redirect:/stock-exchanges";
    }
    /*
     **********************************      STOCK       ****************************************************************
     */
    @GetMapping("/stocks")
    public String displayStocks(Model model) {
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("stocks", stocks);
        return "stocks";
    }

    /*
     **********************************      TRANSACTIONS       ****************************************************************
     */

    @GetMapping("/transactions-account")
    public String displayAccountTransactions(Integer accountID, Model model) {
        Account account = accountService.getAccountByID(accountID);
        List<Transaction> transactions = transactionService.getDESCTransactionsByAccounts(accountID);
        List<Account> transferToAccounts = accountService.getAllAccounts();
        //remove the current account from the list
        transferToAccounts.remove(account);

        //For Add transaction forms
        List<Account> accounts = accountService.getAllAccounts();
        accounts.remove(account); // remove the current account from the list
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        model.addAttribute("cashErr", "");
        model.addAttribute("transferErr", "");
        model.addAttribute("transferToAccounts", transferToAccounts);

        return "transactions-account";
    }

    @GetMapping("/transactions")
    public String displayAllTransactions(Integer accountID, Model model) {
        Account account = accountService.getAccountByID(accountID);
        List<Transaction> transactions = transactionService.geDESCTransactions();
        List<Account> transferToAccounts = accountService.getAllAccounts();
        //remove the current account from the list
        transferToAccounts.remove(account);

        //For Add transaction forms
        List<Account> accounts = accountService.getAllAccounts();
        accounts.remove(account); // remove the current account from the list
        model.addAttribute("transactions", transactions);
        model.addAttribute("transferToAccounts", transferToAccounts);

        return "transactions";
    }

    @PostMapping("cashTransaction")
    public String performCashTransaction(@Valid Transaction transaction, Integer accountID, HttpServletRequest request, Model model) throws InvalidDateException, InsufficientFundsException {
        //Get the account by ID
        Account account = accountService.getAccountByID(accountID);

        //Get the params
        String dateTime = request.getParameter("cashDateTime");
        String transactionType = request.getParameter("cashType");
        String amount = request.getParameter("cashAmount");

        //Set the params
        transaction.setDateTime(LocalDateTime.parse(dateTime));
        transaction.setTransactionType(TransactionType.valueOf(transactionType));
        transaction.setAmount(new BigDecimal(amount));
        transaction.setFrom(account);
        transaction.setTo(account);

        String cashErr = validationService.validateCashTransaction(transaction);

        if(!cashErr.isEmpty()) {
            List<Account> transferToAccounts = accountService.getAllAccounts();
            //remove the current account from the list
            transferToAccounts.remove(account);

            model.addAttribute("cashErr", cashErr);
            model.addAttribute("transaction", transaction);
            model.addAttribute("account", account);
            model.addAttribute("transactions", transactionService.getDESCTransactionsByAccounts(accountID));
            model.addAttribute("transferErr", "");
            model.addAttribute("transferToAccounts", transferToAccounts);


            return "transactions-account";
        }

        transactionService.addTransaction(transaction);
        return "redirect:/transactions-account?accountID="+accountID;
    }

    @PostMapping("transferTransaction")
    public String performTransferTransaction(@Valid Transaction transaction, BindingResult result, Integer accountID, HttpServletRequest request, Model model) throws InvalidDateException, InsufficientFundsException {
        //Get the account by ID
        Account account = accountService.getAccountByID(accountID);

        //Get the params
        String dateTime = request.getParameter("transferDateTime");
        String toAccount = request.getParameter("to");
        String amount = request.getParameter("transferAmount");

        //Get account to
        Account accountTo = accountService.getAccountByID(Integer.parseInt(toAccount));
        //Set the params
        transaction.setDateTime(LocalDateTime.parse(dateTime));
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setFrom(account);
        transaction.setTo(accountTo);

        String transferErr = validationService.validateTransferTransaction(transaction);

        if(!transferErr.isEmpty()) {
            List<Account> transferToAccounts = accountService.getAllAccounts();
            //remove the current account from the list
            transferToAccounts.remove(account);

            model.addAttribute("transferErr", transferErr);
            model.addAttribute("transaction", transaction);
            model.addAttribute("account", account);
            model.addAttribute("cashErr", "");
            model.addAttribute("transactions", transactionService.getDESCTransactionsByAccounts(accountID));
            model.addAttribute("transferToAccounts", transferToAccounts);

            return "transactions-account";
        }

        transactionService.addTransaction(transaction);
        return "redirect:/transactions-account?accountID="+accountID;
    }

    /*
     **********************************      PORTFOLIO       ****************************************************************
     */

    @GetMapping("/customer-portfolio-detail")
    public String displayCustomerPortfolioDetail(Integer customerID, Model model) {
        Customer customer = customerService.getCustomerByID(customerID);
        Portfolio portfolio = portfolioService.getPortfolioByCustomer(customerID);

        List<Account> openAccounts = accountService.getOpenAccountsByCustomer(customer);
        List<Account> closedAccounts = accountService.getClosedAccountsByCustomer(customer);
        model.addAttribute("customer", customer);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("openAccounts", openAccounts);
        model.addAttribute("closedAccounts", closedAccounts);

        return "customer-portfolio-detail";
    }

    //  PRIVATE HELPER METHODS

    /*
     *  Gets the aggregate balance for a list of customer of a given bank
     */
    private Map<Integer, BigDecimal> getABForCustomers(List<Customer> customers, int bankID) {
        Map<Integer, BigDecimal> aggBalanceMap = new HashMap<>();
        for (Customer customer : customers) {
            BigDecimal aggregateBalance =new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

            for (Account account : customer.getAccounts()) {
                if (account.getBank().getBankID() == bankID) {
                    aggregateBalance = aggregateBalance.add(account.getTotalBalance());
                }
            }
            aggBalanceMap.put(customer.getCustomerID(), aggregateBalance);
        }
        return aggBalanceMap;
    }

    /*
     *  Gets the number of accounts for a list of customer of a given bank
     */
    private Map<Integer, Integer> getAccountsForCustomers(List<Customer> customers, int bankID) {
        Map<Integer, Integer> accountMap = new HashMap<>();
        for(Customer customer : customers){
            int accountCount = customer
                    .getAccounts()
                    .stream()
                    .filter(account -> account.getBank().getBankID() == bankID )
                    .collect(Collectors.toList())
                    .size();

            accountMap.put(customer.getCustomerID(), accountCount);
        }
        return accountMap;
    }

    /*
     *  Gets the total number of transactions for a list of customer of a given bank
     */
    private Map<Integer, Integer> getTransactionsForCustomers(List<Customer> customers, int bankID) {
        Map<Integer, Integer> transactionsMap = new HashMap<>();
        for(Customer customer : customers){
            int totalTransactions = 0;
            List<Account> customerAccounts =  customer
                    .getAccounts()
                    .stream()
                    .filter(account -> account.getBank().getBankID() == bankID )
                    .collect(Collectors.toList());
            for(Account account : customerAccounts) {
                totalTransactions += account.getAccountTransactions().size();
            }
            transactionsMap.put(customer.getCustomerID(), totalTransactions);
        }
        return transactionsMap;
    }

    private Map<Integer, LocalDate> getClientSince(List<Customer> customers) {
        Map<Integer, LocalDate> clientSinceMap = new HashMap<>();
        for(Customer customer : customers) {
            LocalDate minDate = customer.getAccounts().get(0).getOpeningDate();
            for(Account account : customer.getAccounts() ) {
                if(minDate.isAfter(account.getOpeningDate())) {
                    minDate = account.getOpeningDate();
                }
            }
            clientSinceMap.put(customer.getCustomerID(), minDate);
        }
        return clientSinceMap;
    }

}
