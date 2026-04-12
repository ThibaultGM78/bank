package bank.application.dao;

import bank.application.domain.Compte;
import bank.application.domain.Virement;

public interface IVirementDAO {
    void sauvegarder(Virement virement);
}
