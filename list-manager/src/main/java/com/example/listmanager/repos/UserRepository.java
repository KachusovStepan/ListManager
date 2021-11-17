package com.example.listmanager.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String name);
}