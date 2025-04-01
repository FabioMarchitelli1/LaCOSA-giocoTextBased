package di.lacosa.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe per la gestione degli alias degli oggetti nel database di gioco.
 * <p>
 * Fornisce metodi per:
 * <ul>
 *   <li>Inserire nuovi alias per gli oggetti</li>
 *   <li>Recuperare tutti gli alias di un oggetto</li>
 * </ul>
 * Gli alias permettono di referenziare gli oggetti con nomi alternativi
 * riconosciuti dal parser dei comandi.
 */
public class TabellaAliasOggetti {

    /**
     * Inserisce un nuovo alias per un oggetto nel database.
     * <p>
     * L'alias permetterà di referenziare l'oggetto con un nome alternativo
     * durante l'interazione col gioco.
     *
     * @param id_oggetto L'ID dell'oggetto a cui associare l'alias
     * @param alias Il nome alternativo da aggiungere (case-sensitive)
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void inserisciAliasOggetto(int id_oggetto, String alias){
    String comando_sql = "INSERT INTO AliasOggetti (id_oggetto, alias) VALUES (?, ?)";

    try (Connection conn = DatabaseManager.getConnessione();
         PreparedStatement pstmt = conn.prepareStatement(comando_sql)) {

        pstmt.setInt(1, id_oggetto);
        pstmt.setString(2, alias);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    /**
     * Recupera tutti gli alias associati a un oggetto specifico.
     * <p>
     * Restituisce un Set contenente tutti i nomi alternativi con cui è possibile
     * referenziare l'oggetto durante il gioco.
     *
     * @param idOggetto L'ID dell'oggetto di cui recuperare gli alias
     * @return Set di stringhe contenente tutti gli alias dell'oggetto
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static Set<String> getAliasesPerOggetto(int idOggetto) {
        String sql = "SELECT * FROM AliasOggetti WHERE id_oggetto = ?";
        Set<String> aliases = new HashSet<>();

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idOggetto);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String alias = rs.getString("alias");
                aliases.add(alias);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aliases;
    }
}
