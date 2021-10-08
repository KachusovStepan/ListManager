package com.example.listmanager.controllers;

import com.example.listmanager.model.ItemList;
import com.example.listmanager.model.User;
import com.example.listmanager.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/login")
//    @CrossOrigin(origins = "*")
//    public String login() {
//        return "Success";
//    }

    @GetMapping("user")
//    @CrossOrigin(origins = "http://localhost:4200")
    ResponseEntity<User> getCurrentUser(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        User user = this.userService.getUser(userName);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("register")
    ResponseEntity<User> registerUser(@RequestBody User user) {
        User userFound = this.userService.getUser(user.getUsername());
        if (userFound != null) {
            return ResponseEntity.badRequest().build();
        }
        User savedUser = this.userService.saveUser(user);
        this.userService.addRoleToUser(savedUser.getUsername(), "ROLE_USER");
        return ResponseEntity.ok().body(savedUser);
    }

    @GetMapping("user/lists")
    ResponseEntity<List<ItemList>> getUserLists(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        User user = this.userService.getUser(userName);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user.getLists());
    }

    @GetMapping("/users")
    List<User> getAllUsers() { return userService.getUsers(); }

//    @GetMapping("/user")
//    User GetUserByName(@RequestParam(defaultValue = "unknown") String name) { return userRepository.findByUsername(name); }
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
