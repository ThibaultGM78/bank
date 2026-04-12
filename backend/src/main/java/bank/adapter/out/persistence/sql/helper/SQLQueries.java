package bank.adapter.out.persistence.sql.helper;

public class SQLQueries {

    public static final String GET_COMPTE_SQL = """
            SELECT id_compte, compte_type, compte_solde, compte_decouvert_autorise, compte_montant_decouvert, compte_plafond
            FROM BANK_COMPTE
            WHERE id_compte = ?
            """;
}
