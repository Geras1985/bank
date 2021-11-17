package com.spring.bank.repositories;

import com.spring.bank.entities.User;
import com.spring.bank.enums.Role;
import com.spring.bank.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserRepoTest {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserService service;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Test", "Testyan", 25, "testing", "123456", Role.USER);
    }


    @AfterEach
    public void truncate() {
        // userRepository.deleteAll();
        user = null;
    }

    @Test
    @Rollback
    void findByUsername() {
    }

    @Test
    void findByid() {
        userRepo.save(user);

        User userById = userRepo.findByid(user.getId());

        assertEquals(userById.getUsername(), user.getUsername());
        assertEquals(userById.getRole(), user.getRole());
    }
}