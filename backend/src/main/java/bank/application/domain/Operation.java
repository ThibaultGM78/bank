package bank.application.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public record Operation(
        Long id,
        String type,
        String numeroCompte,
        String numeroCompteDistant,
        BigDecimal montant,
        BigDecimal soldeApres,
        String libelle,
        LocalDateTime date
) {

    public Operation {
        Objects.requireNonNull(numeroCompte, "Le numéro de compte est obligatoire");
        Objects.requireNonNull(montant, "Le montant est obligatoire");

        if (date == null) {
            date = LocalDateTime.now();
        }

        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant de l'opération doit être strictement positif");
        }
    }
}
