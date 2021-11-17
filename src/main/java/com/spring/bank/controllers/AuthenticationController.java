package com.spring.bank.controllers;

import com.spring.bank.entities.User;
import com.spring.bank.models.request.AuthRequest;
import com.spring.bank.repositories.UserRepo;
import com.spring.bank.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepo userRepo, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
        try {

            org.springframework.security.crypto.password.PasswordEncoder encoder
                    = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            String encoderPass = encoder.encode(request.getPassword());

            System.out.println(encoder.matches("123456", encoderPass));

            System.out.println(request.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), "123456"));

            User user = userRepo.findByUsername(request.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User doesn't exist"));
            String token = jwtTokenProvider.createToken(request.getUsername(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", request.getUsername());
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid username or password.", HttpStatus.FORBIDDEN);

        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

}