package com.agriconnect.agriconnect.repository.messagerie;

import com.agriconnect.agriconnect.entity.messagerie.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("select c from Conversation c join c.participants p where p.id = :utilisateurId " +
            "order by c.derniereActivite desc")
    List<Conversation> findByParticipant(@Param("utilisateurId") Long utilisateurId);

    /** Conversation existante entre deux utilisateurs (pour éviter les doublons). */
    @Query("""
            select c from Conversation c
            where size(c.participants) = 2
              and exists (select 1 from c.participants p where p.id = :a)
              and exists (select 1 from c.participants p where p.id = :b)
            """)
    Optional<Conversation> findEntre(@Param("a") Long a, @Param("b") Long b);
}
