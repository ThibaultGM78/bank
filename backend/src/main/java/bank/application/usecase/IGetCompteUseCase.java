package bank.application.usecase;

import bank.adapter.in.web.dto.CompteDTO;
import bank.application.domain.Compte;

public interface IGetCompteUseCase {
    Compte getCompte(String numeroCompte);
}
