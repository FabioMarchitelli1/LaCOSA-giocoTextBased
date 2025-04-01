package di.lacosa.parser;

import di.lacosa.tipi.*;

import java.util.List;
import java.util.Set;

/**
 * Classe responsabile dell'analisi dei comandi testuali inseriti dall'utente.
 * <p>
 * Il parser svolge le seguenti funzioni:
 * <ul>
 *   <li>Divide l'input in token rimuovendo le stopwords</li>
 *   <li>Identifica il comando principale</li>
 *   <li>Riconosce oggetti, direzioni e personaggi menzionati</li>
 *   <li>Costruisce un oggetto ParserOutput con le informazioni estratte</li>
 * </ul>
 *
 * Supporta comandi complessi come combinazioni di oggetti e interazioni con personaggi.
 * @author fabioMarchitelli
 */
public class Parser {

    private final Set<String> stopwords;

    /**
     * Costruttore del parser.
     *
     * @param stopwords insieme di parole da ignorare durante l'analisi (articoli, preposizioni, ecc.)
     */
    public Parser(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    /**
     * Ricerca un comando nella lista dei comandi disponibili.
     * <p>
     * Confronta il token con i nomi ufficiali dei comandi e i loro alias.
     *
     * @param token la parola da cercare
     * @param comandi lista di tutti i comandi disponibili nel gioco
     * @return l'indice del comando trovato, o -1 se non trovato
     */
    private int ricercaComando(String token, List<Comando> comandi) {

        for (int i = 0; i < comandi.size(); i++) {
            if (comandi.get(i).getNome().equals(token) || comandi.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Ricerca un personaggio tra quelli disponibili.
     *
     * @param token il nome o alias da cercare
     * @param interlocutori insieme dei personaggi presenti nella stanza corrente
     * @return il personaggio trovato o null se non esiste
     */
    private Personaggio ricercaInterlocutore(String token, Set<Personaggio> interlocutori) {

        for (Personaggio interlocutore : interlocutori) {
            if (interlocutore.getNome().equals(token) || interlocutore.getAliases().contains(token)) {
                return interlocutore;
            }
        }
        return null;
    }

    /**
     * Ricerca un oggetto tra quelli disponibili.
     *
     * @param token il nome o alias da cercare
     * @param oggetti insieme degli oggetti disponibili (nella stanza o nell'inventario)
     * @return l'oggetto trovato o null se non esiste
     */
    private Oggetto ricercaOggetto(String token, Set<Oggetto> oggetti) {
        for (Oggetto oggetto : oggetti) {
            if (oggetto.getNome().equals(token) || oggetto.getAlias().contains(token)) {
                return oggetto;
            }
        }
        return null;
    }


    /**
     * Analizza il comando testuale e restituisce una struttura con le informazioni estratte.
     * <p>
     * Logica di funzionamento:
     * 1. Divide l'input in token rimuovendo le stopwords
     * 2. Identifica il comando principale
     * 3. Gestisce casi speciali (movimento, dialogo, osservazione)
     * 4. Cerca oggetti e personaggi menzionati
     * 5. Costruisce l'output appropriato
     *
     * @param comando la stringa inserita dall'utente (es: "prendi torcia")
     * @param listaComandi tutti i comandi disponibili nel gioco
     * @param listaOggettiInStanza oggetti presenti nella stanza corrente
     * @param inventario oggetti posseduti dal giocatore
     * @param interlocutoriInStanzaCorrente personaggi presenti nella stanza
     * @return ParserOutput con le informazioni estratte, o null per comando vuoto/non valido
     */
    public ParserOutput parse(String comando, List<Comando> listaComandi, Set<Oggetto> listaOggettiInStanza, Set<Oggetto> inventario, Set<Personaggio> interlocutoriInStanzaCorrente) {

        // Fase 1: Pulizia e tokenizzazione dell'input
        List<String> tokens = Utils.elaboraStringa(comando, stopwords);
        if (tokens.isEmpty()) {
            return null;
        }

        // Fase 2: Identificazione del comando principale
        int ic = ricercaComando(tokens.get(0), listaComandi);
        if (ic == -1) {
            return new ParserOutput(null);
        }

        // Fase 3: Gestione comandi speciali
        // Caso movimento (es: "vai nord")
        if (controlloTipoComando(ic, TipoComando.VAI, listaComandi) && (tokens.size() == 2)) {
            return gestisciMovimento(listaComandi.get(ic), tokens.get(1));
        }

        // In your Parser.java, add this special case for PARLA command
        if (listaComandi.get(ic).getTipo() == TipoComando.PARLA) {
            return gestisciDialogo(listaComandi.get(ic), tokens, interlocutoriInStanzaCorrente);
        }


        //Questo permette al gioco di non fare un comando complesso per osserva... solo il singolo verbo
        if (listaComandi.get(ic).getTipo() == TipoComando.OSSERVA && tokens.size() > 1) {
            return new ParserOutput(listaComandi.get(ic), PuntoCardinale.INVALIDO);
        }


        // Gestione speciale per SPARA
        if (listaComandi.get(ic).getTipo() == TipoComando.SPARA) {
            return gestisciSparo(listaComandi.get(ic), tokens, interlocutoriInStanzaCorrente);
        }


        // Fase 4: Gestione comandi generici con oggetti
        // Comando semplice senza oggetti (es: "aiuto")
        if (tokens.size() == 1) {
            return new ParserOutput(listaComandi.get(ic));
        }

        Oggetto oggettoInStanza = null;
        Oggetto oggettoInInvetario = null;
        Personaggio interlocutore = null;

        // Controlla se il secondo token corrisponde a un oggetto nella stanza
        oggettoInStanza = ricercaOggetto(tokens.get(1), listaOggettiInStanza);

        //  ci sono più di due token, controlla il terzo token
        if (oggettoInStanza != null && tokens.size() > 2) {
            oggettoInStanza = ricercaOggetto(tokens.get(2), listaOggettiInStanza);
        }

        // Se non è stato trovato nella stanza, controlla l'inventario
        if (oggettoInStanza == null) {
            oggettoInInvetario = ricercaOggetto(tokens.get(1), inventario);

            // Se non è stato trovato nel secondo token, controlla il terzo
            if (oggettoInInvetario == null && tokens.size() > 2) {
                oggettoInInvetario = ricercaOggetto(tokens.get(2), inventario);
            }
        }


        // controlla se il secondo token è un interlocutore
        interlocutore = ricercaInterlocutore(tokens.get(1), interlocutoriInStanzaCorrente);

        if (tokens.size() > 2 && interlocutore == null) {
            interlocutore = ricercaInterlocutore(tokens.get(2), interlocutoriInStanzaCorrente);
        }


        // Fase 5: Costruzione dell'output
        return costruisciOutput(listaComandi.get(ic), oggettoInStanza, oggettoInInvetario, interlocutore);
    }



    /**
     * Verifica se il comando nella posizione specificata è di un certo tipo.
     *
     * @param posizioneComandoInLista indice del comando da verificare
     * @param tipoComando tipo da confrontare
     * @param comandi lista dei comandi disponibili
     * @return true se il comando è del tipo specificato, false altrimenti
     */
    private boolean controlloTipoComando(int posizioneComandoInLista, TipoComando tipoComando, List<Comando> comandi) {
        if(comandi.get(posizioneComandoInLista).getTipo().equals(tipoComando)){
            return true;
        }
        return false;
    }


    /**
     * Gestisce i comandi di movimento convertendo la direzione testuale in un PuntoCardinale.
     *
     * @param comando il comando di movimento (es: "vai")
     * @param direzioneToken la direzione specificata dall'utente (es: "nord")
     * @return ParserOutput configurato per il movimento
     */
    private ParserOutput gestisciMovimento(Comando comando, String direzioneToken) {
        String token = direzioneToken.toLowerCase();
        switch (token) {
            case "nord":
                return new ParserOutput(comando, PuntoCardinale.NORD);
            case "sud":
                return new ParserOutput(comando, PuntoCardinale.SUD);
            case "est":
                return new ParserOutput(comando, PuntoCardinale.EST);
            case "ovest":
                return new ParserOutput(comando, PuntoCardinale.OVEST);
            default:
                return new ParserOutput(comando, PuntoCardinale.INVALIDO);
        }
    }


    /**
     * Gestisce i comandi di dialogo con i personaggi.
     *
     * @param comando il comando di dialogo (es: "parla")
     * @param tokens la lista di token del comando
     * @param interlocutori l'insieme dei personaggi presenti nella stanza
     * @return ParserOutput configurato per il dialogo
     */
    private ParserOutput gestisciDialogo(Comando comando, List<String> tokens,
                                         Set<Personaggio> interlocutori) {
        // Caso "parla" senza target
        if (tokens.size() == 1) {
            return new ParserOutput(comando, PuntoCardinale.INVALIDO);
        }

        // Cerca il personaggio target
        Personaggio interlocutore = null;
        for (int i = 1; i < tokens.size(); i++) {
            interlocutore = ricercaInterlocutore(tokens.get(i), interlocutori);
            if (interlocutore != null) break;
        }

        return new ParserOutput(comando, interlocutore);
    }


    /**
     * Gestisce i comandi di sparo/attacco.
     *
     * @param comando il comando di sparo (es: "spara")
     * @param tokens la lista di token del comando
     * @param bersagli l'insieme dei personaggi attaccabili
     * @return ParserOutput configurato per l'azione di sparo
     */
    private ParserOutput gestisciSparo(Comando comando, List<String> tokens,
                                       Set<Personaggio> bersagli) {
        // Caso "spara" senza target
        if (tokens.size() == 1) {
            return new ParserOutput(comando);
        }

        // Cerca il bersaglio tra gli interlocutori
        Personaggio target = null;
        for (int i = 1; i < tokens.size(); i++) {
            target = ricercaInterlocutore(tokens.get(i), bersagli);
            if (target != null) break;
        }

        return new ParserOutput(comando, target);
    }


    /**
     * Costruisce l'oggetto ParserOutput in base agli elementi trovati.
     *
     * @param comando il comando principale
     * @param oggettoStanza oggetto trovato nella stanza (può essere null)
     * @param oggettoInventario oggetto trovato nell'inventario (può essere null)
     * @param interlocutore personaggio trovato (può essere null)
     * @return ParserOutput configurato in base agli elementi trovati
     */
    private ParserOutput costruisciOutput(Comando comando, Oggetto oggettoStanza,
                                          Oggetto oggettoInventario, Personaggio interlocutore) {
        // Caso comando con due oggetti (es: "combina chiave con porta")
        if (oggettoStanza != null && oggettoInventario != null) {
            return new ParserOutput(comando, oggettoStanza, oggettoInventario);
        }
        // Caso comando con oggetto nella stanza
        else if (oggettoStanza != null) {
            return new ParserOutput(comando, oggettoStanza, null);
        }
        // Caso comando con oggetto nell'inventario
        else if (oggettoInventario != null) {
            return new ParserOutput(comando, null, oggettoInventario);
        }
        // Caso comando con interlocutore
        else if (interlocutore != null) {
            return new ParserOutput(comando, interlocutore);
        }
        // Caso comando semplice senza oggetti
        else {
            return new ParserOutput(comando);
        }
    }
}
