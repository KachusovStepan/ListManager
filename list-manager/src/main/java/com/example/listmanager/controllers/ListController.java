package com.example.listmanager.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;


import com.example.listmanager.repos.ItemListRepository;
import com.example.listmanager.repos.UserRepository;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.listmanager.model.*;


@RestController
@RequestMapping("/api")
public class ListController {
    private static final Logger log = LoggerFactory.getLogger(ListController.class);
    private final IUserService userService;
    private final IListService listService;
    private final ItemListRepository itemListRepository;
    private final UserRepository userRepository;

    public ListController(
            IUserService userService, IListService listService,
            ItemListRepository itemListRepository, UserRepository userRepository) {
        this.userService = userService;
        this.listService = listService;
        this.itemListRepository = itemListRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("itemstatuses")
    ResponseEntity<List<ItemStatus>> getItemStatuses() {
        List<ItemStatus> itemStatuses = listService.getItemStatuses();
        log.info("getItemStatuses Ok with " + itemStatuses.size() + " elements");
        return ResponseEntity.ok().body(itemStatuses);
    }

    @GetMapping("categories")
    ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = listService.getListCategories();
        log.info("getCategories Ok with " + categories.size() + " elements");
        return ResponseEntity.ok().body(categories);
    }


    @GetMapping("/lists")
    ResponseEntity<List<ItemList>> getAllLists() {
        List<ItemList> lists = itemListRepository.findAll();
        log.info("getAllLists Ok with " + lists + " elements");
        return ResponseEntity.ok().body(lists);
    }

    @GetMapping("/lists/{id}")
    ResponseEntity<ItemList> getOneList(@PathVariable Long id) {
        Optional<ItemList> optionalItemList = itemListRepository.findById(id);
        if (optionalItemList.isPresent()) {
            log.info("getOneList Ok found with id: " + id);
            return ResponseEntity.ok().body(optionalItemList.get());
        }
        log.info("getOneList Not Found with id: " + id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/lists")
    ResponseEntity<ItemList> newItemList(@RequestBody ItemList newItemList) {
        ItemList savedItemList = itemListRepository.save(newItemList);
        URI location = URI.create(String.format("api/lists/%d", savedItemList.getId()));
        log.info("newItemList created at: " + location.toString());
        return ResponseEntity.created(location).body(savedItemList);
    }


    @DeleteMapping("/lists/{id}")
    ResponseEntity<?> deleteItemList(@PathVariable Long id) {
        Optional<ItemList> optionalItemList = itemListRepository.findById(id);
        if (optionalItemList.isEmpty()) {
            log.info("deleteItemList Not Found with id: " + id);
            return ResponseEntity.notFound().build();
        }
        itemListRepository.deleteById(id);
        log.info("deleteItemList deleted with id: " + id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/lists")
    ResponseEntity<List<ItemList>> getUserLists(@PathVariable Long userId) {
        User user = userRepository.getById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<ItemList> userLists = user.getLists();
        return ResponseEntity.ok().body(userLists);
    }

//    @PutMapping("/users/{userId}/lists")
//    ResponseEntity<ItemList> saveUserList(@PathVariable Long userId, @RequestBody ItemList newItemList) {
//        User user = userRepository.getById(userId);
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//        if (newItemList.getId() != null) {
//            if (itemListRepository.existsById(newItemList.getId())) {
//                if (user.getLists().stream().anyMatch(il -> il.getId() == newItemList.getId())) {
//                    ItemList updatedList = listService.saveList(newItemList);
//                    return ResponseEntity.ok().body(updatedList);
//                } else {
//                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
//                }
////                ItemList itemListFromRepo = optionalItemList.get();
//            }
//        }
//        ItemList createdItemList = listService.saveList(newItemList);
//        List<ItemList> userLists = user.getLists();
//        return ResponseEntity.ok().body(userLists);
//    }
}
