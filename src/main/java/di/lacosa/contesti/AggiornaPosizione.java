package di.lacosa.contesti;

import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;
import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.ui.InterfacciaGioco;

import java.util.List;
import java.util.Set;

/**
 * Classe che gestisce il comando "VAI" per il movimento tra stanze.
 * Implementa l'interfaccia Observer per reagire ai comandi di movimento.
 * Gestisce la logica di spostamento, porte bloccate e eventi speciali.
 */
public class AggiornaPosizione implements Observer {
    private final InterfacciaGioco interfacciaGioco;

    /**
     * Costruttore della classe.
     *
     * @param interfacciaGioco riferimento all'interfaccia grafica del gioco
     */
    public AggiornaPosizione(InterfacciaGioco interfacciaGioco) {
        this.interfacciaGioco = interfacciaGioco;
    }


    /**
     * Metodo principale che gestisce il comando VAI.
     *
     * @param descrizione il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return messaggio descrittivo dell'esito del movimento o stringa vuota se riuscito
     */
        @Override
        public String update(DescrizioneGioco descrizione, ParserOutput parserOutput) {
            if (parserOutput.getComando().getTipo() != TipoComando.VAI) {
                return "";
            }

            int idStanzaDaRaggiungere = determinaStanzaTarget(descrizione, parserOutput);

            switch (idStanzaDaRaggiungere) {
                case -10: // "vai" senza direzione
                    return "Devi specificare una direzione. Esempio: vai nord, vai sud, ecc.";

                case -20: // "vai fsdfafsd" (direzione non valida)
                    return "Direzione non valida. Usa: nord, sud, est o ovest";

                case -30: // "vai est" ma non c'è stanza
                    return "Non c'è nulla in quella direzione.";

                case 0: // Uscita speciale
                    return "Non puoi andartene via. Il dottor Gasly e chiunque sia ancora vivo hanno bisogno di te...";

                default: // ID stanza valido
                    if (isBloccataDalNemico(descrizione)) {
                        return "LA COSA blocca la porta. Non puoi uscire finché la creatura è ancora viva!";
                    }
                    if (descrizione.isTorciaAccesa()) {
                        descrizione.setTorciaAccesa(false);
                        interfacciaGioco.scriviInAreaDiTesto("(La torcia è stata disattivata)");
                    }
                    if (idStanzaDaRaggiungere == 15) {
                        if (!AccessoConsentitoInSalaCom(descrizione)) {
                            return gestisciAzioneGasly(descrizione);
                        }
                    }
                    return movimentoInStanza(descrizione, idStanzaDaRaggiungere);
            }
        }

    /**
     * Determina la stanza di destinazione in base alla direzione specificata.
     *
     * @param descrizione il contesto di gioco
     * @param parserOutput l'output del parser
     * @return ID della stanza di destinazione o codice di errore
     */
    private int determinaStanzaTarget(DescrizioneGioco descrizione, ParserOutput parserOutput) {
        // Caso 1: Comando "vai" senza direzione
        if (parserOutput.getDirezione() == null) {
            return -10; // Codice speciale per "vai" senza direzione
        }

        // Caso 2: Direzione non valida (es. "vai fsdfafsd")
        if (parserOutput.getDirezione() == PuntoCardinale.INVALIDO) {
            return -20; // Codice per direzione non valida
        }

        // Caso 3: Direzione valida ma senza stanza (-1)
        int idStanza = switch (parserOutput.getDirezione()) {
            case NORD -> descrizione.getStanzaCorrente().getIdStanzaNord();
            case SUD -> descrizione.getStanzaCorrente().getIdStanzaSud();
            case EST -> descrizione.getStanzaCorrente().getIdStanzaEst();
            case OVEST -> descrizione.getStanzaCorrente().getIdStanzaOvest();
            default -> -1; // Questo non dovrebbe mai succedere
        };

        if (idStanza == -1) {
            return -30; // Codice per "nessuna stanza in quella direzione"
        }

        // Caso 4: Uscita speciale (0)
        if (idStanza == 0) {
            return 0; // Manteniamo lo special case per l'uscita
        }
        return idStanza; // Caso normale: ID stanza valido
    }

    /**
     * Verifica se il movimento è bloccato da un nemico nella stanza corrente.
     *
     * @param descrizione il contesto di gioco
     * @return true se il movimento è bloccato, false altrimenti
     */
    private boolean isBloccataDalNemico(DescrizioneGioco descrizione) {
        return descrizione.getStanzaCorrente().getNome().equalsIgnoreCase("Canile")
                && descrizione.isCreaturaCanideAttivata()
                && !descrizione.isCreaturaCanideSconfitta();
    }

