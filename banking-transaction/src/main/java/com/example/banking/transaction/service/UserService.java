package com.example.banking.transaction.service;

import com.example.banking.transaction.dto.LoginRequest;
import com.example.banking.transaction.dto.RegisterRequest;
import com.example.banking.transaction.entity.User;
import com.example.banking.transaction.repository.UserRepository;
import com.example.banking.transaction.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;


@Service
public class UserService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public UserService (UserRepository userRepository,PasswordEncoder passwordEncoder , JwtUtil jwtUtil,UserDetailsService userDetailsService){
        this.userRepository= userRepository;
        this.passwordEncoder= passwordEncoder;
        this.jwtUtil=jwtUtil;
        this.userDetailsService= userDetailsService;
    }
    public User register(RegisterRequest request){

        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new RuntimeException("username already exsist");
        }

        User user= new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
    return userRepository.save(user);
    }
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("user not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        UserDetails userDetails= userDetailsService.loadUserByUsername(request.getUsername());
        return jwtUtil.generateToken(userDetails);
    }

}
