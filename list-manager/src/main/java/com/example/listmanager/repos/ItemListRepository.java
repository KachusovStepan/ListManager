package com.example.listmanager.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.ItemList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemListRepository extends JpaRepository<ItemList, Long> {
    Page<ItemList> findAll(Pageable pageable);
    Page<ItemList> findByCategory(Long categoryId, Pageable pageable);

//    @Query("SELECT t FROM Todo t WHERE " +
//            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
//            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
//    List<ItemList> findBySearchTerm(@Param("searchTerm") String searchTerm);
}
