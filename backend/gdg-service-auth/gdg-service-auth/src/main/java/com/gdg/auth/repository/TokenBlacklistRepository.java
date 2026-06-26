package com.gdg.auth.repository;

import com.gdg.auth.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlacklistRepository
        extends JpaRepository<TokenBlacklist, Long> {

    // Vérifier si un token est blacklisté
    boolean existsByToken(String token);
}