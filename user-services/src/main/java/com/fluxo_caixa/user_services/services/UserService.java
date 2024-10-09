package com.fluxo_caixa.user_services.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.domain.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Async
    public CompletableFuture<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(userDTOs);
    }

    @Async
    public CompletableFuture<UserDTO> findUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceAccessException("User not found"));
        UserDTO userDTO = convertToDTO(user);
        return CompletableFuture.completedFuture(userDTO);
    }

    @Async
    public CompletableFuture<User> createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    @Async
    public CompletableFuture<User> updateUser(Long id, User userDetail) {
        Optional<User> opitionalUser = userRepository.findById(id);
        if (opitionalUser.isPresent()) {
            User user = opitionalUser.get();
            user.setUsername(userDetail.getUsername());
            user.setEmail(userDetail.getEmail());
            user.setSenha(userDetail.getSenha());
            return CompletableFuture.completedFuture(userRepository.save(user));
        } else {
            throw new ResourceAccessException("User not found with id: " + id);
        }
    }

    @Async
    public CompletableFuture<Void> deleteUser(Long id) {
        Optional<User> opitionalUser = userRepository.findById(id);
        if (opitionalUser.isPresent()) {
            userRepository.delete(opitionalUser.get());
        } else {
            throw new ResourceAccessException("User not found with id: " + id);
        }
        return CompletableFuture.completedFuture(null);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
