package com.example.transfermoney.service.impl;

import com.example.transfermoney.model.Transaction;
import com.example.transfermoney.repository.TransactionRepository;
import com.example.transfermoney.service.TransactionService;
import org.springframework.stereotype.Service;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction findById(long transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
