package com.example.banking.transaction.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class WithdrawRequest {

    private Long accountId;
    private BigDecimal amount;
}
