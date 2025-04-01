package di.lacosa.tipi;

import java.io.Serializable;
import java.util.Set;

/**
 * Classe astratta base che rappresenta un oggetto generico nel gioco.
 * Fornisce le proprietà fondamentali e il comportamento base per tutti gli oggetti.
 * Implementa Serializable per permettere il salvataggio dello stato.
 *
 * @author fabioMarchitelli
 * @serial 5L Numero di versione per la serializzazione
 */
public abstract class Oggetto implements Serializable {

    private static final long serialVersionUID = 5L;
    private final int id;
    private String nome;
    private String descrizione;
    private Set<String> alias;
    private int id_stanza;

    public Oggetto(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Oggetto(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public Oggetto(int id, String nome, String descrizione, Set<String> alias, int id_stanza) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.alias = alias;
        this.id_stanza = id_stanza;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    public int getId_stanza() {
        return id_stanza;
    }

    public void setId_stanza(int id_stanza) {
        this.id_stanza = id_stanza;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
        return hash;
    }

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
        final Oggetto other = (Oggetto) obj;
        return this.id == other.id;
    }

    public abstract void interagisci();

}

