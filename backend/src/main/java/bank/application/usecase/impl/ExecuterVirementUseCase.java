package bank.application.usecase.impl;

import bank.application.dao.ICompteDAO;
import bank.application.dao.IVirementDAO;
import bank.application.domain.Compte;
import bank.application.domain.Virement;
import bank.application.usecase.IExecuterVirementUseCase;
import bank.adapter.in.web.helper.BankHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecuterVirementUseCase implements IExecuterVirementUseCase {

    private final ICompteDAO compteDAO;
    private final IVirementDAO virementDAO;

    public void executeTransfer(Virement virement) {

        Compte emetteur = compteDAO.getCompte(virement.numeroCompteEmetteur());
        Compte recepteur = compteDAO.getCompte(virement.numeroCompteEmetteur());

        emetteur.retirer(virement.montant());
        recepteur.deposer(virement.montant());

        compteDAO.sauvegarder(emetteur);
        compteDAO.sauvegarder(recepteur);

        virementDAO.sauvegarder(virement);
    }
}