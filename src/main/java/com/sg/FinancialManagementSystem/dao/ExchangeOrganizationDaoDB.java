package com.sg.FinancialManagementSystem.dao;

import com.sg.FinancialManagementSystem.dao.mappers.ExchangeOrganizationMapper;
import com.sg.FinancialManagementSystem.dao.mappers.StockMapper;
import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */
@Repository
public class ExchangeOrganizationDaoDB implements ExchangeOrganizationDao{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public ExchangeOrganization getExchangeOrganizationByID(int exchangeOrganizationID) {
        final String GET_EO_BY_ID = "SELECT * FROM ExchangeOrganization WHERE exchangeOrganizationID = ?;";

        ExchangeOrganization eo = jdbcTemplate.queryForObject(GET_EO_BY_ID, new ExchangeOrganizationMapper(), exchangeOrganizationID);

        eo.setStocks(getStocksByExchangeOrganization(eo));

        return eo;
    }
    @Override
    public List<ExchangeOrganization> getAllExchangeOrganizations() {
        final String GET_ALL_EOS = "SELECT * FROM ExchangeOrganization;";
        List<ExchangeOrganization> exchangeOrganizationList = jdbcTemplate.query(GET_ALL_EOS, new ExchangeOrganizationMapper());
        // Set the stocks for each exchange organization
        setStocksByExchangeOrganizationList(exchangeOrganizationList);
        return exchangeOrganizationList;
    }

    @Override
    public ExchangeOrganization addExchangeOrganization(ExchangeOrganization exchangeOrganization) {

        final String ADD_EO = "INSERT INTO ExchangeOrganization (tickerCode, name) VALUES (?,?)";

        jdbcTemplate.update(ADD_EO, exchangeOrganization.getTickerCode(), exchangeOrganization.getName());

        int newID = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        exchangeOrganization.setExchangeOrganizationID(newID);

        //Insert the stocks in the bridge StockExchangeOrganization table
        insertStockExchangeOrganization(exchangeOrganization);

        return exchangeOrganization;
    }

    @Override
    public void updateExchangeOrganization(ExchangeOrganization eo) {
        final String UPDATE_EO = "UPDATE ExchangeOrganization SET tickerCode = ?, name = ? WHERE exchangeOrganizationID = ?";

        jdbcTemplate.update(UPDATE_EO, eo.getTickerCode(), eo.getName(), eo.getExchangeOrganizationID());

        //Does not allow the feature to remove a stock from Exchange Organization

    }

    @Override
    public void deleteExchangeOrganizationByID(int eoID) {
        //First delete the Portfolio Bridge table
        final String DELETE_PORTFOLIO_BRIDGE_BY_EO = "DELETE FROM PortfolioBridge WHERE exchangeOrganizationID = ?";
        jdbcTemplate.update(DELETE_PORTFOLIO_BRIDGE_BY_EO, eoID);

        //Then delete from the StockExchangeOrganization Bridge table
        final String DELETE_FROM_SEO_BY_EO = "DELETE FROM StockExchangeOrganization WHERE exchangeOrganizationID = ?";
        jdbcTemplate.update(DELETE_FROM_SEO_BY_EO, eoID);

        //Then delete the exchange organization
        final String DELETE_EO = "DELETE FROM ExchangeOrganization WHERE exchangeOrganizationID = ?";
        jdbcTemplate.update(DELETE_EO, eoID);
    }

    @Override
    public List<ExchangeOrganization> getExchangeOrganizationsByStock(Stock stock) {
        final String GET_EO_BY_STOCK = "SELECT eo.* FROM ExchangeOrganization eo " +
                "JOIN StockExchangeOrganization seo ON seo.exchangeOrganizationID = eo.exchangeOrganizationID " +
                "JOIN Stock s ON s.stockID = seo.stockID " +
                "WHERE s.stockID = ?";

        //TODO set anything for stocks ?
        List<ExchangeOrganization> eoList = jdbcTemplate.query(GET_EO_BY_STOCK, new ExchangeOrganizationMapper(), stock.getStockID());
        return eoList;
    }

    //PRIVATE HELPER FUNCTIONS
    private List<Stock> getStocksByExchangeOrganization(ExchangeOrganization eo) {
        final String GET_STOCKS_BY_EO = "SELECT s.* FROM Stock s\n" +
                "JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID\n" +
                "JOIN ExchangeOrganization eo ON eo.exchangeOrganizationID = seo.exchangeOrganizationID\n" +
                "WHERE eo.exchangeOrganizationID = ?";

        List<Stock> stocks = jdbcTemplate.query(GET_STOCKS_BY_EO, new StockMapper(), eo.getExchangeOrganizationID());
        return stocks.size() == 0 ? new ArrayList<>() : stocks;
    };

    private void setStocksByExchangeOrganizationList(List<ExchangeOrganization> exchangeOrganizationList) {
        for(ExchangeOrganization eo : exchangeOrganizationList) {
            eo.setStocks(getStocksByExchangeOrganization(eo));
        }
    }

    private void insertStockExchangeOrganization(ExchangeOrganization eo) {
        if(eo.getStocks() != null) {
            final String INSERT_EO_STOCKS = "INSERT INTO "
                    + "StockExchangeOrganization (exchangeOrganizationID, stockID) VALUES (?,?);";
            int eoID = eo.getExchangeOrganizationID();
            for(Stock stock : eo.getStocks()) { // Insert for each organization exchange stock.
                int stockID = stock.getStockID();
                jdbcTemplate.update(INSERT_EO_STOCKS, eoID, stockID);
            }
        }
    }

}
