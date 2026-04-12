package bank.adapter.in.web.helper;

import bank.adapter.in.web.dto.CompteDTO;
import bank.adapter.in.web.dto.VirementDTO;
import bank.adapter.in.web.dto.compte.DetailsCompteCourantDTO;
import bank.adapter.in.web.dto.compte.DetailsLivretDTO;
import bank.application.domain.Compte;
import bank.application.domain.Virement;
import bank.application.domain.compte.CompteCourant;
import bank.application.domain.compte.Livret;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BankHelper {

    @NonNull
    public Virement from(@NonNull VirementDTO virementDTO){
        return Virement.builder()
                .numeroCompteEmetteur(virementDTO.getNumeroCompteEmetteur())
                .numeroCompteRecepteur(virementDTO.getNumeroCompteRecepteur())
                .montant(virementDTO.getMontant())
                .libelle(virementDTO.getLibelle())
                .date(virementDTO.getDate())
                .build();
    }

    @NonNull
    public CompteDTO toDTO(@NonNull Compte compte) {
        var builder = CompteDTO.builder()
                .numeroCompte(compte.getNumeroCompte())
                .solde(compte.getSolde());

        if (compte instanceof CompteCourant compteCourant) {
            builder.detailsCompteCourant(DetailsCompteCourantDTO.builder()
                    .decouvertAutorise(compteCourant.isDecouvertAutorise())
                    .montantDecouvert(compteCourant.getMontantDecouvert())
                    .build());
        } else if (compte instanceof Livret livret) {
            builder.detailsLivret(DetailsLivretDTO.builder()
                    .plafond(livret.getPlafond())
                    .build());
        }

        return builder.build();
    }
}
