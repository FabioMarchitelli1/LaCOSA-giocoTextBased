package di.lacosa;

import di.lacosa.implementazione.LaCosa;
import java.io.*;

/**
 * Classe di utilità per la gestione del salvataggio e caricamento delle partite.
 * Fornisce metodi statici per salvare, caricare e gestire i salvataggi del gioco
 * in slot numerati (1-3). I salvataggi vengono memorizzati nella cartella 'saves/'.
 *
 * @author fabioMarchitelli
 */
public class GestoreSalvataggi {

    /**
     * Cartella dove vengono salvati i file di salvataggio
     */
    private static final String CARTELLA_SALVATAGGIO = "saves/";

    /**
     * Numero massimo di slot di salvataggio disponibili
     */
    private static final int MAX_SLOTS = 3;

    // Inizializzazione statica: crea la cartella se non esiste
    static {
        File dir = new File(CARTELLA_SALVATAGGIO);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Salva lo stato del gioco nello slot specificato.
     * Sovrascrive eventuali salvataggi esistenti nello stesso slot.
     *
     * @param game L'istanza del gioco " completamente Serializzabile" da salvare (non null)
     * @param slot Il numero dello slot (1-3)
     * @return true se il salvataggio è avvenuto con successo, false altrimenti
     */
    public static boolean salvaPartita(LaCosa game, int slot) {
        if (slot < 1 || slot > 3) {
            System.err.println("Slot invalido: " + slot);
            return false;
        }
        String nomeFile = CARTELLA_SALVATAGGIO + "slot" + slot + ".dat";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeFile))) {
            oos.writeObject(game);
            System.out.println("Partita salvata su: " + nomeFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carica una partita dallo slot specificato.
     *
     * @param slot Il numero dello slot da caricare (1-3)
     * @return L'istanza del gioco caricata, o null se:
     *         - lo slot non è valido
     *         - il file non esiste
     *         - si verifica un errore durante la lettura
     */
    public static LaCosa caricaPartita(int slot) {
        if (slot < 1 || slot > 3) {
            System.err.println("Slot invalido: " + slot);
            return null;
        }
        String nomeFile = CARTELLA_SALVATAGGIO + "slot" + slot + ".dat";
        File file = new File(nomeFile);

        if (!file.exists()) {
            System.out.println("Nessuna partita salvata trovata nello slot " + slot);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeFile))) {
            Object obj = ois.readObject();
            if (obj instanceof LaCosa) {
                System.out.println("Partita caricata dallo slot " + slot);
                return (LaCosa) obj;
            } else {
                System.err.println("Il file non contiene un oggetto LaCosa!");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Conta quanti slot di salvataggio sono attualmente utilizzati.
     *
     * @return Il numero di slot occupati (0-3)
     */
    public static int getNumeroSlotPieni() {
        int contatore = 0;
        for (int i = 1; i <= MAX_SLOTS; i++) {
            File f = new File(CARTELLA_SALVATAGGIO + "slot" + i + ".dat");
            if (f.exists()) {
                contatore++;
            }
        }
        return contatore;
    }

    /**
     * Elimina il salvataggio nello slot specificato.
     *
     * @param slot Il numero dello slot da cancellare (1-3)
     * @return true se l'eliminazione è avvenuta con successo o il file non esisteva,
     *         false se lo slot non è valido
     */
    public static boolean cancellaSlot(int slot) {
        if (slot < 1 || slot > 3) return false;
        File f = new File(CARTELLA_SALVATAGGIO + "slot" + slot + ".dat");
        if (f.exists()) {
            return f.delete();
        }
        return false;
    }



    /**
     * Verifica se esiste un salvataggio nello slot specificato.
     *
     * @param slot Il numero dello slot da verificare (1-3)
     * @return true se esiste un salvataggio nello slot, false altrimenti
     */
    public static boolean slotEsiste(int slot) {
        if (slot < 1 || slot > MAX_SLOTS) return false;
        File file = new File(CARTELLA_SALVATAGGIO + "slot" + slot + ".dat");
        return file.exists();
    }

}
