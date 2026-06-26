package com.gdg.auth.repository;

import com.gdg.auth.model.TentativeConnexion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface TentativeConnexionRepository
        extends JpaRepository<TentativeConnexion, Long> {

    // Compter les tentatives échouées dans un délai
    long countByEmailAndSuccesFalseAndDateTentativeAfter(
        String email, LocalDateTime depuis);

    // Compter par IP
    long countByAdresseIpAndSuccesFalseAndDateTentativeAfter(
        String ip, LocalDateTime depuis);
}
