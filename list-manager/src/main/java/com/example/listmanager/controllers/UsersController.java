package com.example.listmanager.controllers;

import com.example.listmanager.model.CustomPage;
import com.example.listmanager.model.ItemList;
import com.example.listmanager.model.Role;
import com.example.listmanager.model.User;
import com.example.listmanager.model.dto.ItemListItemVerboseToGetDto;
import com.example.listmanager.model.dto.UserToGetDto;
import com.example.listmanager.repos.*;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/users")
@RestController
@Transactional
public class UsersController {
    private final IUserService userService;

    private final ItemListRepository itemListRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final ModelMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UsersController(
            IUserService userService,

            ItemListRepository itemListRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            ModelMapper mapper
    ) {
        this.userService = userService;

        this.itemListRepository = itemListRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;

        this.mapper = mapper;
    }

    @GetMapping
    ResponseEntity<CustomPage<UserToGetDto>> getUserItemLists(
            Principal principal,
            @RequestParam(value = "sortBy", defaultValue="id", required = false) String sortBy,
            @RequestParam(value = "pageIndex",  defaultValue="0",  required = false) int pageIndex,
            @RequestParam(value = "pageSize",  defaultValue="8",  required = false) int pageSize,
            @RequestParam(value = "roleId", defaultValue="", required = false) Long roleId
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
        if (user.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);

        Page<User> users;
        if (roleId != null) {
            Role role = roleRepository.getById(roleId);
            users = userRepository.getAllByRolesIsContaining(role, PageRequest.of(pageIndex, pageSize, sort));
        } else {
            users = userRepository.findAll(PageRequest.of(pageIndex, pageSize, sort));
        }
        int totalCount = (int) users.getTotalElements();
        int totalPageCount = users.getTotalPages();
        List<UserToGetDto> itemListToGetDtos = users.stream()
                .map(il -> mapper.map(il, UserToGetDto.class)).collect(Collectors.toList());
        CustomPage<UserToGetDto> p = new CustomPage<>(itemListToGetDtos, totalCount, totalPageCount, pageIndex, pageSize);
        return ResponseEntity.ok().body(p);
    }

    @GetMapping("{userId}/lists")
    ResponseEntity<CustomPage<ItemListItemVerboseToGetDto>> getUserItemListsUsingUserId(
            Principal principal,
            @PathVariable Long userId,
            @RequestParam(value = "sortBy", defaultValue="id", required = false) String sortBy,
            @RequestParam(value = "pageIndex",  defaultValue="0",  required = false) int pageIndex,
            @RequestParam(value = "pageSize",  defaultValue="8",  required = false) int pageSize,
            @RequestParam(value = "name", defaultValue="", required = false) String name,
            @RequestParam(value = "categoryName", defaultValue="", required = false) String categoryName
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
        if (user.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!sortBy.equals("category") && !sortBy.equals("name") && !sortBy.equals("id")) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User listOwner = userRepository.getById(userId);
        if (listOwner == null) {
            return ResponseEntity.notFound().build();
        }
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy);
        Page<ItemList> lists = itemListRepository.getAllByUserAndCategory_NameContainingAndNameContaining(listOwner, categoryName, name, PageRequest.of(pageIndex, pageSize, sort));
        int totalCount = (int) lists.getTotalElements();
        int totalPageCount = lists.getTotalPages();
        List<ItemListItemVerboseToGetDto> itemListToGetDtos = lists.stream()
                .map(il -> mapper.map(il, ItemListItemVerboseToGetDto.class)).collect(Collectors.toList());
        CustomPage<ItemListItemVerboseToGetDto> p = new CustomPage<>(itemListToGetDtos, totalCount, totalPageCount, pageIndex, pageSize);
        return ResponseEntity.ok().body(p);
    }
}
