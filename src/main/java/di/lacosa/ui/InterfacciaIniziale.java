package di.lacosa.ui;

import di.lacosa.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;


/**
 * Classe che rappresenta l'interfaccia grafica iniziale del gioco "La COSA".
 * Fornisce all'utente le opzioni per iniziare una nuova partita, caricare una partita salvata o uscire dal gioco.
 * L'interfaccia è composta da un titolo stilizzato e tre pulsanti principali.
 *
 * @author fabioMarchitelli
 */
public class InterfacciaIniziale extends JFrame {
    private JLabel titoloLabel;
    private JButton bottoneNuovaPartita;
    private JButton bottoneCaricaPartita;
    private JButton bottoneUscita;
    private JPanel pannelloPrincipale;


    /**
     * Costruttore della classe InterfacciaIniziale.
     * Inizializza la finestra principale con titolo, dimensioni e comportamento di chiusura.
     * Configura l'icona dell'applicazione e crea gli elementi dell'interfaccia grafica.
     */
    public InterfacciaIniziale() {
        this.setTitle("La COSA - Interfaccia Iniziale");
        this.setSize(1376, 768);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        impostaIconaApplicazione("immagini/immagineIcona.jpg");

        // Pannello principale con GridBagLayout
        pannelloPrincipale = new JPanel(new GridBagLayout());
        pannelloPrincipale.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        // Creazione del titolo
        titoloLabel = new JLabel("<html>La <span style='font-size:200;'> COSA </span></html>");
        titoloLabel.setForeground(Color.WHITE);

        // Font modificato per effetto allungato
        Font fontBase = new Font("Impact", Font.BOLD, 60);
        AffineTransform trasformatore = new AffineTransform();
        trasformatore.scale(6.5, 1.5);
        Font fontAllungato = fontBase.deriveFont(trasformatore);
        titoloLabel.setFont(fontAllungato);

        // Aggiunta del titolo al pannello
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 0, 40, 0);
        pannelloPrincipale.add(titoloLabel, gbc);

        // Creazione dei pulsanti
        bottoneNuovaPartita = new BottoneIniziale("Nuova Partita");
        bottoneCaricaPartita = new BottoneIniziale("Carica Partita");
        bottoneUscita = new BottoneIniziale("Esci");

        // Impostazione dimensioni pulsanti
        Dimension dimensioneBottone = new Dimension(200, 50);
        bottoneNuovaPartita.setPreferredSize(dimensioneBottone);
        bottoneCaricaPartita.setPreferredSize(dimensioneBottone);
        bottoneUscita.setPreferredSize(dimensioneBottone);

        // Aggiunta eventi ai pulsanti
        bottoneNuovaPartita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                avviaNuovaPartita();
            }
        });

        bottoneCaricaPartita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                caricaPartita();
            }
        });

        bottoneUscita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Aggiunta pulsanti al pannello
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        pannelloPrincipale.add(bottoneNuovaPartita, gbc);

        gbc.gridy = 2;
        pannelloPrincipale.add(bottoneCaricaPartita, gbc);

        gbc.gridy = 3;
        pannelloPrincipale.add(bottoneUscita, gbc);

        // Aggiunta del pannello al frame
        add(pannelloPrincipale);
    }

    /**
     * Avvia una nuova partita chiudendo la finestra corrente e richiamando il metodo Main.avviaPartita.
     * Il parametro true indica che si tratta di una nuova partita, mentre -1 è valore di default per indicare
     * uno slot di caricamento non ancora scelto.
     */
    private void avviaNuovaPartita() {
        this.dispose(); // Chiudi la finestra iniziale
        Main.avviaPartita(true, -1); // Inizia una nuova partita
    }

    /**
     * Gestisce il caricamento di una partita salvata.
     * Mostra un dialogo per la selezione dello slot di salvataggio e, se selezionato,
     * chiude la finestra corrente e carica la partita corrispondente.
     */
    private void caricaPartita() {

        SelettoreSlot selettoreSlot = new SelettoreSlot(this, true);
        selettoreSlot.setVisible(true);

        //dopo che l'utente ha scelto lo slot...
        int slot = selettoreSlot.getSlotScelto();
        if (slot >= 1 && slot <= 3) {
            this.dispose();
            // Carica la partita selezionata
            Main.avviaPartita(false, slot);
        } else {
            System.out.println("Nessuno slot selezionato (chiuso o annullato).");
        }
    }



    /**
     * Imposta l'icona dell'applicazione sia per la finestra che per la taskbar (se supportato).
     *
     * @param percorsoImmagine il percorso relativo dell'immagine da utilizzare come icona
     */
    private void impostaIconaApplicazione(String percorsoImmagine) {

        URL iconaURL = getClass().getClassLoader().getResource(percorsoImmagine);

        if (iconaURL != null) {
            ImageIcon icona = new ImageIcon(iconaURL);
            this.setIconImage(icona.getImage());

            if (Taskbar.isTaskbarSupported()) {
                try {
                    Taskbar taskbar = Taskbar.getTaskbar();
                    if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                        taskbar.setIconImage(icona.getImage());
                    }
                } catch (UnsupportedOperationException | SecurityException e) {
                    System.err.println("Icona di TaskBar non supportata da questo OS.");
                }
            }
        } else {
            System.err.println("Immagine non trovata");
        }
    }


    /**
     * Metodo principale per l'avvio dell'applicazione.
     * Crea e visualizza l'interfaccia iniziale in modo thread-safe tramite SwingUtilities.
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfacciaIniziale().setVisible(true));
    }


}
