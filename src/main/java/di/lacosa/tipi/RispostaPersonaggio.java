package di.lacosa.tipi;

import java.io.Serializable;

/**
 * Classe che rappresenta una risposta data da un personaggio durante un dialogo nel gioco.
 * Contiene il testo della risposta e le informazioni per gestire il flusso del dialogo.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @see DialogoGiocatore
 * @author fabioMarchitelli
 * @serial 12L Numero di versione per la serializzazione
 */
public class RispostaPersonaggio implements Serializable {

    private static final long serialVersionUID = 12L;
    private int id;            //Rappresenta l'id univoco della risposta
    private int id_personaggio;   //Rappresenta l'id del personaggio che dà la risposta
    private int id_rispostaConsecutiva;       //Rappresenta l'id della risposta consecutiva a questa
    private String testo;
    private Integer prossimoNodo;     //Numero del prossimo nodo nel flusso del dialogo

    /**
     * Costruttore completo per creare una risposta di personaggio.
     *
     * @param id Identificatore univoco della risposta
     * @param id_personaggio ID del personaggio che risponde
     * @param id_rispostaConsecutiva ID della risposta successiva nel dialogo
     * @param testo Testo della risposta (non nullo o vuoto)
     * @param prossimoNodo Numero del prossimo nodo del dialogo (può essere null)
     * @throws IllegalArgumentException Se testo è nullo o vuoto
     */
    public RispostaPersonaggio(int id, int id_personaggio, int id_rispostaConsecutiva, String testo, Integer prossimoNodo) {
        this.id = id;
        this.id_personaggio = id_personaggio;
        this.id_rispostaConsecutiva = id_rispostaConsecutiva;
        this.testo = testo;
        this.prossimoNodo = prossimoNodo;
    }

    public int getId() {
        return id;
    }

    public int getId_personaggio() {
        return id_personaggio;
    }

    public int getId_rispostaConsecutiva() {
        return id_rispostaConsecutiva;
    }

    public String getTesto() {
        return testo;
    }

    public Integer getProssimoNodo() {
        return prossimoNodo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_personaggio(int id_personaggio) {
        this.id_personaggio = id_personaggio;
    }

    public void setId_rispostaConsecutiva(int id_rispostaConsecutiva) {
        this.id_rispostaConsecutiva = id_rispostaConsecutiva;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setProssimoNodo(Integer prossimoNodo) {
        this.prossimoNodo = prossimoNodo;
    }
}
