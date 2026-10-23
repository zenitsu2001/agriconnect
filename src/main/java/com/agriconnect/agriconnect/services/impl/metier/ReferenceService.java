package com.agriconnect.agriconnect.services.impl.metier;

import com.agriconnect.agriconnect.entity.parametre.Region;
import com.agriconnect.agriconnect.exception.ApiException;
import com.agriconnect.agriconnect.repository.abonnement.PlanAbonnementRepository;
import com.agriconnect.agriconnect.repository.marche.ProduitReferenceRepository;
import com.agriconnect.agriconnect.repository.parametre.CommuneRepository;
import com.agriconnect.agriconnect.repository.parametre.DepartementRepository;
import com.agriconnect.agriconnect.repository.parametre.LangueSupporteeRepository;
import com.agriconnect.agriconnect.repository.parametre.RegionRepository;
import com.agriconnect.agriconnect.services.dto.abonnement.response.PlanResponse;
import com.agriconnect.agriconnect.services.dto.marche.ProduitReferenceDto;
import com.agriconnect.agriconnect.services.dto.parametrage.CommuneDto;
import com.agriconnect.agriconnect.services.dto.parametrage.DepartementDto;
import com.agriconnect.agriconnect.services.dto.parametrage.LangueDto;
import com.agriconnect.agriconnect.services.dto.parametrage.RegionDto;
import com.agriconnect.agriconnect.services.mapper.AbonnementMapper;
import com.agriconnect.agriconnect.services.mapper.ReferenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReferenceService {

    private final LangueSupporteeRepository langues;
    private final RegionRepository regions;
    private final DepartementRepository departements;
    private final CommuneRepository communes;
    private final ProduitReferenceRepository produitsReference;
    private final ReferenceMapper mapper;
    private final AbonnementMapper abonnementMapper;
    private final PlanAbonnementRepository plansRepo ;

    public List<LangueDto> langues() {
        return mapper.toLangueDtos(langues.findByActifTrueOrderByOrdreAsc());
    }

    public List<RegionDto> regions() {
        return mapper.toRegionDtos(regions.findAllByOrderByNomAsc());
    }

    public List<DepartementDto> departements(String regionCode) {
        return mapper.toDepartementDtos(departements.findByRegionIdOrderByNom(region(regionCode).getId()));
    }

    public List<CommuneDto> communes(Long departementId) {
        return mapper.toCommuneDtos(communes.findByDepartementIdOrderByNom(departementId));
    }

    /** Suggestions du formulaire de localisation : région choisie, 2 caractères minimum. */
    public List<CommuneDto> rechercherCommunes(String regionCode, String q) {
        if (q == null || q.trim().length() < 2) return List.of();
        return mapper.toCommuneDtos(communes
                .findTop20ByNomContainingIgnoreCaseAndDepartementRegionIdOrderByNom(q.trim(), region(regionCode).getId()));
    }

    public List<ProduitReferenceDto> produits() {
        return mapper.toProduitReferenceDtos(produitsReference.findAll(Sort.by("nom")));
    }

    private Region region(String code) {
        return regions.findByCode(code).orElseThrow(() -> ApiException.notFound("errors.resource.notFound"));
    }

    public List<PlanResponse> plans() {
        return abonnementMapper.toPlanDtos(plansRepo.findByActifTrueOrderByOrdreAffichageAsc());
    }
}