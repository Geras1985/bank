package com.spring.bank.controllers;


import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.Transaction;
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
import java.util.Base64;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController1 {


    @Autowired UserDAO userDAO;
    @Autowired BankAccountDAO bankAccountDAO;
    @Autowired TransactionDAO transactionDAO;



    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userDAO.findByid(id);

    }


    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        if (user.getUsername() == null || user.getUsername().trim().equals("")){
            //some error text
            return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
        }
        else if(user.getFirstName() == null || user.getFirstName().trim().equals("")){
            //some error text
            return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
        }
        else if(user.getLastName() == null || user.getLastName().trim().equals("")){
            //some error text
            return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
        }
        else if(user.getPassword() == null || user.getPassword().trim().equals("")){
            //some error text
            return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
        }
        else if(userDAO.findByUsername(user.getUsername()) != null){
            //some error text (user with such username already exists)
            return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            // password encoding
            String encodedString = Base64.getEncoder().encodeToString(user.getPassword().getBytes());

            user.setPassword(encodedString);
            LocalDate date = LocalDate.now();
            user.setCreatedAt(date);
            user.setRole(Role.USER);
            User user1 = userDAO.save(user);
            return new ResponseEntity<>(user1, HttpStatus.CREATED);
        }

    }



  @PostMapping("/login")
  public ResponseEntity<User> loginUser(@RequestBody User user) {
      User logUser = userDAO.findByUsername(user.getUsername());
      if (logUser != null){
          String userPassword = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
          if (userPassword.equals(logUser.getPassword())){
              //sucses
              return new ResponseEntity<>(logUser, HttpStatus.OK);
          }
          else {
              //no such user exists, try again
              return new ResponseEntity<>(logUser, HttpStatus.UNAUTHORIZED);
          }
      }
      else {
          //no such user exists, try again
          return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
      }
  }

    @GetMapping("/userHistory/{id}")
    public String userHistoryGet(ModelMap model, @PathVariable Integer id){

        Transaction transaction = new Transaction();
        User user = userDAO.findByid(id);
        Set<Transaction> trSet = user.getTransactions();
        model.addAttribute("user", user);
        model.addAttribute("trSet", trSet);
        model.addAttribute("transaction", transaction);
        return "/see_user_history";
        //TODO return enq anelu list
    }

    @PostMapping("/userHistory/{id}")
    public String userHistoryPost(ModelMap model, @PathVariable Integer id,
                                  @RequestParam(name="user_id", required=false) Integer userID,
                                  @RequestParam(name="transaction_id", required=false) Integer transactionId){
        // TODO tranzakcian hetenq uxarkum ete pendinga , u jnjum enq

        User user = userDAO.findByid(id);
        Transaction transaction = transactionDAO.getById(transactionId);
        transactionDAO.delete(transaction);
        model.addAttribute("user", user);
        return "redirect:/user_page/" + id;
    }

    @GetMapping("/edit_users/{id}")
    public String userEditGet(ModelMap model, @PathVariable Integer id){
        User user = userDAO.findByid(id);
        model.addAttribute("user", user);
        return "/edit_users";

    }

    @PostMapping("/edit_users/{id}")
    public String userEditPost(ModelMap model, @PathVariable Integer id,
                               @RequestParam(name="user_id", required=false) Integer userID,
                               @RequestParam(name="username", required=false) String username,
                               @RequestParam(name="role", required=false) String roleName){


        User user = userDAO.findByid(id);
        model.addAttribute("user", user);
        User userToChangeRole = userDAO.findByUsername(username);

        if (userToChangeRole != null) {
            Role role1 = Role.valueOf(roleName);
            userToChangeRole.setRole(role1);
            userDAO.save(userToChangeRole);
        }
        else{
            //some text that there is no user with such username
        }
        return "redirect:/user_page/" + id;
    }

    @GetMapping("/accept_transactions/{id}")
    public ResponseEntity<List<Transaction>> acceptTransactionsGet( @PathVariable Integer id){
        User user = userDAO.findByid(id);
        List<Transaction> transactionsByStatus = transactionDAO.findAllByTransactionStatus("pending");
        return new ResponseEntity<>(transactionsByStatus, HttpStatus.OK);
    }

    @PostMapping("/accept_transactions/{id}")
    public  ResponseEntity<Transaction> acceptTransactionsPost(
            @PathVariable Integer id,
            @RequestParam(name="user_id", required=false) Integer userID,
            @RequestParam(name="transaction_id", required=false) Integer transactionId){

        User user = userDAO.findByid(id);

        Transaction transaction = transactionDAO.findByid(transactionId);
        BankAccount bankAccount = transaction.getUser().getBankAccount();
        transaction.setTransactionStatus("approved");
        if (bankAccount != null) {
            if (transaction.getTransactionType().equals("d eposit")) {
                bankAccount.setBalance(bankAccount.getBalance() + transaction.getTransactionSum());
            } else if (transaction.getTransactionType().equals("withdrow")) {
                bankAccount.setBalance(bankAccount.getBalance() - transaction.getTransactionSum());
            }
            transactionDAO.save(transaction);
            bankAccountDAO.save(bankAccount);
        }
        else {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

}
