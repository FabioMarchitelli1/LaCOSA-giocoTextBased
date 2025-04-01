package di.lacosa;
import di.lacosa.parser.ParserOutput;

/**
 * Interfaccia che definisce il comportamento di un osservatore nel pattern Observer.
 * Gli oggetti che implementano questa interfaccia possono ricevere aggiornamenti
 * dall'oggetto osservato quando il suo stato cambia.
 *
 * @author fabioMarchitelli
 * @see Observable
 */
public interface Observer {

    /**
     * Metodo chiamato quando l'oggetto osservato notifica un cambiamento di stato.
     *
     * @param description L'istanza del gioco che rappresenta lo stato corrente (non null)
     * @param parserOutput L'output del parser contenente il comando processato (può essere null)
     * @return Una stringa che rappresenta la risposta all'aggiornamento
     */
    public String update(DescrizioneGioco description, ParserOutput parserOutput);
}
