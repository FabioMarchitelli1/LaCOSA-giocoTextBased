package di.lacosa.database;

import java.sql.*;

/**
 * Gestisce la connessione e le operazioni base del database del gioco.
 * <p>
 * Questa classe fornisce:
 * <ul>
 *   <li>Configurazione della connessione al database H2</li>
 *   <li>Metodi per verificare l'esistenza del database</li>
 *   <li>Utility per testare la connessione</li>
 *   <li>Metodo main per l'inizializzazione del database</li>
 * </ul>
 * Utilizza un database H2 embedded con autenticazione.
 *
 * @author fabiomarchitelli
 */
public class DatabaseManager {

    //URL di connessione al database H2 (file-based)
    private static final String JDBC_URL = "jdbc:h2:./database/databaseDiGioco";

    //Nome utente per l'autenticazione al database
    private static final String USER = "giocatore";

    //Password per l'autenticazione al database
    private static final String PASSWORD = "1234567";

    /**
     * Ottiene una connessione al database.
     *
     * @return Connection oggetto di connessione al database
     * @throws SQLException in caso di errori durante la connessione
     */
    public static Connection getConnessione() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    /**
     * Metodo main per l'inizializzazione del database.
     * <p>
     * Verifica se il database esiste già controllando la presenza delle tabelle.
     * Se non esiste, procede con:
     * <ol>
     *   <li>Creazione delle tabelle</li>
     *   <li>Aggiunta dei vincoli di chiave esterna</li>
     *   <li>Inizializzazione dei dati</li>
     * </ol>
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
            try (Connection conn = DatabaseManager.getConnessione()) {
                if (!verificaDatabase(conn)) {
                    CreazioneDatabase.creaTabelle();
                    CreazioneDatabase.aggiungiForeignKey();
                    InizializzaTabelle.inizializzaTutto();
                } else {
                    System.out.println("Il database esiste già.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }


    /**
     * Verifica la presenza delle tabelle nel database.
     * <p>
     * Controlla l'esistenza della tabella "STANZE" come indicatore
     * che il database è già stato inizializzato.
     *
     * @param conn la connessione al database da verificare
     * @return true se la tabella esiste, false altrimenti
     * @throws SQLException in caso di errori durante l'accesso ai metadati
     */
    private static boolean verificaDatabase(Connection conn) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        try (ResultSet rs = dbMetaData.getTables(null, null, "STANZE", null)) {
            return rs.next();
        }
    }
}
