package di.lacosa.tipi;

import java.io.Serializable;

/**
 * Classe che rappresenta una linea di dialogo del giocatore nel sistema del flusso di dialoghi del gioco.
 * Contiene il testo del dialogo, le relazioni con le risposte e lo stato di utilizzo.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @see RispostaPersonaggio
 * @author fabioMarchitelli
 * @serial 3L Numero di versione per la serializzazione
 */
public class DialogoGiocatore implements Serializable {
    private static final long serialVersionUID = 3L;
    private int id;
    private int id_interlocutore;
    private int nodo_dialogo;
    String testo;
    private int id_risposta;
    private boolean utilizzato;


    public DialogoGiocatore(int id, int id_interlocutore, int nodo_dialogo, String testo, int id_risposta) {
        this.id= id;
        this.id_interlocutore = id_interlocutore;
        this.nodo_dialogo = nodo_dialogo;
        this.testo = testo;
        this.id_risposta = id_risposta;
        this.utilizzato = false;
    }

    public int getId() {
        return id;
    }

    public int getId_interlocutore() {
        return id_interlocutore;
    }

    public int getNodo_dialogo() {
        return nodo_dialogo;
    }

    public String getTesto() {
        return testo;
    }

    public int getId_risposta() {
        return id_risposta;
    }

    public boolean isUtilizzato() {
        return utilizzato;
    }

    public void setUtilizzato(boolean utilizzato) {
        this.utilizzato = utilizzato;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_interlocutore(int id_interlocutore) {
        this.id_interlocutore = id_interlocutore;
    }

    public void setNodo_dialogo(int nodo_dialogo) {
        this.nodo_dialogo = nodo_dialogo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setId_risposta(int id_risposta) {
        this.id_risposta = id_risposta;
    }

}
