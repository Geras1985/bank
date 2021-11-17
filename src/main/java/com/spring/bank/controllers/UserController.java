package com.spring.bank.controllers;

import com.spring.bank.entities.Transaction;
import com.spring.bank.entities.User;
import com.spring.bank.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

  final
  UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('read')")
  public ResponseEntity<List<User>> getAll() {
    List<User> userList = userService.getAllUsers();
    if (userList == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    } else {
      return new ResponseEntity<>(userList, HttpStatus.OK);
    }
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('read')")
  public ResponseEntity<User> getById(@PathVariable Integer id) {
    User user = userService.getUserById(id);
    if (user == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    } else {
      return new ResponseEntity<>(user, HttpStatus.OK);
    }
  }

  /**
   * @param user .
   * @return .
   */
  @PostMapping("/register")
  @PreAuthorize("hasAuthority('write')")
  public ResponseEntity<User> createUser(@RequestBody User user) {

    if (user.getUsername() == null || user.getUsername().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getFirstName() == null || user.getFirstName().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getLastName() == null || user.getLastName().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (user.getPassword() == null || user.getPassword().trim().equals("")) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else if (userService.getUserByUsername(user.getUsername()).isPresent()) {
      return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
    } else {
      User registeredUser = userService.createUser(user);
      return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<User> loginUser(@RequestBody User user) {

    User logUser = userService.checkLogin(user);
    if (logUser != null) {
      return new ResponseEntity<>(logUser, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/history/{id}")
  @PreAuthorize("hasAuthority('read')")
  public ResponseEntity<Set<Transaction>> userHistoryPost(@PathVariable Integer id) {
    Set<Transaction> set = userService.getUserHistory(id);
    if (set == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    } else {
      return new ResponseEntity<>(set, HttpStatus.OK);
    }
  }

  /**
   * @param user .
   * @return .
   */
  @PutMapping("/edit/{id}")
  public ResponseEntity<String> userEditPost(@PathVariable Integer id, @RequestBody User user) {
    User toBeChanged = userService.changeRoleOfUser(id, user);
    if (toBeChanged == null) {
      return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    } else {
      return new ResponseEntity<>("text ", HttpStatus.OK);
    }
  }
}