package com.example.listmanager.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.listmanager.model.Role;
import com.example.listmanager.model.User;
import com.example.listmanager.model.dto.UserToGetDto;
import com.example.listmanager.model.dto.UserToPostDto;
import com.example.listmanager.repos.*;
import com.example.listmanager.services.IListService;
import com.example.listmanager.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping("/api")
@RestController
@Transactional
public class AuthController {
    private final IUserService userService;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public AuthController(
            IUserService userService,

            UserRepository userRepository,
            ModelMapper mapper
    ) {
        this.userService = userService;
        this.userRepository = userRepository;

        this.mapper = mapper;
    }

    @PostMapping("register")
    ResponseEntity<UserToGetDto> registerUser(@RequestBody UserToPostDto userToPostDto) {
        log.info("registerUser name: " + userToPostDto.getUsername() + " pass: " + userToPostDto.getPassword());
        if (userRepository.existsByUsername(userToPostDto.getUsername())) {
            log.info("User with this name already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        User user = mapper.map(userToPostDto, User.class);

        User savedUser = userService.saveUserWithRole(user, "ROLE_USER");
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
}
