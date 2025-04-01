package di.lacosa.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe per la gestione degli alias dei personaggi nel database di gioco.
 * <p>
 * Fornisce metodi per:
 * <ul>
 *   <li>Aggiungere alias alternativi per i personaggi</li>
 *   <li>Recuperare tutti gli alias associati a un personaggio</li>
 * </ul>
 * Gli alias permettono ai giocatori di riferirsi ai personaggi usando diversi nomi
 * o termini durante le interazioni di gioco.
 */
public class TabellaAliasPersonaggi {

    /**
     * Aggiunge un nuovo alias per un personaggio specifico.
     *
     * @param id_personaggio L'ID del personaggio a cui associare l'alias
     * @param alias Il nome alternativo da aggiungere (case-sensitive)
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void inserisciAliasPersonaggio(int id_personaggio, String alias){
        String comando_sql = "INSERT INTO AliasPersonaggi (id_personaggio, alias) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(comando_sql)) {

            pstmt.setInt(1, id_personaggio);
            pstmt.setString(2, alias);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera tutti gli alias associati a un personaggio specifico.
     *
     * @param idPersonaggio L'ID del personaggio di cui recuperare gli alias
     * @return Set di stringhe contenente tutti gli alias del personaggio
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static Set<String> getAliasesPersonaggi(int idPersonaggio) {
        String sql = "SELECT * FROM AliasPersonaggi WHERE id_personaggio = ?";
        Set<String> aliases = new HashSet<>();

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPersonaggio);
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
