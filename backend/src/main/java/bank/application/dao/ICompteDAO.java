package bank.application.dao;

import bank.application.domain.Compte;

public interface ICompteDAO {

    Compte getCompte(String numeroCompte);
    void updateSolde(Compte compte);
}