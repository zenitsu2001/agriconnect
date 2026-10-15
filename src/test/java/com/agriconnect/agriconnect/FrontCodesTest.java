package com.agriconnect.agriconnect;

// FrontCodesTest.java
import com.agriconnect.agriconnect.enums.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.*;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class FrontCodesTest {

    static Stream<Class<? extends FrontCode>> enums() {
        return Stream.of(Profil.class, TypeClient.class, CategorieProduit.class, Unite.class,
                StatutProduit.class, StatutCommande.class, TypeStock.class, StatutStock.class,
                TypeMouvement.class, TypeEvenement.class, Priorite.class, CategoriePublication.class,
                ModePaiement.class, StatutPaiement.class, ObjetPaiement.class, PeriodeAbonnement.class,
                StatutAbonnement.class, TypeNotification.class);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ParameterizedTest
    @MethodSource("enums")
    void codesUniquesEtReversibles(Class<? extends FrontCode> type) {
        Set<String> vus = new HashSet<>();
        for (FrontCode valeur : type.getEnumConstants()) {
            assertTrue(vus.add(valeur.getCode()), "Code en double dans " + type.getSimpleName());
            assertSame(valeur, FrontCodes.fromCode((Class) type, valeur.getCode()));
        }
    }
}