package di.lacosa.contesti;

import di.lacosa.parser.ParserOutput;
import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.tipi.*;
import di.lacosa.implementazione.CreaturaThread;
import di.lacosa.ui.InterfacciaGioco;

import java.util.Set;

/**
 * Classe che gestisce il comando "ESAMINA" per l'analisi degli oggetti nell'inventario o nella stanza.
 * Implementa l'interfaccia Observer per reagire ai comandi di esaminazione.
 * Gestisce anche le interazioni speciali con le creature durante l'esaminazione.
 */
public class AggiornaEsamina implements Observer {

    private final InterfacciaGioco interfacciaGioco;

    /**
     * Costruttore della classe.
     *
     * @param interfacciaGioco riferimento all'interfaccia grafica del gioco
     */
    public AggiornaEsamina(InterfacciaGioco interfacciaGioco) {
        this.interfacciaGioco = interfacciaGioco;
    }

    /**
     * Metodo principale per gestire il comando ESAMINA.
     *
     * @param description il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return messaggio descrittivo del risultato dell'esaminazione o stringa vuota se non applicabile
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        Stanza stanzaCorrente = description.getStanzaCorrente();

        if (parserOutput.getComando().getTipo().equals(TipoComando.ESAMINA)) {
            if (controlloOggettoEsaminabile(parserOutput)) {
                    Oggetto oggettoEsaminato = getOggettoDaEsaminare(parserOutput);
                    if (oggettoEsaminato == null) {
                        return "\uD83D\uDD0E: " + "Non c'è nulla da esaminare qui.";
                    }
                    if (!stanzaCorrente.isVisibile() && !description.isTorciaAccesa()) {
                        return "Non puoi esaminare oggetti al buio.";
                    } else {
                        if (controlloContestoCreatura(parserOutput, stanzaCorrente)) {
                            return gestisciInterazioneCreatura(description, stanzaCorrente);
                        }
                        return "\uD83D\uDD0E: " + oggettoEsaminato.getDescrizione();
                    }
                } else {
                    return "Oggetto da esaminare non identificato.";
                }
        }
        return "";
    }

    /**
     * Recupera l'oggetto da esaminare dall'inventario o dalla stanza.
     *
     * @param parserOutput l'output del parser contenente l'oggetto
     * @return l'oggetto da esaminare o null se non trovato
     */
    private Oggetto getOggettoDaEsaminare(ParserOutput parserOutput) {
        return (parserOutput.getOggettoInv() != null) ? parserOutput.getOggettoInv() : parserOutput.getOggetto();
    }

    /**
     * Verifica se l'oggetto è esaminabile (non è un interlocutore o una direzione).
     *
     * @param parserOutput l'output del parser da verificare
     * @return true se l'oggetto è esaminabile, false altrimenti
     */
    private boolean controlloOggettoEsaminabile(ParserOutput parserOutput) {
        return parserOutput.getInterlocutore() == null && parserOutput.getDirezione() == null &&
                (parserOutput.getOggettoInv() != null || parserOutput.getOggetto() != null);
    }

    /**
     * Verifica se il contesto corrente prevede un'interazione con una creatura.
     *
     * @param parserOutput l'output del parser
     * @param stanzaCorrente la stanza corrente del giocatore
     * @return true se è in corso un'interazione con una creatura, false altrimenti
     */
    private boolean controlloContestoCreatura(ParserOutput parserOutput, Stanza stanzaCorrente) {
        Oggetto oggetto = parserOutput.getOggetto();
        return stanzaCorrente.getNome().equalsIgnoreCase("Canile") &&
                oggetto != null && oggetto.getNome().equalsIgnoreCase("Poltiglia");
    }

    /**
     * Gestisce l'interazione con la creatura nel canile.
     *
     * @param description il contesto di gioco
     * @param stanzaCorrente la stanza corrente
     * @return il messaggio descrittivo dell'interazione con la creatura
     */
    private String gestisciInterazioneCreatura(DescrizioneGioco description, Stanza stanzaCorrente) {
        StringBuilder msg = new StringBuilder();
        boolean creaturaSconfitta = description.isCreaturaCanideSconfitta();
        boolean creaturaAttivata = description.isCreaturaCanideAttivata();

        if (!creaturaSconfitta && !creaturaAttivata) {
            description.setCreaturaCanideAttivata(true);
            CreaturaThread creaturaThread = new CreaturaThread(description, interfacciaGioco);
            Thread thread = new Thread(creaturaThread);
            description.setCreaturaThread(creaturaThread, thread);
            thread.start();
            msg.append("! ! ! ! ! ! ! ! ! ! ! ! ! !\n\n")
                    .append("Non appena ti avvicini per esaminare quell'ammasso di peli, un movimento inequivocabile vi si agita dall'interno!\n")
                    .append("Ti discosti prontamente.\n")
                    .append("Una creatura canina, deformata e grottesca, fuoriesce dall'ammasso di putridume!\n")
                    .append("I suoi occhi vitrei non hanno più nulla di naturale. La sua bocca si spalanca completamente in un ringhio innaturale.\n")
                    .append("È lì a fissarti negli occhi, con acido che gli gronda dalla bocca.\n")
                    .append("Si è appostato davanti alla porta.\n")
                    .append("Adesso non hai più via d'uscita...\n\n")
                    .append("! ! ! ! ! ! ! ! ! ! ! ! ! !\n");
        }
        else if (isPalaPresente(stanzaCorrente, description)) {
            msg.append("\uD83D\uDD0E: Tra i peli e il fango scorgi quello che sembra essere un manico di legno... che sembra essere... una PALA!!! {Potrebbe tornarmi utile}");
        }
        else {
            msg.append("\uD83D\uDD0E: Non sembra esserci nient'altro di utile, solo delle ossa umane.");
        }
        return msg.toString();
    }

    /**
     * Verifica la presenza della pala nella stanza.
     *
     * @param stanza la stanza da controllare
     * @param description il contesto di gioco
     * @return true se la pala è presente nella stanza, false altrimenti
     */
    private boolean isPalaPresente(Stanza stanza, DescrizioneGioco description) {
        Set<Oggetto> oggettiStanza = description.getOggettiPerStanza(stanza.getIdStanza());
        return oggettiStanza.stream().anyMatch(oggetto -> oggetto.getNome().equalsIgnoreCase("Pala"));
    }
}
