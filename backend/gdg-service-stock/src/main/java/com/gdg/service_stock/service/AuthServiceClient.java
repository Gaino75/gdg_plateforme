package com.gdg.service_stock.service;

import com.gdg.service_stock.dtos.DistributeurInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class AuthServiceClient {
    private final RestTemplate restTemplate=new RestTemplate();

    @Value("${gdg.auth.url:http://localhost:8081}")
    private String authBaseUrl;

    public DistributeurInfoDTO getDistributeurByAgence(Long agenceId){
        try{
            return restTemplate.getForObject(
              authBaseUrl + "/auth/internal/distributeurs/by-agence/"+ agenceId , DistributeurInfoDTO.class);
        }
            catch(Exception e){
                log.warn("impossible de recupererle distributeur de l'agence{}:{}",agenceId,e.getMessage());
                return null;
        }
    }
    
}
