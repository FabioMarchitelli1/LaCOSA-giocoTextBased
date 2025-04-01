package di.lacosa.contesti;

import di.lacosa.Observer;
import di.lacosa.GestoreSalvataggi;
import di.lacosa.DescrizioneGioco;
import di.lacosa.implementazione.LaCosa;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.Stanza;
import di.lacosa.tipi.TipoComando;

/**
 * Classe che gestisce il comando "SALVA" per il salvataggio dello stato di gioco.
 * Implementa l'interfaccia Observer per reagire ai comandi di salvataggio.
 * Gestisce la logica di salvataggio e le condizioni speciali che possono bloccarlo.
 */
public class AggiornaSalva implements Observer {

    /**
     * Metodo principale che gestisce il comando SALVA.
     *
     * @param description il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return messaggio di conferma del salvataggio o messaggio di errore
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() != TipoComando.SALVA) {
            return "";
        }
        if(!verificaCondizioniSalvataggio(parserOutput)){
            return "Comando non valido. 'salva' è un comando semplice e non accetta parametri aggiuntivi.";
        }
        // Controllo speciale per il Canile
        if (isSalvataggioBloccato(description)) {
            return "Salvataggio non consentito in questo momento";
        }
        if (!(description instanceof LaCosa)) {
                return "Impossibile salvare: tipo di gioco non compatibile.";
            }
            LaCosa gioco = (LaCosa) description;

            // Usa lo slot corrente, non uno nuovo ogni volta
            int slot = gioco.getSlotCorrente();
            if (slot == -1) {
                return "Nessuno slot assegnato a questa partita.";
            }
            System.out.println("Stanza corrente prima del salvataggio: " + gioco.getStanzaCorrente());
            boolean successo = GestoreSalvataggi.salvaPartita(gioco, slot);
            return successo ? "Partita salvata nello slot " + slot + "." : " Errore durante il salvataggio.";
    }

    /**
     * Verifica se il salvataggio è bloccato a causa di condizioni speciali di gioco.
     *
     * @param description il contesto di gioco
     * @return true se il salvataggio è bloccato, false altrimenti
     */
    private boolean isSalvataggioBloccato(DescrizioneGioco description) {
        Stanza stanzaCorrente = description.getStanzaCorrente();
        return stanzaCorrente.getNome().equalsIgnoreCase("Canile")
                && (description.isCreaturaCanideAttivata()==true && description.isCreaturaCanideSconfitta()==false);
    }


    /**
     * Verifica che il comando SALVA sia usato correttamente senza parametri aggiuntivi.
     *
     * @param parserOutput l'output del parser da verificare
     * @return true se il comando è valido (senza parametri extra), false altrimenti
     */
    private boolean verificaCondizioniSalvataggio(ParserOutput parserOutput) {
        return parserOutput.getDirezione() == null
                && parserOutput.getOggetto() == null
                && parserOutput.getOggettoInv() == null
                && parserOutput.getInterlocutore() == null;
    }

}
