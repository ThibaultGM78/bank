package bank.application.usecase.impl;

import bank.adapter.in.web.dto.CompteDTO;
import bank.application.dao.ICompteDAO;
import bank.application.domain.Compte;
import bank.application.usecase.IGetCompteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
@RequiredArgsConstructor
public class GetCompteUseCase implements IGetCompteUseCase {

    private final ICompteDAO compteDAO;

    public Compte getCompte(String numeroCompte) {
        requireNonNull(numeroCompte);
        return compteDAO.getCompte(numeroCompte);
    }
}
