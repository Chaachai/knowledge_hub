package knowledgehub;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import beans.Auteur;
import beans.Domaine;
import beans.Employe;
import beans.Ouvrage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import javafx.scene.control.Alert;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import service.AuteurFacade;
import service.DomaineFacade;
import service.OuvrageFacade;
import util.Session;

/**
 * FXML Controller class
 *
 * @author CHAACHAI Youssef
 */
public class OuvrageController implements Initializable {

    @FXML
    private TextField search;

    @FXML
    private TextField bookTitle;

    @FXML
    private TextField bookPublisher;

    @FXML
    private TextField year;

    @FXML
    private TextField stock;

    @FXML
    private TextField soldOut;

    @FXML
    private ComboBox<Domaine> fieldCombo;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField field;
    @FXML
    private TextField hiddenField;

    @FXML
    private Label close;
    @FXML
    private Label full_name;

    Employe employeSession = (Employe) Session.getAttribut("connectedUser");

    @FXML
    private TableView<Ouvrage> ouvragesTable = new TableView<Ouvrage>();

    @FXML
    private TableColumn<Ouvrage, String> ouvrage_titre = new TableColumn<Ouvrage, String>();

    @FXML
    private TableColumn<Ouvrage, String> ouvrage_editeur = new TableColumn<Ouvrage, String>();
    @FXML
    private TableColumn<Ouvrage, String> ouvrage_annee = new TableColumn<Ouvrage, String>();
    @FXML
    private TableColumn<Ouvrage, Integer> ouvrage_stock = new TableColumn<Ouvrage, Integer>();
    @FXML
    private TableColumn<Ouvrage, Auteur> ouvrage_auteur = new TableColumn<Ouvrage, Auteur>();
    @FXML
    private TableColumn<Ouvrage, Domaine> ouvrage_domaine = new TableColumn<Ouvrage, Domaine>();
    @FXML
    private TableColumn<Ouvrage, Integer> ouvrage_ruptures = new TableColumn<Ouvrage, Integer>();

    OuvrageFacade ouvrageFacade = new OuvrageFacade();
    AuteurFacade auteurFacade = new AuteurFacade();
    DomaineFacade domaineFacade = new DomaineFacade();

    @FXML
    private ComboBox<Auteur> auteursCombo = new ComboBox<>();

    /**
     * Initializes the controller class.
     */
    private void initComboAuteurs() {
        auteursCombo.setItems(FXCollections.observableArrayList(auteurFacade.getAllAuteurs()));

    }

    private void initComboFields() {
        fieldCombo.setItems(FXCollections.observableArrayList(domaineFacade.getAllFields()));

    }

    public void initOuvragesTable() {
        ouvragesTable.getItems().addAll(ouvrageFacade.getAllOuvrages(employeSession.getUniversite().getId()));
        ouvrage_titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        ouvrage_editeur.setCellValueFactory(new PropertyValueFactory<>("editeur"));
        ouvrage_annee.setCellValueFactory(new PropertyValueFactory<>("annee"));
        ouvrage_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ouvrage_auteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        ouvrage_domaine.setCellValueFactory(new PropertyValueFactory<>("domaine"));
        ouvrage_ruptures.setCellValueFactory(new PropertyValueFactory<>("nb_ruptures"));
    }

    public void mouseClickedTable() {
        Ouvrage ouvrage = ouvragesTable.getSelectionModel().getSelectedItem();
        if (ouvrage != null) {
            Session.updateAttribute(ouvrage, "selectedOuvrage");
            hiddenField.setText(ouvrage.getId() + "");
            bookTitle.setText(ouvrage.getTitre());
            bookPublisher.setText(ouvrage.getEditeur());
            year.setText(ouvrage.getAnnee());
            stock.setText(ouvrage.getStock() + "");
            soldOut.setText(ouvrage.getNb_ruptures() + "");
            auteursCombo.getSelectionModel().select(ouvrage.getAuteur());
            fieldCombo.getSelectionModel().select(ouvrage.getDomaine());
        }
    }

    public boolean isBlank(String value) {
        return (value == null || value.equals("") || value.equals("null") || value.trim().equals(""));
    }

    public boolean isOnlyNumber(String value) {
        boolean ret = false;
        if (!isBlank(value)) {
            ret = value.matches("^[0-9]+$");
        }
        return ret;
    }

