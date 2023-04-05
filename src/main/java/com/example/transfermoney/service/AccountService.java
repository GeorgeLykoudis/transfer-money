package com.example.transfermoney.service;

import com.example.transfermoney.model.Account;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public interface AccountService {
    Account findById(long accountId);
    Account save(Account account);
}
