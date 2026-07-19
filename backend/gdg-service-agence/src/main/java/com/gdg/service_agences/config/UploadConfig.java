package com.gdg.service_agences.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class UploadConfig {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            // Crée les dossiers s'ils n'existent pas
            Path enseignePath = Paths.get(uploadDir + "/enseignes");
            Path agencePath = Paths.get(uploadDir + "/agences");
            Path facturePath = Paths.get(uploadDir + "/factures");

            Files.createDirectories(enseignePath);
            Files.createDirectories(agencePath);
            Files.createDirectories(facturePath);

            System.out.println("✅ Dossiers d'upload créés : " + uploadDir);
        } catch (IOException e) {
            System.err.println("❌ Erreur création dossiers : " + e.getMessage());
        }
    }
}