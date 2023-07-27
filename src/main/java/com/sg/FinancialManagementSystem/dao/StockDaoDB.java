package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.CompanyMapper;
import com.sg.FinancialManagementSystem.dao.mappers.ExchangeOrganizationMapper;
import com.sg.FinancialManagementSystem.dao.mappers.StockMapper;
import com.sg.FinancialManagementSystem.dto.Company;
import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-25
 */
@Repository
public class StockDaoDB implements StockDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Stock getStockByID(int stockID) {
        try {
            final String GET_STOCK_BY_ID = "SELECT * FROM Stock WHERE stockID = ?";
            Stock stock = jdbcTemplate.queryForObject(GET_STOCK_BY_ID, new StockMapper(), stockID);
            //Set the company and exchange organizations
            setCompanyAndEOsForStock(stock);
            setFormattedStockFields(stock);
            return stock;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Stock> getAllStocks() {
        final String GET_ALL_STOCKS = "SELECT * FROM Stock";

        List<Stock> stocks = jdbcTemplate.query(GET_ALL_STOCKS, new StockMapper());

        setCompanyAndEOsForStockList(stocks);
        setFormattedStockListFields(stocks);
        return stocks;
    }

    @Override
    public Stock addStock(Stock stock) {
        final String ADD_STOCK = "INSERT INTO Stock " +
                "(tickerCode, sharePrice, status, numberOfOutstandingShares, marketCap, dailyVolume, companyID) " +
                "VALUES (?,?,?,?,?,?,?);";

        jdbcTemplate.update(ADD_STOCK,
                stock.getTickerCode(),
                stock.getSharePrice(),
                stock.getStatus().toString(),
                stock.getNumberOfOutstandingShares(),
                stock.getMarketCap(),
                stock.getDailyVolume(),
                stock.getCompany().getCompanyID());

        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        stock.setStockID(newID);

        // insertStockExchangeOrganization
        insertStockExchangeOrganization(stock);
        return stock;
    }

    @Override
    public void updateStock(Stock stock) {
        final String UPDATE_STOCK = "UPDATE Stock SET " +
                "tickerCode = ?, sharePrice = ?, status = ?, numberOfOutstandingShares = ?, marketCap = ?, dailyVolume = ?, companyID = ? " +
                "WHERE stockID = ?";

        jdbcTemplate.update(UPDATE_STOCK,
                stock.getTickerCode(),
                stock.getSharePrice(),
                stock.getStatus().toString(),
                stock.getNumberOfOutstandingShares(),
                stock.getMarketCap(),
                stock.getDailyVolume(),
                stock.getCompany().getCompanyID(),  // Should not be able to modify company ID
                stock.getStockID()
        );
        setFormattedStockFields(stock);

        //SHOULD NOT BE ABLE TO MODIFY EOs
    }

    @Override
    public void deleteStockByID(int stockID) {
        //First delete the Portfolio Bridge table
        final String DELETE_PORTFOLIO_BRIDGE_BY_STOCK = "DELETE FROM PortfolioBridge WHERE stockID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_BRIDGE_BY_STOCK, stockID);

        //Then delete from the StockExchangeOrganization Bridge table
        final String DELETE_FROM_SEO_BY_STOCK = "DELETE FROM StockExchangeOrganization WHERE stockID = ?";
        jdbcTemplate.update(DELETE_FROM_SEO_BY_STOCK, stockID);

        //Then delete the stock
        final String DELETE_STOCK = "DELETE FROM Stock WHERE stockID = ?";
        jdbcTemplate.update(DELETE_STOCK, stockID);
    }

    @Override
    public Stock getStockByCompany(Company company) {
        final String GET_STOCK_BY_COMPANY = "SELECT * FROM Stock WHERE companyID = ?";
        Stock stock = jdbcTemplate.queryForObject(GET_STOCK_BY_COMPANY, new StockMapper(), company.getCompanyID());
        setCompanyAndEOsForStock(stock);
        setFormattedStockFields(stock);
        return stock;
    }

    @Override
    public List<Stock> getStocksByEo(ExchangeOrganization eo) {
        final String GET_STOCKS_BY_EO = "SELECT s.* FROM Stock s " +
                "JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID " +
                "JOIN ExchangeOrganization eo ON eo.exchangeOrganizationID = seo.exchangeOrganizationID " +
                "WHERE eo.exchangeOrganizationID = ?";

        List<Stock> stocks = jdbcTemplate.query(GET_STOCKS_BY_EO, new StockMapper(), eo.getExchangeOrganizationID());
        //Set the companies & eos for each stock
        setCompanyAndEOsForStockList(stocks);
        setFormattedStockListFields(stocks);
        return stocks;
    }

    @Override
    public List<Stock> getStocksByPortfolio(Portfolio portfolio) {
        final String GET_STOCKS_BY_PORTFOLIO = "SELECT DISTINCT s.* FROM Stock s " +
                "JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID " +
                "JOIN PortfolioBridge pb ON pb.stockID = seo.stockID " +
                "JOIN Portfolio p ON p.portfolioID = pb.portfolioID " +
                "WHERE p.portfolioID = ?";

        List<Stock> stocks = jdbcTemplate.query(GET_STOCKS_BY_PORTFOLIO, new StockMapper(), portfolio.getPortfolioID());
        //Set the companies & eos for each stock
        setCompanyAndEOsForStockList(stocks);
        setFormattedStockListFields(stocks);
        return stocks;
    }


    //PRIVATE HELPER FUNCTIONS
    private void setCompanyAndEOsForStock(Stock stock) {
        stock.setCompany(getCompanyByStock(stock));
        stock.setExchangeOrganizations(getExchangeOrganizationsByStock(stock));
    }

    private void setCompanyAndEOsForStockList(List<Stock> stocks) {
        for(Stock stock : stocks) {
            setCompanyAndEOsForStock(stock);
        }
    }

    private List<ExchangeOrganization> getExchangeOrganizationsByStock(Stock stock) {
        final String GET_EOs_BY_STOCK = "SELECT eo.* FROM ExchangeOrganization eo " +
                "JOIN StockExchangeOrganization seo ON seo.exchangeOrganizationID = eo.exchangeOrganizationID " +
                "JOIN Stock s ON s.stockID = seo.stockID " +
                "WHERE s.stockID = ?";

        List<ExchangeOrganization> eoList = jdbcTemplate.query(GET_EOs_BY_STOCK, new ExchangeOrganizationMapper(), stock.getStockID());

        return eoList.size() == 0 ? new ArrayList<>() : eoList;
    }

    private Company getCompanyByStock(Stock stock) {
        final String GET_COMPANY_BY_STOCK = "SELECT  c.* FROM Company c " +
                "JOIN Stock s on s.companyID = c.companyID " +
                "WHERE s.stockID = ?";

        Company company = jdbcTemplate.queryForObject(GET_COMPANY_BY_STOCK, new CompanyMapper(), stock.getStockID());

        return company;
    }

    private void insertStockExchangeOrganization(Stock stock) {
        if(stock.getExchangeOrganizations() != null) {
            final String INSERT_EO_STOCKS = "INSERT INTO "
                    + "StockExchangeOrganization (exchangeOrganizationID, stockID) VALUES (?,?);";
            int stockID = stock.getStockID();
            for(ExchangeOrganization eo : stock.getExchangeOrganizations()) { // Insert for each organization exchange stock.
                int eoID = eo.getExchangeOrganizationID();
                jdbcTemplate.update(INSERT_EO_STOCKS, eoID, stockID);
            }
        }
    }

    // FORMAT FIELDS
    private void setFormattedStockListFields(List<Stock>  stockList) {
        for(Stock stock: stockList) {
            setFormattedStockFields(stock);
        }
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
