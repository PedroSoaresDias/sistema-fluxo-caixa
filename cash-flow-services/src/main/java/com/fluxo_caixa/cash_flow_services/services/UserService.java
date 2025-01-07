package com.fluxo_caixa.cash_flow_services.services;

import com.fluxo_caixa.cash_flow_services.domain.DTO.UserDTO;

public interface UserService {
    UserDTO validateUser(Long userId, String jwtToken);
}
