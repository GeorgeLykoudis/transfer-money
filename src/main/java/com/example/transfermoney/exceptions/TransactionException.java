package com.example.transfermoney.exceptions;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public class TransactionException extends RuntimeException {
    private String code;

    public TransactionException(String message, String code) {
        super(message);
        this.code = code;
    }
}
