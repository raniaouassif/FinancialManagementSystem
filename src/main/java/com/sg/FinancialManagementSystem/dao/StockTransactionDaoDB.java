package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.*;
import com.sg.FinancialManagementSystem.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-26
 */
@Repository
public class StockTransactionDaoDB implements StockTransactionDao{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public StockTransaction getStockTransactionByID(int stockTransactionID) {
        try {
            final String GET_ST_BY_ID = "SELECT * FROM StockTransaction WHERE stockTransactionID = ?";
            StockTransaction st = jdbcTemplate.queryForObject(GET_ST_BY_ID, new StockTransactionMapper(), stockTransactionID);
            // Set the stock, exchange organization and portfolio
            setStockEOandPortfolioForSt(st);
            return st;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<StockTransaction> getAllStockTransactions() {
        final String GET_ALL_ST = "SELECT * FROM StockTransaction";
        List<StockTransaction> stList = jdbcTemplate.query(GET_ALL_ST, new StockTransactionMapper());
        setStockEOandPortfolioForStList(stList);
        return stList;
    }

    @Override
    public List<StockTransaction> getAllStockTransactionsDescDatetime() {
        final String GET_ALL_ST = "SELECT * FROM StockTransaction ORDER BY dateTime DESC";
        List<StockTransaction> stList = jdbcTemplate.query(GET_ALL_ST, new StockTransactionMapper());
        setStockEOandPortfolioForStList(stList);
        return stList;
    }

    @Override
    public StockTransaction addStockTransaction(StockTransaction st) {
        final String ADD_ST = "INSERT INTO StockTransaction (dateTime, transactionType, numberOfShares, transactionCost) " +
                "VALUES (?,?,?,?)";

        jdbcTemplate.update(ADD_ST,
                Timestamp.valueOf(st.getDateTime()),
                st.getType().toString(),
                st.getNumberOfShares(),
                st.getTransactionCost());

        //Retrieve ID and set it
        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        st.setStockTransactionID(newID);

        //INSERT INTO THE BRIDGE TABLE
        insertNewTransaction(st);
        return st;
    }

    @Override
    public void updateStockTransaction(StockTransaction st) {
        final String UPDATE_ST = "UPDATE StockTransaction SET " +
                "dateTime=?, transactionType=?, numberOfShares=?, transactionCost = ? " +
                "WHERE stockTransactionID = ?";

        jdbcTemplate.update(UPDATE_ST,
                Timestamp.valueOf(st.getDateTime()),
                st.getType().toString(),
                st.getNumberOfShares(),
                st.getTransactionCost(),
                st.getStockTransactionID());
        //CAN'T MODIFY TRANSACTION STOCK, EXCHANGE ORG, PORTFOLIO AFTER BEING ADDED
    }

    @Override
    public void deleteStockTransactionByID(int stockTransactionID) {
        // First delete from bridge table PortfolioBridge
        final String DELETE_PORTFOLIO_BRIDGE = "DELETE FROM PortfolioBridge WHERE stockTransactionID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_BRIDGE, stockTransactionID);

        //Then delete the transaction
        final String DELETE_ST = "DELETE FROM StockTransaction WHERE stockTransactionID = ?";
        jdbcTemplate.update(DELETE_ST, stockTransactionID);
    }

    @Override
    public List<StockTransaction> getStockTransactionsByPortfolio(Portfolio portfolio) {
        final String GET_STs_BY_PORTFOLIO_STOCK = "SELECT st.* FROM StockTransaction st " +
                "JOIN PortfolioBridge pb ON pb.stockTransactionID = st.stockTransactionID " +
                "JOIN Portfolio p ON p.portfolioID = pb.portfolioID " +
                "WHERE p.portfolioID = ?";

        List<StockTransaction> stockTransactionList = jdbcTemplate.query(GET_STs_BY_PORTFOLIO_STOCK, new StockTransactionMapper(), portfolio.getPortfolioID());

        setStockEOandPortfolioForStList(stockTransactionList);

        return stockTransactionList;
    }

    @Override
    public List<StockTransaction> getStockTransactionByStock(Stock stock) {
        final String GET_STs_BY_STOCK = "SELECT st.* FROM stocktransaction st " +
                "JOIN PortfolioBridge pb ON st.stockTransactionID = pb.stockTransactionID " +
                "JOIN Stock s ON pb.StockID = s.StockID " +
                "WHERE s.stockID = ?";

        List<StockTransaction> stockTransactionList = jdbcTemplate.query(GET_STs_BY_STOCK, new StockTransactionMapper(), stock.getStockID());

        setStockEOandPortfolioForStList(stockTransactionList);

        return stockTransactionList;
    }

    //PRIVATE HELPER FUNCTIONS
    private Stock getStockByST(int stockTransactionID) {
        try {
            final String GET_STOCK_BY_ST = "SELECT s.* FROM Stock s " +
                    "JOIN PortfolioBridge pb ON pb.stockID = s.stockID " +
                    "JOIN StockTransaction st ON st.stockTransactionID = pb.stockTransactionID " +
                    "WHERE st.stockTransactionID = ?";
            Stock stock = jdbcTemplate.queryForObject(GET_STOCK_BY_ST, new StockMapper(), stockTransactionID);
            setCompanyAndEOsForStock(stock);
            return stock;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Portfolio getPortfolioByST(int stockTransactionID) {
        try {
            final String GET_PORTFOLIO_BY_ST = "SELECT p.* FROM Portfolio p " +
                    "JOIN PortfolioBridge pb ON pb.portfolioID = p.portfolioID " +
                    "JOIN StockTransaction st ON st.stockTransactionID = pb.stockTransactionID " +
                    "WHERE st.stockTransactionID = ?";

            return jdbcTemplate.queryForObject(GET_PORTFOLIO_BY_ST, new PortfolioMapper(), stockTransactionID);
        } catch (DataAccessException e) {
            return null;
        }
    }

    private ExchangeOrganization getEOByST(int stockTransactionID) {
        try {
            final String GET_EO_BY_ST = "SELECT eo.* FROM ExchangeOrganization eo " +
                    "JOIN PortfolioBridge pb ON pb.exchangeOrganizationID = eo.exchangeOrganizationID " +
                    "JOIN StockTransaction st ON st.stockTransactionID = pb.stockTransactionID " +
                    "WHERE st.stockTransactionID = ?";

            return jdbcTemplate.queryForObject(GET_EO_BY_ST, new ExchangeOrganizationMapper(), stockTransactionID);
        } catch (DataAccessException e) {
            return null;
        }
    }

    private void setStockEOandPortfolioForSt(StockTransaction st) {
        int stockTransactionID = st.getStockTransactionID();
        st.setStock(getStockByST(stockTransactionID));
        st.setEo(getEOByST(stockTransactionID));
        st.setPortfolio(getPortfolioByST(stockTransactionID));
    }


    private void setStockEOandPortfolioForStList(List<StockTransaction> stList) {
        for(StockTransaction st : stList) {
            setStockEOandPortfolioForSt(st);
        }
    }

    private void insertNewTransaction(StockTransaction st) {
        if(st.getStock() != null && st.getPortfolio() != null && st.getEo() != null) {
            final String INSERT_NEW_TRANSACTION = "INSERT INTO PortfolioBridge " +
                    "(portfolioID, stockID, exchangeOrganizationID, stockTransactionID) " +
                    "VALUES (?,?,?,?)";
            jdbcTemplate.update(INSERT_NEW_TRANSACTION,
                    st.getPortfolio().getPortfolioID(),
                    st.getStock().getStockID(),
                    st.getEo().getExchangeOrganizationID(),
                    st.getStockTransactionID());
        }
    }


    // FROM STOCK
    private void setCompanyAndEOsForStock(Stock stock) {
        stock.setCompany(getCompanyByStock(stock));
        stock.setExchangeOrganizations(getExchangeOrganizationsByStock(stock));
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
}
