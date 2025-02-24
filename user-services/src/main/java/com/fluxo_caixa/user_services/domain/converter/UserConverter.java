package com.fluxo_caixa.user_services.domain.converter;

import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;

public class UserConverter {
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        return userDTO;
    }

    public static User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        return user;
    }
}
