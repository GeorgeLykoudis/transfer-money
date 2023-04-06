package com.example.transfermoney.service.impl;

import com.example.transfermoney.exceptions.AccountException;
import com.example.transfermoney.model.Account;
import com.example.transfermoney.model.CurrencyEnum;
import com.example.transfermoney.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author George Lykoudis
 * @date 4/6/2023
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    AccountServiceImpl accountService;
    Long ACCOUNT_ID = 1L;
    Account account;

    @Test
    public void AccountServiceImpl_FindById_ReturnAccount() {
        Account account = Account.builder()
                .id(ACCOUNT_ID)
                .balance(100d)
                .currency(CurrencyEnum.EUR)
                .createdAt(new Date())
                .build();

        when(accountRepository.findById(anyLong())).thenReturn(Optional.ofNullable(account));
        Account resultAccount = accountService.findById(ACCOUNT_ID);

        assertEquals(account, resultAccount);
        assertEquals(account.getBalance(), resultAccount.getBalance());
        assertEquals(account.getCurrency(), resultAccount.getCurrency());
    }

    @Test
    public void findByNotValidIdThrowsAccountException() {
        assertThrows(AccountException.class, () -> accountService.findById(100L));
    }
}