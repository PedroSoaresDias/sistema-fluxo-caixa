package com.fluxo_caixa.user_services.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
// import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.fluxo_caixa.user_services.domain.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);
}
