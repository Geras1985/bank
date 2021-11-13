package com.spring.bank.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.bank.entity.BankAccount;
import com.spring.bank.entity.User;
import com.spring.bank.enums.Role;
import com.spring.bank.repository.BankAccountRepo;
import com.spring.bank.repository.UserRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceImplTest {

    @InjectMocks
    BankAccountServiceImpl service;

    @Mock
    UserRepo userRepo;

    @Mock
    BankAccountRepo bankAccountRepo;


    private User user = null;
    private User user1 = null;
    private BankAccount bankAcc = null;

    @BeforeEach
    public void setup() {
        user = new User(1, "Test", "Testyan", 20, "Testing", "123456", Role.USER);
        user1 = new User(2, "TestAdmin", "TestyanAdmin", 20, "TestingAdmin", "123456", Role.ADMIN);
        bankAcc = new BankAccount(0);
    }

    @AfterEach
    void tearDown() {
        user = null;
        user1 = null;
        bankAcc = null;
    }

    @Test
    public void createBankAccountTest() throws Exception {
        when(userRepo.findByid(any())).thenReturn(user1);
        when(userRepo.findByUsername(any())).thenReturn(user);
        when(bankAccountRepo.save(any())).thenReturn(bankAcc);
        when(userRepo.save(any())).thenReturn(null);
        BankAccount acc = service.createBankAccount(1, user);
        Assertions.assertNotNull(acc);
        Assertions.assertEquals(0, acc.getBalance());
    }

}