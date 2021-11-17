package com.spring.bank.services;

import com.spring.bank.entities.BankAccount;
import com.spring.bank.entities.User;

public interface BankAccountService {

    BankAccount createBankAccount(Integer loggedUserId, User user);
}
