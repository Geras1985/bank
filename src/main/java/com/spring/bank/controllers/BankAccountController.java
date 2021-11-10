package com.spring.bank.controllers;

import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.User;
import com.spring.bank.enums.Role;
import com.spring.bank.repository.BankAccountDAO;
import com.spring.bank.repository.TransactionDAO;
import com.spring.bank.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/bank_account")
public class BankAccountController {

  @Autowired UserDAO userDAO;
  @Autowired BankAccountDAO bankAccountDAO;
  @Autowired TransactionDAO transactionDAO;

  @GetMapping("/create_bank_account/{id}")
  public String createBankAccGet(ModelMap model, @PathVariable Integer id,
                                 @RequestParam(name="username", required=false) String username,
                                 @RequestParam(name="user_id", required=false) String userID){

    User user = userDAO.findByid(id);
    model.addAttribute("user", user);

    return "/create_bank_account";
  }

  @PostMapping("/create_bank_account/{id}")
  public ResponseEntity<BankAccount> createBankAccount(@PathVariable("id") int id) {
    LocalDate date = LocalDate.now();
    User user = userDAO.findByid(id);
    if (user.getBankAccount() == null) {
      BankAccount bankAccount = new BankAccount();
      bankAccount.setBalance(0);
      bankAccount.setCreatedAt(date);
      user.setBankAccount(bankAccount);
      bankAccountDAO.save(bankAccount);
      userDAO.save(user);
      return new ResponseEntity<>(bankAccount, HttpStatus.OK);
    }
    else {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }
  }
}
