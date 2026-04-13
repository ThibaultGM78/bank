package bank.adapter.in.web.helper;

import bank.adapter.in.web.dto.CompteDTO;
import bank.adapter.in.web.dto.OperationDTO;
import bank.adapter.in.web.dto.compte.DetailsCompteCourantDTO;
import bank.adapter.in.web.dto.compte.DetailsLivretDTO;
import bank.application.domain.Compte;
import bank.application.domain.Operation;
import bank.application.domain.compte.CompteCourant;
import bank.application.domain.compte.Livret;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BankHelper {

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

    @NonNull
    public static OperationDTO toDTO(Operation operation) {
        return OperationDTO.builder()
                .numeroCompte(operation.numeroCompte())
                .type(operation.type())
                .montant(operation.montant())
                .date(operation.date())
                .soldeApres(operation.soldeApres())
                .libelle(operation.libelle())
                .build();
    }

    @NonNull
    public static Operation toDomain(OperationDTO dto) {
        return Operation.builder()
                .numeroCompte(dto.getNumeroCompte())
                .montant(dto.getMontant())
                .libelle(dto.getLibelle())
                .build();
    }

}
