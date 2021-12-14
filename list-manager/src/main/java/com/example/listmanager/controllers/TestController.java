package com.example.listmanager.controllers;


import com.example.listmanager.model.Category;
import com.example.listmanager.model.ItemList;
import com.example.listmanager.model.User;
import com.example.listmanager.model.dto.ItemListItemVerboseToGetDto;
import com.example.listmanager.model.dto.ItemListToGetDto;
import com.example.listmanager.model.dto.UserToGetDto;
import com.example.listmanager.repos.ItemListRepository;
import com.example.listmanager.repos.UserRepository;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<List<UserToGetDto>> getAllUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok().body(users.stream().map(u -> mapper.map(u, UserToGetDto.class)).collect(Collectors.toList()));
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

    @GetMapping("/users/{userId}/lists/sort")
    ResponseEntity<List<ItemListToGetDto>> getUserListsSorted(
            @PathVariable Long userId,
            @RequestParam(value = "sortBy", defaultValue="id", required = false) String sortBy,
            @RequestParam(value = "pageIndex",  defaultValue="0",  required = false) int pageIndex,
            @RequestParam(value = "pageSize",  defaultValue="8",  required = false) int pageSize,
            @RequestParam(value = "name", defaultValue="", required = false) String name,
            @RequestParam(value = "categoryName", defaultValue="", required = false) String categoryName
    ) {
        User user = userRepository.getById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!sortBy.equals("category") && !sortBy.equals("name") && !sortBy.equals("id")) {
            sortBy = "id";
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Page<ItemList> lists;
        lists = itemListRepository.getAllByUserAndCategory_NameContainingAndNameContaining(user, categoryName, name, PageRequest.of(pageIndex, pageSize, sort));

        List<ItemListToGetDto> itemListToGetDtos = lists.stream()
                .map(il -> mapper.map(il, ItemListToGetDto.class)).collect(Collectors.toList());

        return ResponseEntity.ok().body(itemListToGetDtos);
    }

    @GetMapping("/users/{userId}/lists/verbose")
    ResponseEntity<List<ItemListItemVerboseToGetDto>> getUserListsVerbose(
            @PathVariable Long userId,
            @RequestParam(value = "sortBy", defaultValue="id", required = false) String sortBy,
            @RequestParam(value = "pageIndex",  defaultValue="0",  required = false) int pageIndex,
            @RequestParam(value = "pageSize",  defaultValue="8",  required = false) int pageSize,
            @RequestParam(value = "name", defaultValue="", required = false) String name,
            @RequestParam(value = "categoryName", defaultValue="", required = false) String categoryName
    ) {
        User user = userRepository.getById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!sortBy.equals("category") && !sortBy.equals("name") && !sortBy.equals("id")) {
            sortBy = "id";
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Page<ItemList> lists;
        lists = itemListRepository.getAllByUserAndCategory_NameContainingAndNameContaining(user, categoryName, name, PageRequest.of(pageIndex, pageSize, sort));

        List<ItemListItemVerboseToGetDto> itemListToGetDtos = lists.stream()
                .map(il -> mapper.map(il, ItemListItemVerboseToGetDto.class)).collect(Collectors.toList());

        return ResponseEntity.ok().body(itemListToGetDtos);
    }
}