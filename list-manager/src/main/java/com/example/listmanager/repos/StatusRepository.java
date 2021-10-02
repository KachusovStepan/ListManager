package com.example.listmanager.repos;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
