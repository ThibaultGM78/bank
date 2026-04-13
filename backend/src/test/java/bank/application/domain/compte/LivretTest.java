package bank.application.domain.compte;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static bank.application.domain.compte.Livret.ERR_PLAFOND_DEPASSE;
import static org.junit.jupiter.api.Assertions.*;

class LivretTest {

    @Test
    void testDeposer_SousLePlafond_Success() {
        Livret livret = new Livret("L-123", new BigDecimal("100"), new BigDecimal("500"));

        livret.deposer(new BigDecimal("300"));

        assertEquals(new BigDecimal("400"), livret.getSolde());
    }

    @Test
    void testDeposer_AtteinteExacteDuPlafond_Success() {
        Livret livret = new Livret("L-123", new BigDecimal("100"), new BigDecimal("500"));

        livret.deposer(new BigDecimal("400"));

        assertEquals(new BigDecimal("500"), livret.getSolde());
    }

    @Test
    void testDeposer_DepassementPlafond_ShouldThrowException() {
        Livret livret = new Livret("L-123", new BigDecimal("450"), new BigDecimal("500"));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            livret.deposer(new BigDecimal("60"));
        });

        assertTrue(exception.getMessage().contains(ERR_PLAFOND_DEPASSE));
    }

    @Test
    void testDeposer_MontantNegatif_ShouldThrowException() {
        Livret livret = new Livret("L-123", BigDecimal.ZERO, new BigDecimal("1000"));

        assertThrows(IllegalArgumentException.class, () -> {
            livret.deposer(new BigDecimal("-5"));
        });
    }


    @Test
    void testRetirer_SoldeSuffisant_Success() {
        Livret livret = new Livret("L-123", new BigDecimal("200"), new BigDecimal("1000"));

        livret.retirer(new BigDecimal("150"));

        assertEquals(new BigDecimal("50"), livret.getSolde());
    }

    @Test
    void testRetirer_SoldeInsuffisant_ShouldThrowException() {
        Livret livret = new Livret("L-123", new BigDecimal("50"), new BigDecimal("1000"));

        assertThrows(IllegalStateException.class, () -> {
            livret.retirer(new BigDecimal("100"));
        });
    }

    @Test
    void testRetirer_MontantNegatif_ShouldThrowException() {
        Livret livret = new Livret("L-123", new BigDecimal("100"), new BigDecimal("1000"));

        assertThrows(IllegalArgumentException.class, () -> {
            livret.retirer(new BigDecimal("-10"));
        });
    }
}

