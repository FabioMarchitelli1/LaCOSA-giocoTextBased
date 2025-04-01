package di.lacosa;

/**
 * Interfaccia listener per gestire l'evento di avvio della fase finale del gioco chiamato dall'observer del dialogo.
 *
 * @author fabioMarchitelli
 */
public interface FaseFinaleListener {

    /**
     * Metodo chiamato quando deve essere avviata la fase finale del gioco.
     * Le classi che implementano questa interfaccia devono definire il comportamento
     * specifico da eseguire quando la fase finale viene attivata.
     */
    void avviaFaseFinale();
}
