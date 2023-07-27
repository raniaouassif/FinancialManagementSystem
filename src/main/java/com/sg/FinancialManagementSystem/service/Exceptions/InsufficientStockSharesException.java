package com.sg.FinancialManagementSystem.service.Exceptions;

/**
 * @author raniaouassif on 2023-07-27
 */
public class InsufficientStockSharesException extends Exception {
    public InsufficientStockSharesException(String message) {
        super(message);
    }

    public InsufficientStockSharesException(String message, Throwable cause) {
        super(message, cause);
    }

}
