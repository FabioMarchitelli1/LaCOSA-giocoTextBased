package di.lacosa.tipi;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Classe che rappresenta un comando disponibile nel gioco.
 * Ogni comando ha un tipo, un nome principale e un insieme di alias.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author Autore sconosciuto
 * @version 1.0
 * @serial 2L Numero di versione per la serializzazione
 * @see TipoComando
 */
public class Comando implements Serializable {

    private static final long serialVersionUID = 2L;
    private final TipoComando tipo;
    private String nome;
    private Set<String> alias;

    /**
     * Costruttore base per creare un comando senza alias.
     *
     * @param tipo Tipo del comando (non null)
     * @param nome Nome principale del comando (non null o vuoto)
     * @throws IllegalArgumentException Se tipo o nome sono null
     */
    public Comando(TipoComando tipo, String nome) {
        this.tipo = tipo;
        this.nome = nome;
    }


    public Comando(TipoComando tipo , String nome, Set<String> alias) {
        this.tipo = tipo;
        this.nome = nome;
        this.alias = alias;
    }

    public TipoComando getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * è stato utilizzato un array per poi convertirlo in un hash-set per la facilità di inizzializzazione degli elemeneti
     */
    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    /**
     * Calcola l'hash code del comando basato su tipo, nome e alias.
     *
     * @return Valore hash calcolato
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.tipo);
        hash = 71 * hash + Objects.hashCode(this.nome);
        hash = 71 * hash + Objects.hashCode(this.alias);
        return hash;
    }

    /**
     * Confronta questo comando con un altro oggetto per verificarne l'uguaglianza.
     * Due comandi sono considerati uguali se hanno lo stesso tipo, nome e alias.
     *
     * @param obj Oggetto da confrontare
     * @return true se gli oggetti sono uguali, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comando other = (Comando) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (this.tipo != other.tipo) {
            return false;
        }
        return Objects.equals(this.alias, other.alias);
    }

}
