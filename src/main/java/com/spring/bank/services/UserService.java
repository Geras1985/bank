package com.spring.bank.services;


import com.spring.bank.entities.Transaction;
import com.spring.bank.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User createUser(User user);
    Optional<User> getUserByUsername(String username);
    User getUserById(Integer userId);
    User checkLogin(User user);

    List<User> getAllUsers();

    Set<Transaction> getUserHistory(Integer userId);
    User changeRoleOfUser(Integer loggedId, User toBeChangedUser);

}
