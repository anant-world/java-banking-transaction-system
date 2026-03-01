package com.example.banking.transaction.service;

import com.example.banking.transaction.dto.LoginRequest;
import com.example.banking.transaction.dto.RegisterRequest;
import com.example.banking.transaction.entity.User;
import com.example.banking.transaction.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService (UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository= userRepository;
        this.passwordEncoder= passwordEncoder;

    }
    public User register(RegisterRequest request){

        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("username already exsist");
        }

        User user= new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
    return userRepository.save(user);
    }
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("user not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return "login successful";
    }


}
