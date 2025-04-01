package di.lacosa;

import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import di.lacosa.implementazione.CreaturaThread;


/**
 * Classe astratta che rappresenta lo stato del gioco e le sue principali funzionalità.
 * Gestisce stanze, oggetti, personaggi, inventario e lo stato delle azioni di gioco.
 * Implementa Serializable per permettere il salvataggio dello stato del gioco.
 *
 * @author fabioMarchitelli
 * @serial 14L Numero di versione per la serializzazione
 */
public abstract class DescrizioneGioco implements Serializable {

    private static final long serialVersionUID = 14L;
    private final List<Stanza> stanze = new ArrayList<>();
    private final List<Comando> comandi = new ArrayList<>();
    private final Set<Oggetto> inventario = new HashSet<>();
    private Stanza stanzaCorrente;
    private final List<Oggetto> oggetti = new ArrayList<>();
    private final Set<Personaggio> personaggi = new HashSet<>();
    private final List<DialogoGiocatore> dialoghi = new ArrayList<>();
    private final List<RispostaPersonaggio> risposte = new ArrayList<>();
    private boolean torciaAccesa = false;
    private boolean creaturaCanideAttivata = false;
    private boolean creaturaCanideSconfitta = false ;
    private boolean primaAzioneGaslyAttivata = false;
    private boolean secondaAzioneGaslyAttivata = false;
    private boolean terzaAzioneGaslyAttivata = false;
    private boolean terrenoScavatoInSerra = false;
    private transient CreaturaThread creaturaThreadInstance;
    private transient Thread creaturaThread;


    /**
     * Restituisce la lista di tutte le stanze del gioco.
     *
     * @return Lista delle stanze
     */
    public List<Stanza> getStanze() {
        return stanze;
    }

    /**
     * Restituisce la lista di tutti gli oggetti del gioco.
     *
     * @return Lista degli oggetti
     */
    public List<Oggetto> getOggetti() {
        return oggetti;
    }

    /**
     * Restituisce gli oggetti presenti in una specifica stanza.
     *
     * @param idStanza L'ID della stanza da cercare
     * @return Set di oggetti presenti nella stanza
     */
    public Set<Oggetto> getOggettiPerStanza(int idStanza){
        Set<Oggetto> oggettiInStanza = new HashSet<>();

        for (Oggetto oggetto : oggetti) {
            if (oggetto.getId_stanza() == idStanza) {
                oggettiInStanza.add(oggetto);
            }
        }

        return oggettiInStanza;
    }

    /**
     * Restituisce i personaggi presenti in una specifica stanza.
     *
     * @param idStanza L'ID della stanza da cercare
     * @return Set di personaggi presenti nella stanza
     */
    public Set<Personaggio> getPersonaggiPerStanza(int idStanza) {
        Set<Personaggio> personaggiInStanza = new HashSet<>();

        for (Personaggio personaggio : personaggi) {
            if (personaggio.getId_stanza() == idStanza) {
                personaggiInStanza.add(personaggio);
            }
        }
        return personaggiInStanza;
    }

    /**
     * Restituisce una stanza per ID.
     *
     * @param idStanza L'ID della stanza da cercare
     * @return La stanza corrispondente o null se non trovata
     */
    public Stanza getStanzaPerId(int idStanza) {
        for (Stanza stanza : stanze) {
            if (stanza.getIdStanza() == idStanza) {
                return stanza;
            }
        }
        return null;
    }

    /**
     * Aggiunge un oggetto all'inventario modificando il suo ID stanza.
     *
     * @param id_oggetto L'ID dell'oggetto da aggiungere all'inventario
     */
    public void setOggettoInInventario(int id_oggetto) {
        //Questo id_inventario è utilizzato per impostare gli oggetti che precedentemente si trovavano nelle stanze, all'interno dell'inventario.
        int id_inventario = 0;
        boolean oggettoTrovato = false;

        for (Oggetto oggetto : oggetti) {
            if (oggetto.getId() == id_oggetto) { //Controlla se l'id dell'oggetto è lo stesso di quello dato in input.
                if (oggetto.getId_stanza() != id_inventario) { //controlla che non sia già nell'inventario
                    oggetto.setId_stanza(id_inventario);
                    oggettoTrovato = true;
                } else {
                    System.out.println("Oggetto già nell'inventario: " + id_oggetto);
                    oggettoTrovato = true;
                }
                break; //esci dal for
            }
        }

        if (!oggettoTrovato) {
            System.out.println("Oggetto con id " + id_oggetto + " non trovato.");
        }
    }

    /**
     * Restituisce la lista di tutti i comandi disponibili nel gioco.
     *
     * @return Lista dei comandi
     */
    public List<Comando> getComandi() {
        return comandi;
    }

    /**
     * Restituisce la stanza corrente in cui si trova il giocatore.
     *
     * @return La stanza corrente
     */
    public Stanza getStanzaCorrente() {
        return stanzaCorrente;
    }

    /**
     * Imposta la stanza corrente del giocatore.
     *
     * @param stanzaCorrente La nuova stanza corrente
     */
    public void setStanzaCorrente(Stanza stanzaCorrente) {
        this.stanzaCorrente = stanzaCorrente;
    }

    /**
     * Restituisce l'inventario del giocatore.
     *
     * @return Set di oggetti nell'inventario
     */
    public Set<Oggetto> getInventario() {
        return inventario;
    }

    /**
     * Restituisce tutti i personaggi del gioco.
     *
     * @return Set di personaggi
     */
    public Set<Personaggio> getPersonaggi() {
        return personaggi;
    }

