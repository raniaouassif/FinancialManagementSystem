package com.sg.FinancialManagementSystem.controller;

import com.sg.FinancialManagementSystem.dto.*;
import com.sg.FinancialManagementSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/customers")
    public String displayCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers";
    }


}
