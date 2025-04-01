package di.lacosa.tipi;

import java.io.Serializable;


/**
 * Classe che rappresenta una stanza nel gioco.
 * Gestisce tutte le proprietà di una stanza inclusi:
 * - Nome e descrizione
 * - Osservazioni e loro stati
 * - Collegamenti con altre stanze
 * - Blocchi e codici di accesso
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 13L Numero di versione per la serializzazione
 */
public class Stanza implements Serializable {

    private static final long serialVersionUID = 13L;
    private final int id;   //Identificatore univoco della stanza
    private String nome;   //Nome della stanza
    private String descrizione;  //Descrizione della stanza

    /*
    Testo dato in risposta al comando "Osserva" del giocatore quando si trova
    nella suddetta stanza. Essa rappresenta una descrizione dei particolari
    della stanza.
     */
    private String testoOsservazione;

    /*
    Testo di osservazione aggioranta dovuta ad un cambiamento dello stato del gioco.
     */
    private String testoOsservazioneAgg;
    private boolean visibile;   //Flag che indica se la stanza è attualmente visibile
    private int idStanzaNord;   //ID della stanza a nord
    private int idStanzaOvest;  //ID della stanza a ovest
    private int idStanzaEst;    //ID della stanza a est
    private int idStanzaSud;    //ID della stanza a sud
    private boolean portaBloccata;  //Flag che indica se il passaggio a questa stanza è bloccata da qualcosa
    private String codicePorta;     //Codice necessario per sbloccare la porta
    private boolean osservazioneAggiornataAttiva;  //Flag che indica se mostrare l'osservazione aggiornata
    private MotivazioneAggDescrizione tipoOsservazioneAggiornata;   //Motivazione per l'aggiornamento dell'osservazione

    /**
     * Costruttore base con solo ID.
     *
     * @param id Identificatore univoco della stanza
     */
    public Stanza(int id) {
        this.id = id;
    }

    /**
     * Costruttore con informazioni base.
     *
     * @param id Identificatore univoco
     * @param nome Nome della stanza
     * @param descrizione Descrizione testuale
     */
    public Stanza(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    /**
     * Costruttore completo per la stanza.
     *
     * @param id Identificatore univoco
     * @param nome Nome della stanza
     * @param descrizione Descrizione testuale
     * @param testoOsservazione Testo osservazione standard
     * @param testoOsservazioneAgg Testo osservazione dopo eventi
     * @param visibile Se la stanza è inizialmente visibile
     * @param idStanzaNord ID stanza a nord
     * @param idStanzaSud ID stanza a sud
     * @param idStanzaEst ID stanza a est
     * @param idStanzaOvest ID stanza a ovest
     * @param portaBloccata Se la porta è inizialmente bloccata
     * @param codicePorta Codice per sbloccare la porta
     * @param motivazione Tipo di aggiornamento osservazione
     */
    public Stanza(int id, String nome, String descrizione, String testoOsservazione, String testoOsservazioneAgg, boolean visibile, int idStanzaNord, int idStanzaSud, int idStanzaEst, int idStanzaOvest, boolean portaBloccata, String codicePorta, MotivazioneAggDescrizione motivazione) {
        this.id= id;
        this.nome = nome;
        this.descrizione= descrizione;
        this.testoOsservazione = testoOsservazione;
        this.testoOsservazioneAgg = testoOsservazioneAgg;
        this.visibile = visibile;
        this.idStanzaNord = idStanzaNord;
        this.idStanzaEst = idStanzaEst;
        this.idStanzaSud = idStanzaSud;
        this.idStanzaOvest = idStanzaOvest;
        this.portaBloccata = portaBloccata;
        this.codicePorta = codicePorta;
        this.osservazioneAggiornataAttiva = false;
        this.tipoOsservazioneAggiornata = motivazione;
    }

    public int getIdStanza() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione(){
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getTestoOsservazioneAgg() {
        return testoOsservazioneAgg;
    }

    public String getTestoOsservazione() {
        return testoOsservazione;
    }

    public void setTestoOsservazione(String testoOsservazione) {
        this.testoOsservazione = testoOsservazione;
    }

    public int getIdStanzaNord() {
        return idStanzaNord;
    }

    public void setIdStanzaNord(int idStanzaNord) {
        this.idStanzaNord = idStanzaNord;
    }

    public int getIdStanzaOvest() {
        return idStanzaOvest;
    }

    public void setIdStanzaOvest(int idStanzaOvest) {
        this.idStanzaOvest = idStanzaOvest;
    }

    public int getIdStanzaEst() {return idStanzaEst; }

    public void setIdStanzaEst(int idStanzaEst) {
        this.idStanzaEst = idStanzaEst;
    }

    public int getIdStanzaSud() {
        return idStanzaSud;
    }

    public void setIdStanzaSud(int idStanzaSud) {
        this.idStanzaSud = idStanzaSud;
    }

    public boolean isPortaBloccata() {
        return portaBloccata;
    }

    public void setBloccoPorta(boolean portaBloccata) {
        this.portaBloccata = portaBloccata;
    }

    public String getCodicePorta() {
        return codicePorta;
    }

    public boolean checkCodicePorta(String codicePorta) {
        return this.codicePorta.equals(codicePorta);
    }

    public void setCodicePorta(String codicePorta) {
        this.codicePorta = codicePorta;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    public void attivaOsservazioneAggiornata() {
        this.osservazioneAggiornataAttiva = true;
    }

    public void disattivaOsservazioneAggiornata() {
        this.osservazioneAggiornataAttiva = false;
    }

    public String getOsservazioneCorrente() {
        return osservazioneAggiornataAttiva ? testoOsservazioneAgg : testoOsservazione;
    }

    public MotivazioneAggDescrizione getTipoOsservazioneAggiornata() {
        return tipoOsservazioneAggiornata;
    }

    public void setTipoOsservazioneAggiornata(MotivazioneAggDescrizione tipoOsservazioneAggiornata) {
        this.tipoOsservazioneAggiornata = tipoOsservazioneAggiornata;
    }

    public boolean isOsservazioneAggiornataAttiva() {
        return osservazioneAggiornataAttiva;
    }

    public void setOsservazioneAggiornataAttiva(boolean osservazioneAggiornataAttiva) {
        this.osservazioneAggiornataAttiva = osservazioneAggiornataAttiva;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Stanza other = (Stanza) obj;
        return this.id == other.id;
    }


}
