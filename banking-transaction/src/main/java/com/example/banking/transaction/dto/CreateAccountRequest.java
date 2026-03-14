package com.example.banking.transaction.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateAccountRequest {

    private Long userId;

    private String accountHolderName;

    private String accountType;

    private Integer withdrawlLimit;

    private BigDecimal interestRate;

    private Double overDraftLimit;

    private BigDecimal initialBalance;



}
