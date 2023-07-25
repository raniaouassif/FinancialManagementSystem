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
public class ExchangeOrganizationMapper implements RowMapper<ExchangeOrganization> {

    @Override
    public ExchangeOrganization mapRow(ResultSet rs, int index) throws SQLException {
        ExchangeOrganization eo = new ExchangeOrganization();
        eo.setExchangeOrganizationID(rs.getInt("exchangeOrganizationID"));
        eo.setName(rs.getString("name"));
        eo.setTickerCode(rs.getString("tickerCode"));
        return eo;
    }
}
