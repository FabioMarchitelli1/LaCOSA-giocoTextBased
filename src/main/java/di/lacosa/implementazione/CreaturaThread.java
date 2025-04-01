package di.lacosa.implementazione;
import di.lacosa.DescrizioneGioco;
import di.lacosa.ui.InterfacciaGioco;

/**
 * Thread che gestisce il comportamento di una creatura antagonista nel gioco.
 * <p>
 * Questo thread si occupa di:
 * <ul>
 *   <li>Simulare l'avvicinamento progressivo della creatura al giocatore</li>
 *   <li>Mostrare messaggi di avvertimento all'utente a intervalli regolari</li>
 *   <li>Gestire la sconfitta della creatura</li>
 * </ul>
 * Implementa {@link Runnable} per essere eseguito in un thread separato.
 */
public class CreaturaThread implements Runnable {

    private boolean creaturaSconfitta = false;    //Flag che indica se la creatura è stata sconfitta
    private DescrizioneGioco descrizioneGioco;   //Riferimento alla descrizione del gioco
    private InterfacciaGioco interfacciaGioco;   //Riferimento all'interfaccia grafica del gioco

    /**
     * Costruttore della classe CreaturaThread.
     *
     * @param description La descrizione del gioco corrente
     * @param interfacciaGioco L'interfaccia grafica per mostrare i messaggi
     */
    public CreaturaThread(DescrizioneGioco description, InterfacciaGioco interfacciaGioco) {
        this.descrizioneGioco = description;
        this.interfacciaGioco = interfacciaGioco;
    }

    /**
     * Metodo principale del thread.
     * <p>
     * Esegue un loop che mostra messaggi di avvertimento ogni 10 secondi
     * finché la creatura non viene sconfitta. Se interrotto, mostra
     * il messaggio di sconfitta della creatura.
     */
    public void run() {
        try {
            while (!creaturaSconfitta) {

                Thread.sleep(10000);
                interfacciaGioco.scriviInAreaDiTesto("\n\n!!QUELLA COSA si sta avvicinando a te… {Se non agisco adesso, sarà la mia fine…}!!\n\n?>" +
                        "\n\n");
                Thread.sleep(10000);
                interfacciaGioco.scriviInAreaDiTesto("\n\n!!Man mano che si avvicina, il ringhio della COSA si fa sempre più intenso e crudele…{Devo fare qualcosa!!!}!!\n\n?>" +
                        "\n\n");
                Thread.sleep(10000);
                interfacciaGioco.scriviInAreaDiTesto("\n\n!!Vedi la bava gocciolare dalle sue fauci aperte. {Devo usare la pistola!!!}!!\n\n?>" +
                        "\n\n");
            }
        } catch (InterruptedException e) {
            interfacciaGioco.scriviInAreaDiTesto("\nLa creatura giace a terra, il pericolo è passato.\n");

            // Quando la creatura viene sconfitta, aggiorna l'osservazione della stanza
            descrizioneGioco.getStanzaCorrente().attivaOsservazioneAggiornata();
        }
    }

    /**
     * Metodo per segnalare la sconfitta della creatura.
     * <p>
     * Interrompe il loop dei messaggi di avvertimento e causa
     * la terminazione del thread.
     */
    public void sconfiggiCreatura() {
        creaturaSconfitta = true;
    }
}