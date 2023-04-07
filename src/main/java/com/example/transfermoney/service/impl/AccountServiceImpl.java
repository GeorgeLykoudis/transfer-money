package com.example.transfermoney.service.impl;

import com.example.transfermoney.exceptions.AccountException;
import com.example.transfermoney.model.Account;
import com.example.transfermoney.model.TransactionEnum;
import com.example.transfermoney.repository.AccountRepository;
import com.example.transfermoney.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account findById(long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> {
            String NOT_FOUND = TransactionEnum
                    .replacePlaceHolder(TransactionEnum.ACCOUNT_NOT_FOUND, String.valueOf(accountId));
            log.error(NOT_FOUND);
            return new AccountException(NOT_FOUND, TransactionEnum.ACCOUNT_NOT_FOUND.getCode());
        });
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }
}
