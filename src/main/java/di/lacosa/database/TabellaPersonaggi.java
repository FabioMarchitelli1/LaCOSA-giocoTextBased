package di.lacosa.database;

import di.lacosa.tipi.Personaggio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe per la gestione dei personaggi nel database di gioco.
 * <p>
 * Fornisce metodi per:
 * <ul>
 *   <li>Inserire nuovi personaggi</li>
 *   <li>Recuperare tutti i personaggi</li>
 *   <li>Ottenere i personaggi presenti in una specifica stanza</li>
 * </ul>
 * La classe gestisce anche il recupero degli alias associati a ciascun personaggio.
 */
public class TabellaPersonaggi {

    /**
     * Inserisce un nuovo personaggio nel database.
     * <p>
     * Il personaggio può essere associato a una stanza specifica o essere generico (id_stanza null).
     *
     * @param nome Il nome del personaggio
     * @param id_stanza L'ID della stanza in cui si trova il personaggio (può essere null)
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void inserisciPersonaggio(String nome, Integer id_stanza) {
        String comando_sql = "INSERT INTO Personaggi (nome, id_stanza) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(comando_sql)) {

            pstmt.setString(1, nome);
            if (id_stanza != null) {
                pstmt.setInt(2, id_stanza);
            } else {
                pstmt.setNull(2, Types.INTEGER); //Questo setta il valore a null.
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Recupera tutti i personaggi presenti nel database.
     * <p>
     * Per ogni personaggio recupera anche gli alias associati.
     *
     * @return Lista di tutti i personaggi del gioco
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static List<Personaggio> getOgniPersonaggio() {
        String sql = "SELECT * FROM Personaggi";
        List<Personaggio> personaggi = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnessione();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id_personaggio= rs.getInt("id");
                String nome = rs.getString("nome");
                int id_stanza = rs.getInt("id_stanza");
                Set<String> alias = TabellaAliasPersonaggi.getAliasesPersonaggi(id_personaggio);

                Personaggio personaggio = new Personaggio( id_personaggio, nome, id_stanza, alias);
                personaggi.add(personaggio);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personaggi;
    }
}