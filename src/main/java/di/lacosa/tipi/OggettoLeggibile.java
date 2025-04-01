package di.lacosa.tipi;

import java.util.Set;

/**
 * Classe che rappresenta un oggetto leggibile nel gioco, estendendo OggettoRaccoglibile.
 * Un oggetto leggibile che può essere preso per essere inserito nell'inventario, che
 * contiene un testo consultabile dal lettore attraverso il comando
 * (Leggi x)
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 7L Numero di versione per la serializzazione
 * @see OggettoRaccoglibile
 * @see OggettoLeggibileNonRaccoglibile
 */
public class OggettoLeggibile extends OggettoRaccoglibile{

    private static final long serialVersionUID = 7L;

    String testo;

    public OggettoLeggibile(int id, String nome, String descrizione, String testo, Set <String> alias, int id_stanza) {
        super(id, nome, descrizione, alias, id_stanza);
        this.testo = testo;
    }

    public String getTesto() {
        return testo;
    }


}
