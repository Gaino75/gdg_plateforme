package com.gdg.service_notifications.repository;

import com.gdg.service_notifications.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository  // ⚠️ AJOUTER CETTE ANNOTATION
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUtilisateurIdOrderByDateEnvoiDesc(Long utilisateurId);
    
    Page<Notification> findByUtilisateurId(Long utilisateurId, Pageable pageable);
    
    List<Notification> findByUtilisateurIdAndStatut(Long utilisateurId, Notification.StatutNotification statut);
    
    List<Notification> findByStatutAndDateEnvoiBefore(Notification.StatutNotification statut, LocalDateTime date);
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.statut = :statut WHERE n.id = :id")
    void updateStatut(@Param("id") Long id, @Param("statut") Notification.StatutNotification statut);
    
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.statut = 'LU', n.dateLecture = CURRENT_TIMESTAMP WHERE n.utilisateurId = :utilisateurId AND n.statut = 'NON_LU'")
    void marquerToutesCommeLues(@Param("utilisateurId") Long utilisateurId);
    
    long countByUtilisateurIdAndStatut(Long utilisateurId, Notification.StatutNotification statut);
}