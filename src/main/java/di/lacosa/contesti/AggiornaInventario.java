package di.lacosa.contesti;

import di.lacosa.parser.ParserOutput;
import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.tipi.Arma;
import di.lacosa.tipi.Oggetto;
import di.lacosa.tipi.TipoComando;

/**
 * Classe che gestisce il comando "INVENTARIO" per visualizzare gli oggetti posseduti dal giocatore.
 * Implementa l'interfaccia Observer per reagire al comando di visualizzazione dell'inventario.
 * Mostra la lista degli oggetti nell'inventario, con informazioni aggiuntive per le armi.
 */
public class AggiornaInventario implements Observer {

    /**
     * Metodo principale che gestisce il comando INVENTARIO.
     *
     * @param description il contesto di gioco corrente con lo stato dell'inventario
     * @param parserOutput l'output del parser contenente il comando
     * @return la descrizione formattata dell'inventario o un messaggio di errore
     * @throws IllegalArgumentException se parserOutput è null
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput == null) {
            throw new IllegalArgumentException("Il parserOutput non può essere null.");
        }

        if (parserOutput.getComando().getTipo() == TipoComando.INVENTARIO) {
            if (!verificaCondizioni(parserOutput)) {
                return "Comando non valido. 'Inventario' è un comando semplice e non accetta parametri.";
            }
            if (description.getInventario().isEmpty()) {
                msg.append("Il tuo inventario è vuoto.");
            } else {
                msg.append("Nel tuo inventario ci sono:\n\n");
                for (Oggetto o : description.getInventario()) {
                    if (o instanceof Arma) {
                        Arma arma = (Arma) o;
                        msg.append(arma.getNome())
                                .append("")
                                .append("\n----------------------------------\n");
                    } else {
                        msg.append(o.getNome())
                                .append("")
                                .append("\n----------------------------------\n");
                    }
                }
            }
        }
        return msg.toString();
    }



    /**
     * Verifica che il comando INVENTARIO sia usato correttamente senza parametri aggiuntivi.
     *
     * @param parserOutput l'output del parser da verificare
     * @return true se il comando è valido (senza parametri extra), false altrimenti
     */
    private boolean verificaCondizioni(ParserOutput parserOutput) {
        return parserOutput.getDirezione() == null
                && parserOutput.getOggetto() == null
                && parserOutput.getOggettoInv() == null
                && parserOutput.getInterlocutore() == null;
    }
}
