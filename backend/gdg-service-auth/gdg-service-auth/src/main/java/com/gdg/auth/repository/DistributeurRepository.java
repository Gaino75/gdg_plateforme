package com.gdg.auth.repository;

import com.gdg.auth.model.Distributeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistributeurRepository extends JpaRepository<Distributeur, Long> {

    Optional<Distributeur> findByAgenceId(Long agenceId);

    Optional<Distributeur> findByEmail(String email);
}
