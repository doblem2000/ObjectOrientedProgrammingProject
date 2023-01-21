package gruppo57;

import java.io.Serializable;


/**
 * La classe Contact definisce l'entità contatto formata da un nome, cognome e numero
 * 
 * @author  gruppo57
 * @version 1.0
 */
public class Contact implements Serializable{
    private String nome;
    private String cognome;
    private String numero;
    /**
     * Costruttore della classe Contact
     * @param nome-nome del contatto
     * @param cognome-cognome del contatto
     * @param numero-numero telefonico del contatto
     */
    public Contact(String nome, String cognome, String numero) {
        this.nome = nome;
        this.cognome = cognome;
        this.numero = numero;
    }
    /**
     * Restituisce il nome del contatto
     * 
     * @return una stringa contenente il nome
     */
    public String getNome() {
        return nome;
    }
    /**
     * Imposta il nome del contatto
     * 
     * @param  nome-nome del contatto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    /**
     * Restituisce il cognome del contatto
     * 
     * @return una stringa contenente il cognome
     */
    public String getCognome() {
        return cognome;
    }
    /**
     * Imposta il cognome del contatto
     * 
     * @param  cognome-cognome del contatto
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    /**
     * Restituisce il numero del contatto
     * 
     * @return una stringa contenente il numero del contatto
     */
    public String getNumero() {
        return numero;
    }
    /**
     * Imposta il numero telefonico del contatto
     * 
     * @param  numero-numero telefonico del contatto
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }
    /**
     * Confronta se due contatti sono uguali, ossia se hanno tutti gli attributi uguali
     * 
     * @param  obj-istanza dell'oggetto contatto
     * @return true-se i due oggetti sono uguali
     *         false-se i due oggetti sono diversi
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
        final Contact other = (Contact) obj;
        
        return this.nome.equals(other.getNome()) && this.cognome.equals(other.getCognome()) && this.numero.equals(other.getNumero());
    }
    /**
     * Converte l'oggetto in una stringa 
     * 
     * @return la stringa dell'oggetto
     */
    @Override
    public String toString() {
        return nome + ";" + cognome + ";" + numero + ";";
    }
}
