package gruppo57;

import java.io.*;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * La classe SaveService è un service che si occupa di salvare su un file binario la lista
 * 
 * @author  gruppo57
 * @version 1.0
 */
public class SaveService extends Service<Boolean>{
    private final List<Contact> list;
    private final String filename;
    
    /**
     * Costruttore della classe SaveService
     * @param list-lista dei contatti che verranno salvati nel file
     * @param filename-nome del file sul quale verranno salvati i contatti
     */
    public SaveService(List<Contact> list, String filename) {
        this.list = list;
        this.filename = filename;
    }
    /**
     * Questo metodo statico permette di caricare da un file di backup i contatti salvati
     * @param nomefile-nome del file dal quale leggere i contatti
     * @return null se il file non esiste oppure se la lettura del file non è andata a buon fine 
     *         una lista che contenente i contatti
     */
    public static List<Contact> loadContacts(String nomefile){
        List<Contact> l=null;
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomefile)))){
            l=(List<Contact>) in.readObject();
            return l;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    /**
     * Questo metodo consente di creare il task che si occupa di scrivere su un file i contatti
     * @return true-se il salvataggio è andato a buon fine
     *         false-se si verifica un IOException
     */
    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean> () {
            @Override
            protected Boolean call() throws Exception {
                try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))){
                    out.writeObject(list);
                }
                catch(IOException e){
                    return false;
                }
                return true;
            }
        };
    }

    
}
