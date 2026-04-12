package bank.application.domain;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Builder
public record Virement(
        String numeroCompteEmetteur,
        String numeroCompteRecepteur,
        BigDecimal montant,
        String libelle,
        LocalDate date
) {

    public Virement {
        Objects.requireNonNull(numeroCompteEmetteur);
        Objects.requireNonNull(numeroCompteRecepteur);
        Objects.requireNonNull(montant);
        if ( montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du virement doit être supérieur à zéro");
        }
    }
}
