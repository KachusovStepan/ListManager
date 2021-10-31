package com.example.listmanager.controllers;


import com.example.listmanager.model.Category;
import com.example.listmanager.model.ItemList;
import com.example.listmanager.model.User;
import com.example.listmanager.model.dto.ItemListToGetDto;
import com.example.listmanager.model.dto.UserToGetDto;
import com.example.listmanager.repos.ItemListRepository;
import com.example.listmanager.repos.UserRepository;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.hibernate.boot.archive.scan.spi.ClassDescriptor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    private final IUserService userService;
    private final IListService listService;
    private final ItemListRepository itemListRepository;
    private final UserRepository userRepository;
    private ModelMapper mapper;

    public TestController(
            IUserService userService, IListService listService, ModelMapper mapper,
            ItemListRepository itemListRepository, UserRepository userRepository) {
        this.userService = userService;
        this.listService = listService;
        this.itemListRepository = itemListRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @GetMapping("/users")
    ResponseEntity<UserToGetDto> getAllUsers() {
        List<User> users = userService.getUsers();
        User user = users.get(1);
        UserToGetDto userDto = mapper.map(user, UserToGetDto.class);
        return ResponseEntity.ok().body(userDto);
    }

    @GetMapping("/users/{userId}/lists/{listId}")
    ResponseEntity<ItemListToGetDto> getUserList(@PathVariable Long userId, @PathVariable Long listId) {
        User user = userRepository.getById(userId);
        Optional<ItemList> oil = user.getLists().stream().filter(l -> l.getId() == listId).findFirst();
        if (oil.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ItemListToGetDto itemListToGetDto = mapper.map(oil.get(), ItemListToGetDto.class);
        return ResponseEntity.ok().body(itemListToGetDto);
    }

    @GetMapping("/users/{userId}/lists")
    ResponseEntity<List<ItemListToGetDto>> getUserListExt(
            @PathVariable Long userId,
            @RequestParam(value = "category", required = false) String categoryName
    ) {
        User user = userRepository.getById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<ItemList> lists;
        if (categoryName != null && categoryName.length() > 0) {
            Category category = listService.getCategory(categoryName);
            if (category == null) {
                return ResponseEntity.notFound().build();
            }
            lists = itemListRepository.findByUserAndCategory(user, category);
        } else {
            lists = user.getLists();
        }

        List<ItemListToGetDto> itemListToGetDtos = lists.stream()
                .map(il -> mapper.map(il, ItemListToGetDto.class)).collect(Collectors.toList());

        return ResponseEntity.ok().body(itemListToGetDtos);
    }
}