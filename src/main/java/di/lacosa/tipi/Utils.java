package di.lacosa.tipi;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * La classe Utils fornisce metodi di utilità per:
 * <ul>
 * <li>Caricare contenuti di file in strutture dati efficienti per l'uso (set di stringhe)</li>
 * <li>Processare stringhe di input per estrarre informazioni utili, escludendo parole non rilevanti (stopwords)</li>
 * </ul>
 * @author fabioMarchitelli
 */
public class Utils {


    /**
     * Carica il contenuto di un file da un InputStream in un Set di stringhe.
     * Ogni riga del file diventa un elemento del Set, convertito in minuscolo e senza spazi iniziali/finali.
     *
     * @param inputStream lo stream di input da cui leggere il contenuto
     * @return un Set contenente le righe del file processate
     * @throws IOException se si verifica un errore durante la lettura del file
     */
    public static Set<String> caricaFileInSet(InputStream inputStream) throws IOException {
        Set<String> insieme = new HashSet<>();

        // Usa InputStreamReader per leggere dall'InputStream
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                insieme.add(reader.readLine().trim().toLowerCase());
            }
        }
        return insieme;
    }

    /**
     * Carica l'intero contenuto di un file da un InputStream in una singola stringa.
     * Mantiene la struttura originale delle righe separandole con caratteri di nuova linea.
     *
     * @param inputStream lo stream di input da cui leggere il contenuto
     * @return una stringa contenente tutto il testo del file
     * @throws IOException se si verifica un errore durante la lettura del file
     */
    public static String caricaTestoInStringa(InputStream inputStream) throws IOException {
        StringBuilder testo = new StringBuilder();

        // Le risorse vengono chiuse automaticamente con la funzione di chiusura del try-with-resources
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                testo.append(linea).append("\n");
            }
        }
        return testo.toString();
    }


    /**
     * Processa una stringa di input dividendo in token ed escludendo le stopwords.
     * La stringa viene convertita in minuscolo e divisa in token basati su spazi bianchi.
     * Tutti i token che compaiono nel set di stopwords vengono esclusi dal risultato.
     *
     * @param stringa la stringa da processare
     * @param stopwords un set di parole da escludere dal risultato
     * @return una lista di token validi (non stopwords) in ordine di apparizione
     */
    public static List<String> elaboraStringa(String stringa, Set<String> stopwords) {
        //inizializza la lista dove verranno memorizzati i tokens.
        List<String> tokens = new ArrayList<>();
        //crea un array e rende la stringa dell'utente tutta in minuscolo e la spezza a metà quando trova uno spazio (es: "prendi orologio" -> "prendi", "orologio")
        String[] split = stringa.toLowerCase().split("\\s+");
        //for each elemento contenuto in slip (es: 1(per prendi), 2(per pala))
        for (String t : split) {
            // se nel documento di stopwords NON esiste la prima parola nell'array split, allora la si aggiunge nella lista di token (es: "prendi" non appartiene a "stopwords" allora si aggiunge alla lista tokern)
            if (!stopwords.contains(t)) {
                tokens.add(t);
            }
        }
        //returna la lista
        return tokens;
    }
}
