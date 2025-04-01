package di.lacosa.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe responsabile della creazione della struttura del database di gioco.
 * <p>
 * Contiene i metodi per:
 * <ul>
 *   <li>Creare tutte le tabelle necessarie</li>
 *   <li>Aggiungere vincoli di chiave esterna complessi</li>
 * </ul>
 * La classe gestisce lo schema completo del database utilizzato dal gioco.
 *
 * @author fabiomarchitelli
 */
public class CreazioneDatabase {

    /**
     * Crea tutte le tabelle del database di gioco.
     * <p>
     * Le tabelle create includono:
     * <ul>
     *   <li>Stanze: per la mappa di gioco</li>
     *   <li>Oggetti: per gli oggetti interagibili</li>
     *   <li>Personaggi: per i personaggi del gioco</li>
     *   <li>Interazioni: per i sistemi di dialogo</li>
     *   <li>Alias: per i nomi alternativi di oggetti e personaggi</li>
     * </ul>
     *
     * Le tabelle vengono create in batch tramite un array di comandi SQL.
     */
    public static void creaTabelle() {
            String[] creazioneTabelle = {
                    "CREATE TABLE Stanze (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "    nome VARCHAR(255) NOT NULL, " +
                            "    descrizione TEXT, " +
                            "    osserva TEXT, " +
                            "    osserva_aggiornato TEXT, " +
                            "    id_stanza_nord INT, " +
                            "    id_stanza_sud INT, " +
                            "    id_stanza_est INT, " +
                            "    id_stanza_ovest INT, " +
                            "    visibile BOOLEAN NOT NULL, " +
                            "    porta_bloccata BOOLEAN NOT NULL, " +
                            "    codice_porta VARCHAR(255) NOT NULL," +
                            "    motivOsservaAggiornato INT NOT NULL" +
                            ");",

                    "CREATE TABLE Oggetti (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "    nome VARCHAR(255) NOT NULL, " +
                            "    descrizione TEXT NOT NULL, " +
                            "    tipo VARCHAR(50) NOT NULL, " +
                            "    testo TEXT, " +
                            "    id_stanza INT, " +
                            "    munizioni INT, " +
                            "    FOREIGN KEY (id_stanza) REFERENCES Stanze(id)" +
                            ");",

                    "CREATE TABLE Personaggi (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "    nome VARCHAR(255) NOT NULL, " +
                            "    id_stanza INT, " +
                            "    FOREIGN KEY (id_stanza) REFERENCES Stanze(id)" +
                            ");",

                    "CREATE TABLE InterazioniGiocatore (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "    id_interlocutore INT NOT NULL, " +
                            "    nodo_dialogo INT, " +
                            "    testo TEXT, " +
                            "    id_dialogo_risposta INT, " +
                            "    FOREIGN KEY (id_interlocutore) REFERENCES Personaggi(id)" +
                            ");",

                     "CREATE TABLE InterazioniPersonaggi (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "    id_personaggio INT, " +
                            "    id_dialogo_risposta INT, " +
                            "    testo TEXT, " +
                            "    prossimo_nodo INT, " +
                            "    FOREIGN KEY (id_personaggio) REFERENCES Personaggi(id)" +
                            ");",


                    "CREATE TABLE AliasOggetti (" +
                            "    id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "    id_oggetto INT, " +
                            "    alias VARCHAR(255), " +
                            "    FOREIGN KEY (id_oggetto) REFERENCES Oggetti(id)" +
                            ");",

                    "CREATE TABLE AliasPersonaggi (" +
                            "     id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "     id_personaggio INT,  " +
                            "     alias VARCHAR(255),   " +
                            "     FOREIGN KEY (id_personaggio) REFERENCES Personaggi(id)" +
                            ");"
            };

            try (Connection conn = DatabaseManager.getConnessione();
                 Statement stmt = conn.createStatement()) {

                for (String sql : creazioneTabelle) {
                    stmt.execute(sql);
                }
                System.out.println("Tabelle create con successo.");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    /**
     * Aggiunge i vincoli di chiave esterna che non possono essere creati durante la creazione iniziale delle tabelle.
     * <p>
     * Questo metodo risolve il problema delle dipendenze circolari tra:
     * <ul>
     *   <li>InterazioniGiocatore</li>
     *   <li>InterazioniPersonaggi</li>
     * </ul>
     *
     * I vincoli vengono aggiunti separatamente perché le tabelle si riferiscono reciprocamente,
     * rendendo impossibile la creazione durante l'istruzione CREATE TABLE iniziale.
     */
    public static void aggiungiForeignKey() {
                String[] aggiuntaVincoli = {
                        "ALTER TABLE InterazioniGiocatore ADD CONSTRAINT per_dialogo_risposta FOREIGN KEY (id_dialogo_risposta) REFERENCES InterazioniPersonaggi(id);",
                        "ALTER TABLE InterazioniPersonaggi ADD CONSTRAINT per_id_risposta FOREIGN KEY (id_dialogo_risposta) REFERENCES InterazioniGiocatore(id);",
                };

                try (Connection conn = DatabaseManager.getConnessione();
                     Statement stmt = conn.createStatement()) {

                    for (String sql : aggiuntaVincoli) {
                        stmt.execute(sql);
                    }
                    System.out.println("Vincoli delle chiavi esterne aggiunti con successo.");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
