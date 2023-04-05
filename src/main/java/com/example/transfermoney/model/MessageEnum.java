package com.example.transfermoney.model;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public enum MessageEnum {
    ACCOUNT_NOT_FOUND("Account with id {} do not exist"),
    SOURCE_EQUAL_TO_TARGET_ACCOUNT("Source account is the same with the target account"),
    CURRENCY_NOT_SPECIFIED("The currency of the transaction is not specified or not valid"),
    DIFFERENT_CURRENCY_FROM_ACCOUNT("The transaction has different currency from the source's account currency"),
    NOT_SUFFICIENT_BALANCE("Account {} does not have sufficient balance to complete the transaction");

    private static final String PLACEHOLDER = "{}";
    private String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static String replacePlaceHolder(MessageEnum messageEnum, String replacement) {
        return messageEnum.getMessage().replace(PLACEHOLDER, replacement);
    }
}
