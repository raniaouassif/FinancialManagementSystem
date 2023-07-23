package com.sg.FinancialManagementSystem.dao.mappers;

import com.sg.FinancialManagementSystem.dto.ExchangeOrganization;
import com.sg.FinancialManagementSystem.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-23
 */

@Component
public class ExchangeOrganizationMapper implements RowMapper<ExchangeOrganization> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public ExchangeOrganization mapRow(ResultSet rs, int index) throws SQLException {
        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setExchangeOrganizationID(rs.getInt("exchangeOrganizationID"));
        eo.setName(rs.getString("name"));
        eo.setTickerCode("tickerCode");

        eo.setStocks(getStocksForExchangeOrganization(eo));
        return eo;
    }

    // Private helper function
    private List<Stock> getStocksForExchangeOrganization(ExchangeOrganization exchangeOrganization) {
        final String GET_STOCKS_FOR_EXCHANGE_ORG = "SELECT s.* FROM Stock s "
                + "JOIN StockExchangeOrganization seo ON seo.stockID = s.stockID "
                + "JOIN ExchangeOrganization eo ON eo.exchangeOrganizationID = seo.exchangeOrganizationID "
                + "WHERE eo.exchangeOrganizationID = ?";

        List<Stock> retrievedStocks = jdbcTemplate.query(
                GET_STOCKS_FOR_EXCHANGE_ORG,
                new StockMapper(),
                exchangeOrganization.getExchangeOrganizationID());

        return retrievedStocks.size() == 0 ? new ArrayList<>(): retrievedStocks;

    }
}
