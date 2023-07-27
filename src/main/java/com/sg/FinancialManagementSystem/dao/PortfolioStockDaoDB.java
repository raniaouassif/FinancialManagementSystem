package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.PortfolioMapper;
import com.sg.FinancialManagementSystem.dao.mappers.PortfolioStockMapper;
import com.sg.FinancialManagementSystem.dao.mappers.StockMapper;
import com.sg.FinancialManagementSystem.dao.mappers.StockTransactionMapper;
import com.sg.FinancialManagementSystem.dto.Portfolio;
import com.sg.FinancialManagementSystem.dto.PortfolioStock;
import com.sg.FinancialManagementSystem.dto.Stock;
import com.sg.FinancialManagementSystem.dto.StockTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-25
 */
@Repository
public class PortfolioStockDaoDB implements PortfolioStockDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public PortfolioStock getPortfolioStockByID(int portfolioStockID) {
        try {
            final String GET_PS_BY_ID = "SELECT * FROM PortfolioStock WHERE portfolioStockID = ?";
            PortfolioStock portfolioStock = jdbcTemplate.queryForObject(GET_PS_BY_ID, new PortfolioStockMapper(), portfolioStockID);

            //SET STOCK AND PORTFOLIO
            setStockAndPortfolioAndTransactionsByPortfolioStock(portfolioStock);

            return portfolioStock;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<PortfolioStock> getAllPortfolioStocks() {
        final String GET_ALL_PS = "SELECT * FROM PortfolioStock ";
        List<PortfolioStock> portfolioStocks = jdbcTemplate.query(GET_ALL_PS, new PortfolioStockMapper());
        //SET STOCK AND PORTFOLIO FOR EACH STOCK
        setStockAndPortfolioAndTransactionsByPortfolioStockList(portfolioStocks);
        return portfolioStocks;
    }

    @Override
    public PortfolioStock addPortfolioStock(PortfolioStock portfolioStock) {
        final String ADD_PS = "INSERT INTO PortfolioStock " +
                "(numberOfShares,marketValue,bookValue,averagePrice,totalReturn,percentageReturn,portfolioID,stockID) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(ADD_PS,
                0,
                new BigDecimal(0.00),
                new BigDecimal(0.00),
                new BigDecimal(0.00),
                new BigDecimal(0.00),
                new BigDecimal(0.00),
                portfolioStock.getPortfolio().getPortfolioID(),
                portfolioStock.getStock().getStockID()
                );
        //Retrieve the int
        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        //Set the id
        portfolioStock.setPortfolioStockID(newID);

        //Set the other fields
        portfolioStock.setNumberOfShares(0);
        portfolioStock.setMarketValue(new BigDecimal(0.00));
        portfolioStock.setBookValue(new BigDecimal(0.00));
        portfolioStock.setAveragePrice(new BigDecimal(0.00));
        portfolioStock.setTotalReturn(new BigDecimal(0.00));
        portfolioStock.setPercentageReturn(new BigDecimal(0.00));

        return portfolioStock;
    }

    @Override
    public void updatePortfolioStock(PortfolioStock portfolioStock) {
        final String UPDATE_PS = "UPDATE PortfolioStock SET numberOfShares=?, marketValue=?, bookValue=?, " +
                "averagePrice=?, totalReturn=?, percentageReturn=?, portfolioID=?, stockID=? " +
                "WHERE portfolioStockID = ?";

        jdbcTemplate.update(UPDATE_PS,
                portfolioStock.getNumberOfShares(),
                portfolioStock.getMarketValue(),
                portfolioStock.getBookValue(),
                portfolioStock.getAveragePrice(),
                portfolioStock.getTotalReturn(),
                portfolioStock.getPercentageReturn(),
                portfolioStock.getPortfolio().getPortfolioID(),
                portfolioStock.getStock().getStockID(),
                portfolioStock.getPortfolioStockID());
    }

    @Override
    public void deletePortfolioStockByID(int portfolioStockID) {
        //Delete stock
        final String DELETE_PS_BY_ID = "DELETE FROM PortfolioStock WHERE portfolioStockID = ?";
        jdbcTemplate.update(DELETE_PS_BY_ID, portfolioStockID);
    }

    @Override
    public List<PortfolioStock> getPortfolioStocksByPortfolio(Portfolio portfolio) {

        final String GET_PS_BY_PORTFOLIO = "SELECT * FROM PortfolioStock WHERE portfolioID = ?";
        List<PortfolioStock> portfolioStockList =
                jdbcTemplate.query(GET_PS_BY_PORTFOLIO, new PortfolioStockMapper(), portfolio.getPortfolioID());
        //SET STOCK AND PORTFOLIO FOR EACH STOCK
        setStockAndPortfolioAndTransactionsByPortfolioStockList(portfolioStockList);

        return portfolioStockList;
    }

    //PRIVATE HELPER FUNCTIONS
    private void setStockAndPortfolioAndTransactionsByPortfolioStockList(List<PortfolioStock> portfolioStockList) {
        for(PortfolioStock ps : portfolioStockList) {
            setStockAndPortfolioAndTransactionsByPortfolioStock(ps);
        }
    }

    private void setStockAndPortfolioAndTransactionsByPortfolioStock(PortfolioStock portfolioStock) {
        int portfolioStockID = portfolioStock.getPortfolioStockID();
        portfolioStock.setPortfolio(getPortfolioByPorfolioStock(portfolioStockID));
        portfolioStock.setStock(getStockByPortfolioStock(portfolioStockID));
        portfolioStock.setStockTransactionList(getStockTransactionByPortfolioStock(portfolioStockID));
    }

    private List<StockTransaction> getStockTransactionByPortfolioStock(int portfolioStockID) {
        final String GET_STs_BY_PORTFOLIO_STOCK = "SELECT st.* FROM stocktransaction st " +
                "JOIN PortfolioBridge pb ON st.stockTransactionID = pb.stockTransactionID " +
                "JOIN PortfolioStock ps ON pb.portfolioID = ps.portfolioID AND pb.stockID = ps.stockID " +
                "WHERE ps.portfoliostockID = ?";

        List<StockTransaction> stockTransactionList = jdbcTemplate.query(GET_STs_BY_PORTFOLIO_STOCK, new StockTransactionMapper(),portfolioStockID);
        return stockTransactionList;
    }

    private Stock getStockByPortfolioStock(int portfolioStockID) {
        try {
            final String GET_STOCK = "SELECT s.* FROM Stock s " +
                    "JOIN PortfolioStock ps ON ps.stockID = s.stockID " +
                    "WHERE ps.portfolioStockID = ?";
            Stock stock = jdbcTemplate.queryForObject(GET_STOCK, new StockMapper(), portfolioStockID);
            // TODO SET ?
            return stock;
        } catch (DataAccessException e) {
            return null;
        }
    }

    private Portfolio getPortfolioByPorfolioStock(int portfolioStockID) {
        try {
            final String GET_PORTFOLIO = "SELECT p.* FROM Portfolio p " +
                    "JOIN PortfolioStock ps ON ps.portfolioID = p.portfolioID " +
                    "WHERE ps.portfolioStockID = ?";

            Portfolio portfolio = jdbcTemplate.queryForObject(GET_PORTFOLIO, new PortfolioMapper(), portfolioStockID);
            return portfolio;
        } catch (DataAccessException e) {
            return null;
        }
    }
}
