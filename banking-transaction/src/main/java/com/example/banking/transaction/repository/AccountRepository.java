package com.example.banking.transaction.repository;

import com.example.banking.transaction.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Accounts,Long> {
}
