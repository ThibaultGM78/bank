package bank.adapter.in.web.helper;

import bank.adapter.in.web.dto.CompteDTO;
import bank.adapter.in.web.dto.OperationDTO;
import bank.application.domain.Operation;
import bank.application.domain.compte.CompteCourant;
import bank.application.domain.compte.Livret;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BankHelperTest {

    @Test
    @DisplayName("toDTO : Devrait mapper un CompteCourant vers CompteDTO avec détails")
    void toDTO_CompteCourant() {
        CompteCourant compte = new CompteCourant("CC123", new BigDecimal("1000"), true, new BigDecimal("500"));

        CompteDTO dto = BankHelper.toDTO(compte);

        assertAll(
                () -> assertEquals("CC123", dto.getNumeroCompte()),
                () -> assertEquals(new BigDecimal("1000"), dto.getSolde()),
                () -> assertNotNull(dto.getDetailsCompteCourant()),
                () -> assertTrue(dto.getDetailsCompteCourant().isDecouvertAutorise()),
                () -> assertEquals(new BigDecimal("500"), dto.getDetailsCompteCourant().getMontantDecouvert()),
                () -> assertNull(dto.getDetailsLivret())
        );
    }

    @Test
    @DisplayName("toDTO : Devrait mapper un Livret vers CompteDTO avec détails")
    void toDTO_Livret() {
        Livret livret = new Livret("L456", new BigDecimal("2000"), new BigDecimal("10000"));

        CompteDTO dto = BankHelper.toDTO(livret);

        assertAll(
                () -> assertEquals("L456", dto.getNumeroCompte()),
                () -> assertNotNull(dto.getDetailsLivret()),
                () -> assertEquals(new BigDecimal("10000"), dto.getDetailsLivret().getPlafond()),
                () -> assertNull(dto.getDetailsCompteCourant())
        );
    }

    @Test
    @DisplayName("toDTO : Devrait mapper une Operation vers OperationDTO")
    void toDTO_Operation() {
        Operation op = Operation.builder()
                .numeroCompte("123")
                .type("DEPOT")
                .montant(new BigDecimal("50"))
                .soldeApres(new BigDecimal("150"))
                .date(LocalDateTime.now())
                .libelle("Test mapping")
                .build();

        OperationDTO dto = BankHelper.toDTO(op);

        assertEquals(op.numeroCompte(), dto.getNumeroCompte());
        assertEquals(op.montant(), dto.getMontant());
        assertEquals(op.libelle(), dto.getLibelle());
    }

    @Test
    @DisplayName("toDomain : Devrait mapper un OperationDTO vers le domaine Operation")
    void toDomain_Operation() {
        OperationDTO dto = OperationDTO.builder()
                .numeroCompte("123")
                .montant(new BigDecimal("50"))
                .libelle("Mapping inverse")
                .build();

        Operation op = BankHelper.toDomain(dto);

        assertAll(
                () -> assertEquals(dto.getNumeroCompte(), op.numeroCompte()),
                () -> assertEquals(dto.getMontant(), op.montant()),
                () -> assertEquals(dto.getLibelle(), op.libelle())
        );
    }

    @Test
    @DisplayName("Validation @NonNull : Devrait lever une exception si l'entrée est nulle")
    void testNonNullConstraints() {
        assertThrows(NullPointerException.class, () -> BankHelper.toDTO((bank.application.domain.Compte) null));
    }
}