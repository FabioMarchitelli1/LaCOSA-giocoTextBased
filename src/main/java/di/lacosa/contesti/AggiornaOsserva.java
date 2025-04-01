package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.Stanza;
import di.lacosa.tipi.TipoComando;

/**
 * Classe che gestisce il comando "OSSERVA" per l'analisi dell'ambiente di gioco.
 * Implementa l'interfaccia Observer per reagire ai comandi di osservazione.
 * Fornisce la descrizione della stanza corrente, tenendo conto delle condizioni di visibilità.
 */
public class AggiornaOsserva implements Observer {

    /**
     * Metodo principale che gestisce il comando OSSERVA.
     *
     * @param description il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return la descrizione della stanza corrente o un messaggio di errore
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getComando().getTipo() == TipoComando.OSSERVA) {
            if (!verificaCondizioniOsserva(parserOutput)) {
                return "Comando non valido. 'Osserva' è un comando semplice e non accetta parametri.";
            }
            Stanza stanzaCorrente = description.getStanzaCorrente();
            if (!stanzaCorrente.isVisibile() && !description.isTorciaAccesa()) {
                msg.append("\uD83D\uDC41\uFE0F: " + "Non si vede niente.");
            } else if (stanzaCorrente.getTestoOsservazione() != null) {
                msg.append("\uD83D\uDC41\uFE0F: " + stanzaCorrente.getOsservazioneCorrente());
            } else {
                msg.append("\uD83D\uDC41\uFE0F: " + "Non c'è niente di interessante qui.");
            }
        }
        return msg.toString();
    }

    /**
     * Verifica che il comando OSSERVA sia usato correttamente senza parametri aggiuntivi.
     *
     * @param parserOutput l'output del parser da verificare
     * @return true se il comando è valido (senza parametri extra), false altrimenti
     */
    private boolean verificaCondizioniOsserva(ParserOutput parserOutput) {
        return parserOutput.getDirezione() == null
                && parserOutput.getOggetto() == null
                && parserOutput.getOggettoInv() == null
                && parserOutput.getInterlocutore() == null;
    }

}
