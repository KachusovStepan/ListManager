package com.example.listmanager.controllers;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import com.example.listmanager.errorHandling.EntityNotFoundException;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.listmanager.model.*;
import com.example.listmanager.repos.UserRepository;
import com.example.listmanager.repos.ItemListRepository;

@RestController
@RequestMapping("/api")
public class ListController {
//    private final UserRepository userRepository;
//    private final ItemListRepository listRepository;
//    public ListController(UserRepository userRepository, ItemListRepository listRepository) {
//        this.userRepository = userRepository;
//        this.listRepository = listRepository;
//    }
    private final IUserService userService;
    private final IListService listService;

    public ListController(IUserService userService, IListService listService) {
        this.userService = userService;
        this.listService = listService;
    }

    @GetMapping("itemstatuses")
    ResponseEntity<List<ItemStatus>> getItemStatuses() {
        return ResponseEntity.ok().body(listService.getItemStatuses());
    }

    @GetMapping("categories")
    ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok().body(listService.getListCategories());
    }


//    @GetMapping("/lists")
//    List<ItemList> getAllLists() {
//        return listRepository.findAll();
//    }
//
//    @GetMapping("/lists/{id}")
//    ItemList getOneList(@PathVariable Long id) {
//        ItemList list = listRepository
//                .findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(id, "List"));
//        return list;
//    }
//
//    @PostMapping("/lists")
//    ItemList newItemList(@RequestBody ItemList newItemList) {
//        return listRepository.save(newItemList);
//    }
//
//    @PutMapping("/lists/{id}")
//    ItemList replaceItemList(@RequestBody ItemList newItemList, @PathVariable Long id) {
//        ItemList itemList = listRepository.findById(id)
//                .map(il -> {
//                    // il.set...(newItemList.get...());
//                    return listRepository.save(il);
//                })
//                .orElseGet(() -> {
//                    newItemList.setId(id);
//                    return listRepository.save(newItemList);
//                });
//        return itemList;
//    }
//
//    @DeleteMapping("/lists/{id}")
//    void deleteItemList(@PathVariable Long id) {
//        listRepository.deleteById(id);
//    }
}
