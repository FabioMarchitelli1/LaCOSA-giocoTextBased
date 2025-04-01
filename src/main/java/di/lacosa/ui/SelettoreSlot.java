package di.lacosa.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Date;



/**
 * Finestra di dialogo per la selezione di uno slot di salvataggio.
 * Mostra tre pannelli corrispondenti agli slot disponibili, ciascuno con informazioni
 * sullo stato del salvataggio e un pulsante per selezionarlo.
 *
 * @author fabioMarchitelli
 */
public class SelettoreSlot extends JDialog {

    private int slotScelto = -1; // Scelta dell'utente (1-3), -1 è il valore di default

    /**
     * Costruttore della finestra di selezione slot.
     *
     * @param padre La finestra padre a cui associare il dialogo
     * @param modal Se true, blocca l'interazione con altre finestre
     */
    public SelettoreSlot(Frame padre, boolean modal) {
        super(padre, "Seleziona uno Slot", modal);

        setSize(600, 300);
        setLocationRelativeTo(padre);
        setLayout(new GridLayout(1, 3, 10, 10));
        setResizable(false);

        // Crea un pannello per ogni slot (1-3)
        for (int i = 1; i <= 3; i++) {
            add(creaPannelloSlot(i));
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Crea un pannello per visualizzare le informazioni di uno slot specifico.
     *
     * @param numeroSlot Il numero dello slot (1-3)
     * @return JPanel configurato con le informazioni dello slot
     */
    private JPanel creaPannelloSlot(int numeroSlot) {
        JPanel pannelloSlot = new JPanel();
        pannelloSlot.setLayout(new BorderLayout());
        pannelloSlot.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        pannelloSlot.setBackground(Color.BLACK);

        // Etichetta del titolo
        JLabel etichettaTitolo = new JLabel("Slot " + numeroSlot, SwingConstants.CENTER);
        etichettaTitolo.setForeground(Color.WHITE);
        pannelloSlot.add(etichettaTitolo, BorderLayout.NORTH);

        // Verifica se esiste il file di salvataggio
        File fileSalvataggio = new File("saves/slot" + numeroSlot + ".dat");

        JLabel infoEtichetta = new JLabel("", SwingConstants.CENTER);
        infoEtichetta.setForeground(Color.WHITE);
        JButton bottoneCarica = new JButton("Carica");

        if (fileSalvataggio.exists()) {
            // Slot occupato
            // 1. Ottiene la data dell'ultima modifica del file
            long ultimaModifica = fileSalvataggio.lastModified(); // Timestamp dell'ultima modifica
            Date data = new Date(ultimaModifica);                 // Converte in oggetto Date leggibile
            infoEtichetta.setText("<html><body><center>Salvataggio<br>" + data.toString() + "</center></body></html>");
            bottoneCarica.setEnabled(true);
            bottoneCarica.addActionListener(e -> {
                slotScelto = numeroSlot;
                dispose();
            });
        } else {
            // Slot vuoto
            infoEtichetta.setText("<html><body><center><i>Vuoto</i></center></body></html>");
            bottoneCarica.setEnabled(false); //Pulsante carica disabilitato
        }

        //Aggiunta al pannelloSlot di info e bottone carica
        pannelloSlot.add(infoEtichetta, BorderLayout.CENTER);
        pannelloSlot.add(bottoneCarica, BorderLayout.SOUTH);

        return pannelloSlot;
    }


    /**
     * Restituisce lo slot selezionato dall'utente.
     *
     * @return Il numero dello slot selezionato (1-3) oppure -1 se l'operazione è stata annullata
     */
    public int getSlotScelto() {
        return slotScelto;
    }
}
