package com.example.listmanager.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.listmanager.model.*;
import com.example.listmanager.model.dto.ItemListItemVerboseToGetDto;
import com.example.listmanager.model.dto.UserToGetDto;
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


@RequestMapping("/api")
@RestController
@Transactional
public class UserController {
    private final IUserService userService;
    private final IListService listService;

    private final ItemListRepository itemListRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemStatusRepository itemStatusRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public UserController(
            IUserService userService, IListService listService,

            ItemListRepository itemListRepository,
            ItemRepository itemRepository,
            CategoryRepository categoryRepository,
            ItemStatusRepository itemStatusRepository,
            UserRepository userRepository,
            ModelMapper mapper
    ) {
        this.userService = userService;
        this.listService = listService;

        this.itemListRepository = itemListRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemStatusRepository = itemStatusRepository;
        this.userRepository = userRepository;

        this.mapper = mapper;
    }

    @GetMapping("user")
    ResponseEntity<UserToGetDto> getCurrentUser(Principal principal) {
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

        UserToGetDto userToGetDto = mapper.map(user, UserToGetDto.class);
        return ResponseEntity.ok().body(userToGetDto);
    }

    @PostMapping("register")
    ResponseEntity<UserToGetDto> registerUser(@RequestBody User user) {
        log.info("registerUser name: " + user.getUsername() + " pass: " + user.getPassword());
        User userFound = this.userService.getUser(user.getUsername());
        if (userFound != null) {
            log.info("User with this name already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User savedUser = this.userService.saveUser(user);
        this.userService.addRoleToUser(savedUser.getUsername(), "ROLE_USER");
        UserToGetDto userToGetDto = mapper.map(savedUser, UserToGetDto.class);
        return ResponseEntity.ok().body(userToGetDto);
    }

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping("user/lists")
    ResponseEntity<CustomPage<ItemListItemVerboseToGetDto>> getUserItemLists(
            Principal principal,
            @RequestParam(value = "sortBy", defaultValue="id", required = false) String sortBy,
            @RequestParam(value = "pageIndex",  defaultValue="0",  required = false) int pageIndex,
            @RequestParam(value = "pageSize",  defaultValue="8",  required = false) int pageSize,
            @RequestParam(value = "name", defaultValue="", required = false) String name,
            @RequestParam(value = "categoryName", defaultValue="", required = false) String categoryName
    ) {
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

    @DeleteMapping("user/lists/{itemListId}")
    ResponseEntity deleteUserItemList(
            @PathVariable Long itemListId,
            Principal principal

    ) {
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
        if (user.getLists().stream().noneMatch(l -> l.getId() == itemListId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ItemList ilToDelete = itemListRepository.getById(itemListId);
        if (ilToDelete == null) {
            return ResponseEntity.notFound().build();
        }
        user.getLists().removeIf(l -> l.getId().equals(itemListId));
        itemListRepository.delete(ilToDelete);
//        user.getLists().removeIf(l -> l.getId() == itemListId);
//        userRepository.save(user);
        return ResponseEntity.ok().build();
    }


//    @GetMapping("user/lists")
//    ResponseEntity<List<ItemList>> getUserLists(
//            Principal principal,
//            @RequestParam(value = "category", required = false) String category,
//            @RequestParam(value = "sort", required = false) String sort
//    ) {
//        log.info("getUserLists");
//        if (principal == null) {
//            log.info("Principal == null");
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        String userName = principal.getName();
//        User user = this.userService.getUser(userName);
//        if (user == null) {
//            log.info("user with name " + userName + " not found in db");
//            return ResponseEntity.notFound().build();
//        }
//        log.info("Returning Ok with " + user.getLists().size() + " elements");
//
//        List<ItemList> resultLists = user.getLists();
//        if (category != null && category.length() > 0) {
//            Category requestedCategory = categoryRepository.findByName(category);
//            if (requestedCategory == null) {
//                ResponseEntity.ok().body(new ArrayList<>());
//            }
//            resultLists = resultLists.stream()
//                    .filter(l -> l.getCategory().getId() == requestedCategory.getId())
//                    .collect(Collectors.toList());
//        }
//        if (sort != null && sort.length() > 0) {
//            boolean success = sortItemListsBy(resultLists, sort);
//            if (!success) {
//                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
//            }
//        }
//        return ResponseEntity.ok().body(resultLists);
//    }
//
//    private boolean sortItemListsBy(List<ItemList> list, String sortParam) {
//        String[] parts = sortParam.split(",");
//        if (parts[0].equals("name")) {
//            if (parts.length > 1 && parts[1] == "desc") {
//                list.sort((l1, l2) -> -l1.getName().compareTo(l2.getName()));
//            } else {
//                list.sort(Comparator.comparing(ItemList::getName));
//            }
//            return true;
//        }
//        if (parts[0].equals("category")) {
//            if (parts.length > 1 && parts[1].equals("desc")) {
//                list.sort((l1, l2) -> -l1.getCategory().getName().compareTo(l2.getCategory().getName()));
//            } else {
//                list.sort(Comparator.comparing(l -> l.getCategory().getName()));
//            }
//            return true;
//        }
//        if (parts[0].equals("count")) {
//            if (parts.length > 1 && parts[1].equals("desc")) {
//                list.sort((l1, l2) -> -(l1.getItems().size() - l2.getItems().size()));
//            } else {
//                list.sort(Comparator.comparingInt(l -> l.getItems().size()));
//            }
//            return true;
//        }
//        return false;
//    }

    @PostMapping("user/lists")
    @Transactional
    ResponseEntity<ItemListItemVerboseToGetDto> saveUserList(Principal principal, @RequestBody ItemList toSave) {
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
