package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.TipoComando;
import di.lacosa.ui.FinestraLettura;
import di.lacosa.ui.InterfacciaGioco;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Classe che gestisce l'aggiornamento e la visualizzazione delle istruzioni di aiuto del gioco.
 * <p>
 * Implementa l'interfaccia {@link Observer} per reagire ai comandi di aiuto inseriti dal giocatore.
 * Quando viene rilevato un comando di aiuto valido, carica e mostra le istruzioni del gioco
 * in una finestra dedicata.
 */
public class AggiornaAiuto implements Observer {

    private final InterfacciaGioco interfacciaGioco;

    /**
     * Costruttore della classe AggiornaAiuto.
     *
     * @param interfacciaGioco L'interfaccia grafica del gioco a cui associare questo gestore
     */
    public AggiornaAiuto(InterfacciaGioco interfacciaGioco) {
        this.interfacciaGioco = interfacciaGioco;
    }


    /**
     * Metodo chiamato quando viene rilevato un comando che potrebbe richiedere l'aggiornamento dell'aiuto.
     * <p>
     * Se il comando è un richiesta di aiuto valida (senza parametri aggiuntivi), carica e mostra
     * le istruzioni del gioco.
     *
     * @param description Lo stato corrente del gioco (non utilizzato in questo contesto)
     * @param parserOutput Il risultato del parsing del comando inserito dal giocatore
     * @return Le istruzioni di aiuto come stringa, o stringa vuota se non era una richiesta di aiuto valida
     * @throws RuntimeException Se si verifica un errore durante il caricamento delle istruzioni
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        if (controlloAiuto(parserOutput)) {
            try {
                String istruzioni = caricaRisorsa("istruzioni");
                mostraFinestraLettura(istruzioni, "Istruzioni");
                return "Lettura Istruzioni...";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    /**
     * Verifica se il comando analizzato è una richiesta di aiuto valida.
     * <p>
     * Un comando di aiuto è considerato valido se:
     * <ul>
     *   <li>È di tipo {@link TipoComando#AIUTO}</li>
     *   <li>Non ha oggetti o interlocutori associati</li>
     * </ul>
     *
     * @param parserOutput Il risultato del parsing del comando da verificare
     * @return true se è una richiesta di aiuto valida, false altrimenti
     */
    private boolean controlloAiuto(ParserOutput parserOutput) {
        if (parserOutput.getComando().getTipo().equals(TipoComando.AIUTO)) {
            if (parserOutput.getOggetto() == null && parserOutput.getInterlocutore() == null && parserOutput.getOggettoInv() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Carica una risorsa dal classpath come stringa.
     * <p>
     * La risorsa viene cercata nella radice del classpath (/).
     *
     * @param percorsoRisorsa Il percorso della risorsa da caricare (senza slash iniziale)
     * @return Il contenuto della risorsa come stringa
     * @throws IOException Se la risorsa non viene trovata o non può essere letta
     */
    private String caricaRisorsa(String percorsoRisorsa) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/" + percorsoRisorsa);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + percorsoRisorsa);
        }
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    /**
     * Mostra una finestra di lettura con il testo specificato.
     * <p>
     * La finestra viene visualizzata in modo asincrono tramite {@link SwingUtilities#invokeLater}.
     *
     * @param testo Il testo da visualizzare nella finestra
     * @param titolo Il titolo della finestra
     */
    private void mostraFinestraLettura(String testo, String titolo) {
        SwingUtilities.invokeLater(() -> {
            FinestraLettura finestra = new FinestraLettura(interfacciaGioco, testo, titolo);
            finestra.setVisible(true);
        });
    }
}