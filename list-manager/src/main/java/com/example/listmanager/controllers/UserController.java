package com.example.listmanager.controllers;

import com.example.listmanager.model.ItemList;
import com.example.listmanager.model.User;
import com.example.listmanager.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequestMapping("/api")
@RestController
public class UserController {
    private final IUserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("user")
    ResponseEntity<User> getCurrentUser(Principal principal) {
        log.info("getCurrentUser");
        if (principal == null) {
            log.info("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        log.info("Principal.Name == " + userName);
        User user = this.userService.getUser(userName);
        if (user == null) {
            log.info("user not found in db");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("register")
    ResponseEntity<User> registerUser(@RequestBody User user) {
        log.info("registerUser name: " + user.getUsername() + " pass: " + user.getPassword());
        User userFound = this.userService.getUser(user.getUsername());
        if (userFound != null) {
            log.info("User with this name already exists");
            return ResponseEntity.badRequest().build();
        }
        User savedUser = this.userService.saveUser(user);
        this.userService.addRoleToUser(savedUser.getUsername(), "ROLE_USER");
        return ResponseEntity.ok().body(savedUser);
    }

    @GetMapping("user/lists")
    ResponseEntity<List<ItemList>> getUserLists(Principal principal) {
        log.info("getUserLists");
        if (principal == null) {
            log.info("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        User user = this.userService.getUser(userName);
        if (user == null) {
            log.info("user with name " + userName + " not found in db");
            return ResponseEntity.notFound().build();
        }
        log.info("Returning Ok with " + user.getLists().size() + " elements");
        return ResponseEntity.ok().body(user.getLists());
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        List<User> users = userService.getUsers();
        log.info("getAllUsers Ok with " + users.size() + " elements");
        return users;
    }
}


class ResponseSimpleInfo {
    String status;
    String message;

    public ResponseSimpleInfo(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseSimpleInfo() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
