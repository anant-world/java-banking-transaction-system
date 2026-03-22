package com.example.banking.transaction.service;

import com.example.banking.transaction.dto.BalanceResponse;
import com.example.banking.transaction.dto.TransactionResponse;
import com.example.banking.transaction.dto.TransferRequest;
import com.example.banking.transaction.dto.WithdrawRequest;
import com.example.banking.transaction.entity.Accounts;
import com.example.banking.transaction.entity.CurrentAccount;
import com.example.banking.transaction.entity.SavingsAccount;
import com.example.banking.transaction.entity.Transaction;
import com.example.banking.transaction.repository.AccountRepository;
import com.example.banking.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        txn.setAccount(account);
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
    @Transactional
    public void withdraw(WithdrawRequest request){
        Accounts accounts= accountRepository.findById(request.getAccountId()).orElseThrow(()-> new RuntimeException("Account not found"));

        BigDecimal currentBalance=accounts.getBalance();

        if (currentBalance.compareTo(request.getAmount())<=0){
            throw new RuntimeException("Insufficient balance");
        }
//        deduct amount
        accounts.setBalance(currentBalance.subtract(request.getAmount()));
        accountRepository.save(accounts);

        Transaction txn = new Transaction();
        txn.setAccount(accounts);
        txn.setAmount(request.getAmount());
        txn.setType("WITHDRAW");
        txn.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(txn);
    }
    @Transactional
    public void transfer(TransferRequest request){
        Accounts fromAccount= accountRepository.findById(request.getFromAccountId()).orElseThrow(()->
                new RuntimeException("Sender account not found"));

        Accounts toAccount= accountRepository.findById(request.getToAccountId()).orElseThrow(()->
                new RuntimeException("Reciever account not found"));

        //Balance check

        if (fromAccount.getBalance().compareTo(request.getAmount())<=0){
            throw new RuntimeException("Insufficient balance");
        }

//        deduct from sender
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));

//        added to the reciever
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        Transaction debitTxn= new Transaction();
        debitTxn.setAccount(fromAccount);
        debitTxn.setAmount(request.getAmount());
        debitTxn.setType("Transfer out");
        debitTxn.setTimeStamp(LocalDateTime.now());


        Transaction creditTxn = new Transaction();
        creditTxn.setAccount(toAccount);
        creditTxn.setAmount(request.getAmount());
        creditTxn.setType("Transfer In");
        creditTxn.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(debitTxn);
        transactionRepository.save(creditTxn);
    }
}
