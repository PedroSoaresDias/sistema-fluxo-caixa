package com.fluxo_caixa.user_services.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.services.UserManagementService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserManagementService userManagementService;

    public UserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ResponseEntity<List<UserDTO>>> getAllUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userManagementService.getAllUsers(pageable).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ResponseEntity<UserDTO>> findUserById(@PathVariable Long id) {
        return userManagementService.findUserById(id).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<ResponseEntity<User>> findUserByUsername(@PathVariable String username) {
        return userManagementService.findUserByUsername(username).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<ResponseEntity<User>> createUser(@RequestBody User user) {
        return userManagementService.createUser(user)
                .thenApply(savedUser -> ResponseEntity.status(HttpStatus.CREATED).build());
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<ResponseEntity<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userManagementService.updateUser(id, user).thenApply(updatedUser -> ResponseEntity.noContent().build());
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userManagementService.deleteUser(id).thenApply(deletedUser -> ResponseEntity.noContent().build());
    }
}
