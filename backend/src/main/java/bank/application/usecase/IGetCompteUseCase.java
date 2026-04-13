package bank.application.usecase;

import bank.application.domain.Compte;

public interface IGetCompteUseCase {
    Compte getCompte(String numeroCompte);
}
