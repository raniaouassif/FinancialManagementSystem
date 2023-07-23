package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Stock {
    private int stockID;
    private String tickerCode;
    private String name;
    private BigDecimal sharePrice;
    private int numberOfOutstandingShares;
    private BigDecimal marketCap;
    private int dailyVolume;
}
