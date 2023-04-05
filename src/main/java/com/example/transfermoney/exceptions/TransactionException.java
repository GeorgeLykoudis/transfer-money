package com.example.transfermoney.exceptions;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public class TransactionException extends RuntimeException {
    public TransactionException(String message) {
        super(message);
    }
}
