package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.ui.*;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;

import java.util.Set;

/**
 * Classe che gestisce il comando "LEGGI" per la visualizzazione di oggetti leggibili.
 * Implementa l'interfaccia Observer per reagire ai comandi di lettura.
 * Mostra il contenuto testuale degli oggetti leggibili in una finestra dedicata.
 */
public class AggiornaLettura implements Observer {

    private final InterfacciaGioco interfacciaGioco;

    /**
     * Costruttore della classe.
     *
     * @param interfacciaGioco riferimento all'interfaccia grafica principale
     */
    public AggiornaLettura(InterfacciaGioco interfacciaGioco) {
        this.interfacciaGioco = interfacciaGioco;
    }


    /**
     * Metodo principale che gestisce il comando LEGGI.
     *
     * @param description il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return messaggio di conferma della lettura o messaggio di errore se l'oggetto non è leggibile
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo() == TipoComando.LEGGI) {
            // Verifica se non è stato specificato alcun oggetto da leggere
            if (parserOutput.getOggetto() == null && parserOutput.getOggettoInv() == null) {
                return "Oggetto da leggere non identificato.";
            }
            int id_stanza_corrente = description.getStanzaCorrente().getIdStanza();
            Set<Oggetto> oggettiInStanza = description.getOggettiPerStanza(id_stanza_corrente);
            Set<Oggetto> inventario = description.getInventario();
            Oggetto oggettoParserOutput = parserOutput.getOggetto();
            Oggetto oggettoInvParserOutput = parserOutput.getOggettoInv();
            if (oggettoParserOutput != null) {
                if (oggettiInStanza.contains(oggettoParserOutput)) {
                    if (oggettoParserOutput instanceof OggettoLeggibileNonRaccoglibile) {
                        OggettoLeggibileNonRaccoglibile oggLeggibile = (OggettoLeggibileNonRaccoglibile) oggettoParserOutput;
                        FinestraLettura.mostraFinestraLettura(interfacciaGioco,oggLeggibile.getTesto(),oggLeggibile.getNome());
                        return "Lettura di " + oggLeggibile.getNome() + "...";
                    }
                    else if (oggettoParserOutput instanceof OggettoLeggibile) {
                        OggettoLeggibile oggettoLegg = (OggettoLeggibile) oggettoParserOutput;
                        FinestraLettura.mostraFinestraLettura(interfacciaGioco, oggettoLegg.getTesto(), oggettoLegg.getNome());
                        return "Lettura di " + oggettoLegg.getNome()+ "...";
                    }
                }
            }
            if (oggettoInvParserOutput != null) {
                if (inventario.contains(oggettoInvParserOutput)) {
                    if (oggettoInvParserOutput instanceof OggettoLeggibileNonRaccoglibile) {
                        OggettoLeggibileNonRaccoglibile oggLeggibile = (OggettoLeggibileNonRaccoglibile) oggettoInvParserOutput;
                        FinestraLettura.mostraFinestraLettura(interfacciaGioco, oggLeggibile.getTesto(), oggLeggibile.getNome());
                        return "Lettura di " + oggLeggibile.getNome()+ "...";
                    }
                    else if (oggettoInvParserOutput instanceof OggettoLeggibile) {
                        OggettoLeggibile oggettoLegg = (OggettoLeggibile) oggettoInvParserOutput;
                        FinestraLettura.mostraFinestraLettura(interfacciaGioco, oggettoLegg.getTesto(), oggettoLegg.getNome());
                        return "Lettura di " + oggettoLegg.getNome()+ "...";
                    }
                }
            }
            return "Non c'è nulla da leggere.";
        }
        return "";
    }

}