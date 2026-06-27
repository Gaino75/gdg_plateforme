package com.gdg.service_agences.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.base-url}")
    private String baseUrl;

    public String sauvegarderFichier(MultipartFile file, String sousDossier) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String nomOriginal = file.getOriginalFilename();
            String extension = "";
            if (nomOriginal != null && nomOriginal.contains(".")) {
                extension = nomOriginal.substring(nomOriginal.lastIndexOf("."));
            }
            String nomFichier = UUID.randomUUID().toString() + extension;

            Path dossier = Paths.get(uploadDir, sousDossier);
            Path cheminFichier = dossier.resolve(nomFichier);

            Files.createDirectories(dossier);
            Files.copy(file.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/" + sousDossier + "/" + nomFichier;

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier", e);
        }
    }

    public boolean supprimerFichier(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        try {
            String cheminRelatif = url.replace(baseUrl + "/", "");
            Path cheminFichier = Paths.get(uploadDir, cheminRelatif);
            return Files.deleteIfExists(cheminFichier);
        } catch (IOException e) {
            return false;
        }
    }
}