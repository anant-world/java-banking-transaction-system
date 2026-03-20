package com.example.banking.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
    private String type;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public TransactionResponse(String type , BigDecimal amount , LocalDateTime timestamp){
        this.type= type;
        this.amount=amount;
        this.timestamp= timestamp;
    }
    public String getType(){
        return type;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public LocalDateTime getTimestamp(){
        return timestamp;
    }
}
