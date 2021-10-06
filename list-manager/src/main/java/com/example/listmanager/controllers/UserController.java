package com.example.listmanager.controllers;

import com.example.listmanager.model.User;
import com.example.listmanager.repos.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
//    @CrossOrigin(origins = "*")
    public String login() {
        return "Success";
    }

    @GetMapping("/users")
    List<User> getAllUsers() { return userRepository.findAll(); }

    @GetMapping("/user")
    User GetUserByName(@RequestParam(defaultValue = "unknown") String name) { return userRepository.findByUsername(name); }
}
