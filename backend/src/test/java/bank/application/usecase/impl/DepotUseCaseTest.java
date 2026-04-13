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
class DepotUseCaseTest {

    @Mock
    private IOperationDAO operationDAO;

    @Mock
    private ICompteDAO compteDAO;

    @InjectMocks
    private DepotUseCase depotUseCase;

    @Test
    @DisplayName("Succès : Le dépôt doit mettre à jour le solde et enregistrer l'opération")
    void testDepot_Success() {
        String numero = "12345";
        BigDecimal montantDepot = new BigDecimal("50");
        BigDecimal soldeInitial = new BigDecimal("100");

        Compte compte = new CompteCourant(numero, soldeInitial, false, BigDecimal.ZERO);

        Operation inputOp = Operation.builder()
                .numeroCompte(numero)
                .montant(montantDepot)
                .libelle("Dépôt test")
                .build();

        when(compteDAO.getCompte(numero)).thenReturn(compte);

        depotUseCase.depot(inputOp);

        assertEquals(new BigDecimal("150"), compte.getSolde());

        verify(compteDAO).updateSolde(compte);

        ArgumentCaptor<Operation> operationCaptor = ArgumentCaptor.forClass(Operation.class);
        verify(operationDAO).save(operationCaptor.capture());

        Operation savedOp = operationCaptor.getValue();
        assertEquals("DEPOT", savedOp.type());
        assertEquals(new BigDecimal("150"), savedOp.soldeApres());
    }

    @Test
    @DisplayName("Erreur : Devrait propager l'exception si le compte n'existe pas")
    void testDepot_CompteInexistant() {
        Operation inputOp = Operation.builder()
                .numeroCompte("INCONNU")
                .montant(new BigDecimal("50"))
                .build();

        when(compteDAO.getCompte("INCONNU")).thenThrow(new RuntimeException("Compte non trouvé"));

        assertThrows(RuntimeException.class, () -> depotUseCase.depot(inputOp));

        verifyNoInteractions(operationDAO);
    }
}