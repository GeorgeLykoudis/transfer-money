package com.example.transfermoney.model.dtos;

import com.example.transfermoney.model.CurrencyEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Getter
@Setter
public class TransactionRequest {
    private long sourceAccountId;
    private long targetAccountId;
    private double amount;
    private String currency;

    public boolean hasValidCurrency() {
        return StringUtils.hasText(currency) && CurrencyEnum.isCurrencyAvailable(currency);
    }
}
