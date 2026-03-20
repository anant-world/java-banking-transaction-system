package com.example.banking.transaction.service;

import com.example.banking.transaction.dto.BalanceResponse;
import com.example.banking.transaction.dto.TransactionResponse;
import com.example.banking.transaction.entity.Accounts;
import com.example.banking.transaction.entity.CurrentAccount;
import com.example.banking.transaction.entity.SavingsAccount;
import com.example.banking.transaction.entity.Transaction;
import com.example.banking.transaction.repository.AccountRepository;
import com.example.banking.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;



    public AccountService (AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository= accountRepository;
        this.transactionRepository=transactionRepository;
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

        Transaction txn= new Transaction();
        txn.setAccountId(accountId);
        txn.setAmount(amount);
        txn.setType("DEPOSIT");
        txn.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(txn);



        return accountRepository.save(account);
    }



    public BalanceResponse getBalance(Long accountId){
        Accounts accounts= accountRepository.findById(accountId).orElseThrow(()-> new RuntimeException ("Account not found"));

        return new BalanceResponse(accountId,accounts.getBalance());
    }


    public List<TransactionResponse> getStatement(Long accountId){
        List<Transaction> transactions= transactionRepository.findByAccountId(accountId);

        return transactions.stream()
                .map(txn -> new TransactionResponse(
                        txn.getType(),
                        txn.getAmount(),
                        txn.getTimeStamp()
                )).toList();

    }
}
