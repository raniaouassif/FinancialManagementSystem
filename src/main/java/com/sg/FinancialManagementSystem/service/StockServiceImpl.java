package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.StockDao;
import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-27
 */
@Service
public class StockServiceImpl implements StockService{
    @Autowired
    StockDao stockDao;
    @Override
    public Stock getStockByID(int stockID) {
        return stockDao.getStockByID(stockID);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockDao.getAllStocks();
    }

    @Override
    public Stock addStock(Stock stock) {
        return stockDao.addStock(stock);
    }

    @Override
    public void updateStock(Stock stock) {
        stockDao.updateStock(stock);
    }

    @Override
    public void deleteStockByID(int stockID) {
        stockDao.deleteStockByID(stockID);
    }

    @Override
    public Stock getStockByCompany(Company company) {
        return stockDao.getStockByCompany(company);
    }

    @Override
    public List<Stock> getStocksByEo(ExchangeOrganization eo) {
        return stockDao.getStocksByEo(eo);
    }

    @Override
    public List<Stock> getStocksByPortfolio(Portfolio portfolio) {
        return stockDao.getStocksByPortfolio(portfolio);
    }

    private void setFormattedStockFields(Stock stock) {
        stock.setFormattedMarketCap(formatWithUnit(stock.getMarketCap()));
        stock.setFormattedDailyVolume(formatWithUnit(BigDecimal.valueOf(stock.getDailyVolume())));
        stock.setFormattedOutstandingShares(formatWithUnit(BigDecimal.valueOf(stock.getNumberOfOutstandingShares())));
    }

    private String formatWithUnit(BigDecimal value) {
        if (value.compareTo(BigDecimal.valueOf(1_000_000_000_000L)) >= 0) {
            return value.divide(BigDecimal.valueOf(1_000_000_000_000L)).setScale(2, BigDecimal.ROUND_HALF_UP) + "T";
        } else if (value.compareTo(BigDecimal.valueOf(1_000_000_000L)) >= 0) {
            return value.divide(BigDecimal.valueOf(1_000_000_000L)).setScale(2, BigDecimal.ROUND_HALF_UP) + "B";
        } else if (value.compareTo(BigDecimal.valueOf(1_000_000L)) >= 0) {
            return value.divide(BigDecimal.valueOf(1_000_000L)).setScale(2, BigDecimal.ROUND_HALF_UP) + "M";
        } else if (value.compareTo(BigDecimal.valueOf(1_000L)) >= 0) {
            return value.divide(BigDecimal.valueOf(1_000L)).setScale(2, BigDecimal.ROUND_HALF_UP) + "K";
        } else {
            return value.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
    }
}
