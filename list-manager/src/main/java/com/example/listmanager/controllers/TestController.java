package com.example.listmanager.controllers;


import com.example.listmanager.model.User;
import com.example.listmanager.model.dto.UserGetDto;
import com.example.listmanager.repos.ItemListRepository;
import com.example.listmanager.repos.UserRepository;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
    ResponseEntity<List<UserGetDto>> getAllUsers() {
        List<User> users = userService.getUsers();
        log.info("getAllUsers Ok with " + users.size() + " elements");

        List<UserGetDto> usersDto = users.stream().map(u -> mapper.map(u, UserGetDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(usersDto);
    }
}
