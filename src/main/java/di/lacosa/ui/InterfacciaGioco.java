package di.lacosa.ui;

import di.lacosa.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.function.Consumer;


/**
 * La classe {@code InterfacciaGioco} rappresenta la finestra principale del gioco,
 * contenente un'area di testo per l'output, un campo di input per i comandi,
 * una mappa visuale e vari controlli per la navigazione.
 * <p>
 * Implementa effetti di testo animati, gestione di input speciali e visualizzazione
 * dinamica della mappa di gioco.
 *
 * @author fabioMarchitelli
 * @see JFrame
 * @see MapPanel
 */
public class InterfacciaGioco extends JFrame {

    // Componenti UI
    private JTextArea areaDiTesto;
    private JTextField campoDiInput;
    private JButton bottoneContinua;
    private JButton bottoneSalta;
    private Thread threadBattitura;
    private Main engine;
    private MapPanel pannelloMappa;
    private String correnteTestoDiBattitura = "";
    private boolean inBattitura = false;


    // Pannelli del layout sud
    private JPanel pannelloBottoni;  // pannello con pulsante Continua
    private JPanel pannelloInput;   // pannello con campo input
    private JPanel contenitoreSud; // pannello che contiene entrambi


    // Variabile per effetto battitura
    private int indiceDiTesto = 0;


    /**
     * Callback per gestire input speciali (come dialoghi multi-linea).
     * Se null, l'input viene passato al parser normale del gioco.
     */
    private Consumer<String> callbackRisposta = null;


    /**
     * Costruisce una nuova interfaccia di gioco collegata al motore specificato.
     *
     * @param engine il motore principale del gioco
     */
    public InterfacciaGioco(Main engine) {
        this.engine = engine;
        setTitle("La COSA - Interfaccia Gioco");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);


        impostaIconaApplicazione("immagini/immagineIcona.jpg");
        dividiSchermo();

