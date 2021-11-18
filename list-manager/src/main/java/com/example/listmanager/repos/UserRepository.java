package com.example.listmanager.repos;

import com.example.listmanager.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String name);
//    Page<ItemList> getAllByUserAndCategory_NameContainingAndNameContaining(User user, String category, String name, Pageable pageable);
    Page<User> getAllByRolesIsContaining(Role role, Pageable pageable);
}