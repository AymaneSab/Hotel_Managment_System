package FXML;

import Classes.Employe;
import Classes.Event;
import Classes.Notif;
import Classes.Service;
import Mysql.Connexion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.mysql.cj.jdbc.Blob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class AdminController implements Initializable {

    PreparedStatement st = null;
    ResultSet rs = null;
    Connection con = null;

    File addFile;
    @FXML
    private Circle adminCercleImage;

    @FXML
    private Pane dashboardPane;
    @FXML
    private Pane agentPane;
    @FXML
    private Pane servicesPane;
    @FXML
    private Pane notificationPane;

    @FXML
    private Pane employePane;
    @FXML
    private Pane servicePane;

    @FXML
    private JFXButton dashboardBtn;
    @FXML
    private JFXButton agentsBtn;
    @FXML
    private JFXButton servicesBtn;
    @FXML
    private JFXButton notificationBtn;
    @FXML
    private JFXButton signOutBtn;

    @FXML
    private Label roomCountLabel;
    @FXML
    private Label agentCountLabel;

    public int getnombreEmploye() {

        int NombreEmploye = 0;

        String select = "SELECT COUNT(*) AS NombreEmploye FROM Employe";

        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                NombreEmploye = rs.getInt("NombreEmploye");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getNombreEmployeEmploye() " + ex.getMessage());

            return 0;

        }
        return NombreEmploye;

    }

    public int getnombreChambre() {

        int NombreChambre = 0;

        String select = "SELECT COUNT(*) AS NombreChambre FROM Chambre";

        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                NombreChambre = rs.getInt("NombreChambre");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getNombreChambre() " + ex.getMessage());

            return 0;

        }
        return NombreChambre;

    }

    void InitialiseChart() {

        roomCountLabel.setText(String.valueOf(getnombreEmploye()));
        agentCountLabel.setText(String.valueOf(getnombreChambre()));
    }

    // Add Employe Pane : 
    @FXML
    private Button addEmployeButton;
    @FXML
    private Button saveEmployeButton;
    @FXML
    private Button exitAddEmployePane;
    @FXML
    private Button addEmployeButton1;
    @FXML
    private Button deleteEmployeButton;
    @FXML
    private Button clearEmployeButton;
    @FXML
    private Button modifyEmployeButton;

    @FXML
    private TextField nomEmploye;
    @FXML
    private TextField prenomEmploye;
    @FXML
    private TextField numeroDeTelephoneEmploye;
    @FXML
    private ComboBox<String> serviceEmploye;
    @FXML
    private DatePicker dateNaissanceEmploye;
    @FXML
    private TextField emailEmploye;

    @FXML
    private Circle addEmployeCircleImage;
    @FXML
    private Circle employeImage;

    @FXML
    private Label nomEmployeLabel;
    @FXML
    private Label prenomEmployeLabel;
    @FXML
    private Label numeroDeTelephoneEmployeLabel;

    // Traitement Dashboard :
    @FXML
    private PieChart roomPieChart;

    public void roomCountOnDashboard() {

        int Suite = 0;
        int Luxe = 0;
        int Economique = 0;
        int Classique = 0;
        int Confort = 0;
        int Familiale = 0;

        try {

            String select = "SELECT  COUNT(*) AS Suite FROM Chambre WHERE TypeChambre = 'Suite' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Suite = rs.getInt("Suite");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Luxe FROM Chambre WHERE TypeChambre = 'Luxe' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Luxe = rs.getInt("Luxe");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Economique FROM Chambre WHERE Typechambre = 'Economique' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Economique = rs.getInt("Economique");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Classique FROM Chambre WHERE TypeChambre = 'Classique' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Classique = rs.getInt("Classique");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Confort FROM Chambre WHERE Typechambre = 'Confort' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Confort = rs.getInt("Confort");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Familiale FROM Chambre WHERE Typechambre = 'Familiale' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Familiale = rs.getInt("Familiale");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // Service Chart :
        PieChart.Data slice1 = new PieChart.Data("Suite", Suite);
        PieChart.Data slice2 = new PieChart.Data("Luxe", Luxe);
        PieChart.Data slice3 = new PieChart.Data("Economique", Economique);
        PieChart.Data slice4 = new PieChart.Data("Classique", Classique);
        PieChart.Data slice5 = new PieChart.Data("Confort", Confort);
        PieChart.Data slice6 = new PieChart.Data("Familiale", Familiale);

        roomPieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5, slice6);

    }

    @FXML
    private PieChart servicePieChartOnDashboard;
    @FXML
    private TextField researchEventOnDashboard;
    @FXML
    private TextField researchNotifOnDashboard;

    @FXML
    private TableView<Event> dashboardTableEvent;
    @FXML
    private TableColumn<Event, String> dashboardEventTitleColumn;
    @FXML
    private TableColumn<Event, String> dashboardResponsableEventcolumn;
    @FXML
    private TableColumn<Event, Date> dashboardStartEventColumn;
    @FXML
    private TableColumn<Event, Date> dashboardFinalEventColumn;

    public ObservableList<Event> getEventOnDashboard() {
        ObservableList<Event> Eventlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Evenement";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Event event = new Event();

                event.setTitre(rs.getString("TitreEvenement"));
                event.setResponsable(rs.getString("ResponsableEvenement"));
                event.setDebut(rs.getString("DateDebutEvenement"));
                event.setFin(rs.getString("DateFinEvenement"));

                Eventlist.add(event);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Eventlist;
    }

    public void afficheEventOnDashborad() {
        try {

            ObservableList<Event> list = getEventOnDashboard();

            dashboardEventTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            dashboardResponsableEventcolumn.setCellValueFactory(new PropertyValueFactory<>("responsable"));
            dashboardStartEventColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
            dashboardFinalEventColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));

            dashboardTableEvent.setItems(list);

            FilteredList<Event> filteredData = new FilteredList<>(list, b -> true);

            researchEventOnDashboard.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Event -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Event.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Event.getResponsable().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Event.getDebut().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Event.getFin().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Event> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(dashboardTableEvent.comparatorProperty());
            dashboardTableEvent.setItems(sortdeData);

            System.out.println("affiche  event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }
    }

    public void serviceCountOnDashboard() {

        int reception = 0;
        int cuisine = 0;
        int comptabilite = 0;
        int nettoyage = 0;
        int maintenance = 0;

        try {

            String select = "SELECT  COUNT(*) AS cuisine FROM Employe WHERE ServiceEmploye = 'Cuisine' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                cuisine = rs.getInt("cuisine");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS reception FROM Employe WHERE ServiceEmploye = 'Reception' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                reception = rs.getInt("reception");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS maintenance FROM Employe WHERE ServiceEmploye = 'Maintenance' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                maintenance = rs.getInt("maintenance");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS nettoyage FROM Employe WHERE ServiceEmploye = 'Nettoyage' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                nettoyage = rs.getInt("nettoyage");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS comptabilite FROM Employe WHERE ServiceEmploye = 'Comptabilite' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                comptabilite = rs.getInt("Comptabilite");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // Service Chart :
        PieChart.Data slice1 = new PieChart.Data("Reception", reception);
        PieChart.Data slice2 = new PieChart.Data("Comptabilite", comptabilite);
        PieChart.Data slice3 = new PieChart.Data("Nettoyage", nettoyage);
        PieChart.Data slice4 = new PieChart.Data("Cuisinier", cuisine);
        PieChart.Data slice5 = new PieChart.Data("Maintenance", maintenance);

        servicePieChartOnDashboard.getData().addAll(slice1, slice2, slice3, slice4, slice5);

    }

    @FXML
    private TableView<Notif> tableNotificationOnDashboard;
    @FXML
    private TableColumn<Notif, String> emetteurNotificationOnDashboardColumn;
    @FXML
    private TableColumn<Notif, String> contentNotificationOnDashboardColumn;

    public ObservableList<Notif> getNotif() {

        ObservableList<Notif> Notiflist = FXCollections.observableArrayList();

        String select = "SELECT * FROM CuisineNotification";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Notif notif = new Notif();

                notif.setId(rs.getInt("IdNotification"));
                notif.setEmetteur(rs.getString("EmetteurNotification"));
                notif.setTitle(rs.getString("TitleNotification"));
                notif.setContent(rs.getString("ContentNotification"));

                Notiflist.add(notif);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Notiflist;
    }

    public void afficheNotificationOnDashboard() {
        try {

            ObservableList<Notif> list = getNotif();

            emetteurNotificationOnDashboardColumn.setCellValueFactory(new PropertyValueFactory<>("emetteur"));
            contentNotificationOnDashboardColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

            tableNotificationOnDashboard.setItems(list);

            FilteredList<Notif> filteredData = new FilteredList<>(list, b -> true);

            researchNotifOnDashboard.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Notif -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Notif.getEmetteur().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else {
                        return false;
                    }

                });
            });
            SortedList<Notif> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableNotificationOnDashboard.comparatorProperty());
            tableNotificationOnDashboard.setItems(sortdeData);

            System.out.println("affiche notif OnDashboard  executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche Notif erreur" + e.getMessage());

        }
    }

    @FXML
    private TableView<Employe> tableEmploye;
    @FXML
    public TableColumn<Employe, Integer> ageEmployeColumn;
    @FXML
    public TableColumn<Employe, String> emailEmployeColumn;
    @FXML
    public TableColumn<Employe, Integer> idEmployeColumn;
    @FXML
    public TableColumn<Employe, String> nomEmployeColumn;
    @FXML
    public TableColumn<Employe, String> numeroDeTelephoneEmployeColumn;
    @FXML
    public TableColumn<Employe, String> prenomEmployeColumn;
    @FXML
    public TableColumn<Employe, ComboBox> serviceEmployeColumn;

    @FXML
    private TextField researchAgent;
    @FXML
    private StackedBarChart<String, Float> agentStackBar;

    public ObservableList<Employe> getEmploye() {

        ObservableList<Employe> Employelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Employe";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Employe employe = new Employe();

                Blob blob = (Blob) rs.getBlob("ImageEmploye");
                InputStream in = blob.getBinaryStream();
                Image blobimage = new Image(in);

                employe.setId(rs.getInt("IdEmploye"));
                employe.setNom(rs.getString("NomEmploye"));
                employe.setPrenom(rs.getString("PrenomEmploye"));
                employe.setAge(rs.getInt("AgeEmploye"));
                employe.setEmail(rs.getString("EmailEmploye"));
                employe.setService(rs.getString("ServiceEmploye"));
                employe.setNumeroDeTelephone(rs.getString("NumeroDeTelephoneEmploye"));
                employe.setImage(blobimage);

                Employelist.add(employe);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Employelist;

    }

    public void afficheEmploye() {

        try {

            ObservableList<Employe> list = getEmploye();

            idEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            ageEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            emailEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            serviceEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
            numeroDeTelephoneEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("numeroDeTelephone"));

            tableEmploye.setItems(list);

            FilteredList<Employe> filteredData = new FilteredList<>(list, b -> true);

            researchAgent.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Employe -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Employe.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getPrenom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getService().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getEmail().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getNumeroDeTelephone().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Employe> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableEmploye.comparatorProperty());
            tableEmploye.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    private void insertEmploye() {

        con = Connexion.getConnection();
        String insert = "INSERT INTO Employe (NomEmploye,PrenomEmploye,AgeEmploye,EmailEmploye,NumeroDeTelephoneEmploye,ServiceEmploye,ImageEmploye)VALUES(?,?,?,?,?,?,?)";

        try {

            Year year = Year.now();
            Employe employe = new Employe();

            employe.setNom(nomEmploye.getText());
            employe.setPrenom(prenomEmploye.getText());
            employe.setAge(year.getValue() - dateNaissanceEmploye.getValue().getYear());
            employe.setEmail(emailEmploye.getText());
            employe.setNumeroDeTelephone(numeroDeTelephoneEmploye.getText());
            employe.setService((String) serviceEmploye.getSelectionModel().getSelectedItem());
            employe.setImage(new Image(addFile.toURI().toURL().toString()));

            FileInputStream input = new FileInputStream(addFile);

            st = con.prepareStatement(insert);

            st.setString(1, employe.getNom());
            st.setString(2, employe.getPrenom());
            st.setInt(3, employe.getAge());
            st.setString(4, employe.getEmail());
            st.setString(5, employe.getNumeroDeTelephone());
            st.setString(6, employe.getService());
            st.setBinaryStream(7, (InputStream) input, (int) addFile.length());

            st.executeUpdate();

            afficheEmploye();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert " + ex.getMessage());
        } catch (FileNotFoundException | MalformedURLException ex) {
            System.out.println("Erreur at file in the insert methode");
        }
    }

    public void deleteEmploye() {

        con = Connexion.getConnection();

        String delete = "DELETE FROM Employe  where IdEmploye = ?";

        try {

            if (tableEmploye.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableEmploye.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheEmploye();
                System.out.println(" Delete Employe Succefful ");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText("No selection was made.");
                alert.setContentText("You have not selected an item to delete. Please try again.");
                alert.showAndWait();
            }

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode delete " + ex.getMessage());

        }
    }

    public void updateEmploye() {
        con = Connexion.getConnection();
        String update = "UPDATE Employe SET  NomEmploye = ?, PrenomEmploye = ? , AgeEmploye = ? , EmailEmploye = ? , NumeroDeTelephoneEmploye = ? , ServiceEmploye = ? , ImageEmploye = ? where IdEmploye =?";
        try {
            FileInputStream input = new FileInputStream(addFile);

            Year year = Year.now();

            st = con.prepareStatement(update);
            st.setString(1, nomEmploye.getText());
            st.setString(2, prenomEmploye.getText());
            st.setInt(3, year.getValue() - dateNaissanceEmploye.getValue().getYear());
            st.setString(4, emailEmploye.getText());
            st.setString(5, numeroDeTelephoneEmploye.getText());
            st.setString(6, (String) serviceEmploye.getSelectionModel().getSelectedItem());
            st.setBinaryStream(7, (InputStream) input, (int) addFile.length());
            st.setInt(8, tableEmploye.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();
            afficheEmploye();

            System.out.println("Update Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update " + ex.getMessage());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AdminController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearEmploye() {

        nomEmploye.setText(null);
        prenomEmploye.setText(null);
        numeroDeTelephoneEmploye.setText(null);
        serviceEmploye.getSelectionModel().selectFirst();
        dateNaissanceEmploye.setValue(LocalDate.now());
        emailEmploye.setText(null);

    }

    void agentCount() {

        con = Connexion.getConnection();

        try {
            float Reception = 0;
            float Maintenance = 0;
            float Comptabilite = 0;
            float Cuisinier = 0;

            int i = 0;

            try {

                String select = "SELECT  AgeEmploye AS Reception  FROM Employe  WHERE ServiceEmploye = 'Reception' ";

                con = Connexion.getConnection();
                st = con.prepareStatement(select);
                rs = st.executeQuery();

                while (rs.next()) {
                    Reception += rs.getInt("Reception");

                }
                Reception = Reception / 10;
            } catch (SQLException ex) {
                System.out.println("Exception en demandeCount()" + ex.getMessage());
            }

            try {

                String select = "SELECT  AgeEmploye AS Cuisinier  FROM Employe WHERE ServiceEmploye = 'Cuisinier' ";

                con = Connexion.getConnection();
                st = con.prepareStatement(select);

                rs = st.executeQuery();
                while (rs.next()) {
                    Cuisinier += rs.getInt("Cuisinier");
                }

                Cuisinier = Cuisinier / 10;

            } catch (SQLException ex) {
                System.out.println("Exception en serviceCount()" + ex.getMessage());
            }
            try {

                String select = "SELECT  AgeEmploye AS Maintenance FROM Employe WHERE ServiceEmploye = 'Maintenance' ";

                con = Connexion.getConnection();
                st = con.prepareStatement(select);

                rs = st.executeQuery();
                while (rs.next()) {
                    Maintenance += rs.getInt("Maintenance");
                }

                Maintenance = Maintenance / 10;

            } catch (SQLException ex) {
                System.out.println("Exception en serviceCount()" + ex.getMessage());
            }

            try {

                String select = "SELECT  AgeEmploye AS Comptabilite  FROM Employe NATURAL JOIN DemandeCuisine WHERE ServiceEmploye = 'Comptabilite' ";

                con = Connexion.getConnection();
                st = con.prepareStatement(select);

                rs = st.executeQuery();
                while (rs.next()) {
                    Comptabilite += rs.getInt("Comptabilite");
                }

                Maintenance = Comptabilite / 10;

            } catch (SQLException ex) {
                System.out.println("Exception en serviceCount()" + ex.getMessage());
            }

            final XYChart.Series<String, Float> series1 = new XYChart.Series<>();

            series1.setName("Employees Ages");
            series1.getData().add(new XYChart.Data<>("Reception", Reception));
            series1.getData().add(new XYChart.Data<>("Cuisinier", Cuisinier));
            series1.getData().add(new XYChart.Data<>("Maintenance", Maintenance));
            series1.getData().add(new XYChart.Data<>("Comptabilite", Comptabilite));

            agentStackBar.getData().addAll(series1);

        } catch (Exception ex) {

        }

    }

    @FXML
    void quitAddEmployeButton(ActionEvent event) {

        employePane.setVisible(false);
    }

    @FXML
    void addEmployeEvent(ActionEvent event) {

        try {
            employePane.setVisible(true);
            employePane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }

    }

    @FXML
    void addEmployeEvent1(ActionEvent event) {

        try {

            insertEmploye();
            clearEmploye();
            employePane.setVisible(false);
            afficheEmploye();

        } catch (Exception ex) {
            System.out.println("Erreur au cours saveEmployeEvent " + ex.getMessage());
        }

    }

    @FXML
    void modifyEmployeEvent(ActionEvent event) {

        try {

            employePane.setVisible(true);

            LocalDate date = LocalDate.now();

            Employe employe = tableEmploye.getSelectionModel().getSelectedItem();

            nomEmploye.setText(employe.getNom());
            prenomEmploye.setText(employe.getPrenom());
            numeroDeTelephoneEmploye.setText(employe.getNumeroDeTelephone());
            serviceEmploye.getSelectionModel().select(employe.getService());
            emailEmploye.setText(employe.getEmail());
            addEmployeCircleImage.setFill(new ImagePattern(employe.getImage()));
            dateNaissanceEmploye.setValue(date);

            modifyEmployeButton.setDisable(true);
            addEmployeButton.setDisable(true);
            deleteEmployeButton.setDisable(true);

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement de mot de passe " + ex.getMessage());
        }

    }

    @FXML
    void deleteEmployeEvent(ActionEvent event) {

        deleteEmploye();
        afficheEmploye();

    }

    @FXML
    void clearEmployeEvent(ActionEvent event) {

        clearEmploye();
    }

    @FXML
    void saveEmployeEvent(ActionEvent event) {
        try {

            updateEmploye();
            clearEmploye();
            employePane.setVisible(false);
            afficheEmploye();

        } catch (Exception ex) {
            System.out.println("Erreur au cours save Employe Event " + ex.getMessage());
        }

    }

    @FXML
    void exitAddEmployePaneEvent(ActionEvent event) {
        System.exit(0);

    }

    @FXML
    void addEmployeImage(MouseEvent event) {

        try {

            FileChooser fileChooser = new FileChooser();
            addFile = fileChooser.showOpenDialog(null);

            addEmployeCircleImage.setFill(new ImagePattern(new Image(addFile.toURI().toURL().toString())));

            System.out.println(" L‘evenement addEmployeImage executed succeffuly ");

        } catch (MalformedURLException ex) {
            System.out.println(" Erreur au cours d‘execution de l‘evenement roomImagesLoad " + ex.getMessage());
        }

    }

    @FXML
    void showEmployeInformation(MouseEvent event) {
        try {

            Employe employe = tableEmploye.getSelectionModel().getSelectedItem();

            employeImage.setFill(new ImagePattern(employe.getImage()));
            nomEmployeLabel.setText(employe.getNom());
            prenomEmployeLabel.setText(employe.getPrenom());
            numeroDeTelephoneEmployeLabel.setText(employe.getNumeroDeTelephone());

        } catch (Exception ex) {
            System.out.println("Erreur au cours action de la tableEmploye" + ex.getMessage());
        }
    }

    //  Service Pane Inforamations  : 
    @FXML
    private Button clearServiceButton;
    @FXML
    private Button saveServiceButton;
    @FXML
    private JFXButton addServiceButton;
    @FXML
    private JFXButton modifyServiceButton;
    @FXML
    private JFXButton deleteServiceButton;

    @FXML
    private TextField nomService;
    @FXML
    private TextField nombrePosteService;
    @FXML
    private TextField nombreEmployeService;
    @FXML
    private TextField responsableService;

    @FXML
    private Label nomServiceLabel;
    @FXML
    private Label responsableServiceLabel;
    @FXML
    private Label nombreEmployeServiceLabel;
    @FXML
    private Label nombrePosteServiceLabel;

    // La table service :
    @FXML
    private TableView<Service> tableService;
    @FXML
    private TableColumn<Service, Integer> idServiceColumn;
    @FXML
    private TableColumn<Service, String> nomServiceColumn;
    @FXML
    private TableColumn<Service, String> responsableServiceColumn;
    @FXML
    private TableColumn<Service, Integer> nombreEmployeColumn;
    @FXML
    private TableColumn<Service, Integer> nombrePosteColumn;

    @FXML
    private PieChart servicePieChart;
    @FXML
    private TextField searchService;
    @FXML
    private TextField searchEmployeOnService;

    @FXML
    private TableView<Employe> tableEmployeOnServicePane;
    @FXML
    private TableColumn<Service, String> nomEmployeColumnOnServicePane;
    @FXML
    private TableColumn<Service, String> prenomEmployeColumnOnServicePane;

    // traitement de la pane service :
    // Les Methodes de traitement de la table service : 
    public ObservableList<Service> getService() {

        ObservableList<Service> Servicelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Service";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {
                Service service = new Service();

                service.setId(rs.getInt("IdService"));
                service.setNom(rs.getString("NomService"));
                service.setResponsable(rs.getString("ResponsableService"));
                service.setNombreEmploye(rs.getInt("NombreEmploye"));
                service.setNombrePoste(rs.getInt("NombrePoste"));

                Servicelist.add(service);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getService() " + ex.getMessage());

        }
        return Servicelist;

    }

    public void afficheService() {

        try {

            ObservableList<Service> list = getService();

            idServiceColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomServiceColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            responsableServiceColumn.setCellValueFactory(new PropertyValueFactory<>("responsable"));
            nombreEmployeColumn.setCellValueFactory(new PropertyValueFactory<>("nombreEmploye"));
            nombrePosteColumn.setCellValueFactory(new PropertyValueFactory<>("nombrePoste"));

            tableService.setItems(list);

            System.out.println("affiche executed succefully");

            FilteredList<Service> filteredData = new FilteredList<>(list, b -> true);

            searchService.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Service -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Service.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Service.getResponsable().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Service> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableService.comparatorProperty());
            tableService.setItems(sortdeData);

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    private void insertService() {

        con = Connexion.getConnection();
        String insert = "INSERT INTO Service (NomService,ResponsableService,NombreEmploye,NombrePoste)VALUES(?,?,?,?)";

        try {

            Service service = new Service();

            service.setNom(nomService.getText());
            service.setResponsable(responsableService.getText());
            service.setNombreEmploye(Integer.parseInt(nombreEmployeService.getText()));
            service.setNombrePoste(Integer.parseInt(nombrePosteService.getText()));

            st = con.prepareStatement(insert);

            st.setString(1, service.getNom());
            st.setString(2, service.getResponsable());
            st.setInt(3, service.getNombreEmploye());
            st.setInt(4, service.getNombrePoste());

            st.executeUpdate();

            afficheService();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert " + ex.getMessage());
        }
    }

    public void deleteService() {
        con = Connexion.getConnection();
        String delete = "DELETE FROM Service  where IdService = ?";

        try {

            if (tableService.getSelectionModel().getSelectedItem().getId() != 0) {

                st = con.prepareStatement(delete);

                st.setInt(1, tableService.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheService();
                System.out.println(" Delete Succefful ");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText("No selection was made.");
                alert.setContentText("You have not selected an item to delete. Please try again.");
                alert.showAndWait();
            }

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode delete " + ex.getMessage());

        }
    }

    private void updateService() {

        con = Connexion.getConnection();

        String update = "UPDATE Service SET  NomService = ?, ResponsableService = ? , NombreEmploye = ? , NombrePoste = ? where IdService =?";
        try {

            st = con.prepareStatement(update);

            st.setString(1, nomService.getText());
            st.setString(2, responsableService.getText());
            st.setInt(3, Integer.valueOf(nombreEmployeService.getText()));
            st.setInt(4, Integer.valueOf(nombrePosteService.getText()));
            st.setInt(5, tableService.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();

            afficheService();

            System.out.println("Update Is Executed succefully");
        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update " + ex.getMessage());

        }
    }

    void clearService() {

        nomService.setText(null);
        responsableService.setText(null);
        nombreEmployeService.setText(null);
        nombrePosteService.setText(null);

        clearServiceButton.setDisable(true);
    }

    void LoadServiceItem() {
        try {
            ObservableList<String> serviceEmployeList = FXCollections.observableArrayList("Cuisine", "Reception", "Maintenance", "Nettoyage", "Comptabilite");
            serviceEmploye.setItems(serviceEmployeList);
        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    public ObservableList<Employe> getCuisineEmployees() {

        ObservableList<Employe> Employelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Employe WHERE ServiceEmploye = 'Cuisine' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Employe employe = new Employe();

                employe.setNom(rs.getString("NomEmploye"));
                employe.setPrenom(rs.getString("PrenomEmploye"));

                Employelist.add(employe);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Employelist;

    }

    public ObservableList<Employe> getReceptionEmployees() {

        ObservableList<Employe> Employelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Employe WHERE ServiceEmploye = 'Reception' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Employe employe = new Employe();

                employe.setNom(rs.getString("NomEmploye"));
                employe.setPrenom(rs.getString("PrenomEmploye"));

                Employelist.add(employe);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Employelist;

    }

    public ObservableList<Employe> getNettoyageEmployees() {

        ObservableList<Employe> Employelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Employe WHERE ServiceEmploye = 'Nettoyage' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Employe employe = new Employe();

                employe.setNom(rs.getString("NomEmploye"));
                employe.setPrenom(rs.getString("PrenomEmploye"));

                Employelist.add(employe);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Employelist;

    }

    public ObservableList<Employe> getMaintenanceEmployees() {

        ObservableList<Employe> Employelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Employe WHERE ServiceEmploye = 'Maintenance' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Employe employe = new Employe();

                employe.setNom(rs.getString("NomEmploye"));
                employe.setPrenom(rs.getString("PrenomEmploye"));

                Employelist.add(employe);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Employelist;

    }

    public ObservableList<Employe> getComptabiliteEmployees() {

        ObservableList<Employe> Employelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Employe WHERE ServiceEmploye = 'Comptabilite' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Employe employe = new Employe();

                employe.setNom(rs.getString("NomEmploye"));
                employe.setPrenom(rs.getString("PrenomEmploye"));

                Employelist.add(employe);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Employelist;

    }

    public void afficheCuisineEmployees() {

        try {

            ObservableList<Employe> list = getCuisineEmployees();

            nomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            tableEmployeOnServicePane.setItems(list);

            FilteredList<Employe> filteredData = new FilteredList<>(list, b -> true);

            searchEmployeOnService.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Employe -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Employe.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getPrenom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Employe> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableEmployeOnServicePane.comparatorProperty());
            tableEmployeOnServicePane.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheReceptionEmployees() {

        try {

            ObservableList<Employe> list = getReceptionEmployees();

            nomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            tableEmployeOnServicePane.setItems(list);

            FilteredList<Employe> filteredData = new FilteredList<>(list, b -> true);

            searchEmployeOnService.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Employe -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Employe.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getPrenom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Employe> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableEmployeOnServicePane.comparatorProperty());
            tableEmployeOnServicePane.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheNettoyageEmployees() {

        try {

            ObservableList<Employe> list = getNettoyageEmployees();

            nomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            tableEmployeOnServicePane.setItems(list);

            FilteredList<Employe> filteredData = new FilteredList<>(list, b -> true);

            searchEmployeOnService.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Employe -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Employe.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getPrenom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Employe> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableEmployeOnServicePane.comparatorProperty());
            tableEmployeOnServicePane.setItems(sortdeData);
            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheMaintenanceEmployees() {

        try {

            ObservableList<Employe> list = getMaintenanceEmployees();

            nomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            tableEmployeOnServicePane.setItems(list);

            FilteredList<Employe> filteredData = new FilteredList<>(list, b -> true);

            searchEmployeOnService.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Employe -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Employe.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getPrenom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Employe> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableEmployeOnServicePane.comparatorProperty());
            tableEmployeOnServicePane.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheComptabiliteEmployees() {

        try {

            ObservableList<Employe> list = getComptabiliteEmployees();

            nomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomEmployeColumnOnServicePane.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            tableEmployeOnServicePane.setItems(list);

            FilteredList<Employe> filteredData = new FilteredList<>(list, b -> true);

            searchEmployeOnService.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Employe -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Employe.getNom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Employe.getPrenom().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Employe> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableEmployeOnServicePane.comparatorProperty());
            tableEmployeOnServicePane.setItems(sortdeData);
            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void serviceCount() {

        int reception = 0;
        int cuisine = 0;
        int comptabilite = 0;
        int nettoyage = 0;
        int maintenance = 0;

        try {

            String select = "SELECT  COUNT(*) AS cuisine FROM Employe WHERE ServiceEmploye = 'Cuisine' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                cuisine = rs.getInt("cuisine");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS reception FROM Employe WHERE ServiceEmploye = 'Reception' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                reception = rs.getInt("reception");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS maintenance FROM Employe WHERE ServiceEmploye = 'Maintenance' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                maintenance = rs.getInt("maintenance");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS nettoyage FROM Employe WHERE ServiceEmploye = 'Nettoyage' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                nettoyage = rs.getInt("nettoyage");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS comptabilite FROM Employe WHERE ServiceEmploye = 'Comptabilite' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                comptabilite = rs.getInt("Comptabilite");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // Service Chart :
        PieChart.Data slice1 = new PieChart.Data("Reception", reception);
        PieChart.Data slice2 = new PieChart.Data("Comptabilite", comptabilite);
        PieChart.Data slice3 = new PieChart.Data("Nettoyage", nettoyage);
        PieChart.Data slice4 = new PieChart.Data("Cuisinier", cuisine);
        PieChart.Data slice5 = new PieChart.Data("Maintenance", maintenance);

        servicePieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5);

    }

    // Traitement des methodes de la pnase service : 
    @FXML
    void quitAddServiceEvent(ActionEvent event) {

        servicePane.setVisible(false);
    }

    @FXML
    void addServiceEvent(ActionEvent event) {

        servicePane.setVisible(true);
        servicePane.toFront();

    }

    @FXML
    void addServiceEvent1(ActionEvent event) {

        insertService();
        clearService();
        servicePane.setVisible(false);
        afficheService();

    }

    @FXML
    void saveServiceEvent(ActionEvent event) {

        try {
            updateService();
            clearService();
            servicePane.setVisible(false);
            afficheService();

        } catch (Exception ex) {
            System.out.println("Erreur au cours de save update service" + ex.getMessage());
        }

    }

    @FXML
    void clearServiceButton(ActionEvent event) {
        clearService();
    }

    @FXML
    void modifyServiceEvent(ActionEvent event) {

        try {
            servicePane.setVisible(true);
            LocalDate date = LocalDate.now();

            Service service = tableService.getSelectionModel().getSelectedItem();

            nomService.setText(service.getNom());
            responsableService.setText(service.getResponsable());
            nombreEmployeService.setText(String.valueOf(service.getNombreEmploye()));
            nombrePosteService.setText(String.valueOf(service.getNombrePoste()));

            modifyServiceButton.setDisable(true);
            addServiceButton.setDisable(true);
            deleteServiceButton.setDisable(true);

        } catch (Exception ex) {
            System.out.println("Erreur au cours de midifcation du service" + ex.getMessage());
        }

    }

    @FXML
    void deleteServiceEvent(ActionEvent event) {

        deleteService();
        afficheService();
        clearServiceButton.setDisable(false);
    }

    @FXML
    void showServiceInformation(MouseEvent event) {
        try {

            Service service = tableService.getSelectionModel().getSelectedItem();

            nomServiceLabel.setText(service.getNom());
            responsableServiceLabel.setText(service.getResponsable());
            nombreEmployeServiceLabel.setText(String.valueOf(service.getNombreEmploye()));
            nombrePosteServiceLabel.setText(String.valueOf(service.getNombrePoste()));

            if (service.getNom().equals("Cuisine")) {
                afficheCuisineEmployees();
            }
            if (service.getNom().equals("Reception")) {
                afficheReceptionEmployees();
            }
            if (service.getNom().equals("Nettoyage")) {
                afficheNettoyageEmployees();
            }
            if (service.getNom().equals("Maintenance")) {
                afficheMaintenanceEmployees();
            }
            if (service.getNom().equals("Comptabilite")) {
                afficheComptabiliteEmployees();
            }

        } catch (Exception ex) {
            System.out.println("Erreur au cours action de la tableEmploye" + ex.getMessage());
        }

    }

    @FXML
    private Label emetteurNotificationLabel;
    @FXML
    private Label titleNotificationLabel;
    @FXML
    private JFXTextArea contentNotificationLabel;

    @FXML
    private TableView<Notif> tableNotification;
    @FXML
    private TableColumn<Notif, Integer> idNotificationColumn;
    @FXML
    private TableColumn<Notif, String> emetteurNotificationColumn;
    @FXML
    private TableColumn<Notif, String> titleNotificationColumn;
    @FXML
    private TableColumn<Notif, String> contentNotificationColumn;

    public void afficheNotification() {
        try {

            ObservableList<Notif> list = getNotif();

            idNotificationColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            emetteurNotificationColumn.setCellValueFactory(new PropertyValueFactory<>("emetteur"));
            titleNotificationColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            contentNotificationColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

            tableNotification.setItems(list);

            System.out.println("affiche notif executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche Notif erreur" + e.getMessage());

        }
    }

    void deleteNotification() {
        try {

            con = Connexion.getConnection();

            String delete = "DELETE FROM CuisineNotification where IdNotification = ?";

            if (tableNotification.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableNotification.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheNotification();
                System.out.println(" Delete Produit  executed Succefful ");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText("No selection was made.");
                alert.setContentText("You have not selected an item to delete. Please try again.");
                alert.showAndWait();
            }

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode delete " + ex.getMessage());

        }
    }

    void showNotificationInformation() {

        Notif notif = tableNotification.getSelectionModel().getSelectedItem();

        emetteurNotificationLabel.setText(notif.getEmetteur());
        titleNotificationLabel.setText(notif.getTitle());
        contentNotificationLabel.setText(notif.getContent());

    }

    @FXML
    void deleteNotificationEvent(ActionEvent event) {
        deleteNotification();
        afficheNotification();
    }

    @FXML
    void showNotificationInformation(MouseEvent event) {
        try {
            showNotificationInformation();

        } catch (Exception ex) {
            System.out.println("Erreur au cours action de la tableRepas" + ex.getMessage());
        }

    }

    @FXML
    void click(ActionEvent event) {
        if (event.getSource() == dashboardBtn) {
            dashboardPane.toFront();
        }
        if (event.getSource() == agentsBtn) {
            agentPane.toFront();
        }
        if (event.getSource() == servicesBtn) {
            servicesPane.toFront();
        }
        if (event.getSource() == notificationBtn) {
            notificationPane.toFront();
        }
        if (event.getSource() == signOutBtn) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        afficheEmploye();
        afficheService();
        serviceCount();
        serviceCountOnDashboard();
        roomCountOnDashboard();
        afficheEventOnDashborad();
        LoadServiceItem();
        InitialiseChart();
        agentCount();
        try{
                    adminCercleImage.setFill(new ImagePattern(new Image(("/Icons/alexander-hipp-iEEBWgY_6lA-unsplash.jpeg"))));

        }catch(Exception ex){
            System.out.println("Erreur " + ex.getMessage());
        }
    }

}
