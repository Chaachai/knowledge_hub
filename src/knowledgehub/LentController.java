package knowledgehub;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import beans.Auteur;
import beans.Domaine;
import beans.Employe;
import beans.Etudiant;
import beans.Ouvrage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import service.AuteurFacade;
import service.DomaineFacade;
import service.EtudiantFacade;
import service.OuvrageFacade;
import service.PretFacade;
import util.Session;

/**
 * FXML Controller class
 *
 * @author CHAACHAI Youssef
 */
public class LentController implements Initializable {

    @FXML
    private TextField search;
    @FXML
    private TextField searchStudent;

    @FXML
    private Label bookTitle;

    @FXML
    private Label bookAuthor;

    @FXML
    private Label year;

    @FXML
    private Label stock;

    @FXML
    private Label field;

    @FXML
    private Label student_name;
    @FXML
    private Label student_faculty;
    @FXML
    private Label student_sector;
    @FXML
    private Label student_address;
    @FXML
    private Label student_borrowings;
    @FXML
    private Label student_blocked;

    @FXML
    private Label booknotSelected;
    @FXML
    private Label studentNotFound;
    @FXML
    private Label provideAllInfo;

    @FXML
    private Label close;
    @FXML
    private Label full_name;

    @FXML
    private Label lendbookTitle;
    @FXML
    private Label lendbookStudent;
    @FXML
    private Label lendbookBorrowDate;

    @FXML
    private DatePicker lendbookExpectedDate;

    Employe employeSession = (Employe) Session.getAttribut("connectedUser");

    @FXML
    private TableView<Ouvrage> ouvragesTable = new TableView<Ouvrage>();

    @FXML
    private TableColumn<Ouvrage, String> ouvrage_titre = new TableColumn<Ouvrage, String>();

    OuvrageFacade ouvrageFacade = new OuvrageFacade();
    AuteurFacade auteurFacade = new AuteurFacade();
    DomaineFacade domaineFacade = new DomaineFacade();
    PretFacade pretFacade = new PretFacade();
    EtudiantFacade etudiantFacade = new EtudiantFacade();

    @FXML
    private ComboBox<Auteur> auteursCombo = new ComboBox<>();

    /**
     * Initializes the controller class.
     */
    public void initOuvragesTable() {
        ouvragesTable.getItems().addAll(ouvrageFacade.getAllOuvrages(employeSession.getUniversite().getId()));
        ouvrage_titre.setCellValueFactory(new PropertyValueFactory<>("titre"));

    }

    public void mouseClickedTable() {
        Ouvrage ouvrage = ouvragesTable.getSelectionModel().getSelectedItem();
        if (ouvrage != null) {
            Session.updateAttribute(ouvrage, "selectedOuvrage");
            bookTitle.setText(ouvrage.getTitre());
            bookAuthor.setText(ouvrage.getAuteur().getNom() + " " + ouvrage.getAuteur().getPrenom());
            year.setText(ouvrage.getAnnee());
            stock.setText(ouvrage.getStock() + "");
            field.setText(ouvrage.getDomaine().getNom() + "");
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

    public void selectbook() {
//        Ouvrage ouvrage = ouvragesTable.getSelectionModel().getSelectedItem();
        if (!isBlank(bookTitle.getText())) {
            lendbookTitle.setText(bookTitle.getText());
        } else {
            booknotSelected.setVisible(true);
        }
    }

    public void showStudent() {
        Etudiant etudiant = etudiantFacade.getEtudiantByCne(searchStudent.getText());
        if (!isBlank(searchStudent.getText()) && etudiant != null) {
            Session.updateAttribute(etudiant, "selectedEtudiant");
            studentNotFound.setVisible(false);
            student_name.setText(etudiant.getNom().toUpperCase() + " " + etudiant.getPrenom());
            student_address.setText(etudiant.getAdresse());
            student_faculty.setText(etudiant.getUniversite().getNom());
            student_sector.setText(etudiant.getCursus().getNom());
            if (etudiant.getBloque() == 1) {
                student_blocked.setText("YES");
            } else {
                student_blocked.setText("No");
            }
            if (etudiant.getNb_emprunts() >= 2) {
                student_borrowings.setText(etudiant.getNb_emprunts() + " Times");
            } else {
                student_borrowings.setText(etudiant.getNb_emprunts() + " Time");
            }

        } else {
            studentNotFound.setVisible(true);
            student_name.setText("");
            student_address.setText("");
            student_blocked.setText("");
            student_sector.setText("");
            student_borrowings.setText("");
            student_faculty.setText("");
        }
    }

    public void selectstudent() {
        if (!isBlank(student_name.getText())) {
            lendbookStudent.setText(student_name.getText());
        } else {
            studentNotFound.setVisible(true);
        }
    }

    public void insertPret(ActionEvent actionEvent) throws IOException {
        if (!isBlank(lendbookStudent.getText()) && !isBlank(lendbookTitle.getText()) && lendbookExpectedDate.getValue() != null) {
            provideAllInfo.setVisible(false);
            Ouvrage ouvrage = (Ouvrage) Session.getAttribut("selectedOuvrage");
            if (ouvrage.getStock() == 0) {
                JOptionPane.showMessageDialog(null, "Sorry, it seems that this book is out of stock right now, please come back later", "Out of stock ", JOptionPane.OK_OPTION);
            } else {
                Etudiant etudiant = (Etudiant) Session.getAttribut("selectedEtudiant");
                if (etudiant.getNb_emprunts() == 3) {
                    JOptionPane.showMessageDialog(null, "Sorry, it seems that this student has exceeded the limit number of borrowings", "Limit exceeded", JOptionPane.OK_OPTION);
                } else {
                    Date input = new Date();
                    LocalDate mDate = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    pretFacade.insertDb(etudiant.getId(), ouvrage.getId(), mDate, lendbookExpectedDate.getValue());
                    JOptionPane.showMessageDialog(null, "The operation is done successfully :)", "Success", JOptionPane.INFORMATION_MESSAGE);
                    KnowledgeHub.forward(actionEvent, "LentFX.fxml", this.getClass());
                    Session.delete("selectedEtudiant");
                    Session.delete("selectedOuvrage");
                }
            }
        } else {
            provideAllInfo.setVisible(true);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Employe employe = (Employe) Session.getAttribut("connectedUser");
        full_name.setText(employe.getPrenom() + " " + employe.getNom());
        initOuvragesTable();

        searchStudent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    searchStudent.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy");
        lendbookBorrowDate.setText(mdyFormat.format(new Date()));
    }

}
