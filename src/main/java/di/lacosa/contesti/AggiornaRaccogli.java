package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;

import java.util.Set;

/**
 * Classe che gestisce il comando "PRENDI" per la raccolta di oggetti.
 * Implementa l'interfaccia Observer per reagire ai comandi di raccolta.
 * Gestisce la logica di raccolta degli oggetti e gli eventi speciali correlati.
 */
public class AggiornaRaccogli implements Observer {

    /**
     * Metodo principale che gestisce il comando PRENDI.
     *
     * @param description  il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return messaggio descrittivo dell'esito dell'operazione
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        Stanza stanzaCorrente = description.getStanzaCorrente();
        int id_stanza_corrente = stanzaCorrente.getIdStanza();
        Set<Oggetto> oggettiInStanza = description.getOggettiPerStanza(id_stanza_corrente);
        if (parserOutput.getComando().getTipo() == TipoComando.PRENDI) {
            if (parserOutput.getOggetto() != null) {
                Oggetto oggettoParserOutput = parserOutput.getOggetto();
                int id_oggettoParserOutput = oggettoParserOutput.getId();
                if (oggettiInStanza.contains(oggettoParserOutput)) {
                    if (parserOutput.getOggetto() instanceof OggettoRaccoglibile) {
                        if (controlloContestoPala(parserOutput, description)) {
                            if (controlloContestoCane(description)) {
                                return "Al momento non puoi raccogliere questo oggetto";
                            }
                        }
                        if (stanzaCorrente.getNome().equalsIgnoreCase("Serra")) {
                            if (controlloContestoTerrario(parserOutput, description)) {
                                if (!description.isTerrenoScavato()) {
                                    return "Al momento non puoi raccogliere questo oggetto";
                                }
                            }
                        }
                        if (controlloContestoFoto(parserOutput, description)) {
                            gestisciOsservazioneDormitorio(description);
                        }
                        if (controlloContestoAnalgesico(parserOutput, description)) {
                            gestisciOsservazioneInfermeria(description);
                        }
                        description.getInventario().add(parserOutput.getOggetto());
                        description.setOggettoInInventario(id_oggettoParserOutput);
                        msg.append("Hai raccolto: " + parserOutput.getOggetto().getNome());
                        msg.append("\n");
                    } else {
                        msg.append("Non puoi raccogliere questo oggetto.");
                    }
                } else {
                    msg.append("In questa stanza non c'è questo oggetto.");
                }
            } else {
                msg.append("Oggetto da raccogliere non identificato.");
            }
        }
        return msg.toString();
    }

    /**
     * Verifica se il contesto è quello della pala nel canile.
     *
     * @param parserOutput l'output del parser
     * @param descrizione  il contesto di gioco
     * @return true se si sta tentando di raccogliere la pala nel canile
     */
    private boolean controlloContestoPala(ParserOutput parserOutput, DescrizioneGioco descrizione) {
        if (parserOutput.getOggetto().getNome().equalsIgnoreCase("Pala") && descrizione.getStanzaCorrente().getNome().equalsIgnoreCase("Canile")) {
            return true;
        }
        return false;
    }

    /**
     * Verifica se il contesto è quello del lanciafiamme nella serra.
     *
     * @param parserOutput l'output del parser
     * @param descrizione  il contesto di gioco
     * @return true se si sta tentando di raccogliere il lanciafiamme nella serra
     */
    private boolean controlloContestoTerrario(ParserOutput parserOutput, DescrizioneGioco descrizione) {
        if (parserOutput.getOggetto().getNome().equalsIgnoreCase("Lanciafiamme")) {
            return true;
        }
        return false;
    }

    /**
     * Verifica lo stato della creatura canide.
     *
     * @param description il contesto di gioco
     * @return true se la creatura non è stata attivata o non è stata sconfitta
     */
    private boolean controlloContestoCane(DescrizioneGioco description) {
        if (!description.isCreaturaCanideAttivata() || !description.isCreaturaCanideSconfitta()) {
            return true;
        }
        return false;
    }

    /**
     * Verifica se il contesto è quello della fotografia nel dormitorio.
     *
     * @param parserOutput l'output del parser
     * @param description  il contesto di gioco
     * @return true se si sta tentando di raccogliere la fotografia nel dormitorio
     */
    private boolean controlloContestoFoto(ParserOutput parserOutput, DescrizioneGioco description) {
        if (parserOutput.getOggetto().getNome().equalsIgnoreCase("Fotografia") && description.getStanzaCorrente().getNome().equalsIgnoreCase("Dormitorio")) {
            return true;
        }
        return false;
    }

    /**
     * Verifica se il contesto è quello dell'analgesico nell'infermeria.
     *
     * @param parserOutput l'output del parser
     * @param description  il contesto di gioco
     * @return true se si sta tentando di raccogliere l'analgesico nell'infermeria
     */
    private boolean controlloContestoAnalgesico(ParserOutput parserOutput, DescrizioneGioco description) {
        if (parserOutput.getOggetto().getNome().equalsIgnoreCase("Analgesico") && description.getStanzaCorrente().getNome().equalsIgnoreCase("Infermeria")) {
            return true;
        }
        return false;
    }

    /**
     * Gestisce l'aggiornamento dell'osservazione nel dormitorio.
     *
     * @param descrizioneGioco il contesto di gioco
     */
    private void gestisciOsservazioneDormitorio(DescrizioneGioco descrizioneGioco) {
        if (descrizioneGioco.getStanzaCorrente().getTipoOsservazioneAggiornata().equals(MotivazioneAggDescrizione.MODIFICATO_DA_EVENTO)) {
            descrizioneGioco.getStanzaCorrente().attivaOsservazioneAggiornata();
        }
    }

    /**
     * Gestisce l'aggiornamento dell'osservazione nell'infermeria.
     *
     * @param descrizioneGioco il contesto di gioco
     */
    private void gestisciOsservazioneInfermeria(DescrizioneGioco descrizioneGioco) {
        if (descrizioneGioco.getStanzaCorrente().getTipoOsservazioneAggiornata().equals(MotivazioneAggDescrizione.MODIFICATO_DA_EVENTO)) {
            descrizioneGioco.getStanzaCorrente().attivaOsservazioneAggiornata();
        }
    }
}