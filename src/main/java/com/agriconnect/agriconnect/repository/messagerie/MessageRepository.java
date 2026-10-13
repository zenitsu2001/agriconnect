package com.agriconnect.agriconnect.repository.messagerie;

import com.agriconnect.agriconnect.entity.messagerie.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByDateEnvoiAsc(Long conversationId);
    Optional<Message> findFirstByConversationIdOrderByDateEnvoiDesc(Long conversationId);    // dernier message du contact

    long countByConversationIdAndExpediteurIdNotAndLuFalse(Long conversationId, Long utilisateurId);   // non lus

    @Modifying
    @Query("update Message m set m.lu = true " +
            "where m.conversation.id = :conversationId and m.expediteur.id <> :utilisateurId and m.lu = false")
    int marquerLus(@Param("conversationId") Long conversationId, @Param("utilisateurId") Long utilisateurId);
}