package di.lacosa.contesti;

import di.lacosa.parser.ParserOutput;
import di.lacosa.DescrizioneGioco;
import di.lacosa.Observer;
import di.lacosa.tipi.Oggetto;
import di.lacosa.tipi.TipoComando;
import di.lacosa.tipi.Stanza;

/**
 * Classe che gestisce il comando "USA" per l'interazione con oggetti nell'inventario.
 * Implementa l'interfaccia Observer per reagire ai comandi di utilizzo oggetti.
 * Gestisce le logiche specifiche per ogni oggetto utilizzabile nel gioco.
 */
public class AggiornaUsa implements Observer {

    /**
     * Metodo principale che gestisce il comando USA.
     *
     * @param description il contesto di gioco corrente
     * @param parserOutput l'output del parser contenente il comando
     * @return messaggio descrittivo dell'esito dell'utilizzo o stringa vuota se il comando non è USA
     */
    @Override
    public String update(DescrizioneGioco description, ParserOutput parserOutput) {
        StringBuilder msg = new StringBuilder();
        Stanza stanzaCorrente = description.getStanzaCorrente();
        Oggetto oggettoUsato = parserOutput.getOggettoInv();
        if (parserOutput.getComando().getTipo() == TipoComando.USA) {
            if (oggettoUsato != null) {
                return gestisciComandoUsa(description, oggettoUsato, stanzaCorrente);
            } else {
                return "Oggetto da usare non identificato.";
            }
        }
        return "";
    }

    /**
     * Gestisce la logica specifica per ogni oggetto utilizzabile.
     *
     * @param description il contesto di gioco
     * @param oggettoUsato l'oggetto che si sta tentando di usare
     * @param stanzaCorrente la stanza corrente del giocatore
     * @return messaggio descrittivo dell'azione compiuta
     */
    private String gestisciComandoUsa(DescrizioneGioco description, Oggetto oggettoUsato, Stanza stanzaCorrente) {
        StringBuilder msg = new StringBuilder();
        boolean oggettoDaUsare = false;
        if (oggettoUsato.getNome().equalsIgnoreCase("Pala") && stanzaCorrente.getNome().equalsIgnoreCase("Serra")) {
            if(!description.isTerrenoScavato()) {
                msg.append("Il terreno si smuove facilmente sotto la tua forza, ma dosi con attenzione ogni movimento.\n\n{Se davvero sotto la terra si nasconde ciò che penso, colpirlo con troppa forza potrebbe essere rischioso.}\n\nCon ogni colpo ponderato della pala, la terra si allenta sempre di più, finché non senti un suono inconfondibile: TONG!\n" +
                        "Ti abbassi e inizi a spostare la terra con le mani, rivelando lentamente la forma di un oggetto.\nÈ robusto, con una struttura massiccia e una canna che non lascia spazio a dubbi.\n\n" +
                        "Hai dissotterrato un LANCIAFIAMME!\n");
                description.getStanzaCorrente().attivaOsservazioneAggiornata();
                description.setTerrenoScavato(true);
                oggettoDaUsare = true;
            }
        }
        if(oggettoUsato.getNome().equalsIgnoreCase("Ricetrasmettitore")){
            msg.append("Afferri il ricetrasmettitore e premi il pulsante di trasmissione.\n\"Mugs? Mugs, riesci a sentirmi? Passo-\" lo ripeti più volte ma nessuno risponde.\nOsservi il display: la frequenza è corretta e attiva, ma dall'altra parte nessuno risponde.\n\"Mugs!\" insisti, con un leggero nervosismo ma il silenzio persiste.\n{Perchè mai non risponde?}");
            oggettoDaUsare = true;
        }
        if(!oggettoDaUsare){
            msg.append("L'oggetto specificato non può essere \"usato\". Ma forse è possibile interagirci in un altro modo.");
        }
        return msg.toString();
    }
}
