package com.example.transfermoney.repository;

import com.example.transfermoney.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
