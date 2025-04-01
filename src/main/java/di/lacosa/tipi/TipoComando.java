package di.lacosa.tipi;

/**
 * Enum che rappresenta tutti i tipi di comandi disponibili nel gioco.
 * Definisce le azioni che il giocatore può eseguire durante la partita.
 *
 * @author fabioMarchitelli
 */
public enum TipoComando {

    /**
     * Mostra gli oggetti nell'inventario del giocatore
     */
    INVENTARIO,

    /**
     * Usa un oggetto dell'inventario
     */
    USA,

    /**
     * Osserva la stanza
     */
    OSSERVA,

    /**
     * Attiva un dispositivo o meccanismo
     */
    ATTIVA,

    /**
     * Disattiva un dispositivo o meccanismo
     */
    DISATTIVA,

    /**
     * Prende un oggetto dall'ambiente e lo aggiunge all'inventario
     */
    PRENDI,

    /**
     * Interagisce con un personaggio
     */
    PARLA,

    /**
     * Si sposta in un'altra stanza (es: "vai nord")
     */
    VAI,

    /**
     * Termina la partita o esce dal gioco
     */
    FINE,

    /**
     * Mostra le istruzioni del gioco
     */
    AIUTO,

    /**
     * Esamina approfonditamente un oggetto
     */
    ESAMINA,

    /**
     * Legge documenti e oggetti
     */
    LEGGI,

    /**
     * Sparare un'arma nell'inventario
     */
    SPARA,

    /**
     * Salva la partita corrente
     */
    SALVA
}
