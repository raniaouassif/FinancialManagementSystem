package com.sg.FinancialManagementSystem.controller;

import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.StockTransaction;
import com.sg.FinancialManagementSystem.service.CompanyService;
import com.sg.FinancialManagementSystem.service.StockTransactionService;
import com.sg.FinancialManagementSystem.service.StockTransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author raniaouassif on 2023-07-26
 */

@Controller
public class ApplicationController {
    @Autowired
    CompanyService companyService;

    @Autowired
    StockTransactionService stockTransactionService;
    @GetMapping("/")
    public String displayHomeView(Model model) {
        List<StockTransaction> stockTransactions = stockTransactionService.getAllStockTransactionsDescDatetime();

        model.addAttribute("stockTransactions",stockTransactions);
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
        model.addAttribute("companies", companies);
        return "companies";
    }
}
