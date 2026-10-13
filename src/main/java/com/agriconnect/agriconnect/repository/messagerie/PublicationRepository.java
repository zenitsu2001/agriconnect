package com.agriconnect.agriconnect.repository.messagerie;

import com.agriconnect.agriconnect.entity.messagerie.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Long> {

    List<Publication> findByAuteurIdOrderByDateCreationDesc(Long auteurId);

    /** Publications visibles par un agriculteur : ciblant sa région ou toutes les régions. */
    @Query("""
            select distinct p from Publication p left join p.regionsCibles r
            where p.publie = true and (r is null or r.id = :regionId)
            order by p.datePublication desc
            """)
    List<Publication> findVisiblesPourRegion(@Param("regionId") Long regionId);
}
