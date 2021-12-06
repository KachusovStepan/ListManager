package com.example.listmanager.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.listmanager.model.*;
import com.example.listmanager.model.dto.ItemListItemVerboseToGetDto;
import com.example.listmanager.model.dto.UserToGetDto;
import com.example.listmanager.model.dto.UserToPostDto;
import com.example.listmanager.repos.*;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RequestMapping("/api/user")
@RestController
@Transactional
public class UserController {
    private final IUserService userService;
    private final IListService listService;

    private final ItemListRepository itemListRepository;
    private final CategoryRepository categoryRepository;
    private final ItemStatusRepository itemStatusRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(
            IUserService userService, IListService listService,

            ItemListRepository itemListRepository,
            CategoryRepository categoryRepository,
            ItemStatusRepository itemStatusRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            ModelMapper mapper
    ) {
        this.userService = userService;
        this.listService = listService;

        this.itemListRepository = itemListRepository;
        this.categoryRepository = categoryRepository;
        this.itemStatusRepository = itemStatusRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.mapper = mapper;
    }

    @GetMapping
    ResponseEntity<UserToGetDto> getCurrentUser(Principal principal) {
        log.info("getCurrentUser");
        String userName = principal.getName();
        log.info("Principal.Name == " + userName);
        User user = userService.getUser(userName);
        if (user == null) {
            log.warn("user not found in db");
            return ResponseEntity.notFound().build();
        }

        UserToGetDto userToGetDto = mapper.map(user, UserToGetDto.class);
        return ResponseEntity.ok().body(userToGetDto);
    }

    @GetMapping("lists")
    ResponseEntity<CustomPage<ItemListItemVerboseToGetDto>> getUserItemLists(
            Principal principal,
            @RequestParam(value = "sortBy", defaultValue="id", required = false) String sortBy,
            @RequestParam(value = "pageIndex",  defaultValue="0",  required = false) int pageIndex,
            @RequestParam(value = "pageSize",  defaultValue="8",  required = false) int pageSize,
            @RequestParam(value = "name", defaultValue="", required = false) String name,
            @RequestParam(value = "categoryName", defaultValue="", required = false) String categoryName
    ) {
        String userName = principal.getName();
        User user = userService.getUser(userName);
        if (user == null) {
            log.warn("user with name " + userName + " not found in db");
            return ResponseEntity.notFound().build();
        }
        if (!sortBy.equals("category") && !sortBy.equals("name") && !sortBy.equals("id")) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Page<ItemList> lists;
        lists = itemListRepository.getAllByUserAndCategory_NameContainingAndNameContaining(user, categoryName, name, PageRequest.of(pageIndex, pageSize, sort));
        int totalCount = (int) lists.getTotalElements();
        int totalPageCount = lists.getTotalPages();
        List<ItemListItemVerboseToGetDto> itemListToGetDtos = lists.stream()
                .map(il -> mapper.map(il, ItemListItemVerboseToGetDto.class)).collect(Collectors.toList());
        CustomPage<ItemListItemVerboseToGetDto> p = new CustomPage<>(itemListToGetDtos, totalCount, totalPageCount, pageIndex, pageSize);
        return ResponseEntity.ok().body(p);
//        return ResponseEntity.ok().body(itemListToGetDtos);
    }

    @DeleteMapping("lists/{itemListId}")
    ResponseEntity<?> deleteUserItemList(
            @PathVariable Long itemListId,
            Principal principal

    ) {
        if (principal == null) {
            log.warn("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        User user = this.userService.getUser(userName);
        if (user == null) {
            log.warn("user with name " + userName + " not found in db");
            return ResponseEntity.notFound().build();
        }
        if (user.getLists().stream().noneMatch(l -> l.getId() == itemListId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ItemList ilToDelete = itemListRepository.getById(itemListId);
        if (ilToDelete == null) {
            return ResponseEntity.notFound().build();
        }
        user.getLists().removeIf(l -> l.getId().equals(itemListId));
        itemListRepository.delete(ilToDelete);
        return ResponseEntity.ok().build();
    }
  
    @PostMapping("lists")
    @Transactional
    ResponseEntity<ItemListItemVerboseToGetDto> saveUserList(Principal principal, @RequestBody ItemList toSave) {
        if (principal == null) {
            log.warn("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String userName = principal.getName();
        User user = this.userService.getUser(userName);
        if (user == null) {
            log.warn("user with name " + userName + " not found in db");
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
        toSave.setUser(user);
        ItemList savedList = listService.saveList(toSave);
        if (user.getLists().stream().noneMatch(l -> l.getId() == savedList.getId())) {
            List<ItemList> userLists = user.getLists();
            userLists.add(savedList);
            user.setLists(userLists);
            userRepository.save(user);
        }

        userRepository.save(user);
        ItemListItemVerboseToGetDto res = mapper.map(toSave, ItemListItemVerboseToGetDto.class);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("user")
    @Transactional
    ResponseEntity<UserToGetDto> updateUser(Principal principal, @RequestBody UserToPostDto userToPostDto) {
        if (principal == null) {
            log.warn("Principal == null");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        User user = userService.getUser(principal.getName());
        if (userToPostDto.getUsername() != null && !user.getUsername().equals(userToPostDto.getUsername())) {
            if (userRepository.existsByUsername(userToPostDto.getUsername())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            user.setUsername(userToPostDto.getUsername());
        }
        if (userToPostDto.getEmail() != null) {
            user.setEmail(userToPostDto.getEmail());
        }
        if (userToPostDto.getPassword() != null && !userToPostDto.getPassword().isEmpty()) {
            user.setPassword(userToPostDto.getPassword());
            User savedUser = userService.saveUser(user);
            UserToGetDto userToGetDto = mapper.map(savedUser, UserToGetDto.class);
            return ResponseEntity.ok().body(userToGetDto);
        }
        User savedUser = userRepository.save(user);
        UserToGetDto userToGetDto = mapper.map(savedUser, UserToGetDto.class);
        return ResponseEntity.ok().body(userToGetDto);
    }
}