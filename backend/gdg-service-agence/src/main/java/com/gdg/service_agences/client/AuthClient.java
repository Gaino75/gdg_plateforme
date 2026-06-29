package com.gdg.service_agences.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthClient {

    private final RestTemplate restTemplate;
    private final String authBaseUrl;

    public AuthClient(RestTemplate restTemplate,
                      @Value("${gdg.auth.url:http://localhost:8081}") String authBaseUrl) {
        this.restTemplate = restTemplate;
        this.authBaseUrl = authBaseUrl;
    }

    public void lierDistributeurAgence(Long distributeurId, Long agenceId) {
        restTemplate.put(
            authBaseUrl + "/auth/internal/distributeurs/"
                + distributeurId + "/agence/" + agenceId,
            null);
    }

    public void retirerLienAgence(Long agenceId) {
        restTemplate.delete(
            authBaseUrl + "/auth/internal/distributeurs/by-agence/" + agenceId);
    }
}
