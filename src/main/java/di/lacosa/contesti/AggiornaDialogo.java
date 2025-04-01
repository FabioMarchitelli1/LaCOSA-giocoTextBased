package di.lacosa.contesti;

import di.lacosa.DescrizioneGioco;
import di.lacosa.FaseFinaleListener;
import di.lacosa.Observer;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;
import di.lacosa.ui.InterfacciaGioco;

import java.util.List;
import java.util.Optional;

/**
 * Classe che gestisce il sistema di dialogo del gioco.
 * Implementa l'interfaccia Observer per reagire ai comandi di dialogo del giocatore.
 * Gestisce l'interazione con i personaggi, la visualizzazione delle opzioni di dialogo,
 * le risposte dei personaggi e gli effetti sul gioco derivanti dalle conversazioni.
 */
public class AggiornaDialogo implements Observer {

    private final Runnable allaFineDelDialogo;    // Callback da eseguire al termine del dialogo
    private final InterfacciaGioco interfacciaGioco;  // Riferimento all'interfaccia grafica
    private final FaseFinaleListener finaleListener; // Listener per gestire la fase finale del gioco
    private Personaggio interlocutoreCorrente; // Personaggio con cui si sta dialogando
    private List<DialogoGiocatore> dialoghiCorrenti; // Opzioni di dialogo disponibili
    private List<RispostaPersonaggio> risposteCorrenti; // Risposte del personaggio
    private int nodoCorrente;  // Nodo corrente nel flusso dei dialoghi
    private boolean dialogoTerminato = false;  // Flag per lo stato del dialogo
    private DescrizioneGioco descrizioneGioco;   // Contesto del gioco

    /**
     * Costruttore principale.
     *
     * @param interfaccia Riferimento all'interfaccia grafica
     * @param listener Listener per la fase finale del gioco
     * @param onFinish Callback da eseguire al termine del dialogo
     */
    public AggiornaDialogo(InterfacciaGioco interfaccia, FaseFinaleListener listener, Runnable onFinish) {
        this.interfacciaGioco = interfaccia;
        this.finaleListener = listener;
        this.allaFineDelDialogo = onFinish;
    }

    /**
     * Costruttore con solo listener per la fase finale.
     *
     * @param interfaccia Riferimento all'interfaccia grafica
     * @param listener Listener per la fase finale del gioco
     */
    public AggiornaDialogo(InterfacciaGioco interfaccia, FaseFinaleListener listener) {
        this(interfaccia, listener, null);
    }

    /**
     * Costruttore con solo callback per la fine del dialogo.
     *
     * @param interfaccia Riferimento all'interfaccia grafica
     * @param onFinish Callback da eseguire al termine del dialogo
     */
    public AggiornaDialogo(InterfacciaGioco interfaccia, Runnable onFinish) {
        this(interfaccia, null, onFinish);
    }

    /**
     * Metodo principale per gestire i comandi di dialogo.
     *
     * @param description Contesto del gioco
     * @param parserOutput Output del parser contenente il comando
     * @return Messaggio di risposta o stringa vuota se il comando non è di dialogo
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        if (!isParlaCommand(parserOutput)) {
            return "";
        }

        if (isTargetMancante(parserOutput)) {
            return "Specificare con chi vuoi parlare";
        }

        interlocutoreCorrente = parserOutput.getInterlocutore();

        if (interlocutoreCorrente == null) {
            return "Interlocutore inserito non trovato";
        }

        if (isUomoInCorridoio(description, parserOutput)) {
            return gestisciCasiSpeciali();
        }

        if (interlocutoreCorrente.isInterazioneAvvenuta()) {
            return "Questo personaggio non ha più nulla da dirti.";
        }

        if (isCaneInCanile(description, parserOutput)) {
            return gestisciCasiSpeciali();
        }
        iniziaDialogo(description);
        return "";
    }

    /**
     * Avvia un dialogo con un personaggio specifico.
     *
     * @param description Contesto del gioco
     * @param personaggio Personaggio con cui dialogare
     */
    public void update(DescrizioneGioco description, Personaggio personaggio) {
        if (personaggio == null) {
            throw new IllegalArgumentException("Errore critico: il personaggio passato a update() è null!");
        }
        this.interlocutoreCorrente = personaggio;
        if (!interlocutoreCorrente.isInterazioneAvvenuta()) {
            iniziaDialogo(description);
        } else {
            interfacciaGioco.scriviInAreaDiTesto("Questo personaggio non ha più nulla da dirti.");
        }
    }

