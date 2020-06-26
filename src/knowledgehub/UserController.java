package knowledgehub;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import beans.Employe;
import beans.Statut;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import service.AuteurFacade;
import service.EmployeFacade;
import service.OuvrageFacade;
import service.StatutFacade;
import util.Session;

/**
 * FXML Controller class
 *
 * @author CHAACHAI Youssef
 */
public class UserController implements Initializable {

    @FXML
    private TextField search;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField address;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField role;

    @FXML
    private TextField hiddenField;

    @FXML
    private Label close;
    @FXML
    private Label full_name;
    
    @FXML
    private AnchorPane anchor;
    @FXML
    private Pane pane;

    @FXML
    private TableView<Employe> usersTable = new TableView<Employe>();

    @FXML
    private TableColumn<Employe, String> nom_emp = new TableColumn<Employe, String>();
    @FXML
    private TableColumn<Employe, String> prenom_emp = new TableColumn<Employe, String>();
    @FXML
    private TableColumn<Employe, String> username_emp = new TableColumn<Employe, String>();
    @FXML
    private TableColumn<Employe, String> adresse_emp = new TableColumn<Employe, String>();
    @FXML
    private TableColumn<Employe, Statut> role_emp = new TableColumn<Employe, Statut>();

    StatutFacade statutFacade = new StatutFacade();
    EmployeFacade employeFacade = new EmployeFacade();

    @FXML
    private ComboBox<Statut> roleCombo = new ComboBox<>();

    /**
     * Initializes the controller class.
     */
    private void initComboRoles() {
        roleCombo.setItems(FXCollections.observableArrayList(statutFacade.getAllStatuts()));

    }

    public void initUsersTable() {
        usersTable.getItems().addAll(employeFacade.getAllEmployes());
        nom_emp.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_emp.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        username_emp.setCellValueFactory(new PropertyValueFactory<>("username"));
        adresse_emp.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        role_emp.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    public void mouseClickedTable() {
        Employe employe = usersTable.getSelectionModel().getSelectedItem();
        if (employe != null) {
            Session.updateAttribute(employe, "selectedEmploye");
            hiddenField.setText(employe.getId() + "");
            fname.setText(employe.getPrenom());
            lname.setText(employe.getNom());
            address.setText(employe.getAdresse());
            roleCombo.getSelectionModel().select(employe.getStatut());
        }
    }

    @FXML
    private void filterUsers() {
        usersTable.setItems(FXCollections.observableArrayList(employeFacade.findEmployes(search.getText())));
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

    @FXML
    private void toUsers(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "UserFX.fxml", this.getClass());
    }

// 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Employe employe = (Employe) Session.getAttribut("connectedUser");
        full_name.setText(employe.getPrenom() + " " + employe.getNom());
        initUsersTable();
        initComboRoles();
        
        if(employe.getStatut().getId() == 1){
            anchor.setVisible(false);
            pane.setVisible(false);
        }else{
            anchor.setVisible(true);
            pane.setVisible(true);
        }
    }

}
