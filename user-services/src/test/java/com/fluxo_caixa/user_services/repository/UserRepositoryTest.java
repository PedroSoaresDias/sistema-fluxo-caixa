package com.fluxo_caixa.user_services.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.domain.repository.UserRepository;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User defaultUser;

    @BeforeEach
    void setup() {
        defaultUser = new User();
        defaultUser.setUsername("Cobaia");
        defaultUser.setEmail("cobaia@test.com");
        defaultUser.setSenha("1234abc");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    private User saveUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setSenha(password);
        return userRepository.save(user);
    }

    @Test
    void findUserById() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());

        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals("Cobaia", result.get().getUsername());
        assertEquals("cobaia@test.com", result.get().getEmail());
    }

    @Test
    void findUserById_NotFound() {
        Optional<User> result = userRepository.findById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void findUserByUsername() {
        saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        Optional<User> result = userRepository.findByUsername("Cobaia");

        assertTrue(result.isPresent());
        assertEquals("Cobaia", result.get().getUsername());
        assertEquals("cobaia@test.com", result.get().getEmail());
    }

    @Test
    void findUserByUsername_NotFound() {
        Optional<User> result = userRepository.findByUsername("Cobaia");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAllUsersWithPagination() {
        for (int i = 1; i <= 10; i++) {
            saveUser("Cobaia" + i, "cobaia" + 1 + "@test.com", "password" + i);
        }

        Page<User> page = userRepository.findAll(PageRequest.of(0, 5));

        assertEquals(5, page.getNumberOfElements());
        assertEquals(2, page.getTotalPages());
        assertEquals(10, page.getTotalElements());
    }

    @Test
    void testSaveUser() {
        User savedUser = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());

        assertNotNull(savedUser.getId());
        assertEquals(defaultUser.getUsername(), savedUser.getUsername());
        assertEquals(defaultUser.getEmail(), savedUser.getEmail());
        assertEquals(defaultUser.getSenha(), savedUser.getSenha());
    }

    @Test
    void testUpdateUsername() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        user.setUsername("Cobaia 2.0");
        userRepository.save(user);

        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals("Cobaia 2.0", result.get().getUsername());
    }

    @Test
    void testUpdateUsername_NotFound() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        user.setUsername("Cobaia 2.0");
        userRepository.save(user);

        Optional<User> result = userRepository.findById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateEmailUser() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        user.setEmail("cobaia2@test.com");
        userRepository.save(user);

        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals("cobaia2@test.com", result.get().getEmail());
    }

    @Test
    void testUpdateEmailUser_NotFound() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        user.setEmail("cobaia2@test.com");
        userRepository.save(user);

        Optional<User> result = userRepository.findById(Long.MAX_VALUE);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdatePasswordUser() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        user.setSenha("5678xyz");
        userRepository.save(user);

        Optional<User> result = userRepository.findById(user.getId());

        assertTrue(result.isPresent());
        assertEquals("5678xyz", result.get().getSenha());
    }

    @Test
    void testUpdatePasswordUser_NotFound() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        user.setSenha("5678xyz");
        userRepository.save(user);

        Optional<User> result = userRepository.findById(2L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteUserById() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        userRepository.deleteById(user.getId());

        Optional<User> result = userRepository.findById(user.getId());
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteUserById_NotFound() {
        User user = saveUser(defaultUser.getUsername(), defaultUser.getEmail(), defaultUser.getSenha());
        userRepository.deleteById(1L);

        Optional<User> result = userRepository.findById(user.getId());
        assertTrue(result.isPresent());
    }
}
