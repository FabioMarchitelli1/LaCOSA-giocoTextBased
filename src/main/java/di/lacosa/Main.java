package di.lacosa;

import di.lacosa.contesti.AggiornaDialogo;
import di.lacosa.database.DatabaseManager;
import di.lacosa.implementazione.LaCosa;
import di.lacosa.parser.Parser;
import di.lacosa.parser.ParserOutput;
import di.lacosa.tipi.*;
import di.lacosa.ui.TitoliDiCoda;
import di.lacosa.ui.InterfacciaGioco;
import di.lacosa.ui.InterfacciaIniziale;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Classe principale che gestisce il flusso del gioco "La COSA".
 * Coordina l'interazione tra l'interfaccia utente, la logica di gioco e il sistema di salvataggio.
 * Implementa FaseFinaleListener per gestire la transizione alla fase conclusiva del gioco.
 *
 * @author fabioMarchitelli
 */
public class Main implements FaseFinaleListener {

    private InterfacciaGioco interfacciaGioco;
    private final DescrizioneGioco game;
    private Parser parser;

    /**
     * Costruttore principale del motore di gioco.
     *
     * @param game L'istanza del gioco da gestire
     * @param nuovaPartita Se true, inizializza una nuova partita, altrimenti significa che la partita deve
     *                     essere caricata da un salvataggio presente
     * @param interfaccia L'interfaccia grafica del gioco (può essere null se non ancora creata)
     */
    public Main(DescrizioneGioco game, boolean nuovaPartita, InterfacciaGioco interfaccia) {

        //Prende la partita e lo mette nella sua variabile personale
        this.game = game;
        //Prende l'interfaccia e la mette nella sua interfaccia personale
        this.interfacciaGioco = interfaccia;
        //Inizializza solo se il giocatore ha scelto una nuova partita
        if (nuovaPartita) {
            try {
                this.game.inizializza();
            } catch (Exception ex) {
                System.err.println("ERRORE: Problema con inizializza(): " + ex.getMessage());
            }
        }

        // Caricamento delle stopwords utilizzate dal parser
        try {
            Set<String> stopwords = Utils.caricaFileInSet(Utils.class.getResourceAsStream("/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.err.println("ERRORE: Problema con stopwords: " + ex.getMessage());
        }

        // Imposta l'interfaccia come quella attuale
        if (game instanceof LaCosa && interfaccia != null) {
            ((LaCosa) game).setInterfacciaGioco(interfaccia);
        }
    }

    /**
     * Punto di ingresso principale dell'applicazione.
     * Verifica il database e avvia l'interfaccia iniziale.
     *
     * @param args Argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        verificaDatabase();         // Accertamento che il database del gioco sia presente e funzionante

        // Lancio dell'interfaccia iniziale del gioco
        SwingUtilities.invokeLater(() -> new InterfacciaIniziale().setVisible(true));

    }

    /**
     * Verifica lo stato del database utilizzato dal gioco.
     * Si accerta che esista, nel caso contrario lo crea.
     */
    private static void verificaDatabase() {
        try {
            System.out.println(" Verifica del database...");
            DatabaseManager.main(new String[]{});
        } catch (Exception e) {
            System.err.println("Errore nella verifica del database: " + e.getMessage());
        }
    }



    /**
     * Avvia una nuova partita o carica una partita esistente.
     *
     * @param nuovaPartita Se true, crea una nuova partita, altrimenti la carica
     * @param slot Il numero dello slot di salvataggio (1-3)
     */
    public static void avviaPartita(boolean nuovaPartita, int slot) {
        System.out.println(nuovaPartita ? "Avvio della nuova partita..." : "Tentativo di caricare la partita dallo slot: " + slot);
        LaCosa gioco;
        Main engine;

        if (nuovaPartita) {

            // Prima di iniziare una nuova partita bisogna controllare che ci sia almeno 1 slot di caricamento disponibile tra i 3 slot di default
            int slotPieni = GestoreSalvataggi.getNumeroSlotPieni();
            if (slotPieni >= 3) {

                // Chiedere all'utente quale dei tre slot desidera cancellare per fare spazio alla sua attuale partita
                Object[] opzioni = {"Slot 1", "Slot 2", "Slot 3", "Annulla"};
                int scelta = JOptionPane.showOptionDialog(
                        null,
                        "Hai già 3 salvataggi. Scegli uno da sovrascrivere o annulla.",
                        "Slot Pieni",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        opzioni,
                        opzioni[3]
                );
                if (scelta >= 0 && scelta < 3) {
                    // Sovrascrizione dello slot scelto dall'utente
                    GestoreSalvataggi.cancellaSlot(scelta + 1);
                    System.out.println("Sovrascrivo slot " + (scelta + 1));
                } else {
                    System.out.println("Nessun nuovo gioco avviato.");
                    SwingUtilities.invokeLater(() -> new InterfacciaIniziale().setVisible(true));
                    return;
                }
            }

            // Creazione di una nuova istanza di gioco
            gioco = new LaCosa();
            engine = new Main(gioco, true, null);

            // Assegna uno slot libero alla nuova partita
            int slotLibero = cercaPrimoSlotLibero();
            if (slotLibero == -1) {
                System.err.println("Nessuno slot libero disponibile.");
                return;
            }

            gioco.setSlotCorrente(slotLibero);  // Slot assegnato alla partita, ma non ancora salvato


        } else {
            // Caricamento di una partita presente
            engine = caricaPartita(slot);
            if (engine == null) {
                System.out.println("ERRORE: Impossibile caricare la partita.");
                return;
            }

            gioco = (LaCosa) engine.game;
            gioco.setSlotCorrente(slot);
            gioco.ripristinaTransient();
        }

        // Creazione dell'interfaccia di gioco
        InterfacciaGioco interfacciaGioco = new InterfacciaGioco(engine);
        engine.setInterfacciaGioco(interfacciaGioco);
        gioco.setInterfacciaGioco(interfacciaGioco);
        interfacciaGioco.nascondiSalta();

        // Imposta la stanza corrente sulla mappa di gioco (dopo aver creato interfaccia)
        if (gioco.getStanzaCorrente() != null) {
            interfacciaGioco.getMappa().setStanzaCorrente(gioco.getStanzaCorrente().getIdStanza());
        }

        // Collegamento degli observer del gioco
        gioco.setObservers(engine);

        // Mostra l'interfaccia del gioco
        engine.mostraInterfacciaGioco();

        if (nuovaPartita) {
            interfacciaGioco.disabilitaInput(); // blocca l'inserimento dell'input durante la scena iniziale

            // Mostra fase d'introduzione solo se è una nuova partita
            engine.mostraIntroParte1(() -> new Thread(engine::presentaAmbienteIniziale).start());
        } else {

            abilitaMappaAttiva(interfacciaGioco);
            // Se è una partita caricata, parte direttamente nel "vivo" del gioco
            // ovvero che salta tutta l'introduzione iniziale
            new Thread(engine::presentaAmbienteIniziale).start();
        }
    }

    /**
     * Cerca il primo slot di salvataggio libero.
     *
     * @return Il numero dello slot libero (1-3) o -1 se tutti occupati
     */
    private static int cercaPrimoSlotLibero() {
         for (int s = 1; s <= 3; s++) {
            File file = new File("saves/slot" + s + ".dat");
            if (!file.exists()) {
                return s;
            }
        }
        return -1;
    }


    /**
     * Carica una partita dallo slot specificato.
     *
     * @param slot Il numero dello slot da caricare (1-3)
     * @return L'istanza Main configurata con la partita caricata
     */
    public static Main caricaPartita(int slot) {

        // caricamento da file
        LaCosa partitaCaricata = GestoreSalvataggi.caricaPartita(slot);
        if (partitaCaricata == null) {
            return null; // indica un errore, non c'è un file
        }
        //Imposta lo slot come lo slot della partita corrente
        partitaCaricata.setSlotCorrente(slot);

        //Creazione di un Main con una partita caricata, ma senza Interfaccia
        Main engine = new Main(partitaCaricata, false, null);
        return engine;
    }


    /**
     * Mostra l'interfaccia principale del gioco.
     */
    public void mostraInterfacciaGioco() {
        if (interfacciaGioco != null) {
            interfacciaGioco.setVisible(true);
        } else {
            System.err.println("ERRORE: InterfacciaGioco è NULL!");
        }
    }


    /**
     * Presenta la stanza corrente al giocatore, mostrandone nome e descrizione,
     * e prepara l'interfaccia per ricevere i comandi.
     */
    public void presentaAmbienteIniziale() {
        interfacciaGioco.scriviInAreaDiTesto("\n Ti trovi in: " + game.getStanzaCorrente().getNome());
        interfacciaGioco.scriviInAreaDiTesto("─────────────────────────────────────");
        interfacciaGioco.scriviInAreaDiTesto("\n" + game.getStanzaCorrente().getDescrizione());
        interfacciaGioco.scriviInAreaDiTesto("\n(Per conoscere la lista dei comandi, inserire il comando: istruzioni)\n");
        interfacciaGioco.scriviInAreaDiTesto("\n?> \n");
    }



    /**
     * Processa il comando inserito dal giocatore.
     *
     * @param comando La stringa del comando da processare
     */
    public void processaComando(String comando) {
        if (comando == null || comando.trim().isEmpty()) return;

        int id_stanza_corrente = game.getStanzaCorrente().getIdStanza();
        Set<Oggetto> oggettiInStanzaCorrente = game.getOggettiPerStanza(id_stanza_corrente);
        Set<Personaggio> personaggiInStanzaCorrente = game.getPersonaggiPerStanza(id_stanza_corrente);

        //Passa il comando nella sua forma naturale al parser
        ParserOutput p = parser.parse(comando, game.getComandi(), oggettiInStanzaCorrente, game.getInventario(), personaggiInStanzaCorrente);
        if (p == null || p.getComando() == null) {
            interfacciaGioco.scriviInAreaDiTesto("Non capisco quello che mi vuoi dire.");
            interfacciaGioco.scriviInAreaDiTesto("\n?> \n");
            return;
        }
        if (p.getComando().getTipo() == TipoComando.FINE && controlloCondizioniUscita(game)) {
            interfacciaGioco.scriviInAreaDiTesto("\n\nNon puoi uscire in questo momento!\n\n\n?>");
            return;
        }

        //Nel caso in cui il comando inserito equivale al comando fine, allora si gestisce l'uscita dalla partita
        if (p.getComando().getTipo() == TipoComando.FINE) {
            gestisciUscita();
        } else {
            game.prossimaMossa(p, System.out);
        }
    }


    /**
     * Gestisce la procedura di uscita dal gioco.
     * Offre opzioni per salvare prima di uscire.
     */
    public void gestisciUscita() {

        int scelta = JOptionPane.showOptionDialog(
                null,
                "Vuoi salvare prima di uscire?",
                "Conferma Uscita",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Sì", "No", "Annulla"},
                "Sì"
        );

        if (scelta == JOptionPane.YES_OPTION) {
            LaCosa gioco = (LaCosa) game;
            int slot = gioco.getSlotCorrente();

            if (slot == -1) {
                System.out.println("Nessuno slot assegnato a questa partita.");
            }

            System.out.println(" Stanza corrente prima del salvataggio: " + gioco.getStanzaCorrente());
            boolean successo = GestoreSalvataggi.salvaPartita(gioco, slot);

            if (successo) {
                interfacciaGioco.svuotaAreaDiTesto();
                interfacciaGioco.disabilitaInput();
                interfacciaGioco.scriviInAreaDiTesto("\n\n Partita salvata nello slot " + slot + ".\n\n");

                new Timer(2500, e -> {
                    ((Timer) e.getSource()).stop();
                    interfacciaGioco.dispose(); //  Chiusura interfaccia di gioco
                    new InterfacciaIniziale().setVisible(true); // Apri il menu iniziale
                }).start();
            } else {
                System.out.println(" Errore durante il salvataggio.");
            }
        } else if (scelta == JOptionPane.NO_OPTION) {
            interfacciaGioco.svuotaAreaDiTesto();
            interfacciaGioco.disabilitaInput();
            interfacciaGioco.scriviInAreaDiTesto("\n\nGrazie per aver giocato.\n\n");

            new Timer(2500, e -> {
                ((Timer) e.getSource()).stop();
                interfacciaGioco.dispose(); //  Chiudi interfaccia di gioco
                new InterfacciaIniziale().setVisible(true); // Apri il menu iniziale
            }).start();
        } else {
            interfacciaGioco.scriviInAreaDiTesto("Uscita annullata.\n\n?>");
        }
    }

    /**
     * Mostra la sequenza introduttiva del gioco, parte 1/13.
     * Presenta la scena iniziale nella stazione di ricerca e avvia l'effetto battitura.
     * Al termine mostra un pulsante "Continua" per procedere alla parte successiva.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    public void mostraIntroParte1(Runnable afterIntro) {
        interfacciaGioco.scriviInAreaDiTesto("\nU.S. Research Station #31  ANTARTIDE        21:00 - Sera\n\n");

        new Timer(1100, e -> {
            ((Timer) e.getSource()).stop();

            String testo = "Sono ore ormai che non senti, né vedi anima viva.\nSolo il rumore sottile della tormenta ti tiene compagnia.\n";

            interfacciaGioco.scriviConEffettoBattitura(testo, () -> {
                // Questo codice viene eseguito solo dopo che il typing è finito
                interfacciaGioco.scriviInAreaDiTesto("\n\n(Premi 'Continua' per proseguire...)\n\n");
                interfacciaGioco.mostraPulsanteContinua(() -> {
                    interfacciaGioco.nascondiPulsanteContinua();
                    mostraIntroParte2(afterIntro);
                });
            }, true);
        }).start();
    }


    /**
     * Mostra la parte 2/13 dell'introduzione.
     * Descrive la situazione nella stazione di ricerca e l'arrivo del messaggio radio.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte2(Runnable afterIntro) {
        interfacciaGioco.scriviConEffettoBattitura("Ora come ora, all’avamposto siete rimasti in tre: tu (il Sorvegliante),\nMugs (il Pilota) e il vecchio Sid (il Cuoco).\nSei di turno nella Sala Comunicazione da cinque ore. La radio tace.\nCosì come ieri, così come il giorno prima, ed i giorni prima ancora.\n" +
                "L’unico contatto recente è stato un messaggio del Quartier Generale:\nuna nuova équipe di scienziati sarebbe arrivata tra cinque giorni.\nMa questi 5 giorni sono ormai trascorsi da un bel pezzo ormai…\n" +
                "Sei esausto. Fuori dalla finestra, solo neve, buio e silenzio.\nOgni fiocco sembra soffocare la luce. Ogni folata spegne i pensieri.\nLe tue palpebre si fanno pesanti.\nStai per cedere al sonno quando...\n\n\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraIntroParte3(afterIntro);
                    });
                }, true);
    }

    /**
     * Mostra la parte 3/13 dell'introduzione.
     * Presenta il messaggio disturbato del Dottor Gasly.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte3(Runnable afterIntro) {
        interfacciaGioco.scriviConEffettoBattitura("BEEP-BEEP-BEEP ~~~iw-^nnnne....^^^--parhh~---^^^~~~\nBEEP-BEEP-BEEP «Qui è ~~~ (INTERFERENZA) ~~~ che PARLA!»\nBEEP-BEEP-BEEP «Accidenti! Dannazione!!!»\nBEEP-BEEP-BEEP «Qui è il DOTTOR MATTHEW GASLY che PARLA!»\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraIntroParte4(afterIntro);
                    });
                },true);
    }

    /**
     * Mostra la parte 4/13 dell'introduzione.
     * Descrive il tentativo fallito di comunicazione radio.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte4(Runnable afterIntro){
        interfacciaGioco.scriviConEffettoBattitura("\n\nTi lanci verso il trasmettitore.\nLa linea è disturbata. La tormenta interferisce con il segnale.\nProvi a rispondere, ma è inutile.", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraIntroParte5(afterIntro);
                    });
                }, true);
    }


    /**
     * Mostra la parte 5/13 dell'introduzione.
     * Riporta il messaggio di soccorso completo con le coordinate.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte5(Runnable afterIntro){
        interfacciaGioco.scriviConEffettoBattitura("\n\n\nCHIAMO DALL’AVAMPOSTO #4.\nDurante la nostra spedizione... siamo entrati in contatto...\n~~~ (INTERFERENZA) ~~~ IMMEDIATO!!!\nRICHIEDIAMO SOCCORSO IMMEDIATO!\nCoordinate: 89°59.85'S, 139°16.37’E. STAZIONE #4.\n" +
                "VI PREGO... AL PIÙ PRESTO...\nQuesta... COSA... è fuori da questo mondo...\nRipeto: DOTTOR MATTHEW GASLY — STAZIONE #4...\nSiamo in grave—\n" +
                "(Rumori metallici in sottofondo: BAM! BAMM! BAMP!)\n" +
                "Oh mio dio...\nBEEP-BEEP-BEEP...", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraIntroParte6(afterIntro);
                    });
                }, true);
    }

    /**
     * Mostra la parte 6/13 dell'introduzione.
     * Descrive la reazione del protagonista al messaggio.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte6(Runnable afterIntro){
        interfacciaGioco.scriviConEffettoBattitura("\n\n\nCerchi disperatamente di ristabilire il contatto...ma niente. È tornato a regnare il silenzio di sempre...\n" +
                "Stringi quel foglio dove hai annotato ogni parola. Ti tremano le dita:\n" +
                "\nAVAMPOSTO #4 — 89°59.85'S, 139°16.37’E\nRICHIESTA DI SOCCORSO IMMEDIATO...\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraTitoloGioco(afterIntro);
                    });
                }, true);
    }

    /**
     * Mostra la parte 7/13 dell'introduzione (titolo del gioco).
     * Visualizza il titolo "LA COSA" centrato sullo schermo.
     *
     * @param dopoTitolo Callback da eseguire dopo la visualizzazione del titolo
     */
    private void mostraTitoloGioco(Runnable dopoTitolo) {
        interfacciaGioco.svuotaAreaDiTesto();

        interfacciaGioco.scriviCentratoConDelay("️  LA COSA", 325, () -> {
            // Attendi un paio di secondi prima di continuare
            new Timer(1100, e -> {
                ((Timer)e.getSource()).stop();
                interfacciaGioco.svuotaAreaDiTesto();
                mostraIntroParte8(dopoTitolo);
            }).start();
        });
    }

    /**
     * Mostra la parte 8/13 dell'introduzione.
     * Cambia scenario all'elicottero e presenta il dialogo con Mugs.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte8(Runnable afterIntro){
        interfacciaGioco.scriviInAreaDiTesto("\nIn Elicottero - Sopra le bianche pianure Antartiche.         05:00- Mattino\n\n");

        new Timer(1100, e -> {
            ((Timer)e.getSource()).stop();

            interfacciaGioco.scriviConEffettoBattitura("MUGS: \n" +
                    "«'Una Cosà', eh? Tutti quei cervelloni si atteggiano come dèi scesi in terra,\n" +
                    "ma quando i problemi sono al difuori delle loro teste, " +
                    "nessuno dei loro\nsette dottorati riesce a tirarli fuori dai casini.\n" +
                    "Non ti hanno detto nient’altro?»", () -> {
                        interfacciaGioco.mostraPulsanteContinua(() -> {
                            interfacciaGioco.nascondiPulsanteContinua();
                            mostraIntroParte9(afterIntro);
                        });
                    }, true);
        }).start();
    }

    /**
     * Mostra la parte 9/13 dell'introduzione.
     * Continua il dialogo tra il protagonista e Mugs.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte9(Runnable afterIntro){

        interfacciaGioco.scriviConEffettoBattitura("\n\n\nTU:\n«No. Solo che fosse un’emergenza.»\n" +
                "\n" +
                "\n" +
                "MUGS:\n«Stronzate. Io dico che hanno trovato un topo.»", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraIntroParte10(afterIntro);
                    });
                }, true);
    }

    /**
     * Mostra la parte 10/13 dell'introduzione.
     * Descrive l'avvistamento dell'avamposto dall'elicottero.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte10(Runnable afterIntro){
        interfacciaGioco.scriviConEffettoBattitura("\n\n\nDal finestrino, una macchia grigia si staglia all’orizzonte,\ninterrompendo l’infinita distesa bianca dell’Antartide.\nDistante, ma inconfondibile.\n" +
                "\n" +
                "MUGS (con tono più serio):\n«Iniziamo la discesa. Preparati.\nSiamo quasi arrivati.»", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraIntroParte11(afterIntro);
                    });
                }, true);
    }

    /**
     * Mostra la parte 11/13 dell'introduzione.
     * Presenta l'atterraggio all'avamposto #4.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte11(Runnable afterIntro) {
        interfacciaGioco.svuotaAreaDiTesto();
        interfacciaGioco.scriviInAreaDiTesto("\nU.S. Research Station #4 – Esterno        06:00 – Mattino\n\n");

        new Timer(1100, e -> {
            ((Timer) e.getSource()).stop();

            interfacciaGioco.scriviConEffettoBattitura("L’elicottero tocca terra con uno stridio metallico, sollevando una nuvola di neve che avvolge tutto per un istante. Il rumore delle pale rallenta gradualmente, ma il vento artico continua a sferzare l’esterno con la sua solita, glaciale insistenza.\n\n\n" +
                    "MUGS si gira verso di te, con lo sguardo serio:\n" +
                    "«Allora, conosciamo entrambi il protocollo. Tu fai quello che devi fare. Io resto qui, pronto a decollare in caso serva. Se hai bisogno di me, usa il ricetrasmettitore.»\n\n", () -> {
                interfacciaGioco.mostraPulsanteContinua(() -> {
                    interfacciaGioco.nascondiPulsanteContinua();
                    mostraIntroParte12(afterIntro);
                });
            }, true);
        }).start();
    }

    /**
     * Mostra la parte 12/13 dell'introduzione.
     * Elenca l'equipaggiamento a disposizione del giocatore.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte12(Runnable afterIntro){
        interfacciaGioco.scriviConEffettoBattitura("Prima di scendere, controlli rapidamente l’equipaggiamento a tua disposizione nella fondina e nella borsa a tracolla:\n\n" +
                "- PISTOLA \n\n" +
                "- TORCIA \n\n" +
                "- RICETRASMETTITORE \n\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraIntroParte13(afterIntro);
                    });
                }, true);
    }

    /**
     * Mostra la parte 13/13 (finale) dell'introduzione.
     * Descrive l'ingresso nell'avamposto e completa la sequenza introduttiva.
     * Abilita l'input del giocatore e avvia il gioco vero e proprio.
     *
     * @param afterIntro Callback da eseguire al termine dell'intera introduzione
     */
    private void mostraIntroParte13(Runnable afterIntro) {
        interfacciaGioco.scriviConEffettoBattitura("Come apri il portellone, un’ondata di vento gelido ti investe il viso. Con passo deciso, metti piede sulla neve compatta e ti dirigi verso l’ingresso dell’avamposto.\n" +
                "Noti che il metallo della porta è coperto da uno spesso strato di brina.\n\n\n(Premi il pulsante 'Continua' per entrare nell'avamposto...)", () -> {
            interfacciaGioco.mostraPulsanteContinua(() -> {
                interfacciaGioco.abilitaInput();
                interfacciaGioco.mostraMappaNormale();
                interfacciaGioco.attivaPuntinoRosso();
                interfacciaGioco.nascondiPulsanteContinua();
                interfacciaGioco.svuotaAreaDiTesto();
                afterIntro.run();
            });
        }, true);
    }


    /**
     * Implementazione dell'interfaccia FaseFinaleListener.
     * Avvia la sequenza finale del gioco.
     */
    @Override
    public void avviaFaseFinale() {
        mostraFinaleParte1();
    }


    /**
     * Mostra la parte 1/15 della sequenza finale del gioco.
     * Sequenza estremamente simile a quella utlizzata da {@code mostraIntroParte}
     * Presenta il Dottor Gasly ferito e incapace di alzarsi.
     * Al termine mostra un pulsante "Continua" per procedere.
     */
    private void mostraFinaleParte1() {

        //viene mostrato con due secondi di ritardo per motivazioni drammatiche
        new Timer(2100, e -> {
            ((Timer) e.getSource()).stop();

            interfacciaGioco.scriviConEffettoBattitura("\n«Non riesco ad alzarmi…» mormora Gasly, ferito.\n\n\n(Premi Continua per andare avanti)\n\n\n", () -> {
                        interfacciaGioco.mostraPulsanteContinua(() -> {
                            interfacciaGioco.svuotaAreaDiTesto();
                            interfacciaGioco.nascondiPulsanteContinua();
                            mostraFinaleParte1eMezzo();
                        });
                    }, true);
        }).start();
    }


    /**
     * Mostra la parte 1.5/15 della sequenza finale.
     * Descrive il tentativo di aiutare Gasly e il sospetto iniziale.
     */
    private void mostraFinaleParte1eMezzo() {
            interfacciaGioco.scriviConEffettoBattitura("\n\nTi avvicini a Gasly con cautela. Ha ancora la pistola in mano...\n" +
                    "Lo sollevi, sentendolo gravare sulla spalla.\n" +
                    "Nel farlo, dai le spalle alla porta.\n" +
                    "Il silenzio nella stanza è denso. Ogni respiro pesa.\n" +
                    "Poi, improvvisamente, senti Gasly sussurrare:\n" +
                    "\n«Sips…»\n\n", () -> {
                        interfacciaGioco.mostraPulsanteContinua(() -> {
                            interfacciaGioco.nascondiPulsanteContinua();
                            mostraFinaleParte2();
                        });
                    }, true);
    }



    /**
     * Mostra la parte 2/15 della sequenza finale.
     * Rivela il misterioso riferimento a "Sips" da parte di Gasly.
     */
    private void mostraFinaleParte2() {
        interfacciaGioco.scriviConEffettoBattitura("Quel nome ti trafigge…\n" +
                "\n«Che cosa hai detto?». Chiedi a Gasly, col cuore che inizia a palpitare incontrollabilmente.\n" +
                "\n«SIPS!» Grida lui, fissando qualcosa alle tue spalle…\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraFinaleParte3();
                    });
                }, true);
    }

    /**
     * Mostra la parte 3/15 della sequenza finale.
     * Presenta l'apparizione di Sips e il suo comportamento sospetto.
     */
    private void mostraFinaleParte3() {
        interfacciaGioco.scriviConEffettoBattitura("\n\nTi giri di scatto.\n" +
                "Sulla soglia si erge una figura immobile, appena illuminata.\nGasly lo riconosce subito:\n" +
                "\n«Sips! Sei vivo!»\n\n" +
                "\nNon è possibile…\n" +
                "C’è qualcosa di sbagliato…\n" +
                "Lo sguardo di quell’uomo è disumano, freddo, distante.\n" +
                "I suoi movimenti sono lenti, meccanici, come se stesse lottando per controllare il proprio corpo.\n" +
                "Qualcosa dentro di te si tende…\n" +
                "Senza distogliere lo sguardo, alzi il lanciafiamme.", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraFinaleParte4();
                    });
                }, true);
    }

    /**
     * Mostra la parte 4/15 della sequenza finale.
     * Descrive la reazione di Gasly e il dilemma del protagonista.
     */
    private void mostraFinaleParte4() {
        interfacciaGioco.scriviConEffettoBattitura("\n\n\n«Cosa diavolo stai facendo?!» Esclama Gasly.\n\n" +
                "\nLe tue mani tremano." +
                "\n" +
                "Qualcosa non torna…\n" +
                "L’istinto ti urla di non fidarti…\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraFinaleParte4eMezzo();
                    });
                }, true);
    }

    /**
     * Mostra la parte 4.5/15 della sequenza finale.
     * Presenta l'avanzamento della figura misteriosa e la tensione crescente.
     */
    private void mostraFinaleParte4eMezzo() {
        interfacciaGioco.scriviConEffettoBattitura("\n\nLa figura avanza...\n\n" +
                "«STAI INDIETRO!» Gridi contro di lui.\n\n" +
                "{Potrebbe essere davvero lui… o ciò che temo? Gasly non lo vede. Ma io sì.}\n\n" +
                "\n«Ora risponderai a qualche mia domanda…»\n\n\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.svuotaAreaDiTesto();
                        avviaDialogoSips();
                        interfacciaGioco.abilitaInput();
                    });
                }, true);
    }


    /**
     * Avvia il dialogo con il personaggio "Sips" nella sequenza finale.
     * Richiama la classe osservatore in modo da gestire il dialogo con il personaggio.
     */
    private void avviaDialogoSips() {
        Personaggio sips = game.getPersonaggioPerNome("Sips");

        // Ora passiamo una callback che si esegue quando il dialogo multi-step viene concluso
        AggiornaDialogo aggiornaDialogoSips = new AggiornaDialogo(interfacciaGioco, (Runnable)() -> {
            interfacciaGioco.disabilitaInput();
            interfacciaGioco.svuotaAreaDiTesto();
            // Fine dialogo Sips -> procedi a Parte 5
            mostraFinaleParte5();
        });

        // Avviamo il dialogo con Sips
        aggiornaDialogoSips.update(game, sips);
    }



    /**
     * Mostra la parte 5/15 della sequenza finale.
     * Descrive l'attacco alla creatura e la sua trasformazione.
     */
    private void mostraFinaleParte5() {
        new Timer(400, e -> {
            ((Timer) e.getSource()).stop();
            interfacciaGioco.scriviConEffettoBattitura("\n\nLa tensione è insostenibile. Premi il grilletto.\nUna lingua di fuoco investe Sips, che si contorce all’istante.\n" +
                        "La sua pelle si lacera, la carne si deforma: qualcosa di enorme esplode dal suo torso.\nZampe scheletriche e contorte si aggrappano al soffitto, mentre la sua testa si spalanca in una bocca mostruosa.\nL’urlo che ne esce è disumano:\n" +
                        "\n" +
                        "«GRHAHAHGRAAAHHHHAHHHHH!!!»\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraFinaleParte6();
                    });
                }, true);
        }).start();
    }

    /**
     * Mostra la parte 6/15 della sequenza finale.
     * Presenta la fuga dall'avamposto in fiamme.
     */
    private void mostraFinaleParte6() {
        interfacciaGioco.scriviConEffettoBattitura("\n\nLa stanza trema. Le fiamme avanzano.\nLa COSA si dimena, urlando, ma il fuoco la divora.\nIl calore è soffocante.\nAfferri Gasly e lo trascini fuori dall’avamposto.\n" +
                "Il gelo dell’Artico vi investe come un pugno.\n" +
                "La neve taglia la pelle. \n" +
                "Rivolgi lo sguardo verso sinistra, dov’era atterrato l’elicottero.\n" +
                "\n" +
                "\n«MUUUGS!!! ACCENDI IL MOTORE! ORA! ORA! ORA!»" +
                "\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.disattivaPuntinoRosso();
                        mostraFinaleParte7();
                    });
                }, true);
    }

    /**
     * Mostra la parte 7/15 della sequenza finale.
     * Descrive la fuga in elicottero e la visione dell'avamposto che brucia.
     */
    private void mostraFinaleParte7() {
        interfacciaGioco.scriviConEffettoBattitura("\n\nLe eliche si attivano.\n" +
                        "Avanzate nella bufera. Gasly è un peso morto sulle tue spalle.\n" +
                        "Salite a bordo.\n" +
                        "Lo sportello si chiude.\n" +
                        "L’elicottero si solleva, tremante.\n" +
                        "Dal finestrino, guardi il fuoco divorare l’avamposto...\n" +
                        "\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.mostraMappaBloccata();
                        interfacciaGioco.svuotaAreaDiTesto();
                        mostraFinaleParte8();
                    });
                }, true);
    }


    /**
     * Mostra la parte 8/15 della sequenza finale.
     * Presenta il dialogo con Mugs durante il volo.
     */
    private void mostraFinaleParte8() {
        interfacciaGioco.scriviInAreaDiTesto("\nIn Elicottero - Sopra le bianche pianure Antartiche.         10:00- Mattino\n\n");
        new Timer(1100, e -> {
            ((Timer)e.getSource()).stop();

            interfacciaGioco.scriviConEffettoBattitura("Finalmente, riesci a prendere un sospiro di sollievo.\n" +
                "Con un mezzo sorriso, dici:\n\n" +
                "«Mugs… Dio, non sono mai stato così felice di vedere quel tuo orribile pizzetto.\nNon saprei nemmeno da dove cominciare… È peggio di quanto pensassimo. Dobbiamo dirlo all’ammiraglio. Al comandante. Al segretario della difesa. A TUTTI!»\n" +
                "\n" +
                "(Silenzio)\n" +
                "\n" +
                "“Mugs…?”" +
                        "\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        mostraFinaleParte9();
                    });
                }, true);

        }).start();
    }

    /**
     * Mostra la parte 9/15 della sequenza finale.
     * Rivela il comportamento sospetto di Mugs.
     */
    private void mostraFinaleParte9() {
        interfacciaGioco.scriviConEffettoBattitura("\nNessuna risposta.\n\n" +
                "Ti volti verso di lui.\n" +
                "Mugs è immobile.\n" +
                "Le mani strette sul volante.\n" +
                "Lo sguardo perso sull’orizzonte." +
                        "\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.svuotaAreaDiTesto();
                        interfacciaGioco.abilitaInput();
                        avviaDialogoMugs();
                    });
                }, true);
    }

    /**
     * Avvia il dialogo con Mugs nella sequenza finale.
     * Gestisce l'interazione con il personaggio Mugs.
     */
    private void avviaDialogoMugs() {
        Personaggio mugs = game.getPersonaggioPerNome("Mugs");

        AggiornaDialogo aggiornaDialogoMugs = new AggiornaDialogo(interfacciaGioco, (Runnable)() -> {
            interfacciaGioco.disabilitaInput();
            mostraFinaleParte10();
        });
        aggiornaDialogoMugs.update(game, mugs);
    }

    /**
     * Mostra la parte 10/15 della sequenza finale.
     * Presenta la tensione crescente nell'elicottero.
     */
    private void mostraFinaleParte10() {
        new Timer(2100, e -> {
            ((Timer) e.getSource()).stop();

            interfacciaGioco.scriviConEffettoBattitura("Ti volti verso Gasly per la prima volta dal decollo...\n\n\n(Premi Continua per andare avanti)\n\n\n", () -> {
                interfacciaGioco.mostraPulsanteContinua(() -> {
                    interfacciaGioco.svuotaAreaDiTesto();
                    interfacciaGioco.nascondiPulsanteContinua();
                    mostraFinaleParte11();
                });
            }, false);
        }).start();

    }

    /**
     * Mostra la parte 11/15 della sequenza finale.
     * Descrive lo scambio di sguardi tra i personaggi.
     * Queste parti successive sono caratterizzata dalla mancata possibilità
     * di effettuare l'azione Salta
     */
    private void mostraFinaleParte11() {
        interfacciaGioco.scriviConEffettoBattitura("\n\nGli occhi del dottore sono spalancati. Fissi.\n" +
                "Quello stesso sguardo che hai visto quando hai aperto la porta.\n" +
                "QUELLO sguardo.\n" +
                "Non servono parole.\n" +
                "Sai esattamente cosa sta pensando.\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.nascondiSalta();
                        mostraFinaleParte12();
                    });
                }, false);
    }



    /**
     * Mostra la parte 12/15 della sequenza finale.
     */
    private void mostraFinaleParte12() {
        interfacciaGioco.scriviConEffettoBattitura("\nIl rumore costante delle eliche amplifica il silenzio tra voi,\nun silenzio che pesa come piombo.\nIl vuoto ti lacera lo stomaco.", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.nascondiSalta();
                        mostraFinaleParte13();
                    });
                },false);
    }


    /**
     * Mostra la parte 13/15 della sequenza finale.
     */
    private void mostraFinaleParte13() {
        interfacciaGioco.scriviConEffettoBattitura("\n\nLa tua mano si muove lentamente, scivola verso il lanciafiamme, ancora tiepido.\n\n", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.nascondiSalta();
                        mostraFinaleParte14();
                    });
                }, false);
    }

    /**
     * Mostra la parte 14/15 della sequenza finale.
     */
    private void mostraFinaleParte14() {
        interfacciaGioco.scriviConEffettoBattitura("Gasly lo nota.\nAnche lui si irrigidisce.\nLa sua mano stringe la pistola sulle ginocchia.", () -> {
                    interfacciaGioco.mostraPulsanteContinua(() -> {
                        interfacciaGioco.nascondiPulsanteContinua();
                        interfacciaGioco.nascondiSalta();
                        mostraFinaleParte15();
                    });
                }, false);
    }

    /**
     * Mostra la parte 15/15 (finale) della sequenza.
     * Conclude con il finale del gioco  e chiama i titoli di coda.
     */
    private void mostraFinaleParte15() {
        interfacciaGioco.scriviConEffettoBattitura("\n\nL’elicottero continua a volare…\n" +
                "\nSotto di voi, solo il bianco infinito dell’Artico…\n" +
                "\n" +
                "Nessuno sa dove vi stiate VERAMENTE dirigendo…\n" +
                "\nNé chi… o COSA… arriverà a destinazione…\n", () -> {

            new Timer(2000, e -> {
                ((Timer) e.getSource()).stop();

                interfacciaGioco.mostraPulsanteContinua(() -> {
                    interfacciaGioco.nascondiPulsanteContinua();
                    conclusionePartita();
                    TitoliDiCoda.mostraTitoliDiCoda();

                });
            }).start();
        }, false);
    }


    /**
     * Controlla se sono soddisfatte le condizioni per uscire dalla partita nel corrente stato di gioco
     *
     * @param game L'istanza del gioco da verificare
     * @return true se non è possibile uscire, false altrimenti
     */
    public boolean controlloCondizioniUscita(DescrizioneGioco game){
        if(game.isCreaturaCanideAttivata() && !game.isCreaturaCanideSconfitta()){
            return true;
        }
        return false;
    }



    /**
     * Conclude la partita liberando lo spazio del salvataggio e chiudendo l'interfaccia.
     */
    private void conclusionePartita(){
        LaCosa gioco = (LaCosa) game;
        int slot = gioco.getSlotCorrente();
        GestoreSalvataggi.cancellaSlot(slot);
        interfacciaGioco.dispose();
    }



    /**
     * Abilita la visualizzazione della mappa nell'interfaccia.
     *
     * @param ui L'interfaccia grafica da configurare
     */
    private static void abilitaMappaAttiva(InterfacciaGioco ui) {
        ui.mostraMappaNormale();
        ui.attivaPuntinoRosso();
    }


    /**
     * Imposta l'interfaccia grafica del gioco.
     *
     * @param interfaccia L'istanza di InterfacciaGioco da utilizzare
     */
    public void setInterfacciaGioco(InterfacciaGioco interfaccia) {
        this.interfacciaGioco = interfaccia;
    }
}
