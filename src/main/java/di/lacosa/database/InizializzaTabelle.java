package di.lacosa.database;

import di.lacosa.tipi.Utils;

import java.io.IOException;

/**
 * Classe responsabile per l'inizializzazione completa del database di gioco.
 * <p>
 * Contiene tutti i metodi necessari per popolare le tabelle del database con:
 * <ul>
 *   <li>Stanze e loro connessioni</li>
 *   <li>Oggetti giocabili e non</li>
 *   <li>Personaggi e dialoghi</li>
 *   <li>Sistemi di interazione tra personaggi e giocatore</li>
 *   <li>Alias per oggetti e personaggi</li>
 * </ul>
 *
 * @author fabiomarchitelli
 */
public class InizializzaTabelle {

    /**
     * Inizializza tutte le tabelle del database in un'unica operazione.
     * <p>
     * Chiama in sequenza tutti i metodi di inizializzazione specifici.
     * Se si verifica un errore durante il caricamento dei file di testo,
     * stampa lo stack trace ma continua l'esecuzione.
     */
   public static void inizializzaTutto() {
       try {
           inizializzaStanze();
           inizializzaOggetti();
           inizializzaPersonaggi();
           inizializzaInterazioniPersonaggi();
           inizializzaInterazioniGiocatore();
           inizializzaAliasesOggetti();
           inizializzaAliasesPersonaggi();
           assegnaRisposteAGiocatore();
           assegnaProssimoNodoAPersonaggi();

           System.out.println("Inizializzato il database");
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    /**
     * Popola la tabella delle stanze con tutti gli ambienti di gioco.
     * <p>
     * Per ogni stanza specifica:
     * <ul>
     *   <li>Nome e descrizione</li>
     *   <li>Testo per l'azione "osserva"</li>
     *   <li>Connessioni con altre stanze</li>
     *   <li>Stato delle porte (bloccate/sbloccate)</li>
     *   <li>Codici per porte bloccate</li>
     * </ul>
     * Infine imposta le relazioni tra stanze adiacenti.
     */
    public static void inizializzaStanze () {
      //1
      TabellaStanze.inserisciStanza("Ingresso", "Ti trovi all’ingresso dell’avamposto.\n" +
              "La porta è stata lasciata aperta, come se fosse stata dimenticata nel caos.\n" +
              "Giarre e scarponi, abbandonati accanto all’entrata, sono sepolti sotto uno spesso strato di ghiaccio.\n" +
              "Davanti a te si apre un’ampia stanza immersa nel silenzio.", "Non c'è niente d'interessante qui. Solo inutili indumenti.", "", -1, -1,-1,-1,true, false, "0", 1);
      //2
      TabellaStanze.inserisciStanza("Sala Comune", "Entri nella sala comune, accolto da un silenzio denso e inquietante.\n" +
                      "Divani e poltrone giacciono sparsi in disordine, alcuni rovesciati come se fossero stati usati per difendersi.\n" +
                      "Una televisione è ancora accesa, ma trasmette solo un ronzio statico che vibra nell’aria,\n" +
                      "amplificando il senso di abbandono."
              , "In un angolo, vicino alla porta, noti una bacheca affissa alla parete:\n" +
                      "una LISTA, che elenca ordinatamente nomi, dati personali ed incarichi di tutti i dipendenti dell'avamposto.\n" +
                      "È aggiornata e curata nei dettagli, segno di una gestione attenta e scrupolosa.\n" +
                      "{Forse vale la pena darle un’occhiata...}"
              , "", -1, -1,-1,-1,true, false, "0", 1);
      //3
      TabellaStanze.inserisciStanza("Mensa", "Entri nella mensa e vieni subito investito da un’atmosfera di desolazione e paura.\n" +
              "Tavoli e sedie rovesciati, piatti e posate sparsi ovunque, come se tutti fossero fuggiti all’improvviso.\n" +
              "La luce fioca delle lampade al neon tremola, proiettando ombre irregolari e inquietanti lungo le pareti spoglie.", "Il silenzio è spezzato da un debole gemito.\n" +
              "In un angolo, un UOMO giace a terra, disteso e tremante. Indossa la tua stessa divisa da GUARDIA, ora lacerata e macchiata di sangue.\n" +
              "Il suo viso è contorto dal dolore; si aggrappa alla vita con ciò che resta delle sue forze.\n" +
              "Ogni respiro sembra un'agonia, i suoi occhi e la sua bocca si muovono impercettibilmente in una supplica silenziosa" +
              "{Vuole dirmi qualcosa...Devo PARLARGLI}","Osservi la piccola pozza di sangue, proveniente dal corpo morto dell'uomo, che si spande" +
              "lentamente verso l'altro lato della stanza.\n{Ormai non c'è più nulla che io possa fare per lui. È morto}", -1, -1,-1,-1,true, false, "0", 3 );
      //4
      TabellaStanze.inserisciStanza("Sala Centrale", "Entri nella sala centrale dell’avamposto, un ambiente ampio e spoglio, illuminato da fredde lampade fluorescenti.\n" +
              "Al centro, un lungo tavolo da conferenza è disseminato di mappe, fascicoli e appunti lasciati in disordine, segni evidenti di una fuga improvvisa.\n" +
              "Le sedie che lo circondano sono vuote, alcune rovesciate. I monitor appesi alle pareti sono perlopiù rotti, crepati come vetri infranti, mentre gli altri trasmettono soltanto canali morti.\n" +
              "\n", "Negli angoli della stanza, due corpi giacciono immobili.\n" +
              "I volti sono pallidi, i loro occhi vitrei fissano il vuoto.\n" +
              "Macchie scure di sangue secco si estendono sui loro vestiti e sul pavimento, tracciando i contorni di una lotta disperata.\n" +
              "{Non c’è più nulla che io possa fare… sono morti.}", "", -1, -1,-1,-1,true, false, "0", 1);
        //5
        TabellaStanze.inserisciStanza("Dormitorio",
                "Il dormitorio è una stanza fredda e austera, con letti a castello disposti lungo le pareti.\n" + "Lenzuola e coperte sono sparse sul pavimento in disordine.\nUn'unica lampada al neon sfarfalla debolmente, gettando ombre inquietanti sugli armadietti metallici.\nL'aria della stanza è pervasa da un senso di abbandono e di tensione latente, come se i precedenti occupanti fossero fuggiti in fretta, lasciando dietro di sé solo il freddo e il silenzio.”",
                "Mentre osservi la stanza, senti un leggero scricchiolio sotto lo scarpone.\n" +
                        "Abbassi lo sguardo: hai calpestato qualcosa. Una FOTOGRAFIA, piegata e logora.\n" +
                        "Ritrae due uomini in uniforme, sorridenti, con le braccia attorno alle spalle.\n" +
                        "{Dovrei ESAMINARLA meglio...}",
                "Non c'è più niente d'interessante qui.", -1, -1, -1, -1, true, false, "0", 3);

        //6
        TabellaStanze.inserisciStanza("Canile",
                "Non appena varchi la soglia, vieni investito da un tanfo acre e nauseante che impregna ogni angolo della stanza.\n" +
                        "L’aria è densa, quasi irrespirabile.\n" +
                        "Istintivamente ti porti una mano al naso, mentre cerchi di\nindividuare l’origine dell’odore.\n" +
                        "Ma è impossibile: la stanza è avvolta nel buio più totale.\n" +
                        "Non riesci a vedere nulla.",
                "Il pavimento è disseminato di ciuffi di peli bianchi e neri, eppure qui non c'è ombra di animale.\n" +
                        "In un angolo, noti una massa informe: una POLTIGLIA viscida, composta da peli, paglia e qualcosa che emana un odore rancido e marcescente.\n" +
                        "Per un attimo... hai avuto la netta impressione che qualcosa si fosse mosso al suo interno.\n" +
                        "{Dovrei ESAMINARLA meglio...}",
                "Osservi la carcassa della creatura, già in stato di decomposizione.\n" +
                        "Da essa si sprigiona un tanfo abominevole che riempie l’aria.\n" +
                        "Tra i peli incrostati e il fango vischioso, intravedi delle sagome indistinte.\n" +
                        "Difficili da identificare\n" +
                        "\n" +
                        "{Potrebbe esserci qualcosa di utile… meglio ESAMINARE di nuovo la POLTIGLIA.}\n" +
                        "\n", -1, -1, -1, -1, false, false, "0", 2);

        //7
        TabellaStanze.inserisciStanza("Armeria",
                "Entri nell'armeria, ma il caos regna sovrano.\n" +
                        "Gli scaffali sono vuoti e le rastrelliere per le armi sono state depredate, lasciando solo segni di frettolosi prelievi.\n" +
                        "Le cassette delle munizioni sono aperte e vuote, con cartucce sparse sul pavimento tra residui di imballaggi e attrezzi abbandonati. \n" +
                        "I tavoli da lavoro sono in disordine, coperti da strumenti di pulizia per armi. \n" +
                        "{Sembra non essere rimasto più nulla che io possa usare}",
                "In un angolo, noti un registro delle armi, aperto e pieno di annotazioni frettolose.\n" +
                        "Una in particolare cattura la tua attenzione, scritta a penna rossa e sottolineata più volte:\n" +
                        "\n" +
                        "\"Il LANCIAFIAMME è mancante. Qualcuno lo ha preso senza autorizzazione. La cosa va indagata immediatamente.\n" +
                        "18 giugno – Szilard\"\n" +
                        "\n" +
                        "{18 giugno... questo significa che il LANCIAFIAMME è stato sottratto prima che tutto precipitasse. Ma da chi? E perché?}\n" +
                        "\n",
                "", -1, -1, -1, -1, true, false, "0", 1);
        //8
        TabellaStanze.inserisciStanza("Infermeria",
                "Entri nell'infermeria e ti ritrovi immerso in uno scenario di caos e devastazione.\n" +
                        "Le pareti sono segnate da graffi profondi e ammaccature come se qualcosa — o qualcuno — avesse lottato con furia.\n" +
                        "Gli armadietti metallici sono stati forzati e lasciati spalancati, con il contenuto sparpagliato sul pavimento in totale disordine.\n" +
                        "Flaconi di medicinali sparsi qua e là, molti rotti o svuotati.\n" +
                        "Il lettino, rovesciato, ha il materasso fuori posto e il telaio deformato, piegato in modo innaturale.\n" +
                        "\n",
                "Nonostante il disordine, una fievole luce proveniente dalla finestra illumina un angolo della " +
                        "stanza dove, sotto una scrivania rovesciata, intravedi un flacone di ANALGESICO " +
                        "ancora intatto.\n{Questo potrebbe decisamente servirmi}",
                "Qui non c'è più niente d'interessante", -1, -1, -1, -1, true, true, "20051982", 3);

//9
        TabellaStanze.inserisciStanza("Laboratorio",
                "Entri in quello che sembra essere il laboratorio dell'avamposto.\n" +
                        "La porta sembra essere stata forzata.\n" +
                        "Al centro della stanza ci sono due lettini da operazione vuoti disposti uno accanto all'altro.\n" +
                        "Vi rimangono solo pezzi di bende e tessuti sporchi, resti di quelle che sembrano essere divise da spedizione, identiche a quella che indossi proprio in questo momento.\n" +
                        "Intorno ai lettini, strumenti medici e attrezzature scientifiche sono disseminati in modo caotico.",
                "Microscopi, provette e computer continuano a funzionare, mostrando dati e analisi complesse sui campioni prelevati da due corpi, ma dei soggetti di quei campioni non vi è traccia. \n" +
                        "Alcune immagini poste sulla lavagna illuminata mostrano le interiora dei corpi completamente alterate. \n" +
                        "Su uno dei tavoli, tra appunti sparsi, noti un DOSSIER ordinato con cura.\n" +
                        "{Potrebbe contenere informazioni fondamentali. Meglio dargli un’occhiata.}\n",
                "", -1, -1, -1, -1, true, false, "0", 1);

//10
        TabellaStanze.inserisciStanza("Serra",
                "Appena entri, una visione insolita ti colpisce: il verde delle piante.\nNon lo vedevi da mesi.\n" +
                        "La serra dell’avamposto si presenta come un miraggio vegetale nel gelo eterno dell’Antartide.\n" +
                        "Questo verde, tuttavia, appare spento e sbiadito. \n" +
                        "Al centro della stanza, tre lunghi terrari dominano lo spazio, ma appaiono abbandonati.\n" +
                        "Foglie annerite dal freddo pendono inerti dai rami, mentre altre giacciono sul pavimento, avvizzite.\n" +
                        "{Senza le dovute cure e le condizioni ottimali dell'avamposto, le piante non sono riuscite a sopravvivere}",
                "Il tuo sguardo si posa su un angolo della stanza e improvvisamente un lungo fremito di terrore ti attraversa la schiena.\n" +
                        "Davanti a te, un corpo senza testa giace disteso sul pavimento! \n" +
                        "A poca distanza dal cadavere, hai notato un QUADERNETTO abbandonato per terra. \n" +
                        "{Mi chiedo chi possa essere stata questa persona. Forse da quel QUADERNO scoprirò qualcosa}",
                "La stanza è adesso disseminata di terriccio sparso ovunque.\n" +
                        "Alcuni cumuli di terra sono persino giunti fino al cadavere senza testa. Arrivando ad assorbire il sangue che lo circondava," +
                        "tingendosi di un rosso scuro e inquietante.\n" +
                        "Hai lasciato la serra in uno stato ben più inquietante di come l’avevi trovata", -1, -1, -1, -1, true, true, "47187546", 3);
//11
        TabellaStanze.inserisciStanza("Inizio Corridoio",
                "Ti addentri in una nuova ala dell’avamposto.\n" +
                        "L’architettura qui appare diversa: più essenziale, più ordinata… eppure non meno inquietante.",
                "Non c’è nulla d’interessante", "", -1, -1, -1, -1, true, false, "0", 1);

//12
        TabellaStanze.inserisciStanza("Metà del Corridio",
                "Il corridoio si estende sia a nord che a sud, perdendosi nell'oscurità.\n" +
                        "A ovest, invece, una porta blindata attira la tua attenzione.\n" +
                        "Sulla sua superficie è fissata una targa metallica consumata dal tempo: \"SERRA\".\n",
                "Non c'è nulla d'interessante qui", "", -1, -1, -1, -1, true, false, "0", 1);

//13
        TabellaStanze.inserisciStanza("Fine del Corridoio",
                "Sei al termine del corridoio e a ovest vedi una porta blindata con la targa: INFERMERIA",
                "Non c'è nulla d'interessante qui", "", -1, -1, -1, -1, true, false, "0", 1);
//14
        TabellaStanze.inserisciStanza("Corridoio",
                "Ti trovi nella sezione organizzativa dell’avamposto.",
                "Non c'è nulla d'interessante qui, solo porte.", "", -1, -1, -1, -1, true, false, "0", 1);

//15
        TabellaStanze.inserisciStanza("Sala Comunicazioni",
                "Pannelli di controllo lampeggiano freneticamente.\nUna grande radiotrasmittente domina la stanza, circondata da monitor che trasmettono solo un canale morto e pannelli di controllo che lampeggiano in modo irregolare.\n" +
                        "Ma è lì, a terra, che il tuo sguardo si blocca:\nGasly; disteso sul pavimento, che ti punta una pistola contro.\nIl volto pallido, madido di sudore. La mano gli trema… ma l’arma rimane sollevata.\nÈ pronto a sparare.\n",
                "Il dottor Gasly è steso sul pavimento, il volto contratto dal dolore.\nCon mano tremante, ti punta la pistola contro.\n{Qualunque cosa abbia visto… l’ha visibilmente destabilizzato. Devo calmarlo, devo PARLARGLI.}\n", "", -1, -1, -1, -1, true, false, "0", 1);

      TabellaStanze.impostaStanzeAdiacenti(1, 2, 0,-1,-1 );
      TabellaStanze.impostaStanzeAdiacenti(2, 4,1, 3, -1);
      TabellaStanze.impostaStanzeAdiacenti(3, -1,-1,-1, 2);
      TabellaStanze.impostaStanzeAdiacenti(4, 14, 2, 5, 11);
      TabellaStanze.impostaStanzeAdiacenti(5, -1,-1,-1, 4);
      TabellaStanze.impostaStanzeAdiacenti(6, -1,-1,-1,14);
      TabellaStanze.impostaStanzeAdiacenti(7, -1,-1,14, -1);
      TabellaStanze.impostaStanzeAdiacenti(8, -1,-1,13, -1);
      TabellaStanze.impostaStanzeAdiacenti(9, -1, -1, 11, -1);
      TabellaStanze.impostaStanzeAdiacenti(10,-1,-1,12, -1);
      TabellaStanze.impostaStanzeAdiacenti(11, 12, -1, 4, 9);
      TabellaStanze.impostaStanzeAdiacenti(12, 13, 11, -1, 10);
      TabellaStanze.impostaStanzeAdiacenti(13, -1, 12, -1, 8);
      TabellaStanze.impostaStanzeAdiacenti(14, 15, 4, 6, 7);
      TabellaStanze.impostaStanzeAdiacenti(15, -1, 14, -1, -1);
    }

    /**
     * Popola la tabella degli oggetti con tutti gli items di gioco.
     * <p>
     * Carica prima i contenuti testuali da file risorse, poi crea oggetti di vario tipo:
     * <ul>
     *   <li>Armi</li>
     *   <li>Oggetti leggibili</li>
     *   <li>Oggetti attivabili</li>
     *   <li>Oggetti raccoglibili/non raccoglibili</li>
     * </ul>
     *
     * @throws IOException se si verificano errori nel caricamento dei file di testo
     */
    public static void inizializzaOggetti() throws IOException {
        String dossierGasly = null;
        String diarioSips = null;
        String listaDipendenti = null;

        try {
            // Conversione dei file di testo in Stringhe, caricandoli come risorse
            dossierGasly = Utils.caricaTestoInStringa(DatabaseManager.class.getResourceAsStream("/dossierGasly"));
            diarioSips = Utils.caricaTestoInStringa(DatabaseManager.class.getResourceAsStream("/diarioSips"));
            listaDipendenti = Utils.caricaTestoInStringa(DatabaseManager.class.getResourceAsStream("/listaDipendenti"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        TabellaOggetti.inserisciOggetto("Pistola", "Questa pistola è un'arma da fuoco di piccolo calibro, compatta e facile da maneggiare, utilizzata dal personale di sicurezza per autodifesa e controllo delle minacce.\nÈ progettata per essere trasportata in modo discreto e offre un equilibrio tra potenza di fuoco e facilità d'uso.", "Arma", null, null, 6);
     TabellaOggetti.inserisciOggetto("Dossier", "Il dossier giace aperto sul tavolo. È un tomo imponente e ben curato, dall'aspetto austero e professionale.\n Le pagine su cui è aperto riportano questo titolo: Analisi dei Corpi Mutati - Membri dell'Equipaggio John Carpenter e Ridley Scott. {Sarà meglio leggere ciò che dice...}", "OggettoLeggibileNonRaccoglibile",  dossierGasly, 9, null);
     TabellaOggetti.inserisciOggetto("Diario", "Questo diario è un quaderno dai bordi usurati e dalla copertina flessibile ma rovinata, con il nome del proprietario scritto con la penna sulla prima pagina: Patrick. Le pagine sono piene di frasi sconnesse e talvolta scurrili, tutte ordinate dai giorni in cui sono state scritte.\nQuesto diario testimonia pensieri e riflessioni messi nero su bianco senza tante pretese. {Sarà meglio che lo legga. Magari posso trovarci qualcosa d’importante.}", "OggettoLeggibile", diarioSips, 10, null);
     TabellaOggetti.inserisciOggetto("Fotografia", "La foto ritrae due uomini che ridono, abbracciati. Leggi i nomi cuciti sulle divise: Gasly e Sips. {Il dottor Gasly... e Sips. Sarà lui l’uomo di cui parlava la guardia?}\n" +
             "Gasly ha capelli corti, biondi, un’espressione seria ma gentile. Sips, con barba folta e capelli mossi, sorride calorosamente. Sembrano amici stretti.\n" +
             "Sul retro della foto, un numero scritto a penna: 47187546. {Un codice? Meglio ricordarlo...}", "OggettoRaccoglibile", null, 5, null);
     TabellaOggetti.inserisciOggetto("Torcia", "Una torcia resistente, con corpo in metallo e impugnatura antiscivolo. La sua luce è potente e regolabile, perfetta per illuminare anche le aree più buie.", "OggettoAttivabile", null, null, null);
     TabellaOggetti.inserisciOggetto("Lista Dipendenti", "Una lista dei dipendenti fissata al muro con puntine, con nomi e ruoli elencati ordinatamente al computer. La carta è ingiallita dal tempo e presenta qualche macchia d'umidità.\nAccanto a diversi nomi ci sono annotazioni che fanno riferimento a tutte le loro informazioni base. {Sarà meglio darci un occhiata}", "OggettoLeggibileNonRaccoglibile", listaDipendenti, 2, null);
     TabellaOggetti.inserisciOggetto("Pala", "Questa pala sembra essere uno strumento robusto, con una lama in metallo resistente e un manico lungo in legno. Adatta a scavare, sollevare e spostare materiali come terra, sabbia o neve.", "OggettoRaccoglibile", null, 6, null);
     TabellaOggetti.inserisciOggetto("Lanciafiamme", "Questo lanciafiamme è un'arma massiccia e rudimentale, con una canna lunga e nera.\nIl serbatoio sul retro è coperto di ghiaccio, ma dalla sua estremità spunta una fiamma vivida e minacciosa che emette un forte odore di benzina.", "Arma", null, 10, 10);
     TabellaOggetti.inserisciOggetto("Analgesico", " Questo analgesico ipodermico è un farmaco contenuto in un piccolo flacone di vetro trasparente, con un'etichetta che riporta il nome del farmaco e le istruzioni per l'uso.\nIl liquido all'interno è limpido e leggermente viscoso. Il tappo è sigillato con cura per mantenere l'integrità del farmaco, essenziale per alleviare il dolore di chiunque.", "OggettoRaccoglibile", null, 8, null);
     TabellaOggetti.inserisciOggetto("Poltiglia", "", "OggettoNonRaccoglibile", null, 6, null);
     TabellaOggetti.inserisciOggetto("Terrario", "Dei tre terrari nella stanza, quello al centro attira subito la tua attenzione. Mentre i terrari ai lati conservano ancora qualche traccia di vegetazione, il terrario centrale è completamente spoglio. Non vi cresce nulla all'interno, e il terreno sembra strano, smosso.\n" +
             "Ti avvicini per osservare meglio: la terra appare inconsistente, come se fosse stata dissodata di recente. {Mi chiedo perché qualcuno abbia dissodato il terreno senza poi aver piantato nulla. Magari dal diario del botanico potrò trovare le mie risposte}\n", "OggettoNonRaccoglibile", null, 10, null);
     TabellaOggetti.inserisciOggetto("Ricetrasmettitore", "Un dispositivo portatile con pochi pulsanti ed un'antenna. Mostra le frequenze su un piccolo schermo ed emette un debole ronzio quando acceso. Tra le frequenze attive, solo una è operativa: quella collegata alla radiotrasmittente dell’elicottero. {Posso usarla solo per comunicare con Mugs (Il pilota)}", "OggettoAttivabile", null, null, null);
    }

    /**
     * Popola la tabella dei personaggi con tutti i personaggi del gioco.
     * <p>
     * Inizializza:
     * <ul>
     *   <li>Personaggi fissi nelle stanze</li>
     *   <li>Personaggi speciali con logiche particolari</li>
     *   <li>La creatura antagonista</li>
     * </ul>
     * Alcuni personaggi hanno ID stanza null per indicare che appaiono in momenti speciali.
     */
    public static void inizializzaPersonaggi(){
      //Personaggio #1
      TabellaPersonaggi.inserisciPersonaggio("Guardia", 3);
      // Personaggio #2
      TabellaPersonaggi.inserisciPersonaggio("Sconosciuto", 14);
      //Personaggio #3
      TabellaPersonaggi.inserisciPersonaggio("Sips", null);
      // Creatura #4
      TabellaPersonaggi.inserisciPersonaggio("Canide", 6);
      //Personaggio #5
      TabellaPersonaggi.inserisciPersonaggio("Gasly", 15);
      //Personaggio #6
      TabellaPersonaggi.inserisciPersonaggio("Mugs", null);
    }

    /**
     * Popola la tabella delle interazioni dei personaggi con tutte le possibili risposte NPC.
     * <p>
     * Per ogni personaggio definisce:
     * <ul>
     *   <li>Risposte ai dialoghi del giocatore</li>
     *   <li>Reazioni in base allo stato di gioco</li>
     *   <li>Brani narrativi</li>
     * </ul>
     * Le interazioni sono organizzate in "nodi" dialogici.
     */
    public static void inizializzaInterazioniPersonaggi() {

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(1, "«Siiipsss...»\n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(2, "«Basta!!!» \n[BAMM!!!]\n\n -Un altro proiettile fuoriesce dalla porta...\n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(2, "«Solo dopo averti centrato in testa!!» \n[BAMM!!!]\n\n -Un altro proiettile fuoriesce dalla porta.\n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(2, "«…»\n" +
              "«Tu…»\n" +
              "(pausa)\n" +
              "«…ma come faccio a sapere che non sei uno di QUELLI?»", null, null);
      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(2,
                      "«…»\n«Sono io... Matthew Gasly. Ho inviato io il segnale di soccorso.\nMa non posso lasciarti entrare... non posso rischiare.\nQuesta COSA è intelligente, astuta. " +
                      "Ma se sei davvero chi dici di\nessere e vuoi aiutarmi, allora devi fare qualcosa per me.\n" +
                      "Siamo stati noi a portarla qui.. siamo stati colpiti dall'interno. Ha preso i miei compagni.\nQuesto organismo è qualcosa di mai visto: si rigenera, si adatta... è praticamente invulnerabile.\n" +
                      "Eccetto per una cosa… il CALORE. È l’unico modo per annientarla.\n" +
                      "Da qualche parte nell’avamposto deve esserci un LANCIAFIAMME. Trovalo e torna da me... ma sbrigati, non c’è molto tempo.»\n" +
                      "\n" +
                      "Mentre ti allontani, la sua voce ti ferma ancora una volta:\n" +
                      "\n" +
                      "«Aspetta! Io... io sono ferito. Non riesco a muovermi. Ho bisogno di MEDICINE.\n Vai in infermeria, portami qualunque cosa che riesci a trovare…»\""
, null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(4, "GRRRRROOOOAAAARRRR ! ! !  ~wrrrrhhhhhh~ WROOOOFFF-WROOOOFFF", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(4, "RROOOOAAARRRRRRRRRRRR ", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(5, "«...»\n\n- Le tue parole sembrano non avere effetto… Gasly è impassibile e la pistola è ancora puntata su di te.", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(5, "«...»\n\n- Gasly osserva a lungo l’analgesico che gli protendi mantenendo sempre una certa distanza; ad un certo punto abbassa la pistola e, con uno scatto, lo prende.\nAnche se ha abbassato la pistola, qualcosa ti dice che è ancora chiaramente diffidente nei tuoi confronti." +
              " Infine ti chiede:\n\n" +
              "«Chi altro hai trovato? Quanti sono i superstiti?»", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(5, "«...»\n\n- Travolto dalla rabbia, getta via il barattolo di analgesico, dispargendo tante piccole pastiglie per tutta la stanza, ritornando a premere sulla sua domanda. \n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(5, "«Non sei tu a decidere cosa posso e non posso sapere!!! Adesso devi dirmi dove sono gli altri.»", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(5, "«...»\n\n- Gasly è visibilmente sconvolto, quasi sull’orlo di una crisi di nervi.\nLa nocche della mano diventano man mano più bianche a forza di stringere l’impugnatura della pistola, anche se non te la tiene più puntata contro.\n\n" +
              "«Non... non posso essere l'unico. Oddio, Sips… oh, Sips! Io non posso lasciarlo qui! Ci devono essere altri là fuori, devono, DEVONO! Torna a cercarli!!»", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(5, "«...»\n\n- Gasly, affranto e scosso da tutto quello che gli è capitato, sembra di aver finalmente accettato la realtà dei fatti: Ormai non c’è più nulla che lui possa fare.\nNon bisogna sfidare la sorte.\nFinché ne ha la possibilità, deve mettersi in salvo.", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(5, "«...»\n\n- Gasly è sconvolto. Tormentato dal dubbio e incapace di accettare la possibilità di lasciare qualcuno indietro, ti chiede:\n\n" +
              "«E se ti sbagliassi? E se lui… e se altri fossero ancora vivi? Non posso vivere con questo dubbio.»", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(3,"«Ero... ero nascosto in uno dei vani della cucina.»\n\n- Mentre pronuncia queste parole, il suo corpo è completamente madido di sudore.\n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(3,"«Ero lì con lui, ma quando ho sentito dei passi avvicinarsi dall'ingresso, sono scappato a nascondermi. Non volevo lasciarlo da solo, ma quella... COSA, lo aveva ridotto male. Non potevo trascinarlo con me.»\n\n- Mentre nelle sue parole c’è sia dolore che rimorso, nei suoi occhi invece non ce n'è traccia. Sembra quasi che stia simulando quel dolore. Questo distacco glaciale riesce a metterti ancora più in allarme.\n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(3,"«Il quaderno? Sì... sì, il quaderno. Clara... il suo nome è Clara.»\n\n- Anche se ha risposto correttamente, c'è qualcosa di sbagliato nel modo in cui lo fa.\nÈ come se stesse recitando una parte.\n\n{Non c'è nulla che mi garantisca che non abbia letto anche lui il contenuto del quaderno dopo averlo ucciso.}\n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(3,"«Era uno di loro! Mi trovavo nella serra e all'improvviso... ho visto me stesso! Voleva uccidermi, e ci sarebbe riuscito se non avessi avuto le mie cesoie! Devi credermi!»\n\n- Si volta verso Gasly, disperato.\n\nSips:\n«Diglielo anche tu, Matthew!»\n\n- Gasly, improvvisamente, cambia espressione. Il sorriso di sollievo che aveva dopo aver ritrovato il suo vecchio amico si spegne all'istante, trasformandosi in un'espressione di dubbio; lo stesso dubbio che adesso nutrite entrambi...\n\nLa sua voce si abbassa, come se stesse parlando solo con se stesso:\n'Tu... non mi chiami mai Matthew…'", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(3,"«no-No-NO!, aspetta...ASPETTA!!!!»", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(6,"«...»\n\nAncora silenzio...\nI suoi occhi vitrei rimangono fissi sull’orizzonte, come se stesse guardando qualcosa che tu non puoi vedere.\nIl sudore ora gli cola sul collo, formando piccole chiazze scure sul colletto della giacca.\nNon una parola, non un accenno che abbia sentito le tue domande.\nSolo il sibilo del vento fuori e il ronzio delle eliche che riempiono l’aria.\n", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(6,"«...»\n\nMugs rimane immobile, le mani strette sul volante dell’elicottero.\nUna goccia di sudore scivola lungo la sua tempia, ma non mostra alcuna reazione alle tue parole.\nNon ti guarda, non si muove. L’unico segno di vita è il lieve tremore delle sue narici mentre respira, lento e pesante.", null, null);

      TabellaInterazioniPersonaggi.inserisciInterazionePersonaggio(6,"«...»\n\nNon c’è risposta...\nLe sue labbra rimangono serrate, quasi come se stesse trattenendo qualcosa.\nTi sembra che il suo respiro sia diventato più rapido.\nQualcosa nella sua immobilità è profondamente sbagliato…\n" +
                "Lentamente, dentro di te, inizia a svelarsi un dubbio…", null, null);

    }


    /**
     * Popola la tabella delle interazioni del giocatore con tutte le possibili scelte dialogiche.
     * <p>
     * Per ogni personaggio definisce:
     * <ul>
     *   <li>Opzioni di dialogo disponibili</li>
     *   <li>Azioni speciali</li>
     *   <li>Risposte a eventi specifici</li>
     * </ul>
     * Le interazioni sono organizzate in "nodi" dialogici.
     */
    public static void inizializzaInterazioniGiocatore() {

      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(1, 1, 1, "'Cos’è successo qui?'\n" );
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(1, 1, 1, "'Chi ti ha fatto questo?'\n" );
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(1, 1, 1, "'Ci sono altri superstiti?'\n" );
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(2, 1, null , "Spiega chi sei.\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(2, 1, null , "Chiedi di smettere di sparare.\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(2, 1, null , "Spiega perché sei venuto.\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(2, 2, null, "'Non puoi. Devi soltanto fidarti di me.'\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(2, 2, null , "'Se lo fossi di certo non starei qui a parlarti...'\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(4, 1, null, "Fermo lí...FERMO!!! NON TI AVVICINARE!" );
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(4, 1, null, "Via... VIA!!!" );
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(4,1, null, "ALLONTANATI!!!");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 1,null,"'Gasly, calmati. Metti giù la pistola, non sono tuo nemico.'");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 1,null, "'Non ho intenzione di farti del male. Guarda. [Tiri fuori l’ analgesico] Ecco, prendi.'");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 2,null, "'No, Gasly… Temo che tu sia l’unico superstite di questo avamposto.'");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 2,null, "Gasly, devi concentrarti. Prendi l’analgesico, ti farà stare meglio…");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 2,null, "Credimi, meno sai e meglio è…");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 2,null, "Mi dispiace tanto… ho trovato qualcuno ma, nessuno di vivo");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 3,null, "Non ci resta più tempo per cercare, Gasly. Se restiamo, finiremo come loro. C’è un elicottero che ci aspetta.");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 3,null, "Senti ci ho quasi rimesso la pelle per trovarti ed è un miracolo se siamo entrambi ancora vivi. Non giochiamo con la sorte. DOBBIAMO. ANDARE. ADESSO.");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 4,null, "‘Non abbiamo altra scelta. Se ci fossero stati superstiti, li avrei trovati. Sono sicuro che i tuoi amici non vorrebbero che rischiassi la tua vita per loro’\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(5, 4,null, "Senti un pò, non ho alcuna intenzione di morire qui. Quindi, o vieni adesso, o me ne vado senza di te. Che ne pensi di QUESTO dubbio invece?'\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(3, 1,null, "'Ho setacciato ogni angolo dell'avamposto, ma di te nessuna traccia. Dove ti nascondevi?'");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(3,1,null,"'Prima di morire, una delle guardie ha pronunciato il tuo nome, 'Sips'.  Hai idea del perché?'");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(3,1,null, "'Nella serra ho trovato il tuo quaderno... ho sfogliato qualche pagina. Ora dimmi, come si chiama tua moglie?'");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(3,1,null, "'Nella serra ho scoperto un corpo... senza testa. Sulla giacca c'era il nome 'Sips'. Come me lo spieghi?\'");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(3,1,null, "Usa il lanciafiamme");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(6,1,null, "'Mugs! Perché accidenti non hai risposto al ricetrasmettitore? Ti ho chiamato più e più volte!'\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(6,1,null, "'Mugs! Perché non sei entrato a darmi una mano? Sono stato via per un pezzo! Avrai sentito i miei colpi di pistola, no?!'\n");
      TabellaInterazioniGiocatore.inserisciInterazioneGiocatore(6,1,null, "'Mugs? Tutto bene amico…'\n");
    }


    /**
     * Assegna a ogni risposta dei personaggi il nodo dialogico successivo.
     * <p>
     * Crea la struttura ad albero dei dialoghi specificando per ogni risposta:
     * <ul>
     *   <li>Quale nodo attivare successivamente</li>
     *   <li>Come evolve la conversazione</li>
     * </ul>
     */
    public static void assegnaProssimoNodoAPersonaggi(){

       TabellaInterazioniPersonaggi.assegnaProssimoNodo(1, 1);

       //Personaggio Gasly (Fuori dalla stanza)
       TabellaInterazioniPersonaggi.assegnaProssimoNodo(2, 1);
       TabellaInterazioniPersonaggi.assegnaProssimoNodo(3, 1);
       TabellaInterazioniPersonaggi.assegnaProssimoNodo(4, 2);


       //Personaggio Creatura Canide
       TabellaInterazioniPersonaggi.assegnaProssimoNodo(6,1);
       TabellaInterazioniPersonaggi.assegnaProssimoNodo(7,1);


       //Personaggio Gasly (DENTRO LA STANZA)
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(8, 1);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(9, 2);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(10, 2);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(11, 2);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(12,3);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(14,4);


        //Personaggio Sips
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(15, 1);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(16, 1);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(17, 1);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(18, 1);

        //Personaggio Mugs
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(20, 1);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(21, 1);
        TabellaInterazioniPersonaggi.assegnaProssimoNodo(21, 1);
    }

    /**
     * Collega ogni opzione di dialogo del giocatore alla corrispondente risposta del personaggio.
     * <p>
     * Stabilisce le relazioni tra:
     * <ul>
     *   <li>Scelte del giocatore</li>
     *   <li>Reazioni degli NPC</li>
     *   <li>Progresso della storia</li>
     * </ul>
     */
    public static void assegnaRisposteAGiocatore(){

      TabellaInterazioniGiocatore.assegnaDialoghi(1,1);
      TabellaInterazioniGiocatore.assegnaDialoghi(2,1);
      TabellaInterazioniGiocatore.assegnaDialoghi(3,1);
      TabellaInterazioniGiocatore.assegnaDialoghi(4,2);
      TabellaInterazioniGiocatore.assegnaDialoghi(5,3);
      TabellaInterazioniGiocatore.assegnaDialoghi(6,4);
      TabellaInterazioniGiocatore.assegnaDialoghi(7,5);
      TabellaInterazioniGiocatore.assegnaDialoghi(8,5);

      //PER Canide
      TabellaInterazioniGiocatore.assegnaDialoghi(9,6);
      TabellaInterazioniGiocatore.assegnaDialoghi(10,7);
      TabellaInterazioniGiocatore.assegnaDialoghi(11,6);

      //PER GASLY (DENTRO LA STANZA)
        TabellaInterazioniGiocatore.assegnaDialoghi(12, 8);
        TabellaInterazioniGiocatore.assegnaDialoghi(13, 9);
        TabellaInterazioniGiocatore.assegnaDialoghi(14, 12);
        TabellaInterazioniGiocatore.assegnaDialoghi(15, 10);
        TabellaInterazioniGiocatore.assegnaDialoghi( 16,11);
        TabellaInterazioniGiocatore.assegnaDialoghi( 17, 12);
        TabellaInterazioniGiocatore.assegnaDialoghi( 18, 14);
        TabellaInterazioniGiocatore.assegnaDialoghi( 19, 13);
        TabellaInterazioniGiocatore.assegnaDialoghi( 20, 13);
        TabellaInterazioniGiocatore.assegnaDialoghi( 21,13);

        //PER SIPS
        TabellaInterazioniGiocatore.assegnaDialoghi(22,15);
        TabellaInterazioniGiocatore.assegnaDialoghi(23,16);
        TabellaInterazioniGiocatore.assegnaDialoghi(24,17);
        TabellaInterazioniGiocatore.assegnaDialoghi(25,18);
        TabellaInterazioniGiocatore.assegnaDialoghi(26,19);

        //PER MUGS
        TabellaInterazioniGiocatore.assegnaDialoghi(27,20);
        TabellaInterazioniGiocatore.assegnaDialoghi(28,21);
        TabellaInterazioniGiocatore.assegnaDialoghi(29,22);
    }

    /**
     * Popola la tabella degli alias per gli oggetti, permettendo nomi alternativi.
     * <p>
     * Per ogni oggetto definisce:
     * <ul>
     *   <li>Nomi alternativi riconosciuti dal parser</li>
     *   <li>Varianti grammaticali</li>
     *   <li>Sinonimi</li>
     * </ul>
     */
  public static void inizializzaAliasesOggetti(){

      TabellaAliasOggetti.inserisciAliasOggetto(1, "pistola");
      TabellaAliasOggetti.inserisciAliasOggetto(2, "dossier");
      TabellaAliasOggetti.inserisciAliasOggetto(2, "documento");
      TabellaAliasOggetti.inserisciAliasOggetto(3, "diario");
      TabellaAliasOggetti.inserisciAliasOggetto(3, "quaderno");
      TabellaAliasOggetti.inserisciAliasOggetto(3, "quadernetto");
      TabellaAliasOggetti.inserisciAliasOggetto(4, "fotografia");
      TabellaAliasOggetti.inserisciAliasOggetto(4, "foto");
      TabellaAliasOggetti.inserisciAliasOggetto(5, "torcia");
      TabellaAliasOggetti.inserisciAliasOggetto(6, "lista");
      TabellaAliasOggetti.inserisciAliasOggetto(7, "pala");
      TabellaAliasOggetti.inserisciAliasOggetto(7, "vanga");
      TabellaAliasOggetti.inserisciAliasOggetto(8, "lanciafiamme");
      TabellaAliasOggetti.inserisciAliasOggetto(9, "analgesico");
      TabellaAliasOggetti.inserisciAliasOggetto(9, "farmaco");
      TabellaAliasOggetti.inserisciAliasOggetto(9, "flacone");
      TabellaAliasOggetti.inserisciAliasOggetto(10, "poltiglia");
      TabellaAliasOggetti.inserisciAliasOggetto(10, "Ammasso");
      TabellaAliasOggetti.inserisciAliasOggetto(11, "terrario");
      TabellaAliasOggetti.inserisciAliasOggetto(11, "terrari");
      TabellaAliasOggetti.inserisciAliasOggetto(11, "Terrario");
      TabellaAliasOggetti.inserisciAliasOggetto(12, "ricetrasmettitore");
      TabellaAliasOggetti.inserisciAliasOggetto(12, "radio");

  }

    /**
     * Popola la tabella degli alias per i personaggi, permettendo nomi alternativi.
     * <p>
     * Per ogni personaggio definisce:
     * <ul>
     *   <li>Nomi alternativi riconosciuti dal parser</li>
     *   <li>Titoli e descrizioni</li>
     *   <li>Varianti grammaticali</li>
     * </ul>
     */
    public static void inizializzaAliasesPersonaggi() {
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(1, "uomo");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(1, "sorvegliante");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(1, "guardia");

        TabellaAliasPersonaggi.inserisciAliasPersonaggio(2, "sconosciuto");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(2, "superstite");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(2, "uomo");


        TabellaAliasPersonaggi.inserisciAliasPersonaggio(4, "cane");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(4, "cosa");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(4, "creatura");


        TabellaAliasPersonaggi.inserisciAliasPersonaggio(5, "Gasly");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(5, "Dottore");
        TabellaAliasPersonaggi.inserisciAliasPersonaggio(5, "gasly");

    }


}
