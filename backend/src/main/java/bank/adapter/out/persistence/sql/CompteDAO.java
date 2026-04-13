package bank.adapter.out.persistence.sql;

import bank.application.dao.ICompteDAO;
import bank.application.domain.Compte;
import bank.application.domain.compte.CompteCourant;
import bank.application.domain.compte.Livret;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static bank.adapter.out.persistence.sql.helper.SQLQueries.GET_COMPTE_SQL;
import static bank.adapter.out.persistence.sql.helper.SQLQueries.SOLDE_UPDATE_SQL;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
public class CompteDAO implements ICompteDAO {
    private static final String ID_COMPTE = "id_compte";
    private static final String COMPTE_TYPE = "compte_type";
    private static final String COMPTE_SOLDE = "compte_solde";
    private static final String COMPTE_DECOUVERT_AUTORISE = "compte_decouvert_autorise";
    private static final String COMPTE_MONTANT_DECOUVERT = "compte_montant_decouvert";
    private static final String COMPTE_PLAFOND = "compte_plafond";

    private final JdbcTemplate jdbcTemplate;

    public Compte getCompte(String numeroCompte) {
        requireNonNull(numeroCompte);

        try {
            final var resultMap = jdbcTemplate.queryForMap(GET_COMPTE_SQL, numeroCompte);

            String id = (String) resultMap.get(ID_COMPTE);
            BigDecimal solde = (BigDecimal) resultMap.get(COMPTE_SOLDE);
            String type = (String) resultMap.get(COMPTE_TYPE);

            if ("COURANT".equals(type)) {
                return CompteCourant.builder()
                        .numeroCompte(id)
                        .solde(solde)
                        .decouvertAutorise(Boolean.TRUE.equals(resultMap.get(COMPTE_DECOUVERT_AUTORISE)))
                        .montantDecouvert((BigDecimal) resultMap.get(COMPTE_MONTANT_DECOUVERT))
                        .build();
            }

            if ("LIVRET".equals(type)) {
                return Livret.builder()
                        .numeroCompte(id)
                        .solde(solde)
                        .plafond((BigDecimal) resultMap.get(COMPTE_PLAFOND))
                        .build();
            }

            throw new IllegalStateException("Type de compte non géré");

        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Compte introuvable");
        }
    }

    public void updateSolde(Compte compte) {
        int rowsAffected = jdbcTemplate.update(SOLDE_UPDATE_SQL,
                compte.getSolde(),
                compte.getNumeroCompte()
        );

        if (rowsAffected == 0) {
            throw new RuntimeException("Échec de la mise à jour : le compte n'existe pas.");
        }
    }

}
