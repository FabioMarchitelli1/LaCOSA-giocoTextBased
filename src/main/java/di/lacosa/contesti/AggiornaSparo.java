package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.Personaggio;
import di.lacosa.tipi.Stanza;
import di.lacosa.tipi.TipoComando;
import java.util.Set;

/**
 * Classe che gestisce il comando "SPARA" per le azioni di combattimento.
 * Implementa l'interfaccia Observer per reagire ai comandi di sparo.
 * Gestisce la logica di combattimento contro i personaggi nemici, in particolare la creatura Canide.
 */
public class AggiornaSparo implements Observer {

    /**
     * Metodo principale che gestisce il comando SPARA.
     *
     * @param description il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return messaggio descrittivo dell'esito dello sparo o stringa vuota se il comando non è SPARA
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {

        if (parserOutput.getComando() == null ||
                !parserOutput.getComando().getTipo().equals(TipoComando.SPARA)) {
            return "";
        }
        if (parserOutput.getInterlocutore() == null) {
            return "Bersaglio non identificato.";
        }
        Personaggio bersaglio = parserOutput.getInterlocutore();
        Stanza currentRoom = description.getStanzaCorrente();

        if (bersaglio.getNome().equalsIgnoreCase("Canide")) {
            if (!currentRoom.getNome().equalsIgnoreCase("Canile")) {
                return "Bersaglio non identificato";
            }
            if(description.isCreaturaCanideAttivata()){
            return gestisciSparaAlCanide(description, bersaglio);
            } else {
                return "Bersaglio non identificato";
            }
        }
        if (isPersonaggioInStanza(description, currentRoom, bersaglio)) {
            return "Non puoi sparare a " + bersaglio.getNome() + "!";
        } else {
            return "Bersaglio non trovato";
        }
    }

    /**
     * Verifica se un personaggio è presente nella stanza specificata.
     *
     * @param description il contesto di gioco
     * @param room la stanza da controllare
     * @param character il personaggio da cercare
     * @return true se il personaggio è nella stanza, false altrimenti
     */
    private boolean isPersonaggioInStanza(DescrizioneGioco description, Stanza room, Personaggio character) {
        Set<Personaggio> personaggiInStanza = description.getPersonaggiPerStanza(room.getIdStanza());
        return personaggiInStanza != null && personaggiInStanza.contains(character);
    }

    /**
     * Gestisce la logica di combattimento contro la creatura Canide.
     *
     * @param description il contesto di gioco
     * @param canide la creatura Canide
     * @return messaggio descrittivo dello stato del combattimento
     */
    private String gestisciSparaAlCanide(DescrizioneGioco description, Personaggio canide) {
        StringBuilder msg = new StringBuilder();
        int vita = canide.getVita();
        switch (vita) {
            case 4:
                msg.append("Spari! La COSA barcolla ma avanza furiosa.");
                canide.setVita(3);
                break;
            case 3:
                msg.append("Secondo colpo! La creatura zoppica ma resiste.");
                canide.setVita(2);
                break;
            case 2:
                msg.append("Terzo colpo! La COSA sanguina ma non si arrende.");
                canide.setVita(1);
                break;
            case 1:
                msg.append("Colpo finale! La creatura crolla al suolo, morta.");
                canide.setVita(0);
                description.setCreaturaCanideSconfitta(true);
                description.stopCreaturaThread();
                break;
            default:
                msg.append("La creatura è già morta.");
                break;
        }
        return msg.toString();
    }
}