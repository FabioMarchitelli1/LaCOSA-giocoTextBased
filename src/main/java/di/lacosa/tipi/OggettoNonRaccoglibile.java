package di.lacosa.tipi;

import java.util.Set;

/**
 * Classe che rappresenta un oggetto non raccoglibile nel gioco, estendendo Oggetto.
 * Un oggetto non raccoglibile rappresenta un oggetto che NON puoò essere preso per essere
 * inserito all'interno dell'inventario del giocatore. La sua posizione rimmarrà quindi fissa
 * nella stanza in cui viene collocato.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 9L Numero di versione per la serializzazione
 * @see Oggetto
 * @see OggettoRaccoglibile
 */
public class OggettoNonRaccoglibile extends Oggetto{

    private static final long serialVersionUID = 9L;

    final boolean raccoglibile = false;

    public OggettoNonRaccoglibile(int id, String nome, String descrizione, Set <String> alias, int id_stanza) {
        super(id, nome, descrizione, alias, id_stanza);
    }

    @Override
    public void interagisci() {
        System.out.println("Non puoi mettere " + getNome() + " nell'inventario.");
    }

}
