package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;

import java.util.Set;

/**
 * Classe che gestisce i comandi di disattivazione degli oggetti nel gioco.
 * Implementa l'interfaccia Observer per reagire ai comandi di disattivazione.
 * Gestisce in particolare la disattivazione della torcia e i suoi effetti sul gioco.
 */
public class AggiornaDisattiva implements Observer {

    /**
     * Metodo principale che gestisce i comandi di disattivazione.
     *
     * @param description  Il contesto di gioco corrente
     * @param parserOutput L'output del parser contenente il comando
     * @return Un messaggio che descrive il risultato dell'operazione, o stringa vuota se non applicabile
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (parserOutput.getComando().getTipo().equals(TipoComando.DISATTIVA)) {
            Stanza stanzaCorrente = description.getStanzaCorrente();
            Set<Oggetto> inventarioPersonaggio = description.getInventario();
            Set<Oggetto> oggettiInStanza = description.getOggettiPerStanza(stanzaCorrente.getIdStanza());
            if (controlloDisattivazioneOggetto(parserOutput)) {
                int idOggetto = controlloTipoOggetto(parserOutput);
                if (idOggetto != -1) {
                    if (confermaTorcia(parserOutput, inventarioPersonaggio)) {
                        return gestisciTorcia(parserOutput, stanzaCorrente, description);
                    }
                }
                return "Oggetto non disattivabile";
            }
            return "Oggetto da disattivare non identificato.";
        }
        return "";
    }

    /**
     * Verifica se l'oggetto specificato è un OggettoAttivabile.
     *
     * @param parserOutput L'output del parser contenente l'oggetto
     * @return 1 se l'oggetto è attivabile, -1 altrimenti
     */
    private int controlloTipoOggetto(ParserOutput parserOutput) {
        if (parserOutput.getOggetto() == null) {
            if (parserOutput.getOggettoInv() instanceof OggettoAttivabile) {
                return 1;
            }
        } else {
            if (parserOutput.getOggetto() instanceof OggettoAttivabile) {
                return 1;
            }
        }
        return -1;
    }

    /**
     * Verifica se l'oggetto da disattivare è la torcia e se è nell'inventario.
     *
     * @param parserOutput          L'output del parser contenente l'oggetto
     * @param inventarioPersonaggio L'inventario del giocatore
     * @return true se l'oggetto è la torcia e si trova nell'inventario, false altrimenti
     */
    private boolean confermaTorcia(ParserOutput parserOutput, Set<Oggetto> inventarioPersonaggio) {

        if (parserOutput.getOggettoInv() != null) {
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
     * Verifica se il comando contiene un oggetto disattivabile.
     *
     * @param parserOutput L'output del parser da controllare
     * @return true se contiene un oggetto disattivabile, false altrimenti
     */
    private boolean controlloDisattivazioneOggetto(ParserOutput parserOutput) {
        if (parserOutput.getInterlocutore() == null && parserOutput.getDirezione() == null) {
            if (parserOutput.getOggettoInv() != null || parserOutput.getOggetto() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gestisce la disattivazione della torcia e i suoi effetti sul gioco.
     *
     * @param parserOutput   L'output del parser contenente il comando
     * @param stanzaCorrente La stanza corrente del giocatore
     * @param description    Il contesto di gioco
     * @return Un messaggio che descrive il risultato dell'operazione
     */
    private String gestisciTorcia(ParserOutput parserOutput, Stanza stanzaCorrente, DescrizioneGioco description) {
        if (description.isTorciaAccesa()) {
            if (stanzaCorrente.getTipoOsservazioneAggiornata().equals(MotivazioneAggDescrizione.ACCENSIONE_TORCIA)) {
                if (description.isCreaturaCanideAttivata() && !description.isCreaturaCanideSconfitta()) {
                    return "{Non posso spegere la torcia adesso! Questa creatura vuole uccidermi!!!}";
                } else {
                    description.setTorciaAccesa(false);
                    return "Torcia disattivata. {Adesso non vedo più un accidenti}.";
                }
            }
            description.setTorciaAccesa(false);
            return ("Torcia disattivata.");
        }
        return "La torcia non è accesa.";
    }

}




