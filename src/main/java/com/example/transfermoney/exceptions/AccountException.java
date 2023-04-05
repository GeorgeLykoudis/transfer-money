package com.example.transfermoney.exceptions;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public class AccountException extends RuntimeException {

    public AccountException(String message) {
        super(message);
    }
}