    /**
     * Verifica se il giocatore può accedere alla Sala Comunicazioni.
     *
     * @param descrizione il contesto di gioco
     * @return true se il giocatore ha gli oggetti necessari, false altrimenti
     */
    private boolean AccessoConsentitoInSalaCom(DescrizioneGioco descrizione) {
        Set<Oggetto> inventario = descrizione.getInventario();
        Oggetto lanciafiamme = descrizione.getOggettoPerId(8);
        Oggetto analgesico = descrizione.getOggettoPerId(9);

        return inventario.contains(lanciafiamme) && inventario.contains(analgesico);
    }

    /**
     * Gestisce lo spostamento nella stanza di destinazione.
     *
     * @param descrizione il contesto di gioco
     * @param idStanzaDaRaggiungere ID della stanza di destinazione
     * @return stringa vuota o messaggio di errore
     */
    private String movimentoInStanza(DescrizioneGioco descrizione, int idStanzaDaRaggiungere) {
        List<Stanza> stanze = descrizione.getStanze();
        Stanza stanzaDaRaggiungere = stanze.get(idStanzaDaRaggiungere - 1);
        if (stanzaDaRaggiungere.isPortaBloccata()) {
            return tentativoDiSbloccarePorta(stanzaDaRaggiungere, descrizione, idStanzaDaRaggiungere);
        } else {
            descrizione.setStanzaCorrente(stanzaDaRaggiungere);
            interfacciaGioco.aggiornaMappa(stanzaDaRaggiungere.getIdStanza());
            return "";
        }
    }

    /**
     * Gestisce il tentativo di sbloccare una porta.
     *
     * @param stanzaDaRaggiungere la stanza da raggiungere
     * @param descrizione il contesto di gioco
     * @param idStanza ID della stanza
     * @return stringa vuota (la logica continua nella callback)
     */
    private String tentativoDiSbloccarePorta(Stanza stanzaDaRaggiungere, DescrizioneGioco descrizione, int idStanza) {
        interfacciaGioco.scriviInAreaDiTesto("La porta è bloccata. Inserisci il codice di apertura:");
        interfacciaGioco.attendiRispostaGiocatore(codice -> {
            if (stanzaDaRaggiungere.checkCodicePorta(codice)) {
                descrizione.getStanze().get(idStanza - 1).setBloccoPorta(false);
                interfacciaGioco.svuotaAreaDiTesto();
                interfacciaGioco.scriviInAreaDiTesto("\nLa porta è stata sbloccata.\n");
                descrizione.setStanzaCorrente(stanzaDaRaggiungere);
                interfacciaGioco.scriviInAreaDiTesto("\n Ti trovi in: " + stanzaDaRaggiungere.getNome());
                interfacciaGioco.scriviInAreaDiTesto("-------------------------------------");
                interfacciaGioco.scriviInAreaDiTesto(stanzaDaRaggiungere.getDescrizione());
                interfacciaGioco.scriviInAreaDiTesto("\n\n?> \n");
                interfacciaGioco.aggiornaMappa(stanzaDaRaggiungere.getIdStanza());
            } else {
                interfacciaGioco.scriviInAreaDiTesto("Codice errato. La porta rimane chiusa.\n\n?>\n\n");
            }
            interfacciaGioco.attendiRispostaGiocatore(null);
        });
        return "";
    }


    /**
     * Gestisce l'evento speciale di Gasly quando si tenta di entrare nella Sala Comunicazioni.
     *
     * @param descrizione il contesto di gioco
     * @return messaggio descrittivo dell'evento
     */
    private String gestisciAzioneGasly(DescrizioneGioco descrizione) {

        if (descrizione.isSecondaAzioneGaslyAttivata()) {
            return "{Entrerò quando avrò trovato sia il lanciafiamme che le medicine. Meglio cercare un'Armeria e l'Infermeria}";
        }
        if (!descrizione.isPrimaAzioneGaslyAttivata()) {
            descrizione.setPrimaAzioneGaslyAttivata(true);
            return """
                    Mentre giri la maniglia, senti un forte fragore dall'interno:
                    
                    BAMM!!!.
                    Le tue orecchie iniziano a fischiare quando ti accorgi di un foro nella porta.
                    Ti stanno sparando contro!!
                    Indietreggi rapidamente, illeso, mentre un altro proiettile colpisce il muro.

                    Dalla stanza proviene una voce maschile che grida: “STAI INDIETRO FIGLIO DI PU***NA!!!“

                    {Un superstite! Quest’UOMO è armato e terrorizzato. Devo farlo ragionare prima di entrare, devo PARLARGLI.}
                    """;
        } else {
            return "{L’UOMO è armato, non posso rischiare di farmi uccidere. Devo PARLARGLI prima di entrare.}";
        }
    }


}
