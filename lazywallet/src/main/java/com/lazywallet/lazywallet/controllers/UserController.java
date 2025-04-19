package com.lazywallet.lazywallet.controllers;

import com.lazywallet.lazywallet.models.UserDTO;
import com.lazywallet.lazywallet.models.UserRegistrationDTO;
import com.lazywallet.lazywallet.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserDTO createdUser = userService.createUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}