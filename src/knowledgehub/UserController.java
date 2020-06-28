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
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
    private ImageView toggle_on;
    @FXML
    private ImageView toggle_off;

    @FXML
    private Label emp_full_name;

    @FXML
    private Label username;

    @FXML
    private Label address;

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

    Employe employeSession = (Employe) Session.getAttribut("connectedUser");

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

    @FXML
    private TableView<Employe> usersTable2 = new TableView<Employe>();

    @FXML
    private TableColumn<Employe, String> nom_emp2 = new TableColumn<Employe, String>();
    @FXML
    private TableColumn<Employe, String> prenom_emp2 = new TableColumn<Employe, String>();

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
        usersTable.getItems().addAll(employeFacade.getAllEmployes(employeSession.getUniversite().getId()));
        nom_emp.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_emp.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        username_emp.setCellValueFactory(new PropertyValueFactory<>("username"));
        adresse_emp.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        role_emp.setCellValueFactory(new PropertyValueFactory<>("statut"));
    }

    public void initUsersTable2() {
        usersTable2.getItems().addAll(employeFacade.getPendingEmployes(employeSession.getUniversite().getId()));
        nom_emp2.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_emp2.setCellValueFactory(new PropertyValueFactory<>("prenom"));
    }

    public void mouseClickedTable() {
        Employe employe = usersTable.getSelectionModel().getSelectedItem();
        if (employe != null) {
            Session.updateAttribute(employe, "selectedEmploye");
            hiddenField.setText(employe.getId() + "");
            emp_full_name.setText(employe.getPrenom() + " " + employe.getNom());
            username.setText(employe.getUsername());
            address.setText(employe.getAdresse());
            roleCombo.getSelectionModel().select(employe.getStatut());
            if (employe.getAccepted() == 0) {
                setToggle(false);
            } else {
                setToggle(true);
            }
        }
    }

    public void mouseClickedTable2() {
        Employe employe = usersTable2.getSelectionModel().getSelectedItem();
        if (employe != null) {
            Session.updateAttribute(employe, "selectedEmploye");
            hiddenField.setText(employe.getId() + "");
            emp_full_name.setText(employe.getPrenom() + " " + employe.getNom());
            username.setText(employe.getUsername());
            address.setText(employe.getAdresse());
            roleCombo.getSelectionModel().select(employe.getStatut());
            if (employe.getAccepted() == 0) {
                setToggle(false);
            } else {
                setToggle(true);
            }
        }
    }

    @FXML
    private void filterUsers() {
        usersTable.setItems(FXCollections.observableArrayList(employeFacade.findEmployes(search.getText(), employeSession.getUniversite().getId())));
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

    private boolean isToggle_on() {
        return (toggle_on.isVisible() && !toggle_off.isVisible());
    }

    private void setToggle(boolean val) {
        if (val) {
            toggle_on.setVisible(true);
            toggle_off.setVisible(false);
        } else {
            toggle_on.setVisible(false);
            toggle_off.setVisible(true);
        }
    }

    @FXML
    private void comboChanged(ActionEvent event) {
        if (!hiddenField.getText().isEmpty()) {
            Employe employe = (Employe) Session.getAttribut("selectedEmploye");
            employe.setStatut(roleCombo.getValue());
            employeFacade.updateDb(employe);
            usersTable.setItems(FXCollections.observableArrayList(employeFacade.getAllEmployes(employeSession.getUniversite().getId())));
            usersTable2.setItems(FXCollections.observableArrayList(employeFacade.getPendingEmployes(employeSession.getUniversite().getId())));
        }
    }

//    @FXML
//    public void delete_user(ActionEvent actionevent){
//        if (!hiddenField.getText().isEmpty()) {
//            Employe employe = (Employe) Session.getAttribut("selectedEmploye");
//            
//        }
//    }
    @FXML
    public void toggle_clicked() {
        if (!hiddenField.getText().isEmpty()) {
            Employe employe = (Employe) Session.getAttribut("selectedEmploye");
            if (isToggle_on()) {
                employe.setAccepted(0);
                setToggle(false);
            } else {
                employe.setAccepted(1);
                setToggle(true);
            }
            employeFacade.updateDb(employe);
            usersTable.setItems(FXCollections.observableArrayList(employeFacade.getAllEmployes(employeSession.getUniversite().getId())));
            usersTable2.setItems(FXCollections.observableArrayList(employeFacade.getPendingEmployes(employeSession.getUniversite().getId())));
        }
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
        initUsersTable2();
        initComboRoles();
        setToggle(false);

        if (employe.getStatut().getId() == 1) {
            anchor.setVisible(false);
            pane.setVisible(false);
        } else {
            anchor.setVisible(true);
            pane.setVisible(true);
        }
    }

}
