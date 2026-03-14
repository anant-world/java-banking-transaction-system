package com.example.banking.transaction.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class SavingsAccount extends Accounts{
    private BigDecimal interestRate;

    private Integer withdrawalLimit;
}
