package com.fluxo_caixa.user_services.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.converter.UserConverter;
import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.domain.repository.UserRepository;
import com.fluxo_caixa.user_services.services.UserManagementService;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Async
    public CompletableFuture<List<UserDTO>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDTO> userDTOs = users.map(UserConverter::toDTO);
        return CompletableFuture.completedFuture(userDTOs.getContent());
    }

    @Async
    public CompletableFuture<UserDTO> findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        UserDTO userDTO = UserConverter.toDTO(user);
        return CompletableFuture.completedFuture(userDTO);
    }

    @Async
    public CompletableFuture<User> findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return CompletableFuture.completedFuture(user);
    }

    @Async
    public CompletableFuture<User> createUser(User user) {
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        user.setCreatedAt(LocalDateTime.now());
        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    @Async
    public CompletableFuture<User> updateUser(Long id, User userDetail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setUsername(userDetail.getUsername());
        user.setEmail(userDetail.getEmail());

        if (userDetail.getSenha() != null) {
            user.setSenha(passwordEncoder.encode(userDetail.getSenha()));
        }

        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    @Async
    public CompletableFuture<Void> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        return CompletableFuture.completedFuture(null);
    }
}
