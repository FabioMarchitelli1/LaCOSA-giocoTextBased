package di.lacosa.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



/**
 * Questa classe {@code BottoneINiziale}rappresenta un bottone personalizzato per l'interfaccia iniziale.
 * Estende {@link JButton} e include effetti di hover e pressione del mouse, con uno stile visivo
 * personalizzato.
 * <p>
 * Questo componente è progettato per essere utilizzato come elemento UI principale con feedback visivi
 * interattivi.
 *
 * @see JButton
 */
public class BottoneIniziale extends JButton {


    /**
     * Crea un nuovo BottoneIniziale con il testo specificato.
     * <p>
     * Configura automaticamente:
     * <ul>
     *   <li>Font personalizzato (Voltaire, bold, size 20)</li>
     *   <li>Colori di sfondo ( grigio scuro) e testo (bianco)</li>
     *   <li>Bordatura sottile bianca</li>
     *   <li>Effetti di hover/pressione del mouse</li>
     * </ul>
     *
     * @param testo il testo da visualizzare sul bottone
     */
    public BottoneIniziale(String testo) {
        super(testo); // Imposta testo del bottone

        Font fontBase = new Font("Voltaire", Font.BOLD, 20);
        setFont(fontBase);

        // Costumizzazione apparenza del bottone
        setBackground(Color.DARK_GRAY);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        setFocusPainted(false);
        setOpaque(true);

        // Aggiunta di interazioni con il mouse
        addMouseListener(new MouseAdapter() {


            /**
             * Cambia il colore di sfondo quando il mouse entra nell'area del bottone.
             *
             * @param evt l'evento del mouse
             */
            @Override
            public void mouseEntered(MouseEvent evt) {
                setBackground(Color.LIGHT_GRAY); //Colore più chiaro quando il mouse si trova sopra il bottone
            }


            /**
             * Ripristina il colore originale quando il mouse esce dall'area del bottone.
             *
             * @param evt l'evento del mouse
             */
            @Override
            public void mouseExited(MouseEvent evt) {
                setBackground(Color.DARK_GRAY); // Ritorno al colore normale quando il mouse esce dall'area del bottone
            }


            /**
             * Cambia il colore di sfondo quando il bottone viene premuto.
             *
             * @param e l'evento del mouse
             */
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(Color.GRAY); // Colore più scuro quando il bottone viene premuto
            }


            /**
             * Ripristina il colore di hover quando il bottone viene rilasciato.
             *
             * @param e l'evento del mouse
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(Color.LIGHT_GRAY); // Ritorno a colore più chiaro quando il bottone viene rilasciato
            }
        });
    }
}
