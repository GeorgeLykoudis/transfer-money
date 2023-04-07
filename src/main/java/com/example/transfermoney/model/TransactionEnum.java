package com.example.transfermoney.model;

import org.springframework.util.StringUtils;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public enum TransactionEnum {
    ACCOUNT_NOT_FOUND("Account with id {} do not exist", "10"),
    SOURCE_EQUAL_TO_TARGET_ACCOUNT("Source account is the same with the target account", "01"),
    CURRENCY_NOT_SPECIFIED("The currency of the transaction is not specified or not valid", "02"),
    DIFFERENT_CURRENCY_FROM_ACCOUNT("The transaction has different currency from the source's or target's account currency", "03"),
    NOT_SUFFICIENT_BALANCE("Account {} does not have sufficient balance to complete the transaction", "04"),
    TRANSACTION_COMPLETED("Transaction completed", "00"),
    FAILED_TRANSACTION("Failed transaction", "05");

    private static final String PLACEHOLDER = "{}";
    private String message;
    private String code;

    TransactionEnum(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public String getCode() { return code; }

    public static String replacePlaceHolder(TransactionEnum transactionEnum, String replacement) {
        return transactionEnum.getMessage().replace(PLACEHOLDER, replacement);
    }

    public static TransactionEnum getTransactionEnumFromName(String name) {
        if (!StringUtils.hasText(name)) {
            return FAILED_TRANSACTION;
        }
        for (TransactionEnum t : values()) {
            if (t.getMessage().equals(name.replaceAll("\\d+", PLACEHOLDER))) {
                return t;
            }
        }
        return FAILED_TRANSACTION;
    }
}
