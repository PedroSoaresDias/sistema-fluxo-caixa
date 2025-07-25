package com.fluxo_caixa.user_services.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluxo_caixa.user_services.configurations.AppConfig;
import com.fluxo_caixa.user_services.controller.UserController;
import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;
import com.fluxo_caixa.user_services.services.UserManagementService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(UserController.class)
// @AutoConfigureMockMvc(addFilters = false)
@Import(AppConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserManagementService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/users";
    private static final MediaType JSON = MediaType.APPLICATION_JSON;

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Cobaia");
        user.setEmail("cobaia@test.com");
        user.setSenha("1234abc");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    @Test
    void shouldCreateUserAndReturnCreatedStatus() throws Exception {
        User user = createTestUser();

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post(BASE_URL)
                .contentType(JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
                // .andExpect(jsonPath("$.username").value("Cobaia"));
        // .andExpect(jsonPath("$.email").value("cobaia@test.com"))
        // .andExpect(jsonPath("$.senha").value("1234abc"));

        // verify(userService, times(1)).createUser(user);
    }

    @Test
    void shouldReturnUserDetailsWhenUserExists() throws Exception {
        UserDTO user = new UserDTO(1L, "Cobaia", "cobaia@test.com");
        when(userService.findUserById(1L))
                .thenReturn(user);

        mockMvc.perform(get(BASE_URL + "/1")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
        // .andExpect(jsonPath("$.username").value("Cobaia"))
        // .andExpect(jsonPath("$.email").value("cobaia@test.com"))
        // .andExpect(result ->
        // assertNotNull(result.getResponse().getContentAsString()));

        // verify(userService, times(1)).findUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.findUserById(1L)).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = createTestUser();

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(put(BASE_URL + "/1")
                .contentType(JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
                // .andExpect(jsonPath("$.username").value("Cobaia"))
                // .andExpect(jsonPath("$.email").value("cobaia@test.com"))
                // .andExpect(jsonPath("$.senha").value("1234abc"));

        // verify(userService, times(1)).updateUser(1L, user);

    }

    @Test
    void testDeleteUserById() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(null);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUserById_NotFound() throws Exception {
        when(userService.deleteUser(any())).thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
