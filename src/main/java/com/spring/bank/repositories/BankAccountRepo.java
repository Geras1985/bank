package com.spring.bank.repositories;

import com.spring.bank.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount, Integer> {



}
