package bank.application.domain.compte;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static bank.application.domain.compte.CompteCourant.ERR_MONTANT_POSITIF_DEPOT;
import static org.junit.jupiter.api.Assertions.*;

class CompteCourantTest {

    @Test
    void testDeposer_Success() {

        CompteCourant compte = new CompteCourant("123", new BigDecimal("100"), false, BigDecimal.ZERO);

        compte.deposer(new BigDecimal("50.50"));

        assertEquals(new BigDecimal("150.50"), compte.getSolde());
    }

    @Test
    void testDeposer_MontantNegatif_ShouldThrowException() {
        CompteCourant compte = new CompteCourant("123", BigDecimal.ZERO, false, BigDecimal.ZERO);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.deposer(new BigDecimal("-10"));
        });

        assertEquals(ERR_MONTANT_POSITIF_DEPOT, exception.getMessage());
    }

    @Test
    void testRetirer_SansDecouvert_Success() {
        CompteCourant compte = new CompteCourant("123", new BigDecimal("100"), false, BigDecimal.ZERO);

        compte.retirer(new BigDecimal("70"));

        assertEquals(new BigDecimal("30"), compte.getSolde());
    }

    @Test
    void testRetirer_AvecDecouvert_Success() {
        CompteCourant compte = new CompteCourant("123", new BigDecimal("100"), true, new BigDecimal("500"));

        compte.retirer(new BigDecimal("400"));

        assertEquals(new BigDecimal("-300"), compte.getSolde());
    }

    @Test
    void testRetirer_DepassementDecouvert_ShouldThrowException() {
        CompteCourant compte = new CompteCourant("123", new BigDecimal("100"), true, new BigDecimal("200"));

        assertThrows(IllegalStateException.class, () -> {
            compte.retirer(new BigDecimal("400"));
        });
    }

    @Test
    void testRetirer_MontantNegatif_ShouldThrowException() {
        CompteCourant compte = new CompteCourant("123", new BigDecimal("100"), false, BigDecimal.ZERO);

        assertThrows(IllegalArgumentException.class, () -> {
            compte.retirer(new BigDecimal("-50"));
        });
    }
}