package com.example.transfermoney.service.impl;

import com.example.transfermoney.model.Account;
import com.example.transfermoney.model.CurrencyEnum;
import com.example.transfermoney.model.Transaction;
import com.example.transfermoney.model.dtos.TransactionRequest;
import com.example.transfermoney.repository.AccountRepository;
import com.example.transfermoney.repository.TransactionRepository;
import com.example.transfermoney.util.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author George Lykoudis
 * @date 4/6/2023
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    Long TRANSACTION_ID = 1L;
    Long ACCOUNT_ID_1 = 1L;
    Long ACCOUNT_ID_2 = 2L;

    @Mock
    AccountRepository accountRepository;
    AccountServiceImpl accountService;
    @Mock
    TransactionRepository transactionRepository;
    TransactionServiceImpl transactionService;

    JsonParser<TransactionRequest> jsonParser;
    Account source;
    Account target;
    TransactionRequest successfulTransactionRequest;
    TransactionRequest insufficientBalanceTransactionRequest;
    TransactionRequest sameAccountTransactionRequest;
    TransactionRequest accountDoNotExistsTransactionRequest;

    @BeforeEach
    void setUp() throws Exception {
        jsonParser = new JsonParser<>();
        source = Account.builder()
                .id(ACCOUNT_ID_1)
                .balance(100d)
                .currency(CurrencyEnum.EUR)
                .createdAt(new Date())
                .build();
        target = Account.builder()
                .id(ACCOUNT_ID_2)
                .balance(50d)
                .currency(CurrencyEnum.EUR)
                .createdAt(new Date())
                .build();
        successfulTransactionRequest = jsonParser.loadJson("testData/successful_transaction.json", TransactionRequest.class);
        insufficientBalanceTransactionRequest = jsonParser.loadJson("testData/insufficient_balance.json", TransactionRequest.class);
        sameAccountTransactionRequest = jsonParser.loadJson("testData/same_account.json", TransactionRequest.class);
        accountDoNotExistsTransactionRequest = jsonParser.loadJson("testData/non_existent_account.json", TransactionRequest.class);

        accountService = new AccountServiceImpl(accountRepository);
        transactionService = new TransactionServiceImpl(transactionRepository, accountService);
    }

    @Test
    void TransactionServiceImpl_ExecuteSuccessfulTransaction_AccountsBeingUpdated() {
        final double amount = successfulTransactionRequest.getAmount();
        final double sourceBalanceBeforeTransaction = source.getBalance();
        final double targetBalanceBeforeTransaction = target.getBalance();

        when(accountRepository.findById(ACCOUNT_ID_1)).thenReturn(Optional.ofNullable(source));
        when(accountRepository.findById(ACCOUNT_ID_2)).thenReturn(Optional.ofNullable(target));

        transactionService.execute(successfulTransactionRequest);

        Account sourceUpdated = accountService.findById(ACCOUNT_ID_1);
        Account targetUpdated = accountService.findById(ACCOUNT_ID_2);

        assertEquals(sourceBalanceBeforeTransaction - amount, sourceUpdated.getBalance());
        assertEquals(targetBalanceBeforeTransaction + amount, targetUpdated.getBalance());

        verify(accountRepository, times(4)).findById(anyLong());
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void TransactionServiceImpl_SaveTransaction_CreateTransactionObject() {
        final CurrencyEnum currency = CurrencyEnum.getCurrencyFromString(successfulTransactionRequest.getCurrency());
        final double amount = successfulTransactionRequest.getAmount();
        Transaction transaction = Transaction
                .builder()
                .id(TRANSACTION_ID)
                .sourceAccount(source)
                .targetAccount(target)
                .amount(amount)
                .currency(currency)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        Transaction savedTransaction = transactionService.save(transaction);

        assertEquals(source.getId(), savedTransaction.getSourceAccount().getId());
        assertEquals(target.getId(), savedTransaction.getTargetAccount().getId());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(currency, savedTransaction.getCurrency());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void TransactionServiceImpl_ExecuteTransactionWithInsufficientBalance_AccountsDoNotChangeAndNoTransactionObjectCreated() {
        when(accountRepository.findById(ACCOUNT_ID_1)).thenReturn(Optional.ofNullable(source));
        when(accountRepository.findById(ACCOUNT_ID_2)).thenReturn(Optional.ofNullable(target));

        transactionService.execute(insufficientBalanceTransactionRequest);

        verify(accountRepository, times(2)).findById(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void TransactionServiceImpl_ExecuteTransactionWithSameAccount_AccountsDoNotChangeAndNoTransactionObjectCreated() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(source));

        transactionService.execute(sameAccountTransactionRequest);

        verify(accountRepository, times(2)).findById(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void TransactionServiceImpl_ExecuteTransactionWithNonExistingAccount_AccountsDoNotChangeAndNoTransactionObjectCreated() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        transactionService.execute(accountDoNotExistsTransactionRequest);

        verify(accountRepository, times(1)).findById(anyLong());
        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}