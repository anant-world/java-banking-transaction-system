package com.example.banking.transaction.controller;


import com.example.banking.transaction.dto.LoginRequest;
import com.example.banking.transaction.dto.RegisterRequest;
import com.example.banking.transaction.entity.User;
import com.example.banking.transaction.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){

        userService.register(request);
        return ResponseEntity.status(201).body("User registered successfully");


    }
    @PostMapping("/login")
    public ResponseEntity<String> login (@Valid @RequestBody LoginRequest request){
        String response= userService.login(request);
        return ResponseEntity.ok(response);

    }

}
