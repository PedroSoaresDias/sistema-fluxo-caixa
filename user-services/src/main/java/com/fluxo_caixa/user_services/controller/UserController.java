package com.fluxo_caixa.user_services.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<UserDTO>>> getAllUsers() {
        return userService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<UserDTO>> findUserById(@PathVariable Long id) {
        return userService.findUserById(id).thenApply(ResponseEntity::ok);
    }

    @GetMapping("/username/{username}")
    public CompletableFuture<ResponseEntity<User>> findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username).thenApply(ResponseEntity::ok);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<User>> createUser(@RequestBody User user) {
        return userService.createUser(user)
                .thenApply(savedUser -> ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @PostMapping("/{id}/transactions")
    public CompletableFuture<ResponseEntity<TransactionDTO>> createTransaction(@PathVariable Long id,
            @RequestBody TransactionDTO transactionDTO) {
        return userService.createTransaction(id, transactionDTO).thenApply(ResponseEntity::ok);
    }
    
    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user).thenApply(updatedUser -> ResponseEntity.noContent().build());
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id).thenApply(deletedUser -> ResponseEntity.noContent().build());
    }
}
