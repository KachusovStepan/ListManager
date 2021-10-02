package com.example.listmanager.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
