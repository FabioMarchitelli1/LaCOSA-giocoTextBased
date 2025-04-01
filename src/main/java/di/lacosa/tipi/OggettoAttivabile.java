package di.lacosa.tipi;

import java.util.Set;

/**
 * Classe che rappresenta un oggetto attivabile nel gioco, estendendo OggettoRaccoglibile.
 * Un oggetto attivabile può essere attivato/disattivato e mantiene il suo stato.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 6L Numero di versione per la serializzazione
 * @see OggettoRaccoglibile
 */
public class OggettoAttivabile extends OggettoRaccoglibile {

    private static final long serialVersionUID = 6L;

    boolean attivo = false;

    public OggettoAttivabile(int id, String nome, String descrizione, Set <String> alias, int id_stanza) {
        super(id, nome, descrizione, alias, id_stanza);
        this.attivo = false;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }
}
