package com.example.banking.transaction.service;

import com.example.banking.transaction.entity.Accounts;
import com.example.banking.transaction.entity.CurrentAccount;
import com.example.banking.transaction.entity.SavingsAccount;
import com.example.banking.transaction.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService (AccountRepository accountRepository){
        this.accountRepository= accountRepository;
    }

    public Accounts createAccount(Long userId,String accountType, BigDecimal initialBalance){
        Accounts account;

        if(accountType.equalsIgnoreCase("SAVINGS")){
            SavingsAccount savingsAccount= new SavingsAccount();
            savingsAccount.setInterestRate(new BigDecimal("3"));
            savingsAccount.setWithdrawalLimit(6);

            account= savingsAccount;

        } else if (accountType.equalsIgnoreCase("CURRENT")) {
            CurrentAccount currentAccount= new CurrentAccount();
            currentAccount.setOverdraftLimit(new BigDecimal("100000"));

            account= currentAccount;
        }
        else{
            throw new RuntimeException("Invalid account type");
        }

        account.setUserId(userId);
        account.setBalance(initialBalance);
        account.setCreatedAt(LocalDateTime.now());

        return accountRepository.save(account);

    }

    public Accounts deposit(Long accountId , BigDecimal amount){
        Accounts account= accountRepository.findById(accountId).orElseThrow(()-> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));

        return accountRepository.save(account);
    }

}