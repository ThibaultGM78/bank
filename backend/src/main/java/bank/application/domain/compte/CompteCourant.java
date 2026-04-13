package bank.application.domain.compte;

import bank.application.domain.Compte;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class CompteCourant extends Compte {

    public static final String ERR_SOLDE_INSUFFISANT = "Retrait refusé : solde insuffisant (limite de découvert atteinte)";

    @Getter
    boolean decouvertAutorise;
    @Getter
    BigDecimal montantDecouvert;

    @Builder
    public CompteCourant(
            String numeroCompte,
            BigDecimal solde,
            boolean decouvertAutorise,
            BigDecimal montantDecouvert
    ){
        super(numeroCompte, solde);
        this.decouvertAutorise = decouvertAutorise;
        this.montantDecouvert = montantDecouvert;
    }

    public void retirer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ERR_MONTANT_POSITIF_RETRAIT);
        }

        BigDecimal soldeMinimum = decouvertAutorise
                ? montantDecouvert.negate()
                : BigDecimal.ZERO;

        BigDecimal nouveauSolde = this.getSolde().subtract(montant);

        if (nouveauSolde.compareTo(soldeMinimum) < 0) {
            throw new IllegalStateException(ERR_SOLDE_INSUFFISANT);
        }

        this.setSolde(nouveauSolde);
    }

    public void deposer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ERR_MONTANT_POSITIF_DEPOT);
        }

        this.setSolde(this.getSolde().add(montant));
    }
}
