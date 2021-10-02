package com.example.listmanager.repos;


import com.example.listmanager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
