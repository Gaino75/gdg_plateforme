package com.gdg.service_agences.service;

import com.gdg.service_agences.client.AuthClient;
import com.gdg.service_agences.model.Agence;
import com.gdg.service_agences.model.Enseigne;
import com.gdg.service_agences.model.Ville;
import com.gdg.service_agences.repository.AgenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgenceServiceDistributeurTest {

    @Mock
    private AgenceRepository agenceRepository;

    @Mock
    private EnseigneService enseigneService;

    @Mock
    private VilleService villeService;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AgenceService agenceService;

    @Test
    void creerAgenceParDistributeurLieAuthApresCreation() {
        Enseigne enseigne = new Enseigne();
        Ville ville = new Ville();
        Agence saved = new Agence();
        saved.setId(10L);

        when(enseigneService.getEnseigneById(1L)).thenReturn(enseigne);
        when(villeService.getVilleById(2L)).thenReturn(ville);
        when(agenceRepository.save(any(Agence.class))).thenReturn(saved);

        agenceService.creerAgenceParDistributeur(new Agence(), 1L, 2L, 5L);

        verify(authClient).lierDistributeurAgence(eq(5L), eq(10L));
    }
}
