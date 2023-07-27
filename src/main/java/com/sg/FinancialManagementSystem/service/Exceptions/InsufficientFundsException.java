package com.sg.FinancialManagementSystem.service.Exceptions;

/**
 * @author raniaouassif on 2023-07-26
 */
public class InsufficientFundsException extends Exception{
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

}
