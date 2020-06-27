package knowledgehub;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import beans.Employe;
import beans.Universite;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import service.Config;
import service.EmployeFacade;
import util.Session;

/**
 * FXML Controller class
 *
 * @author CHAACHAI Youssef
 */
public class SignupController implements Initializable {

    Config config = new Config();

    @FXML
    private Label close;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private ComboBox<Universite> facultyCombo;

    @FXML
    private TextField address;

    @FXML
    private Label errorUsername;
    @FXML
    private Label errorInputs;
    @FXML
    private Label errorServer;
    @FXML
    private Label waiting;

    EmployeFacade employeFacade = new EmployeFacade();

    public void initCombo() {
        List<Universite> universites = new ArrayList();
        Universite fstg = new Universite();
        Universite fssm = new Universite();
        fstg.setId(1);
        fstg.setNom("FSTG");
        fssm.setId(2);
        fssm.setNom("FSSM");
        universites.add(fstg);
        universites.add(fssm);
        facultyCombo.setItems(FXCollections.observableArrayList(universites));
    }

    @FXML
    public void toLogin(ActionEvent actionEvent) throws IOException {
        KnowledgeHub.forward(actionEvent, "LoginFXML.fxml", this.getClass());
    }

    public boolean isBlank(String value) {
        return (value == null || value.equals("") || value.equals("null") || value.trim().equals(""));
    }

    @FXML
    private void signup(ActionEvent actionEvent) {
        hide_messages();
        if (!isBlank(fname.getText()) && !isBlank(lname.getText()) && !isBlank(address.getText())
                && !isBlank(username.getText()) && !isBlank(password.getText()) && !(facultyCombo.getValue() == null)) {
            errorInputs.setVisible(false);
            waiting.setVisible(true);
            PauseTransition pause = new PauseTransition(
                    Duration.millis(10)
            );
            pause.setOnFinished(event -> {
                Connection con;
                if (facultyCombo.getValue().getId() == 1) {
                    con = config.connect("system", "system", "localhost", "fst1");
                    if (con != null) {
                        waiting.setVisible(false);
                        if (!employeFacade.usernameUsed(username.getText())) {
                            errorUsername.setVisible(false);
                            employeFacade.insertDb(fname.getText(), lname.getText(), address.getText(), username.getText(), password.getText(), facultyCombo.getValue().getId());
                            JOptionPane.showMessageDialog(null, "You have signed up succefully!\n You need the confirmation of the ADMIN to sign in.", "Complete registration!", JOptionPane.INFORMATION_MESSAGE);
                            try {
                                KnowledgeHub.forward(actionEvent, "LoginFXML.fxml", this.getClass());
                            } catch (IOException ex) {
                                Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            errorUsername.setVisible(true);
                        }
                    } else {
                        waiting.setVisible(false);
                        errorServer.setVisible(true);
                    }
                } else {
                    con = config.connect("system", "system", "192.168.1.107", "fssm");
                    if (con != null) {
                        waiting.setVisible(false);
                        if (!employeFacade.usernameUsed(username.getText())) {
                            errorUsername.setVisible(false);
                            employeFacade.insertDb(fname.getText(), lname.getText(), address.getText(), username.getText(), password.getText(), facultyCombo.getValue().getId());
                            JOptionPane.showMessageDialog(null, "You have signed up succefully!\n You need the confirmation of the ADMIN to sign in.", "Complete registration!", JOptionPane.INFORMATION_MESSAGE);
                            try {
                                KnowledgeHub.forward(actionEvent, "LoginFXML.fxml", this.getClass());
                            } catch (IOException ex) {
                                Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            errorUsername.setVisible(true);
                        }
                    } else {
                        waiting.setVisible(false);
                        errorServer.setVisible(true);
                    }
                }
            });
            pause.play();
        } else {
            errorInputs.setVisible(true);
        }
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

    private void hide_messages() {
        errorInputs.setVisible(false);
        errorUsername.setVisible(false);
        errorServer.setVisible(false);
        waiting.setVisible(false);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        hide_messages();
        initCombo();
    }

}
