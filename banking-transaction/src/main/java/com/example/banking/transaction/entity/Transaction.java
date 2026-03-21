package com.example.banking.transaction.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private String type;

    private BigDecimal amount;

    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Accounts account;

}
