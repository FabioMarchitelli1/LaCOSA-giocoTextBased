package di.lacosa.tipi;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Classe che rappresenta l'inventario del del giocatore ovvero l'insieme di oggetti
 * a sua disposizione in qualunque stanza lui si trovi.
 *
 * @author fabioMarchitelli
 * @serial 3L Numero di versione per la serializzazione
 */
public class Inventario implements Serializable {

    private static final long serialVersionUID = 4L;

    private List<Oggetto> contenuto = new ArrayList<>();

    public List<Oggetto> getContenuto() {
        return contenuto;
    }

    public void setContenuto(List<Oggetto> contenuto) {
        this.contenuto = contenuto;
    }

    public void aggiungi(Oggetto o) {
        contenuto.add(o);
    }

    public void rimuovi(Oggetto o) {
        contenuto.remove(o);
    }

}
