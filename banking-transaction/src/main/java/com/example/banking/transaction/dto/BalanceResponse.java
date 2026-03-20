package com.example.banking.transaction.dto;

import java.math.BigDecimal;

public class BalanceResponse {
    private Long accountId;
    private BigDecimal balance;

    public BalanceResponse(Long accountId , BigDecimal balance){
        this.accountId=accountId;
        this.balance=balance;
    }
    public Long getAccountId(){
        return accountId;
    }
    public BigDecimal getBalance(){
        return balance;
    }
}
