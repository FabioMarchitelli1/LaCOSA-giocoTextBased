package di.lacosa.tipi;

import di.lacosa.DescrizioneGioco;

import java.io.Serializable;
import java.util.Set;

/**
 * Classe che rappresenta un personaggio nel gioco.
 * Gestisce le proprietà e il comportamento base dei personaggi,
 * inclusa la posizione, gli alias, lo stato di interazione e la vita.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 11L Numero di versione per la serializzazione
 */
public class Personaggio implements Serializable {

    private static final long serialVersionUID = 11L;
    private int id;
    private String nome;
    private int id_stanza;    //ID della stanza in cui si trova il personaggio
    private Set<String> aliases;    //Insieme di alias/nomi alternativi per il personaggio
    private boolean interazioneAvvenuta;   //Flag che indica se è avvenuta un'interazione con il personaggio
    private int vita;    //Punti vita del personaggio (default: 4)

    /**
     * Costruttore completo per creare un nuovo personaggio.
     *
     * @param id        Identificatore univoco
     * @param nome      Nome del personaggio (non nullo o vuoto)
     * @param id_stanza ID della stanza iniziale
     * @param aliases   Set di nomi alternativi (può essere null)
     * @throws IllegalArgumentException Se nome è nullo o vuoto
     */
    public Personaggio(int id, String nome, int id_stanza, Set<String> aliases) {
        this.id = id;
        this.nome = nome;
        this.id_stanza = id_stanza;
        this.aliases = aliases;
        this.interazioneAvvenuta = false;
        this.vita = 4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId_stanza() {
        return id_stanza;
    }

    public int getVita() {
        return vita;
    }

    public void setId_stanza(int id_stanza) {
        this.id_stanza = id_stanza;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setVita(int vita) {
        this.vita = vita;
    }


    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public boolean isInterazioneAvvenuta() {
        return interazioneAvvenuta;
    }

    public void setInterazioneAvvenuta() {
        this.interazioneAvvenuta = true;
    }


    /**
     * Applica un aggiornamento di evento basato sul personaggio specifico.
     * Permette a personaggi diversi di attivare diversi aggiornamenti di gioco.
     *
     * @param descrizione Lo stato corrente del gioco da aggiornare
     */
    public void aggiornaEvento(DescrizioneGioco descrizione) {

        if (interazioneAvvenuta) {
            if (this.nome.equalsIgnoreCase("Gasly")) {
                descrizione.setSecondaAzioneGaslyAttivata(true);
            }
        }
    }
}
