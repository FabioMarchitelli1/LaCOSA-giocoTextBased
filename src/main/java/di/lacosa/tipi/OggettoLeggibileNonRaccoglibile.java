package di.lacosa.tipi;

import java.util.Set;

/**
 * Classe che rappresenta una variante dell'OggettoLeggibile, estendendo OggettoNonRaccoglibile.
 * Un oggetto leggibile non raccoglibile può essere letto dal giocatore ma non preso per essere
 * inserito nel proprio inventario.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 8L Numero di versione per la serializzazione
 * @see OggettoNonRaccoglibile
 * @see OggettoLeggibile
 */
public class OggettoLeggibileNonRaccoglibile extends OggettoNonRaccoglibile {

    private static final long serialVersionUID = 8L;

    String testo;

    public OggettoLeggibileNonRaccoglibile(int id, String nome, String descrizione, String testo, Set <String> alias, int id_stanza) {
        super(id, nome, descrizione, alias, id_stanza);
        this.testo = testo;
    }

    public String getTesto() {
        return testo;
    }
}
