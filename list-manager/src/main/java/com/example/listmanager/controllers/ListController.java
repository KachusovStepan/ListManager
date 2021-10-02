package com.example.listmanager.controllers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import com.example.listmanager.errorHandling.EntityNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.listmanager.model.*;
import com.example.listmanager.repos.UserRepository;
import com.example.listmanager.repos.ItemListRepository;

@RestController
public class ListController {
    private final UserRepository userRepository;
    private final ItemListRepository listRepository;
    public ListController(UserRepository userRepository, ItemListRepository listRepository) {
        this.userRepository = userRepository;
        this.listRepository = listRepository;
    }

    @GetMapping("/lists")
    List<ItemList> getAllLists() {
        return listRepository.findAll();
    }

    @GetMapping("/lists/{id}")
    ItemList getOneList(@PathVariable Long id) {
        ItemList list = listRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, "List"));
        return list;
    }

    @PostMapping("/lists")
    ItemList newItemList(@RequestBody ItemList newItemList) {
        return listRepository.save(newItemList);
    }

    @PutMapping("/lists/{id}")
    ItemList replaceItemList(@RequestBody ItemList newItemList, @PathVariable Long id) {
        ItemList itemList = listRepository.findById(id)
                .map(il -> {
                    // il.set...(newItemList.get...());
                    return listRepository.save(il);
                })
                .orElseGet(() -> {
                    newItemList.setId(id);
                    return listRepository.save(newItemList);
                });
        return itemList;
    }

    @DeleteMapping("/lists/{id}")
    void deleteItemList(@PathVariable Long id) {
        listRepository.deleteById(id);
    }
}
