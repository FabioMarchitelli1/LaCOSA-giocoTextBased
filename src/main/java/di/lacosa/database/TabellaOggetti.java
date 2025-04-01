package di.lacosa.database;

import di.lacosa.tipi.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe per la gestione degli oggetti di gioco nel database.
 * <p>
 * Fornisce metodi per:
 * <ul>
 *   <li>Recuperare oggetti specifici</li>
 *   <li>Inserire nuovi oggetti</li>
 *   <li>Ottenere liste di oggetti per stanza o globali</li>
 * </ul>
 * La classe gestisce la creazione delle istanze corrette in base al tipo di oggetto.
 */
public class TabellaOggetti {

    /**
     * Recupera un oggetto specifico dal database in base al suo ID.
     * <p>
     * Crea l'istanza della classe corretta in base al tipo specificato nel database.
     *
     * @param id L'ID dell'oggetto da recuperare
     * @return L'oggetto istanziato nella classe appropriata, o null se non trovato
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public Oggetto getOggetto(int id) {
        String sql = "SELECT * FROM Oggetti WHERE id = ?";
        Oggetto oggetto = null;

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int oggettoId = rs.getInt("id");
                String nome = rs.getString("nome");
                String descr = rs.getString("descrizione");
                String tipo = rs.getString("tipo");
                String testo = rs.getString("testo");
                int id_stanza = rs.getInt("id_stanza");
                int munizioni = rs.getInt("munizioni");

                Set<String> alias = TabellaAliasOggetti.getAliasesPerOggetto(oggettoId);

                switch (tipo) {
                    case "OggettoRaccoglibile":
                        oggetto = new OggettoRaccoglibile(oggettoId, nome, descr, alias, id_stanza);
                        break;

                    case "OggettoNonRaccoglibile":
                        oggetto = new OggettoNonRaccoglibile(oggettoId, nome, descr, alias, id_stanza);
                        break;

                    case "OggettoAttivabile":
                        oggetto = new OggettoAttivabile(oggettoId, nome, descr, alias, id_stanza);

                    case "OggettoLeggibile":
                        oggetto = new OggettoLeggibile(oggettoId, nome, descr, testo, alias, id_stanza);
                        break;

                    case "OggettoLeggibileNonRaccoglibile":
                        oggetto = new OggettoLeggibileNonRaccoglibile(oggettoId, nome, descr, testo, alias, id_stanza);
                        break;

                    case "Arma":
                        oggetto = new Arma(oggettoId, nome, descr, munizioni, alias, id_stanza);
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oggetto;
    }

    /**
     * Inserisce un nuovo oggetto nel database.
     * <p>
     * I campi opzionali possono essere null e verranno gestiti appropriatamente.
     *
     * @param nome Il nome dell'oggetto
     * @param descrizione La descrizione dell'oggetto
     * @param tipo Il tipo di oggetto (determina la classe concreta)
     * @param testo Il testo per oggetti leggibili (può essere null)
     * @param id_stanza L'ID della stanza in cui si trova (può essere null)
     * @param munizioni Il numero di munizioni per le armi (può essere null)
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static void inserisciOggetto( String nome, String descrizione, String tipo, String testo, Integer id_stanza, Integer munizioni) {
        String comando_sql = "INSERT INTO Oggetti (nome, descrizione, tipo, testo, id_stanza, munizioni) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnessione();
             PreparedStatement pstmt = conn.prepareStatement(comando_sql)) {

            pstmt.setString(1, nome );
            pstmt.setString(2, descrizione);
            pstmt.setString(3, tipo);
            pstmt.setString(4, testo);

            if (id_stanza != null) {
                pstmt.setInt(5, id_stanza);
            } else {
                pstmt.setNull(5, Types.INTEGER); //Questo setta il valore a null.
            }

            if (munizioni != null) {
                pstmt.setInt(6, munizioni);
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Recupera tutti gli oggetti presenti nel database.
     *
     * @return Lista completa di tutti gli oggetti
     * @throws SQLException Se si verifica un errore durante l'accesso al database
     */
    public static List<Oggetto> getOgniOggetto() {
        String sql = "SELECT * FROM Oggetti";
        List<Oggetto> oggetti = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnessione();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int oggettoId = rs.getInt("id");
                String nome = rs.getString("nome");
                String descr = rs.getString("descrizione");
                String tipo = rs.getString("tipo");
                String testo = rs.getString("testo");
                int id_stanza = rs.getInt("id_stanza");
                int munizioni = rs.getInt("munizioni");

                Set<String> alias = TabellaAliasOggetti.getAliasesPerOggetto(oggettoId);

                Oggetto oggetto = null;

                switch (tipo) {
                    case "OggettoRaccoglibile":
                        oggetto = new OggettoRaccoglibile(oggettoId, nome, descr, alias, id_stanza);
                        break;

                    case "OggettoNonRaccoglibile":
                        oggetto = new OggettoNonRaccoglibile(oggettoId, nome, descr, alias, id_stanza);
                        break;

                    case "OggettoAttivabile":
                        oggetto = new OggettoAttivabile(oggettoId, nome, descr, alias, id_stanza);
                        break;

                    case "OggettoLeggibile":
                        oggetto = new OggettoLeggibile(oggettoId, nome, descr, testo, alias, id_stanza);
                        break;

                    case "OggettoLeggibileNonRaccoglibile":
                        oggetto = new OggettoLeggibileNonRaccoglibile(oggettoId, nome, descr, testo, alias, id_stanza);
                        break;

                    case "Arma":
                        oggetto = new Arma(oggettoId, nome, descr, munizioni, alias, id_stanza);
                        break;
                }
                oggetti.add(oggetto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oggetti;
    }


}

