package di.lacosa.tipi;

/**
 * Enum che rappresenta le possibili motivazioni per un aggiornamento della descrizione di una stanza nel gioco.
 * Utilizzato per notificare gli osservatori circa specifici cambiamenti di stato.
 *
 * @author fabioMarchitelli
 */
public enum MotivazioneAggDescrizione {

        /**
         * Indica che la stanza non avra un cambio di descrizione
         */
        PRIVA_DI_AGGIORNAMENTO,

        /**
         * Indica che l'aggiornamento di descrizione è causato dall'accensione o spegnimento
         * della torcia nel gioco.
         */
        ACCENSIONE_TORCIA,

        /**
         * Indica che l'aggiornamento è stato generato da un evento specifico
         * all'interno della logica di gioco.
         */
        MODIFICATO_DA_EVENTO
}
