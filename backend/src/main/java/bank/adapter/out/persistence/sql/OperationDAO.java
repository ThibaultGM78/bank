package bank.adapter.out.persistence.sql;

import bank.application.dao.IOperationDAO;
import bank.application.domain.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static bank.adapter.out.persistence.sql.helper.SQLQueries.*;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
public class OperationDAO implements IOperationDAO {
    public static final String ID_COMPTE = "id_compte";
    public static final String OPERATION_TYPE = "operation_type";
    public static final String OPERATION_MONTANT = "operation_montant";
    public static final String OPERATION_DATE = "operation_date";
    public static final String OPERATION_SOLDE_APRES = "operation_solde_apres";
    public static final String OPERATION_LIBELLE = "operation_libelle";

    private final JdbcTemplate jdbcTemplate;

    public void save(Operation operation) {
        try {
            jdbcTemplate.update(TRANSACTION_INSERT_SQL,
                    operation.numeroCompte(),
                    operation.type(),
                    operation.montant(),
                    java.sql.Timestamp.valueOf(operation.date()),
                    operation.soldeApres(),
                    operation.libelle()
            );
        } catch (DataAccessException e) {

            throw new RuntimeException("Erreur lors de la sauvegarde de l'opération en base de données", e);
        }
    }

    public List<Operation> getOperations(String numeroCompte) {
        requireNonNull(numeroCompte);

        try {
            return jdbcTemplate.query(GET_OPERATIONS_SQL, (rs, rowNum) ->
                Operation.builder()
                        .numeroCompte(rs.getString(ID_COMPTE))
                        .type(rs.getString(OPERATION_TYPE))
                        .montant(rs.getBigDecimal(OPERATION_MONTANT))
                        .date(rs.getTimestamp(OPERATION_DATE).toLocalDateTime())
                        .soldeApres(rs.getBigDecimal(OPERATION_SOLDE_APRES))
                        .libelle(rs.getString(OPERATION_LIBELLE))
                        .build(),
                numeroCompte
            );

        } catch (DataAccessException e) {
            throw new RuntimeException("Erreur lors de la récupération du relevé bancaire", e);
        }
    }
}