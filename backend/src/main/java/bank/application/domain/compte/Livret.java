package bank.application.domain.compte;

import bank.application.domain.Compte;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class Livret extends Compte {

    public static final String ERR_SOLDE_INSUFFISANT = "Retrait refusé : solde insuffisant";
    public static final String ERR_PLAFOND_DEPASSE = "Dépôt refusé : le plafond suivant serait dépassé : ";

    @Getter
    BigDecimal plafond;

    @Builder
    public Livret(
            String numeroCompte,
            BigDecimal solde,
            BigDecimal plafond
    ){
        super(numeroCompte, solde);
        this.plafond = plafond;
    }

    public void retirer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ERR_MONTANT_POSITIF_RETRAIT);
        }

        if (montant.compareTo(this.getSolde()) > 0) {
            throw new IllegalStateException(ERR_SOLDE_INSUFFISANT);
        }

        this.setSolde(this.getSolde().subtract(montant));
    }

    public void deposer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ERR_MONTANT_POSITIF_DEPOT);
        }

        BigDecimal nouveauSolde = this.getSolde().add(montant);
        if (nouveauSolde.compareTo(plafond) > 0) {
            throw new IllegalStateException(ERR_PLAFOND_DEPASSE + plafond);
        }

        this.setSolde(nouveauSolde);
    }
}
