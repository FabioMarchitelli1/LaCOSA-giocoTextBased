package di.lacosa.tipi;

import java.util.Set;

/**
 * Classe che rappresenta un oggetto raccoglibile nel gioco, estendendo Oggetto.
 * Un oggetto raccoglibile rappresenta un oggetto che  può essere preso per essere
 * inserito all'interno dell'inventario del giocatore, cosi che possa usufruirne sempre.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author Autore sconosciuto
 * @version 1.0
 * @serial 6L Numero di versione per la serializzazione
 * @see Oggetto
 * @see OggettoNonRaccoglibile
 */
public class OggettoRaccoglibile extends Oggetto {

    private static final long serialVersionUID = 10L;

    final boolean raccoglibile = true;

    public OggettoRaccoglibile(int id, String nome, String descrizione, Set <String> alias, int id_stanza) {
        super(id, nome, descrizione, alias, id_stanza);
    }

    @Override
    public void interagisci() {
        System.out.println("Puoi mettere " + getNome() + " nell'inventario.");
    }
}

