package knowledgehub;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.Config;
import service.EmployeFacade;
import util.Session;

/**
 * FXML Controller class
 *
 * @author CHAACHAI Youssef
 */
public class LoginFXMLController implements Initializable {

    Config config = new Config();
    @FXML
    private ImageView toggle1_on;
    @FXML
    private ImageView toggle1_off;
    @FXML
    private ImageView toggle2_on;
    @FXML
    private ImageView toggle2_off;
    @FXML
    private Label close;

    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    @FXML
    private Label errorServer;
    @FXML
    private Label errorInputs;
    @FXML
    private Label errorLogin;
    @FXML
    private Label waiting;

    EmployeFacade employeFacade = new EmployeFacade();

    @FXML
    public void connect(ActionEvent actionEvent)  {
        hide_messages();
        if (testChamps()) {
            errorInputs.setVisible(false);
            waiting.setVisible(true);
            PauseTransition pause = new PauseTransition(
                    Duration.millis(100)
            );
            pause.setOnFinished(event -> {
                Connection con;
                if (isToggle1_on()) {
                    con = config.connect("system", "system", "localhost", "fst1");
                    if (con == null) {
                        waiting.setVisible(false);
                        errorServer.setVisible(true);
                    } else {
                        waiting.setVisible(false);
                        errorServer.setVisible(false);
                        System.out.println("FSTG");
                        int res = employeFacade.login(login.getText(), password.getText(), 1);
                        if (res != 1) {
                            errorLogin.setVisible(true);
                        } else {
                            errorLogin.setVisible(false);
                            try {
                                //FORWARD TO THE HOME PAGE NOW ...
                                KnowledgeHub.forward(actionEvent, "HomeFX.fxml", this.getClass());
                            } catch (IOException ex) {
                                Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                } else {
                    con = config.connect("system", "system", "192.168.1.107", "fst2");
                    if (con == null) {
                        waiting.setVisible(false);
                        errorServer.setVisible(true);
                    } else {
                        waiting.setVisible(false);
                        errorServer.setVisible(false);
                        System.out.println("FSSM");
                        int res = employeFacade.login(login.getText(), password.getText(), 2);
                        if (res != 1) {
                            errorLogin.setVisible(true);
                        } else {
                            errorLogin.setVisible(false);
                            try {
                                //FORWARD TO THE HOME PAGE NOW ...
                                KnowledgeHub.forward(actionEvent, "HomeFX.fxml", this.getClass());
                            } catch (IOException ex) {
                                Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });
            pause.play();
        } else {
            errorInputs.setVisible(true);
        }
    }

    private boolean testChamps() {
        if (login.getText().trim().equals("")) {
            return false;
        } else if (password.getText().trim().equals("")) {
            return false;
        } else {
            return true;
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

//    @FXML
//    private void toLogin2(ActionEvent actionEvent) throws IOException {
//        DBAProfiles.forward(actionEvent, "Login2FXML.fxml", this.getClass());
//    }
    private boolean isToggle1_on() {
        if (toggle1_on.isVisible() && !toggle1_off.isVisible()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isToggle2_on() {
        if (toggle2_on.isVisible() && !toggle2_off.isVisible()) {
            return true;
        } else {
            return false;
        }
    }

    private void setToggle1(boolean val) {
        if (val) {
            toggle1_on.setVisible(true);
            toggle1_off.setVisible(false);
        } else {
            toggle1_on.setVisible(false);
            toggle1_off.setVisible(true);
        }
    }

    private void setToggle2(boolean val) {
        if (val) {
            toggle2_on.setVisible(true);
            toggle2_off.setVisible(false);
        } else {
            toggle2_on.setVisible(false);
            toggle2_off.setVisible(true);
        }
    }

    @FXML
    public void toggle1_clicked() {
        if (isToggle1_on()) {
            setToggle1(false);
            setToggle2(true);
        } else {
            setToggle1(true);
            setToggle2(false);
        }
    }

    @FXML
    public void toggle2_clicked() {
        if (isToggle2_on()) {
            setToggle2(false);
            setToggle1(true);
        } else {
            setToggle2(true);
            setToggle1(false);
        }
    }
    
    private void hide_messages(){
        errorServer.setVisible(false);
        errorInputs.setVisible(false);
        errorLogin.setVisible(false);
        waiting.setVisible(false);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        errorMessage.setText("");
//        errorMessage.setVisible(false);
//        pane.setVisible(false);
        setToggle1(true);
        setToggle2(false);
        hide_messages();
        
    }

}
