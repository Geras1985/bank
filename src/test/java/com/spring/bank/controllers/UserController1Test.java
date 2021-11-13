package com.spring.bank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.bank.entity.Transaction;
import com.spring.bank.entity.User;
import com.spring.bank.enums.Role;
import com.spring.bank.service.UserService;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserController1Test {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @InjectMocks
    UserController1 controller1;

    @Mock
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private User user = null;
    private Set<Transaction> set = null;
    private Transaction tr1 = null;
    private Transaction tr2 = null;


    @BeforeEach
    public void setup() {
        user = new User(1, "Test", "Testyan", 20, "Testing", "123456", Role.USER);
        LocalDate date = LocalDate.now();
        set = new HashSet<>();
        tr1 = new Transaction(1, "deposit", "pending", 300);
        tr2 = new Transaction(2, "withdraw", "approved", 300);
        set.add(tr1);
        set.add(tr2);
        mockMvc = MockMvcBuilders.standaloneSetup(controller1).build();
    }

    @AfterEach
    void tearDown() {
        user = null;
        set = null;
        tr1 = null;
        tr2 = null;
    }

//    @Test
//    void getUserById() throws Exception {
//        when(userService.getUserById(user.getId())).thenReturn(user);
//        mockMvc.perform(get("/user/1").
//                        contentType(MediaType.APPLICATION_JSON).
//                        content(asJsonString(user))).
//                andExpect(status().isOk()).
//                andDo(MockMvcResultHandlers.print());
//    }

    @Test
    public void createUserTest() throws Exception{
        when(userService.createUser(any())).thenReturn(user);
        mockMvc.perform(post("/user/register").
                        contentType(MediaType.APPLICATION_JSON).
                        content(asJsonString(user))).
                andExpect(status().isCreated());
        verify(userService,times(1)).createUser(any());
    }

    @Test
    public void loginUserTest() throws Exception {
        when(userService.checkLogin(any())).thenReturn(user);
        mockMvc.perform(post("/user/login").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(user))).
                andExpect(status().isOk());
        verify(userService,times(1)).checkLogin(any());
    }

    @Test
    public void userHistoryTest() throws Exception {
        when(userService.getUserHistory(any())).thenReturn(set);
        mockMvc.perform(get("/user/userHistory/1").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(set))).
                andExpect(status().isOk());
        verify(userService,times(1)).getUserHistory(any());
    }

    @Test
    public void userEditTest() throws Exception {
        when(userService.changeRoleOfUser(any(), any())).thenReturn(user);
        mockMvc.perform(put("/user/edit_users/1").
                contentType(MediaType.APPLICATION_JSON).
                content(asJsonString(user))).
                andExpect(status().isOk());
        verify(userService,times(1)).changeRoleOfUser(any(), any());
    }
}