package com.example.listmanager.controllers;

import com.example.listmanager.model.User;
import com.example.listmanager.repos.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    List<User> getAllUsers() { return userRepository.findAll(); }

    @GetMapping("/user")
    User GetUserByName(@RequestParam(defaultValue = "unknown") String name) { return userRepository.findByUsername(name); }
}
