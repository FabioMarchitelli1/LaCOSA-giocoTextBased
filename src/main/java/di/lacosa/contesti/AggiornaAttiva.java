package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;

import java.util.Set;

/**
 * Classe che gestisce i comandi di attivazione degli oggetti nel gioco, come l'attivazione della torcia.
 * Implementa l'interfaccia Observer per reagire ai cambiamenti nel gioco.
 * Quando un giocatore usa un comando di attivazione (es. "Attiva torcia"), questa classe modifica
 * lo stato degli oggetti e dell'ambiente di gioco di conseguenza.
 */
public class AggiornaAttiva implements Observer {

    /**
     * Metodo chiamato quando viene rilevato un cambiamento nel gioco.
     * Gestisce i comandi di attivazione degli oggetti.
     *
     * @param description Il contesto del gioco con tutte le informazioni attuali
     * @param parserOutput L'output del parser contenente il comando da eseguire
     * @return Un messaggio che descrive il risultato dell'operazione, o stringa vuota se non applicabile
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();

        if (parserOutput.getComando().getTipo().equals(TipoComando.ATTIVA)) {

            Stanza stanzaCorrente = description.getStanzaCorrente();
            Set<Oggetto> inventarioPersonaggio = description.getInventario();
            Set<Oggetto> oggettiInStanza = description.getOggettiPerStanza(stanzaCorrente.getIdStanza());

            if (controlloAttivazioneOggetto(parserOutput)) {

                int idOggetto = controlloTipoOggetto(parserOutput);
                if (idOggetto != -1) {
                    if (confermaTorcia(parserOutput, inventarioPersonaggio)) {
                        return gestisciTorcia(parserOutput, stanzaCorrente, description);
                    }
                }
                return "Oggetto non 'attivabile'";
            }
            return "Oggetto da attivare non identificato.";
        }
        return "";
    }


    /**
     * Controlla se l'output del parser contiene un oggetto attivabile (in inventario o nella stanza).
     *
     * @param parserOutput L'output del parser da controllare
     * @return true se contiene un oggetto attivabile, false altrimenti
     */
    private boolean controlloAttivazioneOggetto(ParserOutput parserOutput){

        if(parserOutput.getInterlocutore()==null && parserOutput.getDirezione() == null) {
            if (parserOutput.getOggettoInv() != null || parserOutput.getOggetto() != null) {
                return true;
            }
        }
        return false;
    }



    /**
     * Determina l'ID dell'oggetto da attivare, verificando se è un OggettoAttivabile.
     *
     * @param parserOutput L'output del parser contenente l'oggetto
     * @return L'ID dell'oggetto se attivabile, -1 altrimenti
     */
    private int controlloTipoOggetto(ParserOutput parserOutput){

        if (parserOutput.getOggetto()==null) {
            if (parserOutput.getOggettoInv() instanceof OggettoAttivabile) {
                return parserOutput.getOggettoInv().getId();
            }
        } else {
            if (parserOutput.getOggetto() instanceof OggettoAttivabile) {
                return parserOutput.getOggetto().getId();
            }
        }
        return -1;
    }

    /**
     * Verifica se l'oggetto da attivare è una torcia presente nell'inventario.
     *
     * @param parserOutput L'output del parser contenente l'oggetto
     * @param inventarioPersonaggio L'inventario del personaggio
     * @return true se l'oggetto è una torcia nell'inventario, false altrimenti
     */
    private boolean confermaTorcia(ParserOutput parserOutput, Set <Oggetto> inventarioPersonaggio){
        if (parserOutput.getOggettoInv()!=null) {
            if (parserOutput.getOggettoInv().getNome().equalsIgnoreCase("Torcia")) {
                for (Oggetto oggetto : inventarioPersonaggio) {
                    if (oggetto.getNome().equalsIgnoreCase("Torcia")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gestisce l'attivazione della torcia, modificando lo stato della stanza e del gioco.
     *
     * @param parserOutput L'output del parser contenente il comando
     * @param stanzaCorrente La stanza corrente del giocatore
     * @param description Il contesto del gioco
     * @return Un messaggio che descrive il risultato dell'operazione
     */
     private String gestisciTorcia(ParserOutput parserOutput, Stanza stanzaCorrente,
                                   DescrizioneGioco description) {

         if (!description.isTorciaAccesa()) {
             if (stanzaCorrente.isVisibile()) {
                 description.setTorciaAccesa(true);
                 return "Accendi la TORCIA ma tutto è lo stesso. {Accendere la torcia non cambia nulla qui, la stanza è già abbastanza illuminata}";
             }
             if (stanzaCorrente.getTipoOsservazioneAggiornata().equals(MotivazioneAggDescrizione.ACCENSIONE_TORCIA)) {
                 description.setTorciaAccesa(true);
                 return "Hai attivato la TORCIA. Adesso puoi osservare chiaramente cosa c'è nella stanza.";
             }
         }
         return "La torcia è già accesa";
     }
}
