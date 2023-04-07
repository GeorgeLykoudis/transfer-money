package com.example.transfermoney.controller;

import com.example.transfermoney.model.dtos.TransactionRequest;
import com.example.transfermoney.model.dtos.TransactionResponse;
import com.example.transfermoney.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> executeTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transaction = transactionService.execute(transactionRequest);
        if (Objects.nonNull(transaction.getTransactionId())) {
            return ResponseEntity.ok(transaction);
        }
        return ResponseEntity.badRequest().body(transaction);
    }
}
