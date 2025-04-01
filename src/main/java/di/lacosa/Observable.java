package di.lacosa;

/**
 * Interfaccia che definisce il comportamento di un oggetto osservabile nel pattern Observer.
 * Un oggetto osservabile può registrare osservatori, rimuoverli e notificarli quando si verificano
 * cambiamenti nel suo stato interno.
 *
 *
 * @author fabioMarchitelli
 * @see Observer
 */
public interface Observable {

    /**
     * Registra un nuovo osservatore che verrà notificato dei cambiamenti.
     *
     * @param o L'osservatore da registrare (non null)
     * @throws IllegalArgumentException Se l'osservatore passato è null
     */
    public void attach(Observer o);

    /**
     * Rimuove un osservatore dalla lista degli osservatori registrati.
     * Se l'osservatore non è registrato, il metodo non ha effetto.
     *
     * @param o L'osservatore da rimuovere
     */
    public void detach(Observer o);

    /**
     * Notifica tutti gli osservatori registrati riguardo un cambiamento di stato.
     * L'implementazione dovrebbe chiamare il metodo {@code aggiorna()} di ogni osservatore.
     */
    public void notificaOsservatori();

}
