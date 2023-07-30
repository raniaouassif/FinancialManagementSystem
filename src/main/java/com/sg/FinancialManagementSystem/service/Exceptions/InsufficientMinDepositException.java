package com.sg.FinancialManagementSystem.service.Exceptions;

/**
 * @author raniaouassif on 2023-07-29
 */
public class InsufficientMinDepositException extends Exception{
    public InsufficientMinDepositException(String message) {
        super(message);
    }

    public InsufficientMinDepositException(String message, Throwable cause) {
        super(message, cause);
    }

}
