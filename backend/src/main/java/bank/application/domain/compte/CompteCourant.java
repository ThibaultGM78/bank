package bank.application.domain.compte;

import bank.application.domain.Compte;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class CompteCourant extends Compte {

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

    @Override
    public void retirer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }

        BigDecimal soldeMinimum = decouvertAutorise
                ? montantDecouvert.negate()
                : BigDecimal.ZERO;

        BigDecimal nouveauSolde = this.getSolde().subtract(montant);

        if (nouveauSolde.compareTo(soldeMinimum) < 0) {
            throw new IllegalStateException("Retrait refusé : solde insuffisant (limite de découvert atteinte)");
        }

        this.setSolde(nouveauSolde);
    }

    @Override
    public void deposer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }

        this.setSolde(this.getSolde().add(montant));
    }
}
