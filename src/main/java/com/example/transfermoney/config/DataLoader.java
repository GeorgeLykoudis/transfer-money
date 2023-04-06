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
        Account account1 = Account.builder().balance(100d).currency(CurrencyEnum.EUR).build();
        Account savedAccount1 = accountService.save(account1);
        Account account2 = Account.builder().balance(200d).currency(CurrencyEnum.EUR).build();
        Account savedAccount2 = accountService.save(account2);
        Account account3 = Account.builder().balance(400d).currency(CurrencyEnum.GBP).build();
        Account savedAccount3 = accountService.save(account3);
        Account account4 = Account.builder().balance(500d).currency(CurrencyEnum.GBP).build();
        Account savedAccount4 = accountService.save(account4);

        Transaction transaction1 = Transaction.builder().sourceAccount(savedAccount1).targetAccount(savedAccount2).amount(50d).currency(CurrencyEnum.EUR).build();
        Transaction savedTransaction1 = transactionService.save(transaction1);
        Transaction transaction2 = Transaction.builder().sourceAccount(savedAccount3).targetAccount(savedAccount4).amount(100d).currency(CurrencyEnum.GBP).build();
        Transaction savedTransaction2 = transactionService.save(transaction2);
    }
}