    @FXML
    private void insertOuvrage(ActionEvent event) {
        if (!isBlank(bookTitle.getText())) {
            if (!isBlank(stock.getText())) {
                ouvrageFacade.insertDb(bookTitle.getText(), bookPublisher.getText(), year.getText(), Integer.valueOf(stock.getText()), auteursCombo.getValue().getId(), fieldCombo.getValue().getId(), employeSession.getUniversite().getId());
                ouvragesTable.setItems(FXCollections.observableArrayList(ouvrageFacade.getAllOuvrages(employeSession.getUniversite().getId())));
                bookTitle.clear();
                bookPublisher.clear();
                stock.clear();
                auteursCombo.setValue(null);
                fieldCombo.setValue(null);
                soldOut.clear();
            } else {
                JOptionPane.showMessageDialog(null, "Stock quantity is required!", "Stock quantity is required", JOptionPane.OK_OPTION);
            }
        } else {
            JOptionPane.showMessageDialog(null, "The book title is required!", "Book title is required!", JOptionPane.OK_OPTION);
        }
    }

    @FXML
    private void updateOuvrage(ActionEvent event) {
        if (isBlank(hiddenField.getText())) {
            JOptionPane.showMessageDialog(null, "Select a book to update !", "No book was selected", JOptionPane.OK_OPTION);
        } else {
            if (!isBlank(bookTitle.getText())) {
                if (!isBlank(stock.getText())) {
                    ouvrageFacade.updateDb(Integer.valueOf(hiddenField.getText()), bookTitle.getText(), bookPublisher.getText(), year.getText(), Integer.valueOf(stock.getText()), auteursCombo.getValue().getId(), fieldCombo.getValue().getId(), employeSession.getUniversite().getId());
                    ouvragesTable.setItems(FXCollections.observableArrayList(ouvrageFacade.getAllOuvrages(employeSession.getUniversite().getId())));
                } else {
                    JOptionPane.showMessageDialog(null, "Stock quantity is required!", "Stock quantity is required", JOptionPane.OK_OPTION);
                }
            } else {
                JOptionPane.showMessageDialog(null, "The book title is required!", "Book title is required!", JOptionPane.OK_OPTION);
            }
        }
    }

    @FXML
    private void deleteBook() {
        if (hiddenField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select a book to delete !", "No book was selected", JOptionPane.OK_OPTION);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("WARNING !!");
            alert.setContentText("You are about to delete the book named '"
                    + bookTitle.getText().toUpperCase() + "'. \nAre you sure about that ?");
            alert.setTitle("WARNING !!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                ouvrageFacade.deleteDb(Integer.valueOf(hiddenField.getText()));
                ouvragesTable.setItems(FXCollections.observableArrayList(ouvrageFacade.getAllOuvrages(employeSession.getUniversite().getId())));
                hiddenField.clear();
            }
        }
    }

    @FXML
    private void insertAuteur(ActionEvent event) {
        if (!isBlank(firstName.getText()) || !isBlank(lastName.getText())) {
            if (!isOnlyNumber(firstName.getText()) && !isOnlyNumber(lastName.getText())) {
                auteurFacade.insertDb(lastName.getText(), firstName.getText());
                initComboAuteurs();
                firstName.clear();
                lastName.clear();
                JOptionPane.showMessageDialog(null, "The Author was added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "The name is invalid!", "Invalid name", JOptionPane.OK_OPTION);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Enter at least one name!", "Invalid name", JOptionPane.OK_OPTION);
        }

    }

    @FXML
    private void insertField(ActionEvent event) {
        if (!isBlank(field.getText())) {
            domaineFacade.insertDb(field.getText());
            initComboFields();
            field.clear();
            JOptionPane.showMessageDialog(null, "The Field was added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "The field name is required!", "Invalid name", JOptionPane.OK_OPTION);
        }
    }

    @FXML
    private void filterBooks() {
        ouvragesTable.setItems(FXCollections.observableArrayList(ouvrageFacade.findOuvrages(search.getText(), employeSession.getUniversite().getId())));
    }

    @FXML
    public void closeApp() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void minimizeApp(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("WARNING !");
        alert.setContentText("Are you sure you want to logout?");
        alert.setTitle("WARNING !");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Session.clear();
            KnowledgeHub.forward(actionEvent, "LoginFXML.fxml", this.getClass());
        }
    }

    @FXML
    private void toOuvrages(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "OuvrageFX.fxml", this.getClass());
    }

    @FXML
    private void toReturnBooks(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "ReturnBookFX.fxml", this.getClass());
    }

    @FXML
    private void toUsers(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "UserFX.fxml", this.getClass());
    }

    @FXML
    private void toLendingBooks(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "LentFX.fxml", this.getClass());
    }

    @FXML
    private void toProfile(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "ProfileFX.fxml", this.getClass());
    }

// 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO
        Employe employe = (Employe) Session.getAttribut("connectedUser");
        full_name.setText(employe.getPrenom() + " " + employe.getNom());
        initOuvragesTable();
        initComboAuteurs();
        initComboFields();

        stock.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    stock.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        year.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    year.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

}
