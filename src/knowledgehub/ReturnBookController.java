package knowledgehub;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import beans.Employe;
import beans.Etudiant;
import beans.Ouvrage;
import beans.Pret;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javafx.scene.control.Alert;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import service.EtudiantFacade;
import service.OuvrageFacade;
import service.PretFacade;
import util.Session;

/**
 * FXML Controller class
 *
 * @author CHAACHAI Youssef
 */
public class ReturnBookController implements Initializable {

    @FXML
    private Label close;
    @FXML
    private Label full_name;
    @FXML
    private Label student_name;
    @FXML
    private Label cne;
    @FXML
    private Label faculty;

    @FXML
    private Label Sector;
    @FXML
    private Label adress;

    @FXML
    private Label book_title;
    @FXML
    private Label book_author;
    @FXML
    private Label year;

    @FXML
    private Label field;

    @FXML
    private Label borrowing_date;
    @FXML
    private Label return_expected;
    @FXML
    private Label state;
    @FXML
    private Label nothingSelected;

    @FXML
    private TextField search;

    @FXML
    private TableView<Pret> pretsTable = new TableView<Pret>();

    @FXML
    private TableColumn<Pret, Etudiant> pret_etudiant = new TableColumn<Pret, Etudiant>();
    @FXML
    private TableColumn<Pret, Ouvrage> pret_ouvrage = new TableColumn<Pret, Ouvrage>();
    @FXML
    private TableColumn<Pret, Date> pret_date = new TableColumn<Pret, Date>();

    PretFacade pretFacade = new PretFacade();
    EtudiantFacade etudiantFacade = new EtudiantFacade();
    OuvrageFacade ouvrageFacade = new OuvrageFacade();
    Employe employeSession = (Employe) Session.getAttribut("connectedUser");

    /**
     * Initializes the controller class.
     */
    public void initPretsTable() {
        pretsTable.getItems().addAll(pretFacade.getPendingPrets(employeSession.getUniversite().getId()));
        pret_etudiant.setCellValueFactory(new PropertyValueFactory<>("etudiant"));
        pret_ouvrage.setCellValueFactory(new PropertyValueFactory<>("ouvrage"));
        pret_date.setCellValueFactory(new PropertyValueFactory<>("date_emprunt"));
    }

    public void mouseClickedTable() throws ParseException {
        Pret pret = pretsTable.getSelectionModel().getSelectedItem();
        if (pret != null) {
            Session.updateAttribute(pret, "selectedPret");
            student_name.setText(pret.getEtudiant().getNom().toUpperCase() + " " + pret.getEtudiant().getPrenom());
            cne.setText(pret.getEtudiant().getCne());
            faculty.setText(pret.getEtudiant().getUniversite().getNom());
            Sector.setText(pret.getEtudiant().getCursus().getNom());
            adress.setText(pret.getEtudiant().getAdresse());
            book_title.setText(pret.getOuvrage().getTitre());
            book_author.setText(pret.getOuvrage().getAuteur().getNom()+" "+pret.getOuvrage().getAuteur().getPrenom());
            year.setText(pret.getOuvrage().getAnnee());
            field.setText(pret.getOuvrage().getDomaine().getNom());
            SimpleDateFormat mdyFormat = new SimpleDateFormat("dd-MM-yyyy");
            borrowing_date.setText(mdyFormat.format(pret.getDate_emprunt()));
            return_expected.setText(mdyFormat.format(pret.getDate_retour_prevue()));
            String sDate1 = mdyFormat.format(new Date());
            String sDate2 = mdyFormat.format(pret.getDate_retour_prevue());
            Date d1 = mdyFormat.parse(sDate1);
            Date d2 = mdyFormat.parse(sDate2);
            System.out.println("D1 === " + d1);
            System.out.println("D2 === " + d2);

            System.out.println("COMARE THE SHIT " + d1.compareTo(d2));
            if (d1.compareTo(d2) > 0) {
                state.setText("Time up !");
            } else if (d1.compareTo(d2) <= 0) {
                state.setText("Time still on");
            }
        }
    }

    @FXML
    private void returnBook(ActionEvent event) {
        Pret pret = (Pret) Session.getAttribut("selectedPret");
        if (pret != null && pret.getDate_emprunt() != null) {
            nothingSelected.setVisible(false);
            pret.setDate_retour_effective(new Date());
            pretFacade.updateDb(pret);
            Etudiant etu = etudiantFacade.getEtudiantById(pret.getEtudiant().getId());
            etu.setNb_emprunts(etu.getNb_emprunts() - 1);
            etudiantFacade.updateDb(etu);
            int newStock = pret.getOuvrage().getStock() + 1;
            ouvrageFacade.updateDb(pret.getOuvrage().getId(), pret.getOuvrage().getTitre(),
                    pret.getOuvrage().getEditeur(), pret.getOuvrage().getAnnee(), newStock,
                    pret.getOuvrage().getAuteur().getId(), pret.getOuvrage().getDomaine().getId(),
                    pret.getOuvrage().getUniversite().getId());
            pretsTable.setItems(FXCollections.observableArrayList(pretFacade.getPendingPrets(employeSession.getUniversite().getId())));
            JOptionPane.showMessageDialog(null, "The operation is done successfully :)", "Success", JOptionPane.INFORMATION_MESSAGE);
            student_name.setText("");
            cne.setText("");
            faculty.setText("");
            Sector.setText("");
            adress.setText("");
            book_title.setText("");
            book_author.setText("");
            year.setText("");
            field.setText("");
            borrowing_date.setText("");
            return_expected.setText("");
            state.setText("");
            Session.delete("selectedPret");
        } else {
            nothingSelected.setVisible(true);
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
    private void filterPrets() {
        pretsTable.setItems(FXCollections.observableArrayList(pretFacade.filterPretsByStudent(search.getText())));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO
        Employe employe = (Employe) Session.getAttribut("connectedUser");
        full_name.setText(employe.getPrenom() + " " + employe.getNom());
        initPretsTable();

    }

}
