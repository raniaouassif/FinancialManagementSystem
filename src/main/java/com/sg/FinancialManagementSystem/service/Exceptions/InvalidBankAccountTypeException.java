package com.sg.FinancialManagementSystem.service.Exceptions;

/**
 * @author raniaouassif on 2023-07-29
 */
public class InvalidBankAccountTypeException extends Exception{
    public InvalidBankAccountTypeException(String message) {
        super(message);
    }

    public InvalidBankAccountTypeException(String message, Throwable cause) {
        super(message, cause);
    }

}
