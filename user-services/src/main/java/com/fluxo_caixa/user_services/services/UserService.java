package com.fluxo_caixa.user_services.services;

import java.time.LocalDateTime;
// import java.util.List;
import java.util.concurrent.CompletableFuture;
// import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fluxo_caixa.user_services.domain.DTO.PaginatedResponse;
import com.fluxo_caixa.user_services.domain.DTO.TransactionDTO;
import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.domain.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
    @Value("${fluxo-caixa.service.url}")
    private String fluxoCaixaServiceUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<PaginatedResponse<UserDTO>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        Page<UserDTO> userDTOs = users.map(this::convertToDTO);
        return CompletableFuture.completedFuture(new PaginatedResponse<>(userDTOs));
    }

    @Async
    public CompletableFuture<UserDTO> findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        UserDTO userDTO = convertToDTO(user);
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
        String url = fluxoCaixaServiceUrl + "/transactions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO, headers);

        ResponseEntity<TransactionDTO> response = restTemplate.postForEntity(url, request, TransactionDTO.class);
        return CompletableFuture.completedFuture(response.getBody());
    }

    @Async
    public CompletableFuture<TransactionDTO> updateTransaction(Long userId, TransactionDTO transactionDTO,
            String jwtToken) {
        transactionDTO.setUserId(userId);
        String url = fluxoCaixaServiceUrl + "/transactions/" + transactionDTO.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<TransactionDTO> request = new HttpEntity<>(transactionDTO, headers);

        restTemplate.exchange(url, HttpMethod.PUT, request, TransactionDTO.class);
        return CompletableFuture.completedFuture(transactionDTO);
    }
    
    @Async
    public CompletableFuture<Void> deleteTransaction(Long userId, Long transactionId, String jwtToken) {
        String url = fluxoCaixaServiceUrl + "/transactions/" + transactionId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE ,request, TransactionDTO.class);
        return CompletableFuture.completedFuture(null);
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

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
