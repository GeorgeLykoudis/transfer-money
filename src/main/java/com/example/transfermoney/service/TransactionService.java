package com.example.transfermoney.service;

import com.example.transfermoney.model.Transaction;
import com.example.transfermoney.model.dtos.TransactionRequest;
import com.example.transfermoney.model.dtos.TransactionResponse;
import org.springframework.http.ResponseEntity;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public interface TransactionService {
    Transaction save(Transaction transaction);
    ResponseEntity<TransactionResponse> execute(TransactionRequest transactionRequest);
}