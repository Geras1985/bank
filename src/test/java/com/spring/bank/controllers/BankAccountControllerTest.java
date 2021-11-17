package com.spring.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.bank.entities.BankAccount;
import com.spring.bank.services.BankAccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(MockitoExtension.class)
public class BankAccountControllerTest {


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @InjectMocks
    BankAccountController bankController;

    @Mock
    BankAccountService bankAccountService;

    @Autowired
    private MockMvc mockMvc;

    private BankAccount bankAcc = null;

    @BeforeEach
    public void setup() {
        bankAcc = new BankAccount(0);
        mockMvc = MockMvcBuilders.standaloneSetup(bankController).build();
    }

    @AfterEach
    void tearDown() {
        bankAcc = null;
    }


    @Test
    public void createBankAccount() throws Exception{
        when(bankAccountService.createBankAccount(any(), any())).thenReturn(bankAcc);
        mockMvc.perform(post("/bank_account/create_bank_account/2").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(bankAcc))).
                andExpect(status().isOk());
        verify(bankAccountService,times(1)).createBankAccount(any(), any());
    }
}