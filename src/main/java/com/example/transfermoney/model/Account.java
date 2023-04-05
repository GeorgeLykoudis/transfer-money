package com.example.transfermoney.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author George Lykoudis
 * @date 4/5/2023
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "balance")
    private double balance;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private CurrencyEnum currency;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

//    @OneToMany(mappedBy = "sourceAccount", fetch = FetchType.LAZY)
//    private Set<Transaction> transactionsAsSource = new HashSet<>();
//
//    @OneToMany(mappedBy = "targetAccount", fetch = FetchType.LAZY)
//    private Set<Transaction> transactionsAsTarget = new HashSet<>();

    private Account() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;
        return getId() != null ? getId().equals(account.getId()) : account.getId() == null;
    }

    public static Account createAccount(double balance, String currency) {
        Account account = new Account();
        account.setBalance(balance);
        account.setCurrency(CurrencyEnum.getCurrencyFromString(currency)); // could add checks for currencies existence
        account.setCreatedAt(new Date());
        return account;
    }

    public static Account createAccount(double balance, CurrencyEnum currency) {
        Account account = new Account();
        account.setBalance(balance);
        account.setCurrency(currency); // could add checks for currencies existence
        account.setCreatedAt(new Date());
        return account;
    }
}
