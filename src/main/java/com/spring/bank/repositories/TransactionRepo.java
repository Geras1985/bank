package com.spring.bank.repositories;

import com.spring.bank.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByTransactionStatus(String transactionStatus);

    Transaction findByid(Integer id);

}
