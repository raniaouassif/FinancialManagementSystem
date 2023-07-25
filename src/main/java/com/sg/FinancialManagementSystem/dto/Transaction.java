package com.sg.FinancialManagementSystem.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author raniaouassif on 2023-07-23
 */
public class Transaction {
    private int transactionID;
    private LocalDateTime dateTime;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Account from;
    private Account to;
    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return getTransactionID() == that.getTransactionID() && Objects.equals(getDateTime(), that.getDateTime()) && getTransactionType() == that.getTransactionType() && Objects.equals(getAmount(), that.getAmount()) && Objects.equals(getFrom(), that.getFrom()) && Objects.equals(getTo(), that.getTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTransactionID(), getDateTime(), getTransactionType(), getAmount(), getFrom(), getTo());
    }

}
