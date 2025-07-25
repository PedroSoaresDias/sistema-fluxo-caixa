package com.fluxo_caixa.user_services.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.domain.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserManagementService userManagementService;

    @Test
    void testandoCriarUsuario() {
        User user = new User();

        user.setUsername("Cobaia");
        user.setEmail("cobaia@test.com");
        user.setSenha("1234abc");

        when(passwordEncoder.encode(anyString())).thenReturn("senhaProtegida");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userManagementService.createUser(user);

        User createUser = result;

        assertEquals("senhaProtegida", createUser.getSenha());
        assertEquals("Cobaia", createUser.getUsername());
        assertEquals("cobaia@test.com", createUser.getEmail());
    }

    @Test
    void testandoAtualizarUsuario() {
        User user = new User();

        user.setUsername("Cobaia");
        user.setEmail("cobaia@test.com");
        user.setSenha("1234abc");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("1234abc")).thenReturn("senhaProtegida");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userManagementService.updateUser(1L, user);

        User updatedUser = result;

        assertEquals("Cobaia", updatedUser.getUsername());
        assertEquals("cobaia@test.com", updatedUser.getEmail());
        assertEquals("senhaProtegida", updatedUser.getSenha());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testandoAtualizarUsuario_NotFound() {
        User user = new User();

        user.setUsername("Cobaia");
        user.setEmail("cobaia@test.com");
        user.setSenha("1234abc");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Execução e verificação
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userManagementService.updateUser(1L, user));

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindUserById() {
        User mockUser = new User();

        mockUser.setId(1L);
        mockUser.setUsername("Cobaia");
        mockUser.setEmail("cobaia@test.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        Optional<User> result = userRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());
    }

    @Test
    void testFindByUsername() {
        User mockUser = new User();

        mockUser.setId(1L);
        mockUser.setUsername("Cobaia");
        mockUser.setEmail("cobaia@test.com");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        Optional<User> result = userRepository.findByUsername("Cobaia");

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());
    }

    @Test
    void testFindUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByUsername_NotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByUsername("Cobaia2");

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteUserById() {
        User user = new User();

        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userManagementService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userManagementService.deleteUser(1L));

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, never()).deleteById(anyLong());
    }
}