    /**
     * Cerca un personaggio per nome (case insensitive).
     *
     * @param nomePersonaggio Il nome del personaggio da cercare
     * @return Il personaggio trovato o null
     */
    public Personaggio getPersonaggioPerNome(String nomePersonaggio) {
        for (Personaggio personaggio : personaggi) {
            if (personaggio.getNome().equalsIgnoreCase(nomePersonaggio)) {
                return personaggio;
            }
        }
        return null;
    }

    /**
     * Cerca un oggetto per ID.
     *
     * @param idOggetto L'ID dell'oggetto da cercare
     * @return L'oggetto trovato o null
     */
    public Oggetto getOggettoPerId(int idOggetto) {
        for (Oggetto oggetto : oggetti) {
            if (oggetto.getId() == idOggetto) {
                return oggetto;
            }
        }
        return null;
    }

    /**
     * Restituisce tutti i dialoghi del giocatore.
     *
     * @return Lista dei dialoghi
     */
    public List<DialogoGiocatore> getDialoghi() {
        return dialoghi;
    }

    /**
     * Restituisce tutte le risposte dei personaggi.
     *
     * @return Lista delle risposte
     */
    public List<RispostaPersonaggio> getRisposte() {
        return risposte;
    }

    /**
     * Restituisce i dialoghi specifici per un personaggio.
     *
     * @param id_interlocutore L'ID del personaggio interlocutore
     * @return Lista dei dialoghi per quel personaggio
     */
    public List<DialogoGiocatore> getDialoghiPerIdPersonaggio(int id_interlocutore){

        List<DialogoGiocatore> dialoghiPerId = new ArrayList<>();
        for (DialogoGiocatore dialogoGiocatore : dialoghi) {
            if (dialogoGiocatore.getId_interlocutore() == id_interlocutore) {
                dialoghiPerId.add(dialogoGiocatore);
            }
        }
        return dialoghiPerId;
    }

    /**
     * Restituisce le risposte specifiche per un personaggio.
     *
     * @param id_interlocutore L'ID del personaggio interlocutore
     * @return Lista delle risposte per quel personaggio
     */
    public List<RispostaPersonaggio> getRispostePerIdPersonaggio(int id_interlocutore){

        List<RispostaPersonaggio> rispostePerId = new ArrayList<>();
        for (RispostaPersonaggio rispostaPersonaggio : risposte) {
            if(rispostaPersonaggio.getId_personaggio() == id_interlocutore){
                rispostePerId.add(rispostaPersonaggio);
            }
        }
        return rispostePerId;
    }

    /**
     * Cerca una risposta specifica tra quelle disponibili.
     *
     * @param idRisposta L'ID della risposta da cercare
     * @param risposte La lista di risposte in cui cercare
     * @return La risposta trovata o null
     */
    public RispostaPersonaggio getRispostaPerId(int idRisposta, List <RispostaPersonaggio> risposte) {
        for (RispostaPersonaggio risposta : risposte) {
            if (risposta.getId() == idRisposta) {
                return risposta;
            }
        }
        return null;
}


    // Metodi getter e setter per gli stati di gioco

    public boolean isCreaturaCanideAttivata() {
        return creaturaCanideAttivata;
    }

    public boolean isCreaturaCanideSconfitta() {
        return creaturaCanideSconfitta;
    }

    public void setCreaturaCanideAttivata(boolean creaturaCanideAttivata) {
        this.creaturaCanideAttivata = creaturaCanideAttivata;
    }

    public void setCreaturaCanideSconfitta(boolean creaturaCanideSconfitta) {
        this.creaturaCanideSconfitta = creaturaCanideSconfitta;
    }

    public void setCreaturaThread(CreaturaThread creaturaThreadInstance, Thread thread) {
        this.creaturaThreadInstance = creaturaThreadInstance;
        this.creaturaThread = thread;
    }

    public void stopCreaturaThread() {
        if (creaturaThreadInstance != null) {
            creaturaThreadInstance.sconfiggiCreatura();
            creaturaThread.interrupt();
        }
    }

    public boolean isPrimaAzioneGaslyAttivata() {
        return primaAzioneGaslyAttivata;
    }

    public void setPrimaAzioneGaslyAttivata(boolean primaAzioneGaslyAttivata) {
        this.primaAzioneGaslyAttivata = primaAzioneGaslyAttivata;
    }

    public boolean isSecondaAzioneGaslyAttivata() {
        return secondaAzioneGaslyAttivata;
    }

    public void setSecondaAzioneGaslyAttivata(boolean secondaAzioneGaslyAttivata) {
        this.secondaAzioneGaslyAttivata = secondaAzioneGaslyAttivata;
    }

    public boolean isTerzaAzioneGaslyAttivata() {
        return terzaAzioneGaslyAttivata;
    }

    public void setTerzaAzioneGaslyAttivata(boolean terzaAzioneGaslyAttivata) {
        this.terzaAzioneGaslyAttivata = terzaAzioneGaslyAttivata;
    }

    public boolean isTorciaAccesa() {
        return torciaAccesa;
    }

    public void setTorciaAccesa(boolean torciaAccesa) {
        this.torciaAccesa = torciaAccesa;
    }

    public boolean isTerrenoScavato() {
        return terrenoScavatoInSerra;
    }

    public void setTerrenoScavato(boolean scavato) {
        this.terrenoScavatoInSerra = scavato;
    }



    /**
     * Metodo astratto per l'inizializzazione del gioco.
     *
     * @throws Exception Se si verifica un errore durante l'inizializzazione
     */
    public abstract void inizializza() throws Exception;



    /**
     * Metodo astratto per gestire la prossima mossa del giocatore.
     *
     * @param p L'output del parser con il comando inserito
     * @param out Lo stream di output per le risposte del gioco
     */
    public abstract void prossimaMossa(ParserOutput p, PrintStream out);




}
