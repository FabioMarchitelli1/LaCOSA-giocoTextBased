package di.lacosa.database;

import di.lacosa.tipi.DialogoGiocatore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per la gestione delle interazioni del giocatore con i personaggi nel database.
 * <p>
 * Fornisce metodi per:
 * <ul>
 *   <li>Inserire nuove opzioni di dialogo per il giocatore</li>
 *   <li>Collegare le risposte del giocatore alle reazioni dei personaggi</li>
 *   <li>Recuperare tutte le interazioni disponibili</li>
 * </ul>
 * La classe gestisce il sistema di dialoghi ad albero tra giocatore e personaggi.
 */
public class TabellaInterazioniGiocatore {

    /**
     * Inserisce una nuova interazione del giocatore nel database.
     * <p>
     * L'interazione rappresenta una possibile scelta dialogica che il giocatore
     * può fare durante una conversazione con un personaggio.
     *
     * @param id_interlocutore L'ID del personaggio con cui si interagisce
     * @param nodo_dialogo Il nodo dell'albero dialogico a cui appartiene l'opzione (può essere null)
     * @param id_dialogo_risposta L'ID della risposta del personaggio a questa opzione (può essere null)
     * @param testo Il testo della scelta dialogica mostrata al giocatore
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void inserisciInterazioneGiocatore(int id_interlocutore, Integer nodo_dialogo, Integer id_dialogo_risposta, String testo) {
        String comando_sql = "INSERT INTO InterazioniGiocatore (id_interlocutore, nodo_dialogo, id_dialogo_risposta, testo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(comando_sql)) {

            pstmt.setInt(1, id_interlocutore);

            if (nodo_dialogo != null) {
                pstmt.setInt(2, nodo_dialogo);
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }

            if (id_dialogo_risposta != null) {
                pstmt.setInt(3, id_dialogo_risposta);
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setString(4, testo);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Collega una risposta del personaggio a un'interazione del giocatore.
     * <p>
     * Questo metodo crea la relazione tra una scelta del giocatore e la
     * corrispondente reazione del personaggio nell'albero dialogico.
     *
     * @param id_risposta L'ID dell'interazione del giocatore da aggiornare
     * @param id_dialogo_risposta L'ID della risposta del personaggio da collegare
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void assegnaDialoghi(int id_risposta, int id_dialogo_risposta) {

        String sql = "UPDATE InterazioniGiocatore SET id_dialogo_risposta = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_dialogo_risposta);
            stmt.setInt(2, id_risposta);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /**
     * Recupera tutte le interazioni del giocatore dal database.
     * <p>
     * Restituisce una lista completa di tutte le possibili scelte dialogiche
     * che il giocatore può fare durante il gioco.
     *
     * @return Lista di oggetti DialogoGiocatore contenenti tutte le interazioni
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static List<DialogoGiocatore> getTutteInterazioniGiocatore(){
        String sql = "SELECT * FROM InterazioniGiocatore";
        List<DialogoGiocatore> interazioniGiocatore = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnessione();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()){
                int id = rs.getInt("id");
                int id_interlocutore = rs.getInt("id_interlocutore");
                int nodo_dialogo = rs.getInt("nodo_dialogo");
                String testo = rs.getString("testo");
                int id_dialogo_risposta = rs.getInt("id_dialogo_risposta");
                DialogoGiocatore dialogoGiocatore = new DialogoGiocatore(id, id_interlocutore, nodo_dialogo, testo, id_dialogo_risposta);
                interazioniGiocatore.add(dialogoGiocatore);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return interazioniGiocatore;

    }
}
