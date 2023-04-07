package com.example.transfermoney.service.impl;

import com.example.transfermoney.exceptions.AccountException;
import com.example.transfermoney.exceptions.TransactionException;
import com.example.transfermoney.model.Account;
import com.example.transfermoney.model.CurrencyEnum;
import com.example.transfermoney.model.TransactionEnum;
import com.example.transfermoney.model.Transaction;
import com.example.transfermoney.model.dtos.TransactionRequest;
import com.example.transfermoney.model.dtos.TransactionResponse;
import com.example.transfermoney.repository.TransactionRepository;
import com.example.transfermoney.service.AccountService;
import com.example.transfermoney.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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
        log.info("saving transaction {}", transaction);
        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public TransactionResponse execute(TransactionRequest transactionRequest) {
        Transaction transaction = null;
        try {
            Account source = accountService.findById(transactionRequest.getSourceAccountId());
            isValidAccount(source);
            Account target = accountService.findById(transactionRequest.getTargetAccountId());
            isValidAccount(target);
            validateTransaction(source, target, transactionRequest);
            log.debug("Fetched and validated accounts");
            transaction = executeTransaction(source, target, transactionRequest);
            if (Objects.isNull(transaction)) {
                return TransactionResponse.buildErrorResponse(TransactionEnum.FAILED_TRANSACTION);
            }
        } catch (AccountException | TransactionException e) {
            TransactionEnum transactionEnum = TransactionEnum.getTransactionEnumFromName(e.getMessage());
            return TransactionResponse.buildErrorResponse(e.getMessage(), transactionEnum);
        } catch (Exception e) {
            return TransactionResponse.buildErrorResponse(TransactionEnum.FAILED_TRANSACTION);
        }
        return TransactionResponse.buildSuccessResponse(transaction.getId());
    }

    private Transaction executeTransaction(Account source, Account target, TransactionRequest transactionRequest) {
        source.setBalance(source.getBalance() - transactionRequest.getAmount());
        target.setBalance(target.getBalance() + transactionRequest.getAmount());
        accountService.save(source);
        accountService.save(target);
        Transaction transaction = Transaction.builder()
                .sourceAccount(source)
                .targetAccount(target)
                .amount(transactionRequest.getAmount())
                .currency(CurrencyEnum.getCurrencyFromString(transactionRequest.getCurrency()))
                .build();
        return save(transaction);
    }

    private void validateTransaction(Account sourceAccount,
                                     Account targetAccount,
                                     TransactionRequest transactionRequest) throws TransactionException {
        validateSourceNotSameWithTarget(sourceAccount, targetAccount);
        validateCurrency(sourceAccount, transactionRequest);
        validateCurrency(targetAccount, transactionRequest);
        validateSourcesAccountBalanceSufficient(sourceAccount, transactionRequest);
    }

    private void isValidAccount(Account account) {
        if (Objects.isNull(account)) {
            log.error(TransactionEnum.ACCOUNT_NOT_FOUND.getMessage());
            throw new AccountException(TransactionEnum.ACCOUNT_NOT_FOUND.getMessage(),
                    TransactionEnum.ACCOUNT_NOT_FOUND.getCode());
        }
    }

    private void validateSourceNotSameWithTarget(Account source, Account target) {
        if (source.equals(target)) {
            log.error(TransactionEnum.SOURCE_EQUAL_TO_TARGET_ACCOUNT.getMessage());
            throw new TransactionException(TransactionEnum.SOURCE_EQUAL_TO_TARGET_ACCOUNT.getMessage(),
                    TransactionEnum.SOURCE_EQUAL_TO_TARGET_ACCOUNT.getCode());
        }
    }

    private void validateCurrency(Account account, TransactionRequest request) throws TransactionException {
        if (!request.hasValidCurrency()) {
            log.error(TransactionEnum.CURRENCY_NOT_SPECIFIED.getMessage());
            throw new TransactionException(TransactionEnum.CURRENCY_NOT_SPECIFIED.getMessage(),
                    TransactionEnum.CURRENCY_NOT_SPECIFIED.getCode());
        }
        // transaction in different currency
        if (!account.getCurrency().getName().equals(request.getCurrency())) {
            log.error(TransactionEnum.DIFFERENT_CURRENCY_FROM_ACCOUNT.getMessage());
            // could calculate the balance with transaction's currency and proceed with the flow
            throw new TransactionException(TransactionEnum.DIFFERENT_CURRENCY_FROM_ACCOUNT.getMessage(),
                    TransactionEnum.DIFFERENT_CURRENCY_FROM_ACCOUNT.getCode());
        }
    }

    private void validateSourcesAccountBalanceSufficient(Account source,
                                                         TransactionRequest request) throws TransactionException {
        if (!source.hasSufficientBalance(request.getAmount())) {
            String message = TransactionEnum
                    .replacePlaceHolder(TransactionEnum.NOT_SUFFICIENT_BALANCE, String.valueOf(source.getId()));
            log.error(message);
            throw new TransactionException(message, TransactionEnum.NOT_SUFFICIENT_BALANCE.getCode());
        }
    }
}
