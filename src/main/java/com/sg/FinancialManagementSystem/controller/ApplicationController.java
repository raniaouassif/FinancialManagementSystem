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
import javax.sound.sampled.Port;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    TransactionValidationService transactionValidationService;
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
//        model.addAttribute("customerID", customerID);
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
        account.setDepositBalance(BigDecimal.valueOf(Integer.parseInt(depositBalance)));
        account.setBank(bankService.getBankByID(Integer.parseInt(bankID)));
        account.setAccountType(accountTypeService.getAccountTypeByID(Integer.parseInt(accountTypeID)));
        account.setCustomer(customer);

        // TODO
//        if (accountService.addAccount(account) == ) {
            //Pass the modified customer
//            model.addAttribute("account", account);
//            return "/customers-add";
//        }


        accountService.addAccount(account);
        return "redirect:/customer-detail?customerID="+customerID;
    }








    /*
     **********************************      BANK       ****************************************************************
     */
    @GetMapping("/banks")
    public String displayBanks(Model model) {
        List<Bank> banks = bankService.getAllBanks();
        model.addAttribute("banks", banks);
        return "banks";
    }

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
    public String editCustomer(Integer id, Model model) {
        Customer customer = customerService.getCustomerByID(id);
        model.addAttribute("customer", customer);
        return "customers-edit";
    }

    @PostMapping("editCustomer")
    public String performEditCustomer(@Valid Customer customer, BindingResult result, HttpServletRequest request, Model model) {
        //Check for errors
        if (result.hasErrors()) {
            //Pass the modified customer
            model.addAttribute("customer", customer);
            return "customers-edit";
        }
        customerService.updateCustomer(customer);
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

    @PostMapping("cashTransaction")
    public String performCashTransaction(@Valid Transaction transaction, BindingResult result, Integer accountID, HttpServletRequest request, Model model) throws InvalidDateException, InsufficientFundsException {
        //Get the account by ID
        Account account = accountService.getAccountByID(accountID);

        //Get the params
        String dateTime = request.getParameter("cashDateTime");
        String transactionType = request.getParameter("cashType");
        String amount = request.getParameter("cashAmount");

        //Set the params
        transaction.setDateTime(LocalDateTime.parse(dateTime).atZone(ZoneOffset.UTC).toLocalDateTime());
        transaction.setTransactionType(TransactionType.valueOf(transactionType));
        transaction.setAmount(new BigDecimal(amount));
        transaction.setFrom(account);
        transaction.setTo(account);

        String cashErr = transactionValidationService.validateCashTransaction(transaction);

        if(!cashErr.isEmpty()) {
            model.addAttribute("cashErr", cashErr);
            model.addAttribute("transaction", new Transaction());
            model.addAttribute("account", account);
            model.addAttribute("transactions", transactionService.getDESCTransactionsByAccounts(accountID));
            model.addAttribute("transferErr", "");

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
        transaction.setDateTime(LocalDateTime.parse(dateTime).atZone(ZoneOffset.UTC).toLocalDateTime());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setFrom(account);
        transaction.setTo(accountTo);

        String transferErr = transactionValidationService.validateTransferTransaction(transaction);

        if(!transferErr.isEmpty()) {
            model.addAttribute("transferErr", transferErr);
            model.addAttribute("transaction", new Transaction());
            model.addAttribute("account", account);
            model.addAttribute("cashErr", "");
            model.addAttribute("transactions", transactionService.getDESCTransactionsByAccounts(accountID));
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
