package bank.application.usecase.impl;

import bank.application.dao.ICompteDAO;
import bank.application.domain.Compte;
import bank.application.domain.compte.CompteCourant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCompteUseCaseTest {

    @Mock
    private ICompteDAO compteDAO;

    @InjectMocks
    private GetCompteUseCase getCompteUseCase;

    @Test
    @DisplayName("Succès : Doit retourner le compte correspondant au numéro")
    void testGetCompte_Success() {
        String numero = "12345";
        Compte mockCompte = new CompteCourant(numero, new BigDecimal("1000"), true, new BigDecimal("500"));

        when(compteDAO.getCompte(numero)).thenReturn(mockCompte);

        Compte result = getCompteUseCase.getCompte(numero);

        assertNotNull(result);
        assertEquals(numero, result.getNumeroCompte());
        assertEquals(new BigDecimal("1000"), result.getSolde());

        verify(compteDAO, times(1)).getCompte(numero);
    }

    @Test
    @DisplayName("Erreur : Doit lever une NullPointerException si le numéro est nul")
    void testGetCompte_WhenNumeroIsNull() {
        assertThrows(NullPointerException.class, () -> {
            getCompteUseCase.getCompte(null);
        });

        verifyNoInteractions(compteDAO);
    }
}