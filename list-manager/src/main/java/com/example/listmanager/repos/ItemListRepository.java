package com.example.listmanager.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.ItemList;

public interface ItemListRepository extends JpaRepository<ItemList, Long> {
}
