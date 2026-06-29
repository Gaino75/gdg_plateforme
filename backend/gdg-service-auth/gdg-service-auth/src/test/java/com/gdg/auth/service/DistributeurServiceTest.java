package com.gdg.auth.service;

import com.gdg.auth.model.Distributeur;
import com.gdg.auth.model.Utilisateur;
import com.gdg.auth.repository.DistributeurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistributeurServiceTest {

    @Mock
    private DistributeurRepository distributeurRepository;

    @InjectMocks
    private DistributeurService distributeurService;

    @Test
    void assignerAgenceLieLeDistributeur() {
        Distributeur distributeur = new Distributeur();
        distributeur.setId(5L);
        distributeur.setRole(Utilisateur.Role.DISTRIBUTEUR);
        when(distributeurRepository.findById(5L)).thenReturn(Optional.of(distributeur));
        when(distributeurRepository.save(any(Distributeur.class))).thenAnswer(i -> i.getArgument(0));

        String result = distributeurService.assignerAgence(5L, 12L);

        assertEquals("Agence assignée au distributeur", result);
        assertEquals(12L, distributeur.getAgenceId());
        verify(distributeurRepository).save(distributeur);
    }
}
