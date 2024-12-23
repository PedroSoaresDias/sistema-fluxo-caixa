package com.fluxo_caixa.user_services.services;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fluxo_caixa.user_services.domain.DTO.PaginatedResponse;
import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.converter.UserConverter;
import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.domain.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionService transactionService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TransactionService transactionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.transactionService = transactionService;
    }

    @Async
    public CompletableFuture<PaginatedResponse<UserDTO>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDTO> userDTOs = users.map(UserConverter::toDTO);
        return CompletableFuture.completedFuture(new PaginatedResponse<>(userDTOs));
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
    public CompletableFuture<TransactionDTO> createTransaction(Long userId, TransactionDTO transactionDTO,
            String jwtToken) {
        transactionDTO.setUserId(userId);
        return CompletableFuture.completedFuture(transactionService.createTransaction(transactionDTO, jwtToken));
    }

    @Async
    public CompletableFuture<TransactionDTO> updateTransaction(Long userId, TransactionDTO transactionDTO,
            String jwtToken) {
        transactionDTO.setUserId(userId);
        return CompletableFuture.completedFuture(transactionService.updateTransaction(transactionDTO, jwtToken));
    }
    
    @Async
    public CompletableFuture<Boolean> deleteTransaction(Long userId, Long transactionId, String jwtToken) {
        boolean success = transactionService.deleteTransaction(transactionId, jwtToken);
        return CompletableFuture.completedFuture(success);
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
