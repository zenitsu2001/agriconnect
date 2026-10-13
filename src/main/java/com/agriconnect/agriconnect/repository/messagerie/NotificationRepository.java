package com.agriconnect.agriconnect.repository.messagerie;

import com.agriconnect.agriconnect.entity.messagerie.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDestinataireIdOrderByDateCreationDesc(Long destinataireId, Pageable pageable);
    long countByDestinataireIdAndLueFalse(Long destinataireId);                // pastille de la cloche

    @Modifying
    @Query("update Notification n set n.lue = true where n.destinataire.id = :id and n.lue = false")
    int marquerToutesLues(@Param("id") Long destinataireId);
}