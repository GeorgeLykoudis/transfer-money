package com.example.transfermoney.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;
        return getId() != null ? getId().equals(account.getId()) : account.getId() == null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean hasSufficientBalance(double amount) {
        return this.balance >= amount;
    }
}
