package com.example.listmanager.services;

import com.example.listmanager.model.Role;
import com.example.listmanager.model.User;

import java.util.List;

public interface IUserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
