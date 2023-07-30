package com.sg.FinancialManagementSystem.service.Exceptions;

/**
 * @author raniaouassif on 2023-07-29
 */
public class InvalidDateException extends Exception{
    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
