package bank.application.usecase.impl;

import bank.application.dao.IOperationDAO;
import bank.application.domain.Operation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOperationsUseCaseTest {

    @Mock
    private IOperationDAO operationDAO;

    @InjectMocks
    private GetOperationsUseCase getOperationsUseCase;

    @Test
    @DisplayName("Succès : Doit retourner la liste des opérations du compte")
    void testGetOperations_Success() {
        String numero = "12345";
        List<Operation> mockList = List.of(
                Operation.builder().numeroCompte(numero).montant(new BigDecimal("100")).type("DEPOT").build(),
                Operation.builder().numeroCompte(numero).montant(new BigDecimal("50")).type("RETRAIT").build()
        );

        when(operationDAO.getOperations(numero)).thenReturn(mockList);

        List<Operation> result = getOperationsUseCase.getOperations(numero);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(new BigDecimal("100"), result.get(0).montant());

        verify(operationDAO, times(1)).getOperations(numero);
    }

    @Test
    @DisplayName("Erreur : Doit lever une NullPointerException si le numéro est nul")
    void testGetOperations_WhenNumeroIsNull() {
        assertThrows(NullPointerException.class, () -> {
            getOperationsUseCase.getOperations(null);
        });

        verifyNoInteractions(operationDAO);
    }
}