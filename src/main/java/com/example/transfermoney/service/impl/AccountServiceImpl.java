package com.example.transfermoney.service.impl;

import com.example.transfermoney.model.Account;
import com.example.transfermoney.repository.AccountRepository;
import com.example.transfermoney.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account findById(long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
