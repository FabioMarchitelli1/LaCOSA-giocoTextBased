package di.lacosa.database;

import di.lacosa.tipi.MotivazioneAggDescrizione;
import di.lacosa.tipi.Stanza;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per la gestione delle stanze nel database di gioco.
 * <p>
 * Fornisce metodi per:
 * <ul>
 *   <li>Inserire nuove stanze</li>
 *   <li>Recuperare stanze specifiche o tutte le stanze</li>
 *   <li>Gestire le connessioni tra stanze</li>
 *   <li>Modificare lo stato delle porte</li>
 *   <li>Eliminare stanze</li>
 * </ul>
 * La classe gestisce anche le descrizioni aggiornate delle stanze in base agli eventi di gioco.
 */
public class TabellaStanze {

    /**
     * Inserisce una nuova stanza nel database.
     *
     * @param nome Nome della stanza
     * @param descrizione Descrizione base della stanza
     * @param osserva Testo per l'azione "osserva"
     * @param osserva_aggiornato Testo aggiornato per l'azione "osserva" dopo eventi specifici
     * @param id_stanza_nord ID della stanza a nord (-1 se non presente)
     * @param id_stanza_sud ID della stanza a sud (-1 se non presente)
     * @param id_stanza_est ID della stanza a est (-1 se non presente)
     * @param id_stanza_ovest ID della stanza a ovest (-1 se non presente)
     * @param visibile Se la stanza è visibile nella mappa
     * @param porta_bloccata Se la stanza ha porte bloccate
     * @param codice_porta Codice per sbloccare la porta (se bloccata)
     * @param motivazione_aggiornamento Motivazione per l'aggiornamento della descrizione
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void inserisciStanza(String nome, String descrizione, String osserva, String osserva_aggiornato, int id_stanza_nord, int id_stanza_sud, int id_stanza_est, int id_stanza_ovest, boolean visibile, boolean porta_bloccata, String codice_porta, int motivazione_aggiornamento) {
        String comando_sql = "INSERT INTO Stanze (nome, descrizione, osserva, osserva_aggiornato, id_stanza_nord, id_stanza_sud, id_stanza_est, id_stanza_ovest, visibile, porta_bloccata, codice_porta, motivOsservaAggiornato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(comando_sql)) {

            pstmt.setString(1, nome);
            pstmt.setString(2, descrizione);
            pstmt.setString(3, osserva);
            pstmt.setString(4, osserva_aggiornato);
            pstmt.setInt(5, id_stanza_nord);
            pstmt.setInt(6, id_stanza_sud);
            pstmt.setInt(7, id_stanza_est);
            pstmt.setInt(8, id_stanza_ovest);
            pstmt.setBoolean(9, visibile);
            pstmt.setBoolean(10, porta_bloccata);
            pstmt.setString(11, codice_porta);
            pstmt.setInt(12, motivazione_aggiornamento);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera tutte le stanze presenti nel database.
     *
     * @return Lista di tutte le stanze del gioco
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static List<Stanza> getOgniStanza() {
        String sql = "SELECT * FROM Stanze";
        List<Stanza> stanze = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnessione();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descrizione = rs.getString("descrizione");
                String osserva = rs.getString("osserva");
                String osserva_aggiornato = rs.getString("osserva_aggiornato");
                boolean visibile = rs.getBoolean("visibile");
                int id_stanza_nord = rs.getInt("id_stanza_nord");
                int id_stanza_sud = rs.getInt("id_stanza_sud");
                int id_stanza_est = rs.getInt("id_stanza_est");
                int id_stanza_ovest = rs.getInt("id_stanza_ovest");
                boolean porta_bloccata = rs.getBoolean("porta_bloccata");
                String codice_porta = rs.getString("codice_porta");
                int motivOsservaAggiornato = rs.getInt("motivOsservaAggiornato");

                Stanza stanza = null;

                switch (motivOsservaAggiornato){
                    case 1:
                        stanza = new Stanza(id, nome, descrizione, osserva, osserva_aggiornato, visibile, id_stanza_nord, id_stanza_sud, id_stanza_est, id_stanza_ovest, porta_bloccata, codice_porta, MotivazioneAggDescrizione.PRIVA_DI_AGGIORNAMENTO);
                        break;
                    case 2:
                        stanza = new Stanza(id, nome, descrizione, osserva, osserva_aggiornato, visibile, id_stanza_nord, id_stanza_sud,id_stanza_est, id_stanza_ovest, porta_bloccata, codice_porta, MotivazioneAggDescrizione.ACCENSIONE_TORCIA);
                        break;
                    case 3:
                        stanza = new Stanza(id, nome, descrizione, osserva, osserva_aggiornato, visibile, id_stanza_nord, id_stanza_sud,id_stanza_est, id_stanza_ovest, porta_bloccata, codice_porta, MotivazioneAggDescrizione.MODIFICATO_DA_EVENTO);

                }
                stanze.add(stanza);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stanze;
    }


    /**
     * Imposta le stanze adiacenti a una stanza specifica.
     *
     * @param idStanza ID della stanza principale
     * @param idNord ID della stanza a nord (-1 per nessuna stanza)
     * @param idSud ID della stanza a sud (-1 per nessuna stanza)
     * @param idEst ID della stanza a est (-1 per nessuna stanza)
     * @param idOvest ID della stanza a ovest (-1 per nessuna stanza)
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void impostaStanzeAdiacenti(int idStanza, int idNord, int idSud, int idEst, int idOvest) {

        String sql = "UPDATE Stanze SET id_stanza_nord = ?, id_stanza_sud = ?, id_stanza_est = ?, id_stanza_ovest = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idNord);
            stmt.setInt(2, idSud);
            stmt.setInt(3, idEst);
            stmt.setInt(4, idOvest);
            stmt.setInt(5, idStanza);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
