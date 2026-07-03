package com.gdg.admin.repository;

import com.gdg.admin.model.ParametrePlateforme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParametrePlateformeRepository
        extends JpaRepository<ParametrePlateforme, Long> {

    Optional<ParametrePlateforme> findByCle(String cle);
    boolean existsByCle(String cle);
}