    /**
     * Verifica se il comando è di tipo PARLA.
     *
     * @param parserOutput Output del parser
     * @return true se il comando è di tipo PARLA, false altrimenti
     */
    private boolean isParlaCommand(ParserOutput parserOutput) {
        return parserOutput != null
                && parserOutput.getComando() != null
                && parserOutput.getComando().getTipo() == TipoComando.PARLA;
    }

    /**
     * Verifica se il comando PARLA è privo di target.
     *
     * @param parserOutput Output del parser
     * @return true se manca il target, false altrimenti
     */
    private boolean isTargetMancante(ParserOutput parserOutput) {
        return parserOutput.getInterlocutore() == null
                &&
                parserOutput.getDirezione() == PuntoCardinale.INVALIDO;
    }

    /**
     * Inizia un nuovo dialogo con il personaggio corrente.
     *
     * @param description Contesto del gioco
     */
    private void iniziaDialogo(DescrizioneGioco description) {
        this.descrizioneGioco = description;
        dialoghiCorrenti = description.getDialoghiPerIdPersonaggio(interlocutoreCorrente.getId());
        risposteCorrenti = description.getRispostePerIdPersonaggio(interlocutoreCorrente.getId());
        nodoCorrente = 1;
        dialogoTerminato = false;
        mostraOpzioniDialogo();
        interfacciaGioco.attendiRispostaGiocatore(this::processaSceltaGiocatore);
    }

    /**
     * Mostra le opzioni di dialogo disponibili al giocatore.
     */
    private void mostraOpzioniDialogo() {
        interfacciaGioco.scriviInAreaDiTesto("\n\n\n\n\n\n\n\n\n\n Opzioni di Dialogo:");
        interfacciaGioco.scriviInAreaDiTesto("-----------------------------------");
        int numero = 1;
        for (DialogoGiocatore dialogo : dialoghiCorrenti) {
            if (dialogo.getNodo_dialogo() == nodoCorrente && !dialogo.isUtilizzato()) {
                interfacciaGioco.scriviInAreaDiTesto(numero + ". " + dialogo.getTesto()+"\n");
                numero++;
            }
        }
        promptScelta();
    }

    /**
     * Mostra il prompt per la scelta del giocatore.
     */
    private void promptScelta() {
        interfacciaGioco.scriviInAreaDiTesto("\n Digita il numero della tua risposta:");
    }


    /**
     * Processa la scelta del giocatore durante il dialogo.
     *
     * @param input Input del giocatore
     */
    private void processaSceltaGiocatore(String input) {
        if (dialogoTerminato) {
            return;
        }
        int scelta;

        try {
            scelta = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            interfacciaGioco.scriviInAreaDiTesto("\n\n\n\n Input non valido. Inserisci un numero.\n");
            promptScelta();
            return;
        }

        int maxScelte = contaLineeNodoCorrente();
        if (scelta < 1 || scelta > maxScelte) {
            interfacciaGioco.scriviInAreaDiTesto("\n\n\n\n Scelta non valida (scegli tra 1 e " + maxScelte + ").\n");
            promptScelta();
            return;
        }

        DialogoGiocatore scelto = cercaDialogoScelto(scelta).get();
        scelto.setUtilizzato(true);

        RispostaPersonaggio risposta = descrizioneGioco.getRispostaPerId(scelto.getId_risposta(), risposteCorrenti);
        interfacciaGioco.scriviInAreaDiTesto("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                "\n\n\n\n\n\n\n\n\n\n\n"
                + interlocutoreCorrente.getNome() +":\n" + risposta.getTesto() + "\n");

        Integer prossimoNodo = risposta.getProssimoNodo();
        nodoCorrente = (prossimoNodo != null) ? prossimoNodo : -1;
        aggiornaOsservazioneStanza(descrizioneGioco);
        aggiornaAzioneGasly(descrizioneGioco);
        aggiornaStatoGioco(descrizioneGioco, interlocutoreCorrente);

        if (nodoCorrente == -1) {
            fineDialogo();
            return;
        }
        if (tutteOpzioniUsate()) {
            fineDialogo();
            return;
        }
        mostraOpzioniDialogo();
    }

    /**
     * Conta le opzioni di dialogo disponibili nel nodo corrente.
     *
     * @return Numero di opzioni disponibili
     */
    private int contaLineeNodoCorrente() {
        return (int) dialoghiCorrenti.stream()
                .filter(d -> d.getNodo_dialogo() == nodoCorrente && !d.isUtilizzato())
                .count();
    }

