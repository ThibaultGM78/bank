package bank.application.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
public abstract class Compte {

    public abstract void retirer(BigDecimal montant);
    public abstract void deposer(BigDecimal montant);

    @Getter
    private String numeroCompte;

    @Getter@Setter
    private BigDecimal solde;

    public Compte(
        final String numeroCompte,
        final BigDecimal solde
    ){
        this.numeroCompte = numeroCompte;
        this.solde = solde;
    }
}
