package bank.application.dao;

import bank.adapter.in.web.dto.CompteDTO;
import bank.application.domain.Compte;

public interface ICompteDAO {

    Compte getCompte(String numeroCompte);

    void sauvegarder(Compte compte);
}