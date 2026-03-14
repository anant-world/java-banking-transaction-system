package com.example.banking.transaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, precision = 19 , scale = 4)
    private BigDecimal balance;

    @Version
    private Long version;

    private LocalDateTime createdAt;
}
