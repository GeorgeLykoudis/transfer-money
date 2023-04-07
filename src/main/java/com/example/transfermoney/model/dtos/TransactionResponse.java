package com.example.transfermoney.model.dtos;

import com.example.transfermoney.model.TransactionEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private String message;
    private String code;
    private Long transactionId;

    public static TransactionResponse buildErrorResponse(String message, TransactionEnum transactionEnum) {
        return TransactionResponse.builder()
                .message(message)
                .code(transactionEnum.getCode())
                .build();
    }

    public static TransactionResponse buildErrorResponse(TransactionEnum transactionEnum) {
        return TransactionResponse.builder()
                .message(transactionEnum.getMessage())
                .code(transactionEnum.getCode())
                .build();
    }

    public static TransactionResponse buildSuccessResponse(Long transactionId) {
        return TransactionResponse.builder()
                .message(TransactionEnum.TRANSACTION_COMPLETED.getMessage())
                .code(TransactionEnum.TRANSACTION_COMPLETED.getCode())
                .transactionId(transactionId)
                .build();
    }
}
