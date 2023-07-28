package com.sg.FinancialManagementSystem.controller;

import com.sg.FinancialManagementSystem.dto.*;
import com.sg.FinancialManagementSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
    BankService bankService;

    @Autowired
    AccountTypeService accountTypeService;
    @Autowired
    StockTransactionService stockTransactionService;
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

    @GetMapping("/stocks")
    public String displayStocks(Model model) {
        List<Stock> stocks = stockService.getAllStocks();

        model.addAttribute("stocks", stocks);
        return "stocks";
    }

    @GetMapping("/banks")
    public String displayBanks(Model model) {
        List<Bank> banks = bankService.getAllBanks();
        model.addAttribute("banks", banks);
        return "banks";
    }

    @GetMapping("/banks/detail")
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
        return "/bankDetail";
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

    @GetMapping("/customers")
    public String displayCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }

    //  PRIVATE HELPER METHODS
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

}
