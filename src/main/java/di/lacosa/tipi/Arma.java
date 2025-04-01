package di.lacosa.tipi;

import java.util.Set;

/**
 * Classe che rappresenta un'arma presente nel gioco, estendendo OggettoRaccoglibile.
 * Aggiunge la gestione delle munizioni all'oggetto base.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 1L Numero di versione per la serializzazione
 * @see OggettoRaccoglibile
 */
public class Arma extends OggettoRaccoglibile{

    private static final long serialVersionUID = 1L;

    int munizioni;        //Quantità corrente del numero di munizioni

    /**
     * Costruttore completo per creare un'istanza di Arma.
     *
     * @param id Identificatore univoco dell'arma
     * @param nome Nome dell'arma
     * @param descrizione Descrizione testuale dell'arma
     * @param munizioni Quantità iniziale di munizioni
     * @param alias Set di alias per l'arma
     * @param id_stanza ID della stanza in cui l'arma si trova inizialmente
     */
    public Arma(int id, String nome, String descrizione, int munizioni, Set<String> alias, int id_stanza) {
        super(id, nome, descrizione, alias, id_stanza);
        this.munizioni =  munizioni;
    }

    /**
     * Restituisce il numero corrente di munizioni disponibili.
     *
     * @return Quantità di munizioni rimanenti
     */
    public int getMunizioni() {
        return munizioni;
   }


    /**
     * Imposta il numero di munizioni disponibili.
     *
     * @param munizioni Nuova quantità di munizioni
     * @throws IllegalArgumentException Se munizioni è negativo
     */
   public void setMunizioni(int munizioni) {
        this.munizioni = munizioni;
   }

}
