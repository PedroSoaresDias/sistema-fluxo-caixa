package com.fluxo_caixa.user_services.services;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.fluxo_caixa.user_services.domain.DTO.UserDTO;
import com.fluxo_caixa.user_services.domain.model.User;

public interface UserManagementService {
    public List<UserDTO> getAllUsers(Pageable pageable);
    public UserDTO findUserById(Long id);
    public User findUserByUsername(String username);
    public User createUser(User user);
    public User updateUser(Long id, User userDetail);
    public Void deleteUser(Long id);
}
