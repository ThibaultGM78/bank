package bank.adapter.out.persistence.sql.helper;

public class SQLQueries {

    public static final String GET_COMPTE_SQL = """
            SELECT id_compte, compte_type, compte_solde, compte_decouvert_autorise, compte_montant_decouvert, compte_plafond
            FROM BANK_COMPTE
            WHERE id_compte = ?
            """;

    public static final String TRANSACTION_INSERT_SQL = """
    INSERT INTO BANK_OPERATION (
        id_compte,
        operation_type,
        operation_montant,
        operation_date,
        operation_solde_apres,
        operation_libelle
    ) VALUES (?, ?, ?, ?, ?, ?)
    """;

    public static final String SOLDE_UPDATE_SQL = """
    UPDATE BANK_COMPTE
    SET compte_solde = ?
    WHERE id_compte = ?
    """;

    public static final String GET_OPERATIONS_SQL = """
    SELECT
        id_operation,
        id_compte,
        operation_type,
        operation_montant,
        operation_date,
        operation_solde_apres,
        operation_libelle
    FROM BANK_OPERATION
    WHERE id_compte = ? AND operation_date >= DATE_SUB(NOW(), INTERVAL 1 MONTH)
    ORDER BY operation_date DESC
    """;
}
