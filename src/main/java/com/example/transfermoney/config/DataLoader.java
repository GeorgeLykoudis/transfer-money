package com.example.transfermoney.config;

import com.example.transfermoney.model.Account;
import com.example.transfermoney.model.CurrencyEnum;
import com.example.transfermoney.model.Transaction;
import com.example.transfermoney.service.AccountService;
import com.example.transfermoney.service.TransactionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Component
public class DataLoader implements CommandLineRunner {
    private final AccountService accountService;
    private final TransactionService transactionService;

    public DataLoader(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Override
    public void run(String... args) throws Exception {
        Account account1 = Account.createAccount(100d, CurrencyEnum.EUR);
        Account savedAccount1 = accountService.save(account1);
        Account account2 = Account.createAccount(200d, CurrencyEnum.EUR);
        Account savedAccount2 = accountService.save(account2);
        Account account3 = Account.createAccount(400d, CurrencyEnum.GBP);
        Account savedAccount3 = accountService.save(account3);
        Account account4 = Account.createAccount(500d, CurrencyEnum.GBP);
        Account savedAccount4 = accountService.save(account4);

        Transaction transaction1 = Transaction.createTransaction(savedAccount1, savedAccount2, 50d, CurrencyEnum.EUR.getName());
        Transaction savedTransaction1 = transactionService.save(transaction1);
        Transaction transaction2 = Transaction.createTransaction(savedAccount3, savedAccount4, 100d, CurrencyEnum.GBP.getName());
        Transaction savedTransaction2 = transactionService.save(transaction2);
    }
}
