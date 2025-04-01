package di.lacosa.ui;

import javax.swing.*;
import java.awt.*;


/**
 * Questa classe {@code FinestraLettura} rappresenta una finestra di dialogo "modale"
 * per la visualizzazione di testo in un'area di lettura non modificabile.
 * <p>
 * Implementata come {@link JDialog} modale, blocca l'interazione con la finestra
 * principale finché non viene chiusa.È particolarmente utile per mostrare
 * informazioni testuali che richiedono la piena attenzione dell'utente.
 *
 * @author fabioMarchitelli
 * @see JDialog
 */
public class FinestraLettura extends JDialog {

    /**
     * Crea una nuova finestra di lettura modale (ovvero una finestra che
     * blocca l'interazione con le altre finestre dell'applicazione finché non viene chiusa).
     * <p>
     * Essa configura automaticamente:
     * <ul>
     *   <li>Dimensioni fisse (550x600 pixel)</li>
     *   <li>Modalità APPLICATION_MODAL</li>
     *   <li>Non ridimensionabile</li>
     *   <li>Chiusura con DISPOSE_ON_CLOSE</li>
     *   <li>Stile visivo con testo bianco su sfondo nero</li>
     *   <li>Area di testo con scrollbar e word wrapping</li>
     * </ul>
     *
     * @param padre la finestra padre a cui questa finestra è relazionata
     * @param testo il contenuto testuale da visualizzare
     * @param titolo il titolo della finestra
     */
    public FinestraLettura(JFrame padre, String testo, String titolo) {
        super(padre, titolo, true);  //Modalità modale
        setSize(550, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(padre);  // Centra rispetto alla finestra padre
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);  // Blocca tutta l'applicazione

        // Configurazione area testo
        JTextArea areaTesto = new JTextArea(testo);
        areaTesto.setEditable(false);
        areaTesto.setLineWrap(true);
        areaTesto.setWrapStyleWord(true);
        areaTesto.setForeground(Color.WHITE);
        areaTesto.setBackground(Color.BLACK);
        areaTesto.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane pannelloDiScorrimento = new JScrollPane(areaTesto);
        pannelloDiScorrimento.setBorder(BorderFactory.createEmptyBorder());

        getContentPane().setBackground(Color.BLACK);
        getContentPane().add(pannelloDiScorrimento, BorderLayout.CENTER);

    }


    /**
     * Metodo statico per visualizzare una finestra di lettura in modo thread-safe.
     * <p>
     * L'invocazione avviene tramite {@link SwingUtilities#invokeLater} per garantire
     * la corretta gestione del thread dell'interfaccia grafica.
     *
     * @param padre la finestra padre a cui questa finestra è relazionata
     * @param testo il contenuto testuale da visualizzare
     * @param titolo il titolo della finestra
     */
    public static void mostraFinestraLettura(JFrame padre, String testo, String titolo) {
        SwingUtilities.invokeLater(() -> {
            FinestraLettura finestra = new FinestraLettura(padre, testo, titolo);
            finestra.setVisible(true);
        });
    }
}