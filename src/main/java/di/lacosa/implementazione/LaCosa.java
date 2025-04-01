package di.lacosa.implementazione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.PrintStream;

import di.lacosa.DescrizioneGioco;
import di.lacosa.FaseFinaleListener;
import di.lacosa.Observer;
import di.lacosa.contesti.*;
import di.lacosa.database.*;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;
import di.lacosa.Observable;
import di.lacosa.ui.InterfacciaGioco;


/**
 * Classe principale che rappresenta una partita di gioco.
 * <p>
 * Gestisce tutta la logica del gioco, inclusi:
 * <ul>
 *   <li>Inizializzazione degli elementi di gioco (stanze, oggetti, personaggi)</li>
 *   <li>Elaborazione dei comandi del giocatore</li>
 *   <li>Gestione degli osservatori per le diverse azioni di gioco</li>
 *   <li>Comunicazione con l'interfaccia grafica</li>
 * </ul>
 * Implementa {@link Serializable} per permettere il salvataggio dello stato del gioco.
 */
public class LaCosa extends DescrizioneGioco implements Observable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Interfaccia grafica del gioco (transiente per la serializzazione) */
    private transient InterfacciaGioco interfacciaGioco;

    /** Lista degli osservatori per i vari contesti di gioco */
    private transient List<Observer> contestiGioco = new ArrayList<>();

    /** Flag per evitare doppia registrazione degli osservatori */
    private transient boolean observersImpostati = false;

    /** Slot di salvataggio corrente (1-3) */
    private int slotCorrente = -1;

    /** Buffer per l'output dei comandi */
    private final List<String> outputComando = new ArrayList<>();

    /** Risultato dell'ultimo parsing di comando */
    private transient ParserOutput parserOutput;


    /**
     * Inizializza una nuova partita.
     * <p>
     * Carica tutti gli elementi del gioco:
     * <ul>
     *   <li>Comandi disponibili con relativi alias</li>
     *   <li>Stanze del gioco</li>
     *   <li>Oggetti divisi tra inventario e stanze</li>
     *   <li>Personaggi e dialoghi</li>
     * </ul>
     * Imposta la stanza iniziale del gioco.
     */
    @Override
    public void inizializza (){
        outputComando.clear();

        // Inizializzazione comandi
        Comando inventario = new Comando(TipoComando.INVENTARIO, "inventario");
        inventario.setAlias(new String[]{"Inventario"});
        getComandi().add(inventario);
        Comando parla = new Comando(TipoComando.PARLA, "parla");
        parla.setAlias(new String[]{"Parla", "comunica", "dialoga", "discuti"});
        getComandi().add(parla);
        Comando prendi = new Comando(TipoComando.PRENDI, "prendi");
        prendi.setAlias(new String[]{"raccogli", "Prendi"});
        getComandi().add(prendi);
        Comando vai = new Comando(TipoComando.VAI, "vai");
        vai.setAlias(new String[]{"Vai", "prosegui", "procedi", "dirigiti", "corri", "cammina", "muoviti"});
        getComandi().add(vai);
        Comando fine = new Comando(TipoComando.FINE, "fine");
        fine.setAlias(new String[]{"Fine", "termina", "exit", "Exit", "esci", "Esci"});
        getComandi().add(fine);
        Comando osserva = new Comando(TipoComando.OSSERVA, "osserva");
        osserva.setAlias(new String[]{"Osserva", "guarda", "Guarda"});
        getComandi().add(osserva);
        Comando usa = new Comando(TipoComando.USA, "usa");
        usa.setAlias(new String[]{"Usa", "utilizza"});
        getComandi().add(usa);
        Comando attiva = new Comando(TipoComando.ATTIVA, "attiva");
        attiva.setAlias(new String[]{"Attiva", "aziona", "innesca", "premi", "accendi"});
        getComandi().add(attiva);
        Comando disattiva = new Comando(TipoComando.DISATTIVA, "disattiva");
        disattiva.setAlias(new String[]{"Disattiva", "spegni", "Spegni"});
        getComandi().add(disattiva);
        Comando aiuto = new Comando(TipoComando.AIUTO, "aiuto");
        aiuto.setAlias(new String[]{"Aiuto", "help", "Istruzioni", "istruzioni", "Help"});
        getComandi().add(aiuto);
        Comando leggi = new Comando(TipoComando.LEGGI, "leggi");
        leggi.setAlias(new String[]{"Leggi", "consulta", "Consulta"});
        getComandi().add(leggi);
        Comando esamina = new Comando(TipoComando.ESAMINA, "esamina");
        esamina.setAlias(new String[]{"Esamina", "analizza", "Analizza"});
        getComandi().add(esamina);
        Comando spara = new Comando(TipoComando.SPARA, "spara");
        spara.setAlias(new String[]{"Spara"});
        getComandi().add(spara);
        Comando salva = new Comando(TipoComando.SALVA, "salva");
        salva.setAlias(new String[]{"Salva", "Salvataggio"});
        getComandi().add(salva);


        // Caricamento stanze
        for (Stanza stanza : TabellaStanze.getOgniStanza()) {
            getStanze().add(stanza);
        }

        // Caricamento oggetti
        for (Oggetto oggetto : TabellaOggetti.getOgniOggetto()){

            if(oggetto.getId_stanza() != 0){
                getOggetti().add(oggetto);
            } else {
                getInventario().add(oggetto);
            }
        }

        // Caricamento personaggi
        for(Personaggio interlocutore: TabellaPersonaggi.getOgniPersonaggio()){
            getPersonaggi().add(interlocutore);
        }

        // Caricamento dialoghi
        for(DialogoGiocatore dialogo: TabellaInterazioniGiocatore.getTutteInterazioniGiocatore()){
            getDialoghi().add(dialogo);
        }

        // Caricamento risposte personaggi
        for(RispostaPersonaggio risposta : TabellaInterazioniPersonaggi.getTutteInterazioniPersonaggi()){
            getRisposte().add(risposta);
        }

        // Imposta stanza iniziale
        setStanzaCorrente(getStanzaPerId(1));
    }

    /**
     * Elabora la prossima mossa del giocatore.
     *
     * @param p Il risultato del parsing del comando
     * @param ps Stream di output per la console (non usato nell'implementazione corrente)
     */
     @Override
     public void prossimaMossa(ParserOutput p, PrintStream ps) {
         parserOutput = p;
         outputComando.clear();

         if (p.getComando() == null) {
             interfacciaGioco.scriviInAreaDiTesto("Non ho capito cosa intendi fare. Riprova inserendo un altro comando.");
             return;
         }
         Stanza stanzaCorr = getStanzaCorrente();
         notificaOsservatori();
         boolean mossa = !stanzaCorr.equals(getStanzaCorrente()) && getStanzaCorrente() != null;

         if (!outputComando.isEmpty()) {
             for (String m : outputComando) {
                 if (m.length() > 0) {
                     interfacciaGioco.scriviInAreaDiTesto("\n" + m + "\n");
                 }
             }
         }
         if (mossa) {
             interfacciaGioco.mostraStanzaPulita(getStanzaCorrente().getNome(), getStanzaCorrente().getDescrizione());
         }
         interfacciaGioco.scriviInAreaDiTesto("\n?> \n");
     }

    /**
     * Notifica tutti gli osservatori registrati.
     * <p>
     * Ogni osservatore processa il comando e può aggiungere messaggi all'output.
     */
    @Override
    public void notificaOsservatori() {
        for (Observer a : contestiGioco) {
            outputComando.add(a.update(this, parserOutput));
        }
    }

    /**
     * Registra tutti gli osservatori necessari per il gioco.
     *
     * @param finaleListener Listener per gestire la fase finale del gioco
     */
    public void setObservers(FaseFinaleListener finaleListener) {

        if (observersImpostati){
            return;
        }
        this.attach(new AggiornaDialogo(interfacciaGioco, finaleListener));
        this.attach(new AggiornaInventario());
        this.attach(new AggiornaEsamina(interfacciaGioco));
        this.attach(new AggiornaPosizione(interfacciaGioco));
        this.attach(new AggiornaOsserva());
        this.attach(new AggiornaRaccogli());
        this.attach(new AggiornaLettura(interfacciaGioco));
        this.attach(new AggiornaSparo());
        this.attach(new AggiornaAttiva());
        this.attach(new AggiornaAiuto(interfacciaGioco));
        this.attach(new AggiornaDisattiva());
        this.attach(new AggiornaUsa());
        this.attach(new AggiornaSalva());
        observersImpostati = true;
    }

    /**
     * Aggiunge un osservatore alla lista.
     *
     * @param a L'osservatore da aggiungere
     */
    @Override
    public void attach(Observer a){

        if(!contestiGioco.contains(a)){
            contestiGioco.add(a);
        }
    }

    /**
     * Rimuove un osservatore dalla lista.
     *
     * @param a L'osservatore da rimuovere
     */
    @Override
    public void detach(Observer a){
        contestiGioco.remove(a);
    }


    /**
     * Ripristina le variabili transient (ovvero quelle che non vengono serializzate)
     * dopo il caricamento.
     */
    public void ripristinaTransient() {
        this.contestiGioco = new ArrayList<>();
        this.observersImpostati = false;
    }

    /**
     * Imposta l'interfaccia grafica del gioco.
     *
     * @param interfaccia L'interfaccia grafica
     */
    public void setInterfacciaGioco(InterfacciaGioco interfaccia) {
        this.interfacciaGioco = interfaccia;
    }

    /**
     * Imposta lo slot di salvataggio corrente.
     *
     * @param slot Numero dello slot (1-3)
     */
    public void setSlotCorrente(int slot) {
        this.slotCorrente = slot;
    }

    /**
     * Restituisce lo slot di salvataggio corrente.
     *
     * @return Numero dello slot (1-3)
     */
    public int getSlotCorrente() {
        return slotCorrente;
    }


}
