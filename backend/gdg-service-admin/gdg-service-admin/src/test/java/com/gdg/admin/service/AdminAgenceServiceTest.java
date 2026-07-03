package com.gdg.admin.service;

import com.gdg.admin.config.RabbitMQConfig;
import com.gdg.admin.dto.AgenceDetailResponse;
import com.gdg.admin.dto.DistributeurInfoDTO;
import com.gdg.admin.event.AgenceValideeEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAgenceServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JournalAuditService journalAuditService;

    @InjectMocks
    private AdminAgenceService adminAgenceService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(adminAgenceService, "agencesBaseUrl", "http://localhost:8082");
        ReflectionTestUtils.setField(adminAgenceService, "authBaseUrl", "http://localhost:8081");
    }

    @Test
    void validerAgencePublieEvenementAvecEmailDistributeur() {
        AgenceDetailResponse agence = new AgenceDetailResponse();
        agence.setId(3L);
        agence.setNom("Total Bafoussam");
        agence.setEmail("dist@example.com");

        DistributeurInfoDTO distributeur = new DistributeurInfoDTO();
        distributeur.setId(7L);
        distributeur.setEmail("dist@example.com");

        when(restTemplate.getForObject(
            eq("http://localhost:8082/api/agences/3"),
            eq(AgenceDetailResponse.class))).thenReturn(agence);
        when(restTemplate.getForObject(
            eq("http://localhost:8081/auth/internal/distributeurs/by-agence/3"),
            eq(DistributeurInfoDTO.class))).thenReturn(distributeur);

        adminAgenceService.validerAgence(3L, 1L, "127.0.0.1");

        verify(restTemplate).put(contains("/api/agences/3/valider"), isNull());

        ArgumentCaptor<AgenceValideeEvent> captor = ArgumentCaptor.forClass(AgenceValideeEvent.class);
        verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.EXCHANGE),
            eq(RabbitMQConfig.KEY_AGENCE_VALIDEE),
            captor.capture());

        AgenceValideeEvent event = captor.getValue();
        assertEquals(3L, event.getAgenceId());
        assertEquals("Total Bafoussam", event.getNomAgence());
        assertEquals(7L, event.getDistributeurId());
        assertEquals("dist@example.com", event.getEmailDistributeur());
    }
}
