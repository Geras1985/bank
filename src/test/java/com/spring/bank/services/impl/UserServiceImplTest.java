package com.spring.bank.services.impl;

import com.spring.bank.entities.Transaction;
import com.spring.bank.entities.User;
import com.spring.bank.enums.Role;
import com.spring.bank.repositories.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl service;

    @Mock
    UserRepo userRepo;

    private User user = null;
    private User user1 = null;
    private User user2 = null;
    private User userAdmin = null;
    private User userToBeChangedRole = null;
    private String encodedPass = null;

    @BeforeEach
    public void setup() {
        user = new User("Test", "Testyan", 20, "Testing", "123456", Role.USER);
        encodedPass = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        user1 = new User(1, "Test", "Testyan", 20, "Testing", encodedPass, Role.USER);
        userAdmin = new User(10, "Test", "Testyan", 20, "Testing", encodedPass, Role.ADMIN);
        user2 = new User(1, "Test", "Testyan", 20, "Testing", "123456", Role.USER);
        userToBeChangedRole = new User(1, "Test", "Testyan", 20, "Testing", "123456", Role.ADMIN);
        Set<Transaction> set = new HashSet<>();
        Transaction tr1 = new Transaction(1, "deposit", "pending", 300, user1);
        Transaction tr2 = new Transaction(2, "withdraw", "approved", 300, user2);
        set.add(tr1);
        set.add(tr2);
        user1.setTransactions(set);
    }

    @AfterEach
    void tearDown() {
        user = null;
        user1 = null;
        user2 = null;
        encodedPass = null;
        userAdmin = null;
        userToBeChangedRole = null;
    }

    @Test
    public void createUser() {
        when(userRepo.save(any())).thenReturn(user1);
        User savedUser = service.createUser(user);
        assertEquals(savedUser.getPassword(), encodedPass);
    }

    @Test
    public void checkLogin() {
        when(service.getUserByUsername(any())).thenReturn(java.util.Optional.ofNullable(user1));
        User result = service.checkLogin(user2);
        assertNotNull(result);
    }

    @Test
    public void getUserHistory() {
        when(service.getUserById(any())).thenReturn(user1);
        Set<Transaction> result = service.getUserHistory(user1.getId());
        assertEquals(2, result.size());
    }

    @Test
    public void changeRoleOfUser() {
        when(userRepo.findByid(any())).thenReturn(userAdmin);
        when(userRepo.findByUsername(any())).thenReturn(java.util.Optional.ofNullable(user2));
        when(userRepo.save(any())).thenReturn(null);
        User result = service.changeRoleOfUser(10, userToBeChangedRole);
        assertEquals("ADMIN", result.getRole().name());
    }
}