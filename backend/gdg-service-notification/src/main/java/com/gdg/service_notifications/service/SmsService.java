// service/SmsService.java
package com.gdg.service_notifications.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    @Value("${twilio.phone-number:}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {
        if (accountSid != null && !accountSid.isEmpty()) {
            Twilio.init(accountSid, authToken);
            log.info("Twilio initialisé pour l'envoi de SMS");
        } else {
            log.warn("Twilio non configuré. Les SMS ne seront pas envoyés.");
        }
    }

    public void sendSms(String phoneNumber, String message) {
        if (accountSid == null || accountSid.isEmpty()) {
            log.info("SMS simulé à {}: {}", phoneNumber, message);
            return;
        }

        try {
            Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioPhoneNumber),
                    message
            ).create();
            log.info("SMS envoyé avec succès à {}", phoneNumber);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi du SMS à {}: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("Erreur d'envoi SMS", e);
        }
    }
/*
    public void sendSms(String phoneNumber, String message){
        //SMS desactive pour le developpement local
        log.info("[SMS SIMULE] A {} : {}", phoneNumber,message);
    }*/
}