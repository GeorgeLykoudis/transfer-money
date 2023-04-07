package com.example.transfermoney.exceptions;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public class AccountException extends RuntimeException {

    private String code;

    public AccountException(String message, String code) {
        super(message);
        this.code = code;
    }
}
