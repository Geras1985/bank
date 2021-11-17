package com.spring.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.bank.entities.Transaction;
import com.spring.bank.entities.User;
import com.spring.bank.enums.Role;
import com.spring.bank.services.TransactionService;
import com.spring.bank.services.impl.TransactionServiceImpl;
import com.spring.bank.services.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @InjectMocks
    TransactionController controller;


    private final TransactionService service = Mockito.mock(TransactionService.class);
    private final TransactionServiceImpl serviceImpl = Mockito.mock(TransactionServiceImpl.class);
    private final UserServiceImpl userService = Mockito.mock(UserServiceImpl.class);

    @Autowired
    private MockMvc mockMvc;

    private Transaction transaction = null;
    private User user = null;

    @BeforeEach
    public void setup() {
        user = new User(10, "Test", "Testyan", 25, "testing", "123456", Role.USER);
        transaction = new Transaction(1, "deposit", "pending", 500, user);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        transaction = null;
        user = null;
    }

    @Test
    void makeTransactionPost() throws Exception {
        when(service.createTransaction(any(),any())).thenReturn(true);
        mockMvc.perform(post("/transaction/make_transaction/10").
                        contentType(MediaType.APPLICATION_JSON).content(asJsonString(transaction))).
                andExpect(status().isOk());
        verify(service,times(1)).createTransaction(any(),any());

    }

    @Test
    void cancelTransaction() throws Exception {
        when(service.cancelTransaction(user.getId(), transaction)).thenReturn(true);
        mockMvc.perform(delete("/transaction/cancel_transaction/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(user))).
                andExpect(status().isOk()).
                andDo(MockMvcResultHandlers.print());
    }

    @Test
    void acceptTransactionsGet() throws Exception {
        when(service.acceptTransaction(user.getId(), transaction)).thenReturn(true);
        mockMvc.perform(get("/transaction/accept_transactions/1").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(user))).
                andExpect(status().isOk()).
                andDo(print());
    }

    @Test
    void acceptTransactionsPost() throws Exception {
        when(service.acceptTransaction(user.getId(), transaction)).thenReturn(true);
        mockMvc.perform(post("/transaction/accept_transactions/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(user))).
                andExpect(status().isOk()).
                andDo(print());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}