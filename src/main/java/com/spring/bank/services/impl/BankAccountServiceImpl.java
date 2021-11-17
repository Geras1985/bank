package com.spring.bank.services.impl;

import com.spring.bank.entities.BankAccount;
import com.spring.bank.entities.User;
import com.spring.bank.repositories.BankAccountRepo;
import com.spring.bank.repositories.UserRepo;
import com.spring.bank.services.BankAccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final UserRepo userRepo;
    private final BankAccountRepo bankAccountRepo;

    public BankAccountServiceImpl(UserRepo userRepo, BankAccountRepo bankAccountRepo) {
        this.userRepo = userRepo;
        this.bankAccountRepo = bankAccountRepo;
    }

    @Override
    public BankAccount createBankAccount(Integer loggedUserId, User user) {
        LocalDate date = LocalDate.now();
        User loggedUser = userRepo.findByid(loggedUserId);
        Optional<User> userToAddBankAcc = userRepo.findByUsername(user.getUsername());
        if (loggedUser.getRole().name().equals("ADMIN")) {
            if (userToAddBankAcc.get().getBankAccount() == null) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setBalance(0);
                bankAccount.setCreatedAt(date);
                userToAddBankAcc.get().setBankAccount(bankAccount);
                bankAccountRepo.save(bankAccount);
                userRepo.save(userToAddBankAcc.get());
                return bankAccount;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
