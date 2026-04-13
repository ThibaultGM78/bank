package bank.application.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    @DisplayName("Succès : Création d'une opération valide avec tous les champs")
    void shouldCreateValidOperation() {
        String numero = "12345";
        BigDecimal montant = new BigDecimal("100.00");
        LocalDateTime manualDate = LocalDateTime.of(2026, 4, 14, 10, 0);

        Operation op = Operation.builder()
                .numeroCompte(numero)
                .montant(montant)
                .type("DEPOT")
                .date(manualDate)
                .build();

        assertAll(
                () -> assertEquals(numero, op.numeroCompte()),
                () -> assertEquals(montant, op.montant()),
                () -> assertEquals(manualDate, op.date()),
                () -> assertEquals("DEPOT", op.type())
        );
    }

    @Test
    @DisplayName("Succès : La date doit être générée automatiquement si elle est nulle")
    void shouldGenerateDateWhenNull() {
        Operation op = Operation.builder()
                .numeroCompte("123")
                .montant(new BigDecimal("50"))
                .date(null)
                .build();

        assertNotNull(op.date());

        assertTrue(op.date().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Erreur : Numéro de compte manquant")
    void shouldThrowExceptionWhenNumeroCompteIsNull() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            Operation.builder()
                    .numeroCompte(null)
                    .montant(new BigDecimal("100"))
                    .build();
        });

        assertEquals(Operation.ERR_NUMERO_OBLIGATOIRE, ex.getMessage());
    }

    @Test
    @DisplayName("Erreur : Montant manquant (null)")
    void shouldThrowExceptionWhenMontantIsNull() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            Operation.builder()
                    .numeroCompte("123")
                    .montant(null)
                    .build();
        });

        assertEquals(Operation.ERR_MONTANT_OBLIGATOIRE, ex.getMessage());
    }

    @Test
    @DisplayName("Erreur : Montant égal à zéro")
    void shouldThrowExceptionWhenMontantIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Operation.builder()
                    .numeroCompte("123")
                    .montant(BigDecimal.ZERO)
                    .build();
        });

        assertEquals(Operation.ERR_MONTANT_STRICT_POSITIF, ex.getMessage());
    }

    @Test
    @DisplayName("Erreur : Montant négatif")
    void shouldThrowExceptionWhenMontantIsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            Operation.builder()
                    .numeroCompte("123")
                    .montant(new BigDecimal("-0.01"))
                    .build();
        });

        assertEquals(Operation.ERR_MONTANT_STRICT_POSITIF, ex.getMessage());
    }
}