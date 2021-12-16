package com.example.listmanager.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.ItemStatus;


public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {
    ItemStatus findByName(String name);
}
