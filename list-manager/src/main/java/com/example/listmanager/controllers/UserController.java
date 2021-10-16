package com.example.listmanager.controllers;

import com.example.listmanager.model.*;
import com.example.listmanager.repos.*;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequestMapping("/api")
@RestController
public class UserController {
    private final IUserService userService;
    private final IListService listService;

    private final ItemListRepository itemListRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemStatusRepository itemStatusRepository;
    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public UserController(
            IUserService userService, IListService listService,

            ItemListRepository itemListRepository,
            ItemRepository itemRepository,
            CategoryRepository categoryRepository,
            ItemStatusRepository itemStatusRepository,
            UserRepository userRepository
    ) {
        this.userService = userService;
        this.listService = listService;

        this.itemListRepository = itemListRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemStatusRepository = itemStatusRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("user")
    ResponseEntity<User> getCurrentUser(Principal principal) {
        log.info("getCurrentUser");
        if (principal == null) {
            log.info("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        log.info("Principal.Name == " + userName);
        User user = this.userService.getUser(userName);
        if (user == null) {
            log.info("user not found in db");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("register")
    ResponseEntity<User> registerUser(@RequestBody User user) {
        log.info("registerUser name: " + user.getUsername() + " pass: " + user.getPassword());
        User userFound = this.userService.getUser(user.getUsername());
        if (userFound != null) {
            log.info("User with this name already exists");
            return ResponseEntity.badRequest().build();
        }
        User savedUser = this.userService.saveUser(user);
        this.userService.addRoleToUser(savedUser.getUsername(), "ROLE_USER");
        return ResponseEntity.ok().body(savedUser);
    }

    @GetMapping("user/lists")
    ResponseEntity<List<ItemList>> getUserLists(
            Principal principal,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        log.info("getUserLists");
        if (principal == null) {
            log.info("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        User user = this.userService.getUser(userName);
        if (user == null) {
            log.info("user with name " + userName + " not found in db");
            return ResponseEntity.notFound().build();
        }
        log.info("Returning Ok with " + user.getLists().size() + " elements");

        List<ItemList> resultLists = user.getLists();
        if (category != null && category.length() > 0) {
            Category requestedCategory = categoryRepository.findByName(category);
            if (requestedCategory == null) {
                ResponseEntity.ok().body(new ArrayList<>());
            }
            resultLists = resultLists.stream()
                    .filter(l -> l.getCategory().getId() == requestedCategory.getId())
                    .collect(Collectors.toList());
        }
        if (sort != null && sort.length() > 0) {
            boolean success = sortItemListsBy(resultLists, sort);
            if (!success) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
        }
        return ResponseEntity.ok().body(resultLists);
    }

    private boolean sortItemListsBy(List<ItemList> list, String sortParam) {
        String[] parts = sortParam.split(",");
        if (parts[0].equals("name")) {
            if (parts.length > 1 && parts[1] == "desc") {
                list.sort((l1, l2) -> -l1.getName().compareTo(l2.getName()));
            } else {
                list.sort(Comparator.comparing(ItemList::getName));
            }
            return true;
        }
        if (parts[0].equals("category")) {
            if (parts.length > 1 && parts[1].equals("desc")) {
                list.sort((l1, l2) -> -l1.getCategory().getName().compareTo(l2.getCategory().getName()));
            } else {
                list.sort(Comparator.comparing(l -> l.getCategory().getName()));
            }
            return true;
        }
        if (parts[0].equals("count")) {
            if (parts.length > 1 && parts[1].equals("desc")) {
                list.sort((l1, l2) -> -(l1.getItems().size() - l2.getItems().size()));
            } else {
                list.sort(Comparator.comparingInt(l -> l.getItems().size()));
            }
            return true;
        }
        return false;
    }

    @PostMapping("user/lists")
//    @Transactional
    ResponseEntity<ItemList> saveUserList(Principal principal, @RequestBody ItemList toSave) {
        if (principal == null) {
            log.info("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        User user = this.userService.getUser(userName);
        if (user == null) {
            log.info("user with name " + userName + " not found in db");
            return ResponseEntity.notFound().build();
        }
        // check if it belongs to
        if (toSave.getId() != null) {
            if (user.getLists().stream().noneMatch(l -> l.getId() == toSave.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        if (toSave.getCategory() != null) {
            toSave.setCategory(categoryRepository.findByName(toSave.getCategory().getName()));
        }
        for (Item item : toSave.getItems()) {
            item.setStatus(itemStatusRepository.findByName(item.getStatus().getName()));
        }
        // delete item if not present ^
        ItemList savedList = listService.saveList(toSave);
        if (user.getLists().stream().noneMatch(l -> l.getId() == savedList.getId())) {
            List<ItemList> userLists = user.getLists();
            userLists.add(savedList);
            user.setLists(userLists);
            userRepository.save(user);
        }

        return ResponseEntity.ok().body(toSave);
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        List<User> users = userService.getUsers();
        log.info("getAllUsers Ok with " + users.size() + " elements");
        return users;
    }
}


class ResponseSimpleInfo {
    String status;
    String message;

    public ResponseSimpleInfo(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseSimpleInfo() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
