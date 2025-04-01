package di.lacosa.parser;

import di.lacosa.tipi.Comando;
import di.lacosa.tipi.Oggetto;
import di.lacosa.tipi.Personaggio;
import di.lacosa.tipi.PuntoCardinale;

/**
 * La classe ParserOutput rappresenta il risultato dell'analisi di un comando utente.
 * Contiene tutte le informazioni estratte dal parser necessarie per l'esecuzione del comando.
 * @author fabioMarchitelli
 */
public class ParserOutput {

    private Comando comando;

    private Oggetto oggetto;

    private Oggetto oggettoInv;

    private PuntoCardinale direzione;

    private Personaggio interlocutore;

    /**
     * Costruttore per comandi che coinvolgono un oggetto.
     *
     * @param comando il comando analizzato
     * @param oggetto l'oggetto coinvolto nel comando
     */
    public ParserOutput(Comando comando, Oggetto oggetto) {
        this.comando = comando;
        this.oggetto = oggetto;
    }

    /**
     * Costruttore specifico per comandi di movimento/spostamento.
     *
     * @param comando il comando di movimento (es: "vai", "cammina", "corri")
     * @param direzione la direzione di movimento (punto cardinale)
     */
    public ParserOutput(Comando comando, PuntoCardinale direzione){
        this.comando = comando;
        this.direzione = direzione;
    }

    /**
     * Costruttore per comandi senza parametri aggiuntivi.
     *
     * @param comando il comando base (es: "osserva", "aiuto")
     */
    public ParserOutput(Comando comando){
        this.comando = comando;
        this.oggetto = null;
        this.oggettoInv = null;
    }


    public ParserOutput(Comando comando, Oggetto oggetto, Oggetto oggettoInv) {
        this.comando = comando;
        this.oggetto = oggetto;
        this.oggettoInv = oggettoInv;
    }


    /**
     * Costruttore per comandi che coinvolgono un personaggio.
     *
     * @param comando il comando analizzato
     * @param interlocutore il personaggio con cui interagire
     */
    public ParserOutput (Comando comando, Personaggio interlocutore){
        this.comando = comando;
        this.interlocutore = interlocutore;
    }

    public Comando getComando() {
        return comando;
    }

    public void setComando(Comando command) {
        this.comando = command;
    }

    public Oggetto getOggetto() {
        return oggetto;
    }

    public void setOggetto(Oggetto object) {
        this.oggetto = object;
    }

    public Oggetto getOggettoInv() {
        return oggettoInv;
    }

    public void setOggettoInv(Oggetto invObject) {
        this.oggettoInv = invObject;
    }

    public Personaggio getInterlocutore() {
        return interlocutore;
    }

    public void setDirezione(PuntoCardinale direzione) {this.direzione = direzione;}

    public PuntoCardinale getDirezione() { return direzione;}
}
