package com.spring.bank.controllers;


import com.spring.bank.entity.Transaction;
import com.spring.bank.entity.User;
import com.spring.bank.repository.BankAccountDAO;
import com.spring.bank.repository.TransactionDAO;
import com.spring.bank.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    UserDAO userDAO;
    @Autowired
    BankAccountDAO bankAccountDAO;
    @Autowired
    TransactionDAO transactionDAO;



//    @GetMapping("/deposit/{id}")
//    public ResponseEntity<Transaction> depositGet(ModelMap model, @PathVariable Integer id){
//        Transaction transaction = new Transaction();
//        User user = userDAO.findByid(id);
//        Set<Transaction> trSet = user.getTransactions();
//        for (Transaction t:trSet){
//            String transactionType = t.getTransactionType();
//            transaction.setTransactionType(transactionType);
//        }
//
//
//
//        model.addAttribute("user", user);
//
//        return new ResponseEntity<>(transaction, HttpStatus.OK);
//    }

    @PostMapping("/deposit/{id}")
    public String depositPost(ModelMap model, @PathVariable Integer id,
                              @RequestParam(name="deposit_sum", required=false) Integer depositSum,
                              @RequestParam(name="user_id", required=false) String userID){

        User user = userDAO.findByid(id);
        if (user.getBankAccount() != null) {
            Transaction transaction = new Transaction();
            LocalDate date = LocalDate.now();
            transaction.setCreatedAt(date);
            transaction.setTransactionType("deposit");
            transaction.setUser(user);
            transaction.setTransactionStatus("pending");
            transaction.setTransactionSum(depositSum);
            transactionDAO.save(transaction);
        }
        else {
            //some text that user doesnt have bank account, so cant do transactions
        }
        model.addAttribute("user", user);
        return "redirect:/user_page/" + id;
    }

    @GetMapping("/withdrow/{id}")
    public String withdrowGet(ModelMap model, @PathVariable Integer id){

        User user = userDAO.findByid(id);
        model.addAttribute("user", user);
        return "/withdrow";
    }

    @PostMapping("/withdrow/{id}")
    public String withdrowPost(ModelMap model, @PathVariable Integer id,
                               @RequestParam(name="withdrow_sum", required=false) Integer depositSum,
                               @RequestParam(name="user_id", required=false) String userID){

        User user = userDAO.findByid(id);
        if (user.getBankAccount() != null) {
            Transaction transaction = new Transaction();
            LocalDate date = LocalDate.now();
            transaction.setCreatedAt(date);
            transaction.setTransactionType("withdrow");
            transaction.setUser(user);
            transaction.setTransactionStatus("pending");
            transaction.setTransactionSum(depositSum);
            transactionDAO.save(transaction);
        }
        else {
            //some text that user doesnt have bank account, so cant do transactions
        }

        model.addAttribute("user", user);
        return "redirect:/user_page/" + id;
    }

}