        //Assicura che l'UI venga aggiornata prima dell'effetto di battitura
        revalidate();
        repaint();
        setVisible(true);
    }


    /**
     * Configura il layout principale dell'interfaccia, dividendo lo schermo in:
     * - Area testo/input (sinistra)
     * - Mappa di gioco (destra)
     */
    private void dividiSchermo() {

        //Dividiamo l'interfaccia di gioco in due pannelli principali
        //Il pannello di sinistra è riservato all'input e output testuale del gioco
        //Il pannello di destra è riservato alla visualizzazione della mappa
        JPanel pannelloSinistra = new JPanel(new BorderLayout());
        JPanel pannelloDestra = new JPanel(new BorderLayout());

        pannelloSinistra.setBackground(Color.BLACK);
        pannelloDestra.setBackground(Color.BLACK);

        // Area di testo in outpu
        areaDiTesto = new JTextArea();
        areaDiTesto.setLineWrap(true);
        areaDiTesto.setWrapStyleWord(true);
        areaDiTesto.setBackground(Color.BLACK);
        areaDiTesto.setForeground(Color.WHITE);
        areaDiTesto.setFont(new Font("Monospaced", Font.PLAIN, 15));
        areaDiTesto.setEditable(false);

        //Pannello di scorrimento per l'area di testo
        JScrollPane pannelloScorrimento = new JScrollPane(areaDiTesto);
        pannelloScorrimento.setBorder(BorderFactory.createEmptyBorder());
        pannelloScorrimento.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pannelloSinistra.add(pannelloScorrimento, BorderLayout.CENTER);



        // -------------------------------
        //  Configurazione dei bottoni di Salta & Continua (per l'effetto battitura)
        // -------------------------------

        //Il pulsante "Salta"
        bottoneSalta = new JButton("Salta");
        bottoneSalta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        bottoneSalta.setBackground(Color.BLACK);
        bottoneSalta.setForeground(Color.WHITE);
        bottoneSalta.setBorder(BorderFactory.createEmptyBorder());
        bottoneSalta.setPreferredSize(new Dimension(80, 30));
        bottoneSalta.addActionListener(e -> saltaEffettoBattitura());

        // Il pulsante "Continua"
        bottoneContinua = new JButton("Continua");
        bottoneContinua.setFont(new Font("Monospaced", Font.PLAIN, 12));
        bottoneContinua.setBackground(Color.BLACK);
        bottoneContinua.setForeground(Color.WHITE);
        bottoneContinua.setBorder(BorderFactory.createEmptyBorder());
        bottoneContinua.setPreferredSize(new Dimension(120, 40));
        bottoneContinua.setVisible(false);

        // -------------------------------
        //  Campo di input
        //  In questo campo l'utente inserirà i propri comandi
        // -------------------------------
        campoDiInput = new JTextField();
        campoDiInput.setFont(new Font("Monospaced", Font.PLAIN, 14));
        campoDiInput.setBackground(Color.DARK_GRAY);
        campoDiInput.setForeground(Color.WHITE);
        campoDiInput.setCaretColor(Color.WHITE);
        campoDiInput.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        //Tutto cio che verrà inserito dall'utente in questo campo,
        //una volta inviato verrà passato al metodo processaInput()
        campoDiInput.addActionListener(e -> {
            String command = campoDiInput.getText().trim();
            campoDiInput.setText("");
            processaInput(command);
        });


        // -------------------------------
        //  Pannello per il contenimento dei bottoni
        //       Salta e Continua
        // -------------------------------
        pannelloBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pannelloBottoni.setBackground(Color.BLACK);
        pannelloBottoni.add(bottoneSalta);
        pannelloBottoni.add(bottoneContinua);

        // -------------------------------
        //  Pannello per il contenimento
        //       del campo input
        // -------------------------------
        pannelloInput = new JPanel(new BorderLayout());
        pannelloInput.setBackground(Color.BLACK);
        pannelloInput.add(campoDiInput, BorderLayout.CENTER);

        // -------------------------------
        //  Il contenitoreSud conterrà a sua volta
        //  il pannelloInput e il pannelloBottoni
        // -------------------------------
        contenitoreSud = new JPanel();
        contenitoreSud.setLayout(new BoxLayout(contenitoreSud, BoxLayout.Y_AXIS));
        contenitoreSud.setBackground(Color.BLACK);

        // Aggiungiamo i pannelli in ordine:
        // 1. pannelloBottoni
        // 2. pannelloInput
        contenitoreSud.add(pannelloBottoni);
        contenitoreSud.add(pannelloInput);

        // Posizioniamo southContainer in fondo al pannelloSinistra
        pannelloSinistra.add(contenitoreSud, BorderLayout.SOUTH);

        // Creazione della mappa
        pannelloMappa = new MapPanel();
        pannelloDestra.add(pannelloMappa);

        // DivisorePannelli per separare i due pannelli
        JSplitPane divisorePannelli = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pannelloSinistra, pannelloDestra);
        divisorePannelli.setDividerLocation(0.25);
        divisorePannelli.setResizeWeight(0.25);
        divisorePannelli.setEnabled(false);
        divisorePannelli.setDividerSize(3);
        divisorePannelli.setBorder(BorderFactory.createEmptyBorder());

        setLayout(new BorderLayout());
        add(divisorePannelli, BorderLayout.CENTER);

        // Impostiamo la divisione dopo il layout
        SwingUtilities.invokeLater(() -> {
            divisorePannelli.setDividerLocation(0.60);
        });
    }


    /**
     * Interrompe l'effetto di battitura corrente e mostra immediatamente il testo
     * nella sua interezza
     */
    private void saltaEffettoBattitura() {
        if (!inBattitura) return;

        if (threadBattitura != null && threadBattitura.isAlive()) {
            threadBattitura.interrupt();
        }
        // Mostra il testo completo
        areaDiTesto.append(correnteTestoDiBattitura.substring(indiceDiTesto));
        areaDiTesto.setCaretPosition(areaDiTesto.getDocument().getLength());
        inBattitura = false;
        indiceDiTesto = 0;

        // Nascondi il pulsante "Salta"
        nascondiSalta();

        // E mostra il pulsante "Continua"
        mostraContinua();
    }

    /**
     * Mostra un testo nell'area di gioco con effetto battitura animata.
     *
     * @param testo          Il testo da visualizzare (non può essere nullo)
     * @param alTermine      Azione da eseguire a completamento (può essere null)
     * @param mostraSalta Se true, mostra il pulsante "Salta" durante l'animazione
     *
     *
     */
    public void scriviConEffettoBattitura(String testo, Runnable alTermine, boolean mostraSalta) {
        correnteTestoDiBattitura = testo;
        indiceDiTesto = 0;
        inBattitura = true;

        if (threadBattitura != null && threadBattitura.isAlive()) {
            threadBattitura.interrupt();
        }

        threadBattitura = new Thread(() -> {
            try {
                while (indiceDiTesto < testo.length()) {
                    final char lettera = testo.charAt(indiceDiTesto);

                    SwingUtilities.invokeLater(() -> {
                        areaDiTesto.append(String.valueOf(lettera));
                        areaDiTesto.setCaretPosition(areaDiTesto.getDocument().getLength());
                    });
                    indiceDiTesto++;
                    Thread.sleep(30);
                }
            } catch (InterruptedException ignored) {
            } finally {
                inBattitura = false;
                SwingUtilities.invokeLater(() -> {
                    nascondiSalta();
                    if (alTermine != null) {
                        alTermine.run();
                    } else {
                        mostraContinua();
                    }
                });
            }
        });
        threadBattitura.start();

        if(mostraSalta){
            mostraSalta();
        }
    }


    /**
     * Versione semplificata di {@link #scriviConEffettoBattitura(String, Runnable, boolean)}
     * chiamata quando non è necessario il callback.
     *
     * @param testo il testo da visualizzare
     */    public void scriviConEffettoBattitura(String testo) {
        scriviConEffettoBattitura(testo, null, true);
    }



    /**
     * Imposta un callback per gestire input speciali (utilizzato unicamente per gestire i dialoghi).
     *
     * @param callback la funzione che riceverà l'input
     */
    public void attendiRispostaGiocatore(java.util.function.Consumer<String> callback) {
        this.callbackRisposta = callback;
    }


    /**
     * Processa l'input dell'utente: lo invia al callback se attivo (per dialoghi),
     * altrimenti al parser di gioco standard.
     * Ignora input vuoti/nulli.
     *
     * @param input Comando inserito
     *
     * @see #attendiRispostaGiocatore(Consumer) Per la modalità dialoghi
     */
    private void processaInput(String input) {
        if (input == null || input.isEmpty()) {
            return; //ignora linee vuote
        }

        if (callbackRisposta != null) {
            callbackRisposta.accept(input); // Modalità dialogo
        } else {
            engine.processaComando(input); // Elaborazione standard
        }
    }


    /**
     * Scrive direttamente il testo nell'area di output, senza effetti di animazione.
     *
     * @param testo il testo da visualizzare
     */
    public void scriviInAreaDiTesto(String testo) {
        areaDiTesto.append(testo + "\n");
        areaDiTesto.setCaretPosition(areaDiTesto.getDocument().getLength());
    }


    public void setEngine(Main engine) {
        this.engine = engine;
    }

    public Main getEngine() {
        return engine;
    }


    /**
     * Aggiorna la posizione del giocatore sulla mappa.
     *
     * @param idStanza l'ID della stanza corrente
     */
    public void aggiornaMappa(int idStanza) {

        if (pannelloMappa != null) {
            pannelloMappa.setStanzaCorrente(idStanza);
        }
    }



    /**
     * Imposta l'icona dell'applicazione sia per la finestra che per la taskbar.
     *
     * @param percorsoIcona Percorso relativo dell'immagine (es. "img/icona.png")
     * @throws SecurityException Se l'OS blocca l'accesso alla taskbar
     */
    private void impostaIconaApplicazione(String percorsoIcona) {
        // 1. Caricamento icona dalle risorse
        URL iconaURL = getClass().getClassLoader().getResource(percorsoIcona);

        if (iconaURL != null) {
            // 2. Impostazione icona finestra
            ImageIcon icona = new ImageIcon(iconaURL);
            this.setIconImage(icona.getImage()); // Imposta l'icona della finestra

            // 3. Impostazione icona taskbar (se supportata)
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
     * Mostra e configura il pulsante "Continua" con un'azione personalizzata.
     * @param callback Azione da eseguire al click (non null)
     */
    public void mostraPulsanteContinua(Runnable callback) {

        // Rimuove i vecchi listener
        for (ActionListener al : bottoneContinua.getActionListeners()) {
            bottoneContinua.removeActionListener(al);
        }

        // Imposta il nuovo listener
        bottoneContinua.addActionListener(e -> {
            nascondiPulsanteContinua();
            callback.run();
        });

        // Mostra e abilita il pulsante
        bottoneContinua.setText("Continua");
        bottoneContinua.setVisible(true);
        bottoneContinua.setEnabled(true);
        bottoneContinua.requestFocusInWindow();

        getRootPane().setDefaultButton(bottoneContinua);

        revalidate();
        repaint();
    }


    /**
     * Nasconde il pulsante "Continua" e disabilita il pulsante "Salta".
     */
    public void nascondiPulsanteContinua() {
        bottoneContinua.setVisible(false);
        bottoneContinua.setEnabled(false);
        bottoneSalta.setVisible(false);
        bottoneSalta.setEnabled(false);
        revalidate();
        repaint();
    }


    /**
     * Restituisce il pannello della mappa di gioco.
     * @return L'istanza di MapPanel in uso
     */
    public MapPanel getMappa() {
        return pannelloMappa;
    }


    /**
     * Svuota completamente il contenuto dell'area di testo.
     */
    public void svuotaAreaDiTesto() {
        areaDiTesto.setText("");
    }


    /**
     * Mostra un testo centrato con effetto battitura e font ingrandito.
     * @param testo Testo da visualizzare
     * @param delay Ritardo tra caratteri (ms)
     * @param quandoFinito Callback eseguito a fine animazione (può essere null)
     */
    public void scriviCentratoConDelay(String testo, int delay, Runnable quandoFinito) {
        scriviInAreaDiTesto("");

        // Salvataggio dell'attuale font utilizzato per mostrare il testo
        Font fontOriginale = areaDiTesto.getFont();

        // Imposta un font più grande per mostrare il titolo
        Font fontTitolo = new Font("Impact", Font.BOLD, 139);
        areaDiTesto.setFont(fontTitolo);


        Timer timer = new Timer(delay, null);
        StringBuilder current = new StringBuilder();
        final int[] i = {0};

        //codice utilizzato per creare un effetto di battitura lenta
        //del titolo del gioco
        timer.addActionListener(e -> {
            if (i[0] < testo.length()) {
                current.append(testo.charAt(i[0]));
                areaDiTesto.setText("\n     " + current.toString());
                i[0]++;
            } else {
                timer.stop();

                // Qualche secondo dopo che il titolo è stato mostrato, si resetta il font e si chiama callback
                new Timer(1500, evt -> {
                    ((Timer) evt.getSource()).stop();
                    areaDiTesto.setFont(fontOriginale);
                    areaDiTesto.setText("");               //cancella il titolo dall'area di testo
                    if (quandoFinito != null) {
                        quandoFinito.run();              //Passerà alla successiva fase di intro
                    }
                }).start();
            }
        });
        timer.start();
    }


    /**
     * Verifica se è in corso un'animazione di battitura.
     * @return true se un'animazione è in corso
     */
    public boolean isInBattitura() {
        return inBattitura;
    }


    /**
     * Mostra il pulsante "Salta" durante le animazioni di testo.
     */
    private void mostraSalta() {
        bottoneSalta.setVisible(true);
        bottoneSalta.setEnabled(true);
        revalidate();
        repaint();
    }

    /**
     * Nasconde il pulsante "Salta".
     */
    public void nascondiSalta() {
        bottoneSalta.setVisible(false);
        bottoneSalta.setEnabled(false);
        revalidate();
        repaint();
    }


    /**
     * Mostra il pulsante "Continua".
     */
    private void mostraContinua() {
        bottoneContinua.setVisible(true);
        bottoneContinua.setEnabled(true);
        revalidate();
        repaint();
    }


    public void mostraStanzaPulita(String nomeStanza, String descrizione) {
        svuotaAreaDiTesto();
        scriviInAreaDiTesto("");
        scriviInAreaDiTesto("Ti trovi in: " + nomeStanza);
        scriviInAreaDiTesto("─────────────────────────────────────");
        scriviInAreaDiTesto(descrizione);
    }



    /**
     * Attiva la visualizzazione del marker di posizione sulla mappa.
     */
    public void attivaPuntinoRosso() {
        if (pannelloMappa != null) {
            pannelloMappa.setPallino(true);
        }
    }

    /**
     * Disattiva il marker di posizione sulla mappa.
     */
    public void disattivaPuntinoRosso() {
        if (pannelloMappa != null) {
            pannelloMappa.setPallino(false);
        }
    }


    /**
     * Mostra la versione completa e interattiva della mappa.
     */
    public void mostraMappaNormale(){
        if(pannelloMappa != null) {
            pannelloMappa.mostraMappaNormale();
        }
    }

    /**
     * Mostra la versione semplificata/bloccata della mappa.
     */
    public void mostraMappaBloccata(){
        if(pannelloMappa != null) {
            pannelloMappa.mostraMappaBloccata();
        }
    }


    /**
     * Disabilita il campo di input dei comandi.
     */
    public void disabilitaInput() {
        campoDiInput.setEnabled(false);
        campoDiInput.setBackground(Color.DARK_GRAY);
    }

    /**
     * Abilita il campo di input dei comandi e gli dà focus.
     */
    public void abilitaInput() {
        campoDiInput.setEnabled(true);
        campoDiInput.setBackground(Color.DARK_GRAY);
        campoDiInput.requestFocusInWindow();
    }

}


