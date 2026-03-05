package com.example.banking.transaction.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user/data")
    @PreAuthorize("hasRole('USER')")
    public String userData(){
        return "user data accessed";
    }
    @GetMapping("/admin/data")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminData(){
        return "Admin data accessed";
    }
}
