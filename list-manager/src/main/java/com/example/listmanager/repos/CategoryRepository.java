package com.example.listmanager.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
