package com.example.transfermoney.service.impl;

import com.example.transfermoney.exceptions.AccountException;
import com.example.transfermoney.exceptions.TransactionException;
import com.example.transfermoney.model.Account;
import com.example.transfermoney.model.MessageEnum;
import com.example.transfermoney.model.Transaction;
import com.example.transfermoney.model.dtos.TransactionRequest;
import com.example.transfermoney.model.dtos.TransactionResponse;
import com.example.transfermoney.repository.TransactionRepository;
import com.example.transfermoney.service.AccountService;
import com.example.transfermoney.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<TransactionResponse> execute(TransactionRequest transactionRequest) {
        try {
            Account source = accountService.findById(transactionRequest.getSourceAccountId());
            Account target = accountService.findById(transactionRequest.getTargetAccountId());
            validateTransaction(source, target, transactionRequest);
            log.info("Fetched and validated accounts");
            executeTransaction(source, target, transactionRequest);
        } catch (AccountException | TransactionException e) {
            return createTransactionResponseWithError(e);
        }
        return createSuccessTransactionResponse();
    }

    private void executeTransaction(Account source, Account target, TransactionRequest transactionRequest) {
        source.setBalance(source.getBalance() - transactionRequest.getAmount());
        target.setBalance(target.getBalance() + transactionRequest.getAmount());
        accountService.save(source);
        accountService.save(target);
        Transaction transaction = Transaction
                .createTransaction(source, target, transactionRequest.getAmount(), transactionRequest.getCurrency());
        save(transaction);
    }

    private void validateTransaction(Account sourceAccount,
                                     Account targetAccount,
                                     TransactionRequest transactionRequest) throws TransactionException {
        validateSourceNotSameWithTarget(sourceAccount, targetAccount);
        validateCurrency(sourceAccount, transactionRequest);
        validateSourcesAccountBalanceSufficient(sourceAccount, transactionRequest);
    }

    private void validateSourceNotSameWithTarget(Account source, Account target) {
        if (source.equals(target)) {
            log.error(MessageEnum.SOURCE_EQUAL_TO_TARGET_ACCOUNT.getMessage());
            throw new TransactionException(MessageEnum.SOURCE_EQUAL_TO_TARGET_ACCOUNT.getMessage());
        }
    }

    private void validateCurrency(Account source, TransactionRequest request) throws TransactionException {
        if (!request.hasValidCurrency()) {
            log.error(MessageEnum.CURRENCY_NOT_SPECIFIED.getMessage());
            throw new TransactionException(MessageEnum.CURRENCY_NOT_SPECIFIED.getMessage());
        }
        // transaction in different currency
        if (!source.getCurrency().getName().equals(request.getCurrency())) {
            log.error(MessageEnum.DIFFERENT_CURRENCY_FROM_ACCOUNT.getMessage());
            // could calculate the balance with transaction's currency and proceed with the flow
            throw new TransactionException(MessageEnum.DIFFERENT_CURRENCY_FROM_ACCOUNT.getMessage());
        }
    }

    private void validateSourcesAccountBalanceSufficient(Account source,
                                                         TransactionRequest request) throws TransactionException {
        if (!source.hasSufficientBalance(request.getAmount())) {
            String message = MessageEnum
                    .replacePlaceHolder(MessageEnum.NOT_SUFFICIENT_BALANCE, String.valueOf(source.getId()));
            log.error(message);
            throw new TransactionException(message);
        }
    }

    private ResponseEntity<TransactionResponse> createSuccessTransactionResponse() {
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<TransactionResponse> createTransactionResponseWithError(Exception e) {
        TransactionResponse transactionResponse = new TransactionResponse(e.getMessage());
        return new ResponseEntity<>(transactionResponse, HttpStatus.BAD_REQUEST);
    }
}
