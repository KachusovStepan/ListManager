package com.example.listmanager.repos;

import com.example.listmanager.model.Category;
import com.example.listmanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.listmanager.model.ItemList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemListRepository extends JpaRepository<ItemList, Long> {
    Page<ItemList> findAll(Pageable pageable);
    Page<ItemList> findByCategory(Long categoryId, Pageable pageable);

    @Query("SELECT il FROM ItemList il WHERE il.category = :scategory AND il.user = :suser")
    List<ItemList> findByUserAndCategory(@Param("suser") User suser, @Param("scategory") Category scategory);

//    List<ItemList> getAllByUserOrderByCategoryAsc(User suser, Pageable pageable);
//    List<ItemList> getAllByUserOrderByCategoryDesc(User suser, Pageable pageable);
//    List<ItemList> getAllByUserAndCategoryOrderByNameAsc(User user, Category category, Pageable pageable);
//    List<ItemList> getAllByUserAndCategoryOrderByNameDesc(User user, Category category, Pageable pageable);
    Page<ItemList> getAllByUserAndNameContaining(User user, String name, Pageable pageable);
    Page<ItemList> getAllByUserAndCategory(User user, Category category, Pageable pageable);
    Page<ItemList> getAllByUserAndCategoryAndNameContaining(User user, Category category, String name, Pageable pageable);
    Page<ItemList> getAllByUserAndCategory_NameAndNameContaining(User user, String category, String name, Pageable pageable);
    Page<ItemList> getAllByUserAndCategory_NameContainingAndNameContaining(User user, String category, String name, Pageable pageable);
    Page<ItemList> getAllByUser(User user, Pageable pageable);

    @Query("SELECT il FROM ItemList il WHERE il.user = :suser")
    List<ItemList> getAllByUserQuery(@Param("suser") User suser, Pageable pageable);

//    @Query("SELECT t FROM Todo t WHERE " +
//            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
//            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
//    List<ItemList> findBySearchTerm(@Param("searchTerm") String searchTerm);
}
