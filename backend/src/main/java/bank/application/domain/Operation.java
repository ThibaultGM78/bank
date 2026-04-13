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

    public static final String ERR_NUMERO_OBLIGATOIRE = "Le numéro de compte est obligatoire";
    public static final String ERR_MONTANT_OBLIGATOIRE = "Le montant est obligatoire";
    public static final String ERR_MONTANT_STRICT_POSITIF = "Le montant de l'opération doit être strictement positif";

    public Operation {
        Objects.requireNonNull(numeroCompte, ERR_NUMERO_OBLIGATOIRE);
        Objects.requireNonNull(montant, ERR_MONTANT_OBLIGATOIRE);

        if (date == null) {
            date = LocalDateTime.now();
        }

        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ERR_MONTANT_STRICT_POSITIF);
        }
    }
}
