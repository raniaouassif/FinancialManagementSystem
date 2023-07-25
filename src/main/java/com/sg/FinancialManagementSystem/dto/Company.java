package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Company {
    private int companyID;
    private String name;
    private String industry;
    private CompanyStatus status;
    private BigDecimal revenue;
    private BigDecimal profit;
    private BigDecimal grossMargin;
    private BigDecimal cashFlow;
    private Stock stock;

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(BigDecimal grossMargin) {
        this.grossMargin = grossMargin;
    }

    public BigDecimal getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(BigDecimal cashFlow) {
        this.cashFlow = cashFlow;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company company = (Company) o;
        return getCompanyID() == company.getCompanyID() && Objects.equals(getName(), company.getName()) && Objects.equals(getIndustry(), company.getIndustry()) && getStatus() == company.getStatus() && Objects.equals(getRevenue(), company.getRevenue()) && Objects.equals(getProfit(), company.getProfit()) && Objects.equals(getGrossMargin(), company.getGrossMargin()) && Objects.equals(getCashFlow(), company.getCashFlow()) && Objects.equals(getStock(), company.getStock());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCompanyID(), getName(), getIndustry(), getStatus(), getRevenue(), getProfit(), getGrossMargin(), getCashFlow(), getStock());
    }
}
