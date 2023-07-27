package com.sg.FinancialManagementSystem.service;

import com.sg.FinancialManagementSystem.dao.PortfolioStockDao;
import com.sg.FinancialManagementSystem.dao.StockTransactionDao;
import com.sg.FinancialManagementSystem.dto.*;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientFundsException;
import com.sg.FinancialManagementSystem.service.Exceptions.InsufficientStockSharesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author raniaouassif on 2023-07-26
 */
@Service
public class StockTransactionServiceImpl implements StockTransactionService {
    @Autowired
    StockTransactionDao stockTransactionDao;

    @Autowired
    PortfolioStockDao portfolioStockDao;

    @Override
    public StockTransaction getStockTransactionByID(int stockTransactionID) {
        return stockTransactionDao.getStockTransactionByID(stockTransactionID);
    }

    @Override
    public List<StockTransaction> getAllStockTransactions() {
        return stockTransactionDao.getAllStockTransactions();
    }

    @Override
    public List<StockTransaction> getAllStockTransactionsDescDatetime() {
        return stockTransactionDao.getAllStockTransactionsDescDatetime();
    }

    @Override
    public StockTransaction addStockTransaction(StockTransaction st) throws InsufficientFundsException, InsufficientStockSharesException {
        BigDecimal transactionCost = BigDecimal.valueOf(st.getNumberOfShares()).multiply(st.getStock().getSharePrice());
        st.setTransactionCost(transactionCost);

        //See if the transaction price is more than the portfolio balance
        int comparisonResult = transactionCost.compareTo(st.getPortfolio().getBalance());

        if(comparisonResult >= 0) {
            throw new InsufficientFundsException("Insufficient funds for this transaction.");
        } else {
            PortfolioStock ps = portfolioStockDao.getPortfolioStockByStockTransaction(st);
            //Check if the portfolio is already in the stock
            if(ps == null) {
                //if user is trying to sell
                if(st.getType().equals(StockTransactionType.SELL)) {
                    throw new InsufficientStockSharesException("You do not own any shares of this stock.");
                } else {
                    List<StockTransaction> stList = new ArrayList<>();
                    stList.add(st);

                    //Create a new portfolio stock
                    ps = new PortfolioStock();
                    ps.setStock(st.getStock());
                    ps.setPortfolio(st.getPortfolio());
                    ps.setStockTransactionList(stList);
                    ps.setNumberOfShares(st.getNumberOfShares());
                    ps.setMarketValue(st.getTransactionCost());
                    ps.setBookValue(st.getTransactionCost());
                    ps.setAveragePrice(st.getStock().getSharePrice());
                    ps.setPercentageReturn(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
                    ps.setTotalReturn(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));

                    //Add to the DAO
                    portfolioStockDao.addPortfolioStock(ps);

                    //Add the transaction to the dao
                    return stockTransactionDao.addStockTransaction(st);
                }
            } else { // if the portfolio already has the stock
                // Check if it has enough shares to sell
                if(ps.getNumberOfShares() < st.getNumberOfShares() && st.getType().equals(StockTransactionType.SELL)) {
                    throw new InsufficientStockSharesException("Insufficient stocks  " + ps.getNumberOfShares() +" shares. \nTransaction is cancelled.");
                } else {//Update the portfolio stock
                    // Get previous number of shares
                    PortfolioStock prevPs = portfolioStockDao.getPortfolioStockByID(ps.getPortfolioStockID());
                    int prevNumberOfShares = prevPs.getNumberOfShares();
                    int newNumberOfShares = st.getType().equals(StockTransactionType.SELL) ?
                            prevNumberOfShares - st.getNumberOfShares() : prevNumberOfShares + st.getNumberOfShares();

                    BigDecimal prevBookValue = prevPs.getBookValue();
                    BigDecimal prevAveragePrice = prevPs.getAveragePrice();

                    // Calculate new average price
                    BigDecimal newAveragePrice = (prevBookValue.add(transactionCost)).divide(new BigDecimal(newNumberOfShares), 2, RoundingMode.HALF_UP);

                    // Calculate new book value
                    BigDecimal newBookValue = prevBookValue.add(transactionCost);

                    // Calculate new market value
                    BigDecimal newMarketValue = BigDecimal.valueOf(newNumberOfShares).multiply(st.getStock().getSharePrice());

                    // Calculate new total return
                    BigDecimal newTotalReturn = newMarketValue.subtract(newBookValue);

                    // Calculate new percentage return
                    BigDecimal newPercentageReturn = newTotalReturn.divide(newBookValue, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                    //Get the old transaction list and add the new one
                    List<StockTransaction> newStockTransactionList = ps.getStockTransactionList();
                    newStockTransactionList.add(st);

                    //Set the new values
                    ps.setNumberOfShares(st.getNumberOfShares());
                    ps.setMarketValue(newMarketValue);
                    ps.setBookValue(newBookValue);
                    ps.setAveragePrice(newAveragePrice);
                    ps.setPercentageReturn(newPercentageReturn);
                    ps.setTotalReturn(newTotalReturn);
                    ps.setStockTransactionList(newStockTransactionList);

                    //Update in the dao
                    portfolioStockDao.updatePortfolioStock(ps);

                    //Add the transaction
                    return stockTransactionDao.addStockTransaction(st);
                }
            }
        }
    }

    @Override
    public void updateStockTransaction(StockTransaction st) {
        stockTransactionDao.updateStockTransaction(st);
    }

    @Override
    public void deleteStockTransactionByID(int stockTransactionID) {
        stockTransactionDao.deleteStockTransactionByID(stockTransactionID);
    }

    @Override
    public List<StockTransaction> getStockTransactionsByPortfolio(Portfolio portfolio) {
        return stockTransactionDao.getStockTransactionsByPortfolio(portfolio);
    }

    @Override
    public List<StockTransaction> getStockTransactionByStock(Stock stock) {
        return stockTransactionDao.getStockTransactionByStock(stock);
    }

    private void getSharePriceForTransaction(StockTransaction stockTransaction) {
        //Update the stock price based on the transaction.
        //Since it might have changed
        // TODO
    }
}