    /**
     * Cerca l'opzione di dialogo scelta dal giocatore.
     *
     * @param scelta Numero della scelta
     * @return Optional contenente l'opzione di dialogo se trovata
     */
    private Optional<DialogoGiocatore> cercaDialogoScelto(int scelta) {
        return dialoghiCorrenti.stream()
                .filter(d -> d.getNodo_dialogo() == nodoCorrente && !d.isUtilizzato())
                .skip(scelta - 1L)
                .findFirst();
    }

    /**
     * Verifica se tutte le opzioni del nodo corrente sono state usate.
     *
     * @return true se tutte le opzioni sono state usate, false altrimenti
     */
    private boolean tutteOpzioniUsate() {
        return dialoghiCorrenti.stream()
                .filter(d -> d.getNodo_dialogo() == nodoCorrente)
                .allMatch(DialogoGiocatore::isUtilizzato);
    }

    /**
     * Gestisce la fine del dialogo corrente.
     */
    private void fineDialogo() {
        dialogoTerminato = true;
        interfacciaGioco.scriviInAreaDiTesto("\nIl dialogo è terminato.\n\n");
        interlocutoreCorrente.setInterazioneAvvenuta();

        // Se Gasly è nella Sala Comunicazioni, avvia immediatamente la fase finale
        if (interlocutoreCorrente.getNome().equalsIgnoreCase("Gasly") &&
                descrizioneGioco.getStanzaCorrente().getNome().equalsIgnoreCase("Sala Comunicazioni")) {
            interfacciaGioco.disabilitaInput();
            finaleListener.avviaFaseFinale();
            return;
        }

        // Se esiste un callback personalizzato (per Sips o Mugs), lo si chiama
        if (allaFineDelDialogo != null) {
            allaFineDelDialogo.run();
        } else {
            interfacciaGioco.attendiRispostaGiocatore(null);
        }
    }

    /**
     * Verifica se il giocatore sta parlando con l'uomo nel corridoio.
     *
     * @param description Contesto del gioco
     * @param parserOutput Output del parser
     * @return true se è l'uomo nel corridoio, false altrimenti
     */
    private boolean isUomoInCorridoio(DescrizioneGioco description, ParserOutput parserOutput) {

        return description.getStanzaCorrente().getNome().equalsIgnoreCase("Corridoio") &&
                parserOutput.getInterlocutore().getNome().equalsIgnoreCase("Sconosciuto") &&
                description.isPrimaAzioneGaslyAttivata() == false;
    }

    /**
     * Verifica se il giocatore sta parlando con il cane nel canile.
     *
     * @param description Contesto del gioco
     * @param parserOutput Output del parser
     * @return true se è il cane nel canile, false altrimenti
     */
    private boolean isCaneInCanile(DescrizioneGioco description, ParserOutput parserOutput) {
        return description.getStanzaCorrente().getNome().equalsIgnoreCase("Canile") &&
                parserOutput.getInterlocutore().getNome().equalsIgnoreCase("Canide") &&
                (description.isCreaturaCanideAttivata() == false || description.isCreaturaCanideSconfitta()==true);
    }


    /**
     * Gestisce i casi speciali di dialogo.
     *
     * @return Messaggio per il giocatore
     */
    private String gestisciCasiSpeciali(){
        return "Personaggio inserito non trovato";
    }


    /**
     * Aggiorna l'osservazione della stanza se necessario.
     *
     * @param description Contesto del gioco
     */
    private void aggiornaOsservazioneStanza(DescrizioneGioco description) {
        Stanza stanzaCorrente = description.getStanzaCorrente();

        if (stanzaCorrente.getTipoOsservazioneAggiornata() == MotivazioneAggDescrizione.MODIFICATO_DA_EVENTO) {
            stanzaCorrente.attivaOsservazioneAggiornata();
        }
    }

    /**
     * Aggiorna lo stato del gioco in base alle interazioni con i personaggi.
     *
     * @param description Contesto del gioco
     * @param personaggio Personaggio con cui si è interagito
     */
    private void aggiornaStatoGioco(DescrizioneGioco description, Personaggio personaggio) {
        if (personaggio != null && personaggio.isInterazioneAvvenuta()) {
            personaggio.aggiornaEvento(description);
        }
    }

    /**
     * Gestisce le azioni speciali legate al personaggio Gasly.
     *
     * @param description Contesto del gioco
     */
    private void aggiornaAzioneGasly(DescrizioneGioco description) {
        if (description.getStanzaCorrente().getNome().equalsIgnoreCase("Corridoio")) {
            if(description.isPrimaAzioneGaslyAttivata()) {
                if (!description.isSecondaAzioneGaslyAttivata()) {
                    description.setSecondaAzioneGaslyAttivata(true);
                }
            }
        }
    }
}
