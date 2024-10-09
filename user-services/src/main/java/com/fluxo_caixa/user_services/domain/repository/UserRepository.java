package com.fluxo_caixa.user_services.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fluxo_caixa.user_services.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
