package di.lacosa.ui;

import javax.swing.*;
import java.awt.*;


/**
 * Classe che gestisce la visualizzazione dei titoli di coda con effetti animati.
 * Implementa un fade-in iniziale, una pausa e uno scorrimento verso l'alto dei titoli.
 * Al termine dell'animazione, riporta l'utente all'interfaccia iniziale.
 *
 * @author fabioMarchitelli
 */
public class TitoliDiCoda extends JFrame {
    private JLabel titoloLabel;
    private JLabel creditiLabel;
    private Timer timerScorrimento;
    private Timer timerPausa;
    private int titoloY;      // Posizione verticale del titolo
    private int creditiY;    // Posizione verticale dei crediti
    private float alpha = 0f; // Valore alpha per l'effetto scomparsa

    /**
     * Costruttore della finestra dei titoli di coda.
     * Inizializza l'interfaccia e avvia l'animazione.
     */
    public TitoliDiCoda() {
        setTitle("Grazie per aver giocato!");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        JPanel pannello = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, alpha));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        pannello.setBackground(Color.BLACK);
        add(pannello);

        // Titolo "La Cosa"
        titoloLabel = new JLabel(
                "<html>" +
                        "<span style='color: white; font-family: Impact; font-size: 199pt;'>La </span>" +
                        "<span style='color: white; font-family: Impact; font-size: 310pt;'>COSA</span>" +
                        "</html>",
                SwingConstants.CENTER
        );

        titoloY = getHeight() / 9;  // Porre il titolo sulla parte superiore dall'interfaccia
        titoloLabel.setBounds(0, titoloY, getWidth(), 600);
        pannello.add(titoloLabel);


        // Testo dei titoli di coda
        String creditsText = "<html><div style='text-align: center; color: white;'>"
                + "<br>" +
                // Ruoli nella progettazione del progetto
                "<p style='font-size: 18px;'>SCRITTO DA</p>" +
                "<p style='font-size: 20px;'><b>Fabio Marchitelli</b></p>" +
                "<br>" +

                "<p style='font-size: 18px;'>PROGETTATO DA</p>" +
                "<p style='font-size: 20px;'><b>Fabio Marchitelli</b></p>" +
                "<br>" +

                "<p style='font-size: 18px;'>SVILUPPATO DA</p>" +
                "<p style='font-size: 20px;'><b>Fabio Marchitelli</b></p>" +
                "<br><br>" +

                // Ispirazioni del progetto
                "<p style='font-size: 14px;'>LIBERAMENTE ISPIRATO A</p>" +
                "<p style='font-size: 16px;'><b>\"THE THING\" DI JOHN CARPENTER (1982)</b></p>" +
                "<p style='font-size: 14px;'>E AL RACCONTO \"CHI VA LÀ?\" DI JOHN W. CAMPBELL</p>" +
                "<br>" +
                "<br><br>" +

                // Strumenti utilizzati nella realizzazione del progetto
                "<p style='font-size: 14px;'>REALIZZATO CON</p>" +
                "<p style='font-size: 16px;'>Java · Swing · IntelliJ IDEA</p>" +
                "<br>" +
                "<p style='font-size: 12px;'>GRAZIE A</p>" +
                "<p style='font-size: 14px;'>[Elena, Giorgio, e Marica]</p>" +
                "<br><br>" +
                 "</div></html>";

        creditiLabel = new JLabel(creditsText, SwingConstants.CENTER);
        creditiY = titoloY + 350;  // Porre i titoli di coda sotto al titolo
        creditiLabel.setBounds(0, creditiY, getWidth(), 2000);
        pannello.add(creditiLabel);

        Timer timerEffettoScomparsa = new Timer(30, e -> {
            alpha = Math.min(1.0f, alpha + 0.02f);
            pannello.repaint();
            if (alpha >= 1.0f) {
                ((Timer) e.getSource()).stop();
                impostaRitardoScorrimento();
            }
        });
        timerEffettoScomparsa.start();

        setVisible(true);
    }

    /**
     * Avvia una pausa di 3 secondi dopo che il titolo è apparso.
     * Al termine della pausa, avvia lo scorrimento dei crediti.
     */
    private void impostaRitardoScorrimento() {
        timerPausa = new Timer(3000, e -> {
            ((Timer) e.getSource()).stop();
            iniziaScorrimento();
        });
        timerPausa.setRepeats(false);
        timerPausa.start();
    }


    /**
     * Avvia l'animazione di scorrimento verso l'alto dei titoli di coda.
     * L'animazione termina quando tutti i crediti sono usciti dalla visuale.
     */
    private void iniziaScorrimento() {
        timerScorrimento = new Timer(30, e -> {
            titoloY -= 2;
            creditiY -= 2;
            titoloLabel.setLocation(0, titoloY);
            creditiLabel.setLocation(0, creditiY);

            // Termina lo scorrimento quando tutti i titoli di coda sono usciti dalla visuale
            if (creditiY + creditiLabel.getHeight() < 550) {
                timerScorrimento.stop();
                dispose();
                SwingUtilities.invokeLater(() -> new InterfacciaIniziale().setVisible(true)); // al termine dello scorrimento si riporta l'utente all'interfacca iniziale
            }
        });
        timerScorrimento.start();
    }

    /**
     * Chiude la finestra fermando eventuali timer attivi.
     */
    @Override
    public void dispose() {
        if (timerPausa != null) timerPausa.stop();
        if (timerScorrimento != null) timerScorrimento.stop();
        super.dispose();
    }

    /**
     * Metodo statico per mostrare i titoli di coda.
     * Garantisce che la creazione avvenga nel thread di Swing.
     */
    public static void mostraTitoliDiCoda() {
        SwingUtilities.invokeLater(TitoliDiCoda::new);
    }
}
