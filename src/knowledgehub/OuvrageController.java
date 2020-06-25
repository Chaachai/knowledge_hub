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
    private Label close;
    @FXML
    private Label full_name;

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
        ouvragesTable.getItems().addAll(ouvrageFacade.getAllOuvrages());
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
            bookTitle.setText(ouvrage.getTitre());
            bookPublisher.setText(ouvrage.getEditeur());
            year.setText(ouvrage.getAnnee());
            stock.setText(ouvrage.getStock() + "");
            soldOut.setText(ouvrage.getNb_ruptures() + "");
            auteursCombo.getSelectionModel().select(ouvrage.getAuteur());
            fieldCombo.getSelectionModel().select(ouvrage.getDomaine());
        }
    }

    @FXML
    private void filterBooks() {
//        System.out.println("filtering ...");
//        for (int i = 0; i < ouvragesTable.getItems().size(); i++) {
//            ouvragesTable.getItems().clear();
//        }
//        ouvragesTable.getItems().removeAll();
//        ouvragesTable.getItems().addAll(ouvrageFacade.findOuvrages(search.getText().toUpperCase()));
        ouvragesTable.setItems(FXCollections.observableArrayList(ouvrageFacade.findOuvrages(search.getText())));
//        usersFXHelper.setList(ouvrageFacade.findOuvrages(search.getText().toUpperCase()));
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
    private void toHome(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "HomeFX.fxml", this.getClass());
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
    }

}