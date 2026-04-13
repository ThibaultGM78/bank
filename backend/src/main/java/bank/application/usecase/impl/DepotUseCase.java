package bank.application.usecase.impl;

import bank.application.dao.ICompteDAO;
import bank.application.dao.IOperationDAO;
import bank.application.domain.Compte;
import bank.application.domain.Operation;
import bank.application.domain.OperationType;
import bank.application.usecase.IDepotUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class DepotUseCase implements IDepotUseCase {

    private final IOperationDAO operationDAO;
    private final ICompteDAO compteDAO;



    public void depot(Operation operation) {
        requireNonNull(operation);
        requireNonNull(operation.numeroCompte());
        requireNonNull(operation.montant());

        Compte compte = compteDAO.getCompte(operation.numeroCompte());

        compte.deposer(operation.montant());

        Operation operationToSave = Operation.builder()
                .type(OperationType.DEPOT.name())
                .numeroCompte(operation.numeroCompte())
                .montant(operation.montant())
                .soldeApres(compte.getSolde())
                .libelle(operation.libelle())
                .build();

        operationDAO.save(operationToSave);
        compteDAO.updateSolde(compte);
    }
}
