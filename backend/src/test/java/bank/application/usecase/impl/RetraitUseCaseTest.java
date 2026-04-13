package bank.application.usecase.impl;

import bank.application.dao.ICompteDAO;
import bank.application.dao.IOperationDAO;
import bank.application.domain.Compte;
import bank.application.domain.Operation;
import bank.application.domain.compte.CompteCourant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetraitUseCaseTest {

    @Mock
    private IOperationDAO operationDAO;

    @Mock
    private ICompteDAO compteDAO;

    @InjectMocks
    private RetraitUseCase retraitUseCase;

    @Test
    @DisplayName("Succès : Le retrait diminue le solde et enregistre l'opération")
    void testRetrait_Success() {
        String numero = "12345";
        BigDecimal montantRetrait = new BigDecimal("40");

        Compte compte = new CompteCourant(numero, new BigDecimal("100"), false, BigDecimal.ZERO);

        Operation inputOp = Operation.builder()
                .numeroCompte(numero)
                .montant(montantRetrait)
                .libelle("Retrait distributeur")
                .build();

        when(compteDAO.getCompte(numero)).thenReturn(compte);

        retraitUseCase.retrait(inputOp);

        assertEquals(new BigDecimal("60"), compte.getSolde());
        verify(compteDAO).updateSolde(compte);

        ArgumentCaptor<Operation> operationCaptor = ArgumentCaptor.forClass(Operation.class);
        verify(operationDAO).save(operationCaptor.capture());

        Operation savedOp = operationCaptor.getValue();
        assertEquals("RETRAIT", savedOp.type());
        assertEquals(new BigDecimal("60"), savedOp.soldeApres());
    }

    @Test
    @DisplayName("Erreur : Devrait échouer si le solde est insuffisant")
    void testRetrait_SoldeInsuffisant() {
        String numero = "12345";

        Compte compte = new CompteCourant(numero, new BigDecimal("10"), false, BigDecimal.ZERO);
        Operation inputOp = Operation.builder()
                .numeroCompte(numero)
                .montant(new BigDecimal("50"))
                .build();

        when(compteDAO.getCompte(numero)).thenReturn(compte);

        assertThrows(IllegalStateException.class, () -> retraitUseCase.retrait(inputOp));

        verify(operationDAO, never()).save(any());
        verify(compteDAO, never()).updateSolde(any());
    }

    @Test
    @DisplayName("Erreur : NullPointerException si l'opération est nulle")
    void testRetrait_NullOperation() {
        assertThrows(NullPointerException.class, () -> retraitUseCase.retrait(null));
    }
}