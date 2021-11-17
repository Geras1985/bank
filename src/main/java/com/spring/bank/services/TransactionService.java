package com.spring.bank.services;

import com.spring.bank.entities.Transaction;

import java.util.List;

public interface TransactionService {

    Boolean createTransaction(Integer loggedId, Transaction transaction);
    Boolean cancelTransaction(Integer loggedId, Transaction transaction);
    List<Transaction> getAllPendingTransactions(Integer loggedId);
    Boolean acceptTransaction(Integer loggedId, Transaction transaction);

}
