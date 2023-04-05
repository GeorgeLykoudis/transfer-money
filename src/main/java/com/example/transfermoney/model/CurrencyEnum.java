package com.example.transfermoney.model;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public enum CurrencyEnum {
    EUR("eur"), GBP("gbp");

    private String name;

    CurrencyEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CurrencyEnum getCurrencyFromString(String currency) {
        for (CurrencyEnum val : values()) {
            if (val.getName().equals(currency.toLowerCase())) {
                return val;
            }
        }
        return EUR; // keep it as default
    }

    public static boolean isCurrencyAvailable(String currency) {
        for (CurrencyEnum val : values()) {
            if (val.getName().equals(currency.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
