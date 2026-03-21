package com.example.banking.transaction.controller;

import com.example.banking.transaction.dto.*;
import com.example.banking.transaction.entity.Accounts;
import com.example.banking.transaction.entity.Transaction;
import com.example.banking.transaction.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/api/admin/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController (AccountService accountService){
        this.accountService=accountService;

    }
    @PostMapping("/create")
    public Accounts createAccount(@RequestBody CreateAccountRequest request){
        return accountService.createAccount(
                request.getUserId(),
                request.getAccountType(),
                request.getInitialBalance()
        );

    }

    @PostMapping("/{accountId}/deposit")
    public Accounts deposit(@PathVariable Long accountId,
                            @RequestParam("amount") BigDecimal amount){
        return accountService.deposit(accountId,amount);
    }

    @GetMapping("/{accountId}/balance")
    public BalanceResponse getBalance(@PathVariable Long accountId){
        return accountService.getBalance(accountId);
    }

    @GetMapping("/{accountId}/statement")
    public List<TransactionResponse> getStatement(@PathVariable Long accountId){
        return accountService.getStatement(accountId);
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestBody WithdrawRequest request){
        accountService.withdraw(request);
        return "Amount withdrawn successfully";
    }

}
