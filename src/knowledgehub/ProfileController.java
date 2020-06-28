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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import service.EmployeFacade;
import service.StatutFacade;
import util.Session;

/**
 * FXML Controller class
 *
 * @author CHAACHAI Youssef
 */
public class ProfileController implements Initializable {

    @FXML
    private Label close;
    @FXML
    private Label full_name;

    @FXML
    private Label avatar_full_name;

    @FXML
    private Label role;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField username;

    @FXML
    private TextField adresse;

    @FXML
    private PasswordField password;

    EmployeFacade employeFacade = new EmployeFacade();

    @FXML
    private ComboBox<Statut> roleCombo = new ComboBox<>();

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

    @FXML
    private void updateProfile(ActionEvent actionEvent) throws IOException {
        if (!isBlank(fname.getText()) && !isBlank(lname.getText()) && !isBlank(adresse.getText()) && !isBlank(username.getText()) && !isBlank(password.getText())) {
            Employe employe = (Employe) Session.getAttribut("connectedUser");
            if (!employe.getUsername().equalsIgnoreCase(username.getText())) {
                if (employeFacade.usernameUsed(username.getText())) {
                    JOptionPane.showMessageDialog(null, "The username is already taken", "Error!", JOptionPane.OK_OPTION);
                    return;
                } else {
                    employe.setPrenom(fname.getText());
                    employe.setNom(lname.getText());
                    employe.setAdresse(adresse.getText());
                    employe.setUsername(username.getText());
                    employe.setPassword(password.getText());
                }
            } else {
                employe.setPrenom(fname.getText());
                employe.setNom(lname.getText());
                employe.setAdresse(adresse.getText());
                employe.setUsername(username.getText());
                employe.setPassword(password.getText());
            }
            employeFacade.updateDb(employe);
            JOptionPane.showMessageDialog(null, "Your profile has been updated successfully", "Success!", JOptionPane.INFORMATION_MESSAGE);
            KnowledgeHub.forward(actionEvent, "ProfileFX.fxml", this.getClass());
        } else {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Error!", JOptionPane.OK_OPTION);
        }
    }

    public boolean isBlank(String value) {
        return (value == null || value.equals("") || value.equals("null") || value.trim().equals(""));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Employe employe = (Employe) Session.getAttribut("connectedUser");
        full_name.setText(employe.getPrenom() + " " + employe.getNom());
        avatar_full_name.setText(employe.getPrenom() + " " + employe.getNom());
        fname.setText(employe.getPrenom());
        lname.setText(employe.getNom());
        adresse.setText(employe.getAdresse());
        username.setText(employe.getUsername());
        password.setText(employe.getPassword());
        role.setText(employe.getStatut().getNom() + " at " + employe.getUniversite().getNom());

    }

}
