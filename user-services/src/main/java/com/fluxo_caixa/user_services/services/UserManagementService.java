package com.fluxo_caixa.user_services.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Pageable;
import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;

public interface UserManagementService {
    public CompletableFuture<List<UserDTO>> getAllUsers(Pageable pageable);
    public CompletableFuture<UserDTO> findUserById(Long id);
    public CompletableFuture<User> findUserByUsername(String username);
    public CompletableFuture<User> createUser(User user);
    public CompletableFuture<User> updateUser(Long id, User userDetail);
    public CompletableFuture<Void> deleteUser(Long id);
}
