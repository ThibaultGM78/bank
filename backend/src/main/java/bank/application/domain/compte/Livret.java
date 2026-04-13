package bank.application.domain.compte;

import bank.application.domain.Compte;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class Livret extends Compte {

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
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }

        if (montant.compareTo(this.getSolde()) > 0) {
            throw new IllegalStateException("Retrait refusé : solde insuffisant");
        }

        this.setSolde(this.getSolde().subtract(montant));
    }

    public void deposer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }

        BigDecimal nouveauSolde = this.getSolde().add(montant);
        if (nouveauSolde.compareTo(plafond) > 0) {
            throw new IllegalStateException("Dépôt refusé : le plafond de " + plafond + " serait dépassé");
        }

        this.setSolde(nouveauSolde);
    }
}
