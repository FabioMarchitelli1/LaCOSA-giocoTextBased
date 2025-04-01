package di.lacosa.ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Pannello dedicato alla visualizzazione della mappa di gioco.
 * Disegna un'immagine di sfondo e un indicatore (cerchio rosso pulsante) sulla posizione corrente del giocatore.
 * Supporta due modalità di visualizzazione: mappa normale e mappa bloccata.
 * Le coordinate delle stanze sono mappate attraverso un sistema di coordinate interne.
 *
 * @author fabioMarchitelli
 */
public class MapPanel extends JPanel {

    private Image mappa;                // L'immagine di sfondo della mappa
    private Image mappaNormale;         // Immagine della mappa in modalità normale
    private Image mappaBloccata;        // Immagine della mappa in modalità bloccata
    private final Map<Integer, Point> stanzaCoordinate; // Mappa idStanza -> coordinate
    private int playerX = 293, playerY = 594;       // coordinate iniziali del giocatore (Si trova all'ingresso)
    private float scalaPulsante = 1.0f;         // Fattore di scala per l'effetto pulsante
    private boolean crescente = true;          // Direzione dell'animazione pulsante
    private boolean PallinoGiocatoreAttivo = false;   // Flag per visualizzare l'indicatore

    /**
     * Costruttore della classe MapPanel.
     * Inizializza le immagini della mappa, le coordinate delle stanze
     * e avvia l'animazione dell'indicatore pulsante.
     */
    public MapPanel() {

        setBackground(Color.BLACK);

        // Caricamento delle immagini della mappa
        mappaNormale = new ImageIcon(getClass().getClassLoader().getResource("immagini/mappaDiGioco.png")).getImage();
        mappaBloccata = new ImageIcon(getClass().getClassLoader().getResource("immagini/mappaBloccata.png")).getImage();

        // Imposta la mappa iniziale come bloccata
        mappa= mappaBloccata;

        // Inizializzazione della mappa delle coordinate delle stanze
        stanzaCoordinate = new HashMap<>();

        // Definizione delle coordinate per ogni stanza
        stanzaCoordinate.put( 1, new Point(336,  641));
        stanzaCoordinate.put(2,  new Point(336, 538));
        stanzaCoordinate.put(3,  new Point(455, 538));
        stanzaCoordinate.put(4,  new Point(336, 347));
        stanzaCoordinate.put(5,  new Point(480, 347));
        stanzaCoordinate.put(6,  new Point(449, 168));
        stanzaCoordinate.put(7,  new Point(220, 168));
        stanzaCoordinate.put(8,  new Point(74, 116));
        stanzaCoordinate.put(9,  new Point(74, 346));
        stanzaCoordinate.put(10,  new Point(74, 229));
        stanzaCoordinate.put(11,  new Point(153, 345));
        stanzaCoordinate.put(12,  new Point(153, 230));
        stanzaCoordinate.put(13,  new Point(153, 114));
        stanzaCoordinate.put(14,  new Point(334, 168));
        stanzaCoordinate.put(15,  new Point(334, 99));





        // Timer per l'animazione pulsante dell'indicatore
        new Timer(100, e -> {
            if (crescente) {
                scalaPulsante += 0.05f;
                if (scalaPulsante >= 1.3f) crescente = false; // Inverte la direzione a scale=1.3
            } else {
                scalaPulsante -= 0.05f;
                if (scalaPulsante <= 0.8f) crescente = true;  // Inverte la direzione a scale=0.8
            }
            repaint();
        }).start();


    }


    /**
     * Aggiorna la posizione del giocatore in base all'id della stanza corrente.
     * Se la stanza non viene trovata, nasconde l'indicatore.
     *
     * @param idStanza L'identificativo della stanza in cui si trova il giocatore
     */
    public void setStanzaCorrente(int idStanza) {
        Point p = stanzaCoordinate.get(idStanza);
        if (p != null) {
            // Aggiorniamo le coordinate
            playerX = p.x;
            playerY = p.y;
        } else {
            // Nasconde l'indicatore se la stanza non è trovata
            playerX = -1;
            playerY = -1;
        }
        repaint();
    }

    /**
     * Imposta la mappa in modalità "normale".
     */
    public void mostraMappaNormale() {
        mappa = mappaNormale;
        repaint();
    }

    /**
     * Imposta la mappa in modalità "bloccata".
     * Ovvero, una versione della mappa che indica che il giocatore
     * non è correntemente all'interno dell'avamposo del gioco
     */
    public void mostraMappaBloccata() {
        mappa = mappaBloccata;
        repaint();
    }


    /**
     * Abilita o disabilita la visualizzazione dell'indicatore del giocatore.
     *
     * @param mostra true per mostrare l'indicatore, false per nasconderlo
     */
    public void setPallino(boolean mostra) {
        PallinoGiocatoreAttivo = mostra;
        repaint();
    }

    /**
     * Metodo override per il disegno del componente.
     * Disegna l'immagine della mappa e l'eventuale indicatore del giocatore.
     *
     * @param g Il contesto grafico su cui disegnare
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna la mappa di sfondo scalata alle dimensioni del pannello
        if (mappa != null) {
            int larghezzaPannello = getWidth();
            int altezzaPannello = getHeight();

            g.drawImage(mappa, 0, 0, larghezzaPannello, altezzaPannello, this);
        }

        // Disegna l'indicatore del giocatore se attivo e con coordinate valide
        //La simulazione della pulsazione è data dalla variazione della dimensione del pallino
        // che varia tra 0.8 e 1.3
        if (PallinoGiocatoreAttivo && playerX >= 0 && playerY >= 0) {
            Graphics2D g2 = (Graphics2D) g;

            //Dimensione base del puntino rosso
            float dimensioneBase = 10.0f;
            float dimensioneAttuale = dimensioneBase * scalaPulsante;

            // centra il palllino sulle coordinate del giocatore
            int drawX = (int) (playerX - dimensioneAttuale / 2);
            int drawY = (int) (playerY - dimensioneAttuale / 2);

            g2.setColor(Color.RED);
            g2.fillOval(drawX, drawY, (int) dimensioneAttuale, (int) dimensioneAttuale);
        }
    }


}
