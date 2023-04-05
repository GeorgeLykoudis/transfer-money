package com.example.transfermoney.service;

import com.example.transfermoney.model.Transaction;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public interface TransactionService {
    Transaction findById(long transactionId);
    Transaction save(Transaction transaction);
}