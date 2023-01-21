package gruppo57;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane loginPane;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private Button submitPassword;
    @FXML
    private Label wrongPasswordLbl;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private TableView<Contact> table;
    @FXML
    private MenuItem cancellaContatto;
    @FXML
    private MenuItem copiaContatto;
    @FXML
    private TableColumn<Contact, String> nameCol;
    @FXML
    private TableColumn<Contact, String> surnameCol;
    @FXML
    private TableColumn<Contact, String> numberCol;
    @FXML
    private Button addContact;
    @FXML
    private MenuItem saveBtn;
    @FXML
    private TextField nome;
    @FXML
    private TextField cognome;
    @FXML
    private TextField numero;
    
    private Thread otpGenerator;
    private IntegerProperty otp;
    private ObservableList<Contact> contactList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contactList= FXCollections.observableArrayList();
        table.setItems(contactList);
        
        List<Contact> backup = SaveService.loadContacts("save.bin");
        if(backup!=null){
            contactList.addAll(backup);
        }
        
        nameCol.setCellValueFactory(new PropertyValueFactory("nome"));
        surnameCol.setCellValueFactory(new PropertyValueFactory("cognome"));
        numberCol.setCellValueFactory(new PropertyValueFactory("numero"));
        
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        numberCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        addContact.disableProperty().bind(nome.textProperty().isEmpty().or(cognome.textProperty().isEmpty().or(numero.textProperty().isEmpty())));
        cancellaContatto.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        copiaContatto.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        submitPassword.disableProperty().bind(loginPassword.textProperty().isEmpty());
        
        numero.setTextFormatter(new TextFormatter<String>(change->{
            String newText = change.getControlNewText();
            if(newText.matches("\\+?\\d*"))
                return change;
            else
                return null;
        }));
        
        otp = new SimpleIntegerProperty(46);
        otpGenerator = new Thread(new GenerateOTP(10,"otp.txt",otp));
        otpGenerator.setDaemon(true);
        otpGenerator.start();
    }    

    @FXML
    private void onSubmitPassword(ActionEvent event) {
        synchronized(otp){
            if(loginPassword.getText().equals(otp.getValue().toString())){
                otpGenerator.interrupt();
                loginPane.setVisible(false);
                mainPane.setVisible(true);
            }
            else{
                wrongPasswordLbl.setVisible(true);
            }
        }
    }

    @FXML
    private void onCancellaContatto(ActionEvent event) {
        Contact c= table.getSelectionModel().getSelectedItem();
        contactList.remove(c);
        saveBtn.setDisable(false);
    }

    @FXML
    private void onCopiaContatto(ActionEvent event) {
        Contact c = table.getSelectionModel().getSelectedItem();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(c.toString());
        clipboard.setContent(content);
    }


    @FXML
    private void onAddContact(ActionEvent event) {
        Contact c = new Contact(nome.getText(),cognome.getText(),numero.getText());
        if(contactList.contains(c)){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText("Contatto già presente");
            alert.showAndWait();
            return;
        }
        contactList.add(c);
        nome.clear();
        cognome.clear();
        numero.clear();
        saveBtn.setDisable(false);
    }

    @FXML
    private void onSave(ActionEvent event) {
        SaveService s= new SaveService(new LinkedList(contactList), "save.bin");
        /*s.setOnSucceeded(eventService ->{
            System.out.println("Salvataggio effettuato");
        });*/
        s.start();
        saveBtn.setDisable(true);
    }

    @FXML
    private void onEsci(ActionEvent event) {
        Platform.exit();
    }
    @FXML
    private void onNameEdit(TableColumn.CellEditEvent<Contact, String> event) {
        Contact cEsi= table.getSelectionModel().getSelectedItem();
        String nMod=event.getNewValue();
        Contact cMod= new Contact(nMod, cEsi.getCognome(),  cEsi.getNumero());
        
        if(cEsi.equals(cMod))
            return;
        
        if(contactList.contains(cMod)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText("Contatto già esistente");
            alert.showAndWait();
            return;
        }
        
        cEsi.setNome(nMod);
        saveBtn.setDisable(false);
    }
    @FXML
    private void onSurnameEdit(TableColumn.CellEditEvent<Contact, String> event) {
        Contact cEsi= table.getSelectionModel().getSelectedItem();
        String sMod=event.getNewValue();
        Contact cMod= new Contact(cEsi.getNome(), sMod,  cEsi.getNumero());
        
        if(cEsi.equals(cMod))
            return;
        
        if(contactList.contains(cMod)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText("Contatto già esistente");
            alert.showAndWait();
            return;
        }
        
        cEsi.setCognome(sMod);
        saveBtn.setDisable(false);
    }
    @FXML
    private void onNumberEdit(TableColumn.CellEditEvent<Contact, String> event) {
        Contact cEsi= table.getSelectionModel().getSelectedItem();
        String bMod=event.getNewValue();
        Contact cMod= new Contact(cEsi.getNome(), cEsi.getCognome(), bMod);
        
        if(cEsi.equals(cMod))
            return;
        
        if(contactList.contains(cMod)){
            Alert alert= new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Errore");
            alert.setContentText("Contatto già esistente");
            alert.showAndWait();
            return;
        }
        
        if(!bMod.matches("\\+?\\d*")){
            return;
        }
            
        
        cEsi.setNumero(bMod);
        saveBtn.setDisable(false);
    }

}
