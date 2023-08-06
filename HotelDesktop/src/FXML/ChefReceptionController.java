package FXML;

import Classes.Chambre;
import Classes.Demande;
import Classes.Event;
import Classes.Notif;
import Classes.Produit;
import Mysql.Connexion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class ChefReceptionController implements Initializable {

    PreparedStatement st = null;
    ResultSet rs = null;
    Connection con = null;

    float montant;
    String demande;

    @FXML
    private Circle adminCercle;

    @FXML
    private Pane dashboardPane;
    @FXML
    private Pane eventPane;
    @FXML
    private Pane roomPane;
    @FXML
    private Pane demandeProduitPane;
    @FXML
    private Pane addFacturePane;

    @FXML
    private JFXButton dashboardBtn;
    @FXML
    private JFXButton roomBtn;
    @FXML
    private JFXButton eventBtn;
    @FXML
    private JFXButton productBtn;
    @FXML
    private JFXButton notificationBtn;
    @FXML
    private JFXButton signOutBtn;

    // dashboard Pane traitement : 
    // La Table Evenement : 
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

    @FXML
    private PieChart roomPieChartOnDashboard;
    @FXML
    private TextField searchNotifOnDashboard;
    @FXML
    private TextField searchEventOnDashboard;

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

            ObservableList<Event> list = getEvent();

            dashboardEventTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            dashboardResponsableEventcolumn.setCellValueFactory(new PropertyValueFactory<>("responsable"));
            dashboardStartEventColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
            dashboardFinalEventColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));

            dashboardTableEvent.setItems(list);

            FilteredList<Event> filteredData = new FilteredList<>(list, b -> true);

            searchEventOnDashboard.textProperty().addListener((observable, oldValue, newValue) -> {
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

        roomPieChartOnDashboard.getData().addAll(slice1, slice2, slice3, slice4, slice5, slice6);

    }

    @FXML
    private TableView<Notif> tableNotificationOnDashboard;
    @FXML
    private TableColumn<Notif, String> emetteurNotificationOnDashboardColumn;
    @FXML
    private TableColumn<Notif, String> contentNotificationOnDashboardColumn;

    public ObservableList<Notif> getNotif() {

        ObservableList<Notif> Notiflist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ReceptionNotification";

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

            searchNotifOnDashboard.textProperty().addListener((observable, oldValue, newValue) -> {
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
    private Pane AddEventPane;

    // Main Pane : 
    @FXML
    private JFXButton addEventButton1;
    @FXML
    private JFXButton deleteEventButton;
    @FXML
    private JFXButton modifyEventButton;
    // Second Pane : 
    @FXML
    private Button addEventButton;
    @FXML
    private Button clearEventButton;
    @FXML
    private Button saveEventButton;

    //Second Pane Inforamtion : 
    @FXML
    private TextField eventTitle;
    @FXML
    private TextField eventResponsable;
    @FXML
    private TextField eventAdress;
    @FXML
    private DatePicker eventStartDate;
    @FXML
    private DatePicker eventFinalDate;

    @FXML
    private TextField searchEvent;

    // La Table Evenement : 
    @FXML
    private TableView<Event> tableEvenement;
    @FXML
    private TableColumn<Event, Integer> eventIdColumun;
    @FXML
    private TableColumn<Event, String> eventTitleColumn;
    @FXML
    private TableColumn<Event, String> eventResponsableColumn;
    @FXML
    private TableColumn<Event, Date> eventDateStartColumn;
    @FXML
    private TableColumn<Event, Date> eventDateFinalColumn;
    @FXML
    private TableColumn<Event, String> eventAdressColumn;

    // VBox Show Information Informations : hhhhh
    @FXML
    private Label titreEvenementLabel;
    @FXML
    private Label responsableEvenementLabel;
    @FXML
    private Label dateDebutEvenementLabel;
    @FXML
    private Label DateFinEvenementLabel;

    // Tratement : 
    public ObservableList<Event> getEvent() {

        ObservableList<Event> Eventlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Evenement";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Event event = new Event();

                event.setId(rs.getInt("IdEvenement"));
                event.setTitre(rs.getString("TitreEvenement"));
                event.setAdress(rs.getString("EmplacementsEvenement"));
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

    public void afficheEvent() {
        try {

            ObservableList<Event> list = getEvent();

            eventIdColumun.setCellValueFactory(new PropertyValueFactory<>("id"));
            eventTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            eventResponsableColumn.setCellValueFactory(new PropertyValueFactory<>("responsable"));
            eventDateStartColumn.setCellValueFactory(new PropertyValueFactory<>("debut"));
            eventDateFinalColumn.setCellValueFactory(new PropertyValueFactory<>("fin"));
            eventAdressColumn.setCellValueFactory(new PropertyValueFactory<>("adress"));

            tableEvenement.setItems(list);

            FilteredList<Event> filteredData = new FilteredList<>(list, b -> true);

            searchEvent.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Event -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Event.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else if (Event.getResponsable().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else {
                        return false;
                    }

                });
            });
            SortedList<Event> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableEvenement.comparatorProperty());
            tableEvenement.setItems(sortdeData);

            System.out.println("affiche  event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }
    }

    public void insertEvent() {
        con = Connexion.getConnection();
        String insert = "INSERT INTO Evenement (TitreEvenement,ResponsableEvenement,DateDebutEvenement,DateFinEvenement,EmplacementsEvenement)VALUES(?,?,?,?,?)";

        try {

            Event event = new Event();

            event.setTitre(eventTitle.getText());
            event.setResponsable(eventResponsable.getText());
            event.setAdress(eventAdress.getText());
            event.setDebut(eventStartDate.getValue().toString());
            event.setFin(eventFinalDate.getValue().toString());

            st = con.prepareStatement(insert);

            st.setString(1, event.getTitre());
            st.setString(2, event.getResponsable());
            st.setString(3, event.getDebut());
            st.setString(4, event.getFin());
            st.setString(5, event.getAdress());

            st.executeUpdate();

            afficheEvent();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert " + ex.getMessage());
        }
    }

    public void modifyEvent() {

        try {

            AddEventPane.setVisible(true);

            Event event = tableEvenement.getSelectionModel().getSelectedItem();

            eventTitle.setText(event.getTitre());
            eventResponsable.setText(event.getResponsable());
            eventAdress.setText(event.getAdress());
            eventStartDate.setValue(LocalDate.now());
            eventFinalDate.setValue(LocalDate.now());

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement  de l‘evenement  " + ex.getMessage());
        }
    }

    public void updateEvent() {
        con = Connexion.getConnection();
        String update = "UPDATE Evenement SET  TitreEvenement = ?, ResponsableEvenement = ? , DateDebutEvenement = ? , DateFinEvenement = ? , EmplacementsEvenement  = ? where IdEvenement  =?";
        try {

            st = con.prepareStatement(update);

            st.setString(1, eventTitle.getText());
            st.setString(2, eventResponsable.getText());
            st.setString(3, eventStartDate.getValue().toString());
            st.setString(4, eventFinalDate.getValue().toString());
            st.setString(5, eventAdress.getText());
            st.setInt(6, tableEvenement.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();
            afficheEvent();

            System.out.println("Update Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update " + ex.getMessage());

        }
    }

    public void deleteEvent() {
        con = Connexion.getConnection();

        String delete = "DELETE FROM Evenement where IdEvenement = ?";

        try {

            if (tableEvenement.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableEvenement.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheEvent();
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

    public void clearEvent() {

        eventTitle.setText(null);
        eventResponsable.setText(null);
        eventAdress.setText(null);
        eventStartDate.setValue(LocalDate.now());
        eventFinalDate.setValue(LocalDate.now());

    }

    //Main Pane :
    @FXML
    void addEventEvent1(ActionEvent event) {
        try {
            AddEventPane.setVisible(true);
            AddEventPane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }

    }

    @FXML
    void modifyEventEvent(ActionEvent event) {
        try {
            AddEventPane.setVisible(true);
            AddEventPane.toFront();

            modifyEvent();

        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }

    }

    @FXML
    void deleteEventEvent(ActionEvent event) {
        deleteEvent();
        afficheEvent();
    }

    //Second Pane :
    @FXML
    void addEventEvent(ActionEvent event) {
        insertEvent();
        clearEvent();
        afficheEvent();
        AddEventPane.setVisible(false);

    }

    @FXML
    void clearEventEvent(ActionEvent event) {
        clearEvent();
        afficheEvent();

    }

    @FXML
    void saveEventEvent(ActionEvent event) {

        updateEvent();
        clearEvent();
        afficheEvent();

        AddEventPane.setVisible(false);

    }

    // Information VBox :
    @FXML
    void showEventInformation(MouseEvent event) {

        Event evenement = tableEvenement.getSelectionModel().getSelectedItem();

        titreEvenementLabel.setText(evenement.getTitre());
        responsableEvenementLabel.setText(evenement.getResponsable());
        dateDebutEvenementLabel.setText(evenement.getDebut().toString());
        DateFinEvenementLabel.setText(evenement.getFin().toString());

    }

    @FXML
    void exitAddEventPaneEvent(ActionEvent event) {
        AddEventPane.setVisible(false);

    }

    @FXML
    private Pane addChambrePane;

    File addFile;

    // Main Pane : 
    @FXML
    private JFXButton addChambreButton1;
    @FXML
    private JFXButton deleteChamreButton;
    @FXML
    private JFXButton modifyChambreButton;
    // Second Pane : 
    @FXML
    private Button addChambreButton;
    @FXML
    private Button clearChambreButton;
    @FXML
    private Button saveChambreButton;

    @FXML
    private TableView<Chambre> tableChambre;
    @FXML
    private TableColumn<Chambre, Integer> idChambreColumn;
    @FXML
    private TableColumn<Chambre, Integer> numeroChambreColumn;
    @FXML
    private TableColumn<Chambre, ComboBox> nombrePersonneChambreColumn;
    @FXML
    private TableColumn<Chambre, ComboBox> etageChambreColumn;
    @FXML
    private TableColumn<Chambre, ComboBox> typeChambreColumn;
    @FXML
    private TableColumn<Chambre, String> descriptionChambreColumn;
    @FXML
    private TableColumn<Chambre, Float> prixChambreColumn;

    @FXML
    private TextField numeroChambre;
    @FXML
    private ComboBox<Integer> etageChambre;
    @FXML
    private ComboBox<Integer> nombreDePersonneChambre;
    @FXML
    private ComboBox<String> typeChambre;
    @FXML
    private TextField prixChambre;
    @FXML
    private Rectangle roomImageRectangle;
    @FXML
    private JFXCheckBox climatiseur;
    @FXML
    private JFXCheckBox coffrefort;
    @FXML
    private JFXCheckBox salleBain;
    @FXML
    private JFXCheckBox telephone;
    @FXML
    private JFXCheckBox frigo;
    @FXML
    private JFXCheckBox television;
    @FXML
    private Rectangle roomImage;
    @FXML
    private PieChart roomPieChart;
    @FXML
    private TextField searchChambre;

    String getDescription() {

        String description = "La Chambre Se Caracterise : ";

        if (climatiseur.isSelected()) {
            description = description + climatiseur.getText();
        }
        if (coffrefort.isSelected()) {
            description = description + coffrefort.getText();
        }
        if (salleBain.isSelected()) {
            description += salleBain.getText();
        }
        if (telephone.isSelected()) {
            description += telephone.getText();
        }
        if (television.isSelected()) {
            description += television.getText();
        }
        if (frigo.isSelected()) {
            description += frigo.getText();
        }

        return description;
    }

    void selectedCheckbox() {

        Chambre chambre = tableChambre.getSelectionModel().getSelectedItem();

        int indexClimatseur = chambre.getDescription().indexOf(climatiseur.getText());

        if (indexClimatseur != -1) {
            climatiseur.setSelected(true);
        }

        int indexCoffreFort = chambre.getDescription().indexOf(coffrefort.getText());

        if (indexCoffreFort != -1) {
            coffrefort.setSelected(true);
        }

        int indexSalleBain = chambre.getDescription().indexOf(salleBain.getText());

        if (indexSalleBain != -1) {
            salleBain.setSelected(true);
        }

        int indexTelephone = chambre.getDescription().indexOf(telephone.getText());

        if (indexTelephone != -1) {
            telephone.setSelected(true);
        }

        int indexTelevision = chambre.getDescription().indexOf(television.getText());

        if (indexTelevision != -1) {
            television.setSelected(true);
        }

        int indexFrigo = chambre.getDescription().indexOf(frigo.getText());

        if (indexFrigo != -1) {
            frigo.setSelected(true);
        }
    }

    public ObservableList<Chambre> getChambre() {

        ObservableList<Chambre> Chambrelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Chambre";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Chambre chambre = new Chambre();

                Blob blob = (Blob) rs.getBlob("ImageChambre");
                InputStream in = blob.getBinaryStream();
                Image blobimage = new Image(in);

                chambre.setId(rs.getInt("IdChambre"));
                chambre.setNumero(rs.getInt("NumeroChambre"));
                chambre.setEtage(rs.getInt("EtageChambre"));
                chambre.setType(rs.getString("TypeChambre"));
                chambre.setNombrePersonne(rs.getInt("NombrePersonneChambre"));
                chambre.setDescription(rs.getString("DescriptionChambre"));
                chambre.setPrix(rs.getFloat("PrixChambre"));
                chambre.setImage(blobimage);

                Chambrelist.add(chambre);

                System.out.println("getChambre() executed sucefully");
            }
        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours De La Methode getChambre() " + ex.getMessage());

        }
        return Chambrelist;

    }

    public void afficheChambre() {
        try {

            ObservableList<Chambre> list = getChambre();

            idChambreColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            numeroChambreColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
            etageChambreColumn.setCellValueFactory(new PropertyValueFactory<>("etage"));
            typeChambreColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            nombrePersonneChambreColumn.setCellValueFactory(new PropertyValueFactory<>("nombrePersonne"));
            descriptionChambreColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            prixChambreColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

            tableChambre.setItems(list);

            FilteredList<Chambre> filteredData = new FilteredList<>(list, b -> true);

            searchChambre.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Chambre -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Chambre.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;

                    } else {
                        return false;
                    }

                });
            });
            SortedList<Chambre> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableChambre.comparatorProperty());
            tableChambre.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void insertChambre() {
        con = Connexion.getConnection();

        String insert = "INSERT INTO Chambre (NumeroChambre,EtageChambre,TypeChambre,NombrePersonneChambre,DescriptionChambre ,PrixChambre,ImageChambre) VALUES(?,?,?,?,?,?,?)";

        try {

            Chambre chambre = new Chambre();

            chambre.setNumero(Integer.valueOf(numeroChambre.getText()));
            chambre.setEtage((int) etageChambre.getSelectionModel().getSelectedItem());
            chambre.setType((String) typeChambre.getSelectionModel().getSelectedItem());
            chambre.setNombrePersonne((int) nombreDePersonneChambre.getSelectionModel().getSelectedItem());
            chambre.setDescription(getDescription());
            chambre.setPrix(Float.valueOf(prixChambre.getText()));
            chambre.setImage(new Image(addFile.toURI().toURL().toString()));

            FileInputStream input = new FileInputStream(addFile);

            st = con.prepareStatement(insert);

            st.setInt(1, chambre.getNumero());
            st.setInt(2, chambre.getEtage());
            st.setString(3, chambre.getType());
            st.setInt(4, chambre.getNombrePersonne());
            st.setString(5, chambre.getDescription());
            st.setFloat(6, chambre.getPrix());
            st.setBinaryStream(7, (InputStream) input, (int) addFile.length());

            st.executeUpdate();

            afficheChambre();

            System.out.println(" Insert Executed Sucefully ");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode insert " + ex.getMessage());

        } catch (FileNotFoundException | NumberFormatException | MalformedURLException ex) {
            System.out.println("Erreur au cours de chargement de l‘image a l‘objet chambre " + ex.getMessage());
        }
    }

    public void modifyChambe() {
        clearChambre();

        Chambre chambre = tableChambre.getSelectionModel().getSelectedItem();

        try {

            numeroChambre.setText(String.valueOf(chambre.getNumero()));
            etageChambre.getSelectionModel().select(chambre.getEtage() - 1);
            typeChambre.getSelectionModel().select(chambre.getType());
            nombreDePersonneChambre.getSelectionModel().select(chambre.getNombrePersonne() - 1);
            prixChambre.setText(String.valueOf(chambre.getPrix()));
            roomImageRectangle.setFill(new ImagePattern(chambre.getImage()));

            selectedCheckbox();

            System.out.println(" modify event executed succeffuly  ");

        } catch (Exception ex) {

            System.out.println("Erreur au cours de modification de la chambre " + ex.getMessage());

        }
    }

    public void deleteChambre() {
        con = Connexion.getConnection();

        String delete = "DELETE FROM Chambre WHERE NumeroChambre = ?";

        try {

            if (tableChambre.getSelectionModel().getSelectedItem().getNumero() != 0) {

                st = con.prepareStatement(delete);

                st.setInt(1, tableChambre.getSelectionModel().getSelectedItem().getNumero());
                st.executeUpdate();
                afficheChambre();

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

    public void clearChambre() {
        numeroChambre.setText(null);
        etageChambre.getSelectionModel().clearSelection();
        typeChambre.getSelectionModel().clearSelection();
        nombreDePersonneChambre.getSelectionModel().clearSelection();
        prixChambre.setText(null);
        climatiseur.setSelected(false);
        coffrefort.setSelected(false);
        salleBain.setSelected(false);
        telephone.setSelected(false);
        television.setSelected(false);
        frigo.setSelected(false);
    }

    public void updateChambre() {
        con = Connexion.getConnection();

        String update = "UPDATE Chambre SET NumeroChambre = ? , EtageChambre = ?, TypeChambre = ? , NombrePersonneChambre = ? , DescriptionChambre = ?  , PrixChambre = ? , ImageChambre = ?  where IdChambre = ?";

        try {

            /* Chambre chambre = new Chambre();

            chambre.setNumero(Integer.valueOf(numeroChambre.getText()));
            chambre.setEtage((int) etageChambre.getSelectionModel().getSelectedItem());
            chambre.setType((String) typeChambre.getSelectionModel().getSelectedItem());
            chambre.setNombrePersonne((int) nombreDePersonneChambre.getSelectionModel().getSelectedItem());
            chambre.setDescription(getDescription());
            chambre.setPrix(Float.valueOf(prixChambre.getText()));
            chambre.setImage(new Image(addFile.toURI().toURL().toString()));*/
            FileInputStream input = new FileInputStream(addFile);

            st = con.prepareStatement(update);

            st.setInt(1, Integer.valueOf(numeroChambre.getText()));
            st.setInt(2, (int) etageChambre.getSelectionModel().getSelectedItem());
            st.setString(3, (String) typeChambre.getSelectionModel().getSelectedItem());
            st.setInt(4, (int) nombreDePersonneChambre.getSelectionModel().getSelectedItem());
            st.setString(5, getDescription());
            st.setFloat(6, Float.valueOf(prixChambre.getText()));
            st.setBinaryStream(7, (InputStream) input, (int) addFile.length());
            st.setInt(8, tableChambre.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();

            System.out.println("Update Is Executed succefully");
            afficheChambre();

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update " + ex.getMessage());

        } catch (Exception ex) {
            System.out.println("Erreur au cours de chargement de l‘image a l‘objet chambre " + ex.getMessage());
        }

    }

    public void LoadItems() {
        ObservableList<Integer> nombrePersonneList = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7);
        nombreDePersonneChambre.setItems(nombrePersonneList);

        ObservableList<Integer> etageChambreList = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7);
        etageChambre.setItems(etageChambreList);

        ObservableList<String> typeChambreList = FXCollections.observableArrayList("Suite", "Luxe", "Economique", "Classique", "Confort", "Familiale");
        typeChambre.setItems(typeChambreList);

    }

    public void roomCount() {

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

    // Main Pane :
    @FXML
    void addChambreEvent1(ActionEvent event) {
        try {
            addChambrePane.setVisible(true);
            addChambrePane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }
    }

    @FXML
    void modifyChambreEvent(ActionEvent event) {
        addChambrePane.setVisible(true);
        modifyChambe();
    }

    @FXML
    void deleteChambreEvent(ActionEvent event) {
        deleteChambre();
        afficheChambre();
    }

    @FXML
    void showRoomInformation(MouseEvent event) {

        Chambre chambre = tableChambre.getSelectionModel().getSelectedItem();

        roomImage.setFill(new ImagePattern(chambre.getImage()));

    }

    // Second Pane :
    @FXML
    void addChambreEvent(ActionEvent event) {
        insertChambre();
        clearChambre();
        afficheChambre();
        addChambrePane.setVisible(false);
    }

    @FXML
    void clearChambreEvent(ActionEvent event) {
        clearChambre();
    }

    @FXML
    void saveChambrEvent(ActionEvent event) {

        updateChambre();
        clearChambre();
        afficheChambre();

        addChambrePane.setVisible(false);

    }

    @FXML
    void addRoomImage(MouseEvent event) {

        try {

            FileChooser fileChooser = new FileChooser();
            addFile = fileChooser.showOpenDialog(null);

            roomImageRectangle.setFill(new ImagePattern(new Image(addFile.toURI().toURL().toString())));

            System.out.println(" L‘evenement addRoomImage executed succeffuly ");

        } catch (MalformedURLException ex) {
            System.out.println(" Erreur au cours d‘execution de l‘evenement roomImagesLoad " + ex.getMessage());
        }

    }

    @FXML
    void exitAddChambreEvent(ActionEvent event) {
        addChambrePane.setVisible(false);

    }

    @FXML
    private TableView<Demande> tableDemande;
    @FXML
    private TableColumn<Demande, Integer> idDemandeColumn;
    @FXML
    private TableColumn<Demande, String> dateDemandeColumn;
    @FXML
    private TableColumn<Demande, Float> montantDemandeColumn;
    @FXML
    private TableColumn<Demande, String> descriptionDemandeColumn;
    @FXML
    private TableColumn<Demande, Boolean> statusDemandeColumn;

    @FXML
    private ComboBox<String> typeProduitOnDemande;
    @FXML
    private ComboBox<Produit> produitOnDemande;
    @FXML
    private TextField quantiteProduit;
    @FXML
    private TextArea ficheDemande;
    @FXML
    private TextField searchDemande;
    @FXML
    private PieChart categorieProduitPieChart;

    public ObservableList<Produit> getElectroniqueProductsItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitReception WHERE TypeProduit = 'Electronique'";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setTitre(rs.getString("TitreProduit"));
                produit.setPrix(rs.getFloat("PrixProduit"));
                produit.setType(rs.getString("TypeProduit"));
                produit.setDescription(rs.getString("DescriptionProduit"));

                Produitlist.add(produit);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getSanteProductsItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitReception WHERE TypeProduit = 'Sante'";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setTitre(rs.getString("TitreProduit"));
                produit.setPrix(rs.getFloat("PrixProduit"));
                produit.setType(rs.getString("TypeProduit"));
                produit.setDescription(rs.getString("DescriptionProduit"));

                Produitlist.add(produit);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getAnimeauxProductsItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Produitreception WHERE TypeProduit = 'Animeaux'";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setTitre(rs.getString("TitreProduit"));
                produit.setPrix(rs.getFloat("PrixProduit"));
                produit.setType(rs.getString("TypeProduit"));
                produit.setDescription(rs.getString("DescriptionProduit"));

                Produitlist.add(produit);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getSportProductsItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitReception WHERE TypeProduit = 'Sport'";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setTitre(rs.getString("TitreProduit"));
                produit.setPrix(rs.getFloat("PrixProduit"));
                produit.setType(rs.getString("TypeProduit"));
                produit.setDescription(rs.getString("DescriptionProduit"));

                Produitlist.add(produit);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getArtisanalProductsItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitReception WHERE TypeProduit = 'Artisanal'";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setTitre(rs.getString("TitreProduit"));
                produit.setPrix(rs.getFloat("PrixProduit"));
                produit.setType(rs.getString("TypeProduit"));
                produit.setDescription(rs.getString("DescriptionProduit"));

                Produitlist.add(produit);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getAlimentationProductsItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitReception WHERE TypeProduit = 'Alimentation'";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setTitre(rs.getString("TitreProduit"));
                produit.setPrix(rs.getFloat("PrixProduit"));
                produit.setType(rs.getString("TypeProduit"));
                produit.setDescription(rs.getString("DescriptionProduit"));

                Produitlist.add(produit);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getMaterielsProductsItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitReception WHERE TypeProduit = 'Materiels'";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setTitre(rs.getString("TitreProduit"));
                produit.setPrix(rs.getFloat("PrixProduit"));
                produit.setType(rs.getString("TypeProduit"));
                produit.setDescription(rs.getString("DescriptionProduit"));

                Produitlist.add(produit);

            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    void LoadPieChartItems() {

        int Electronique = 0;
        int Sante = 0;
        int Animeaux = 0;
        int Sport = 0;
        int Artisanal = 0;
        int Alimentation = 0;
        int Materiels = 0;

        try {

            String select = "SELECT  COUNT(*) AS Electronique FROM ProduitReception WHERE TypeProduit = 'Electronique' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Electronique = rs.getInt("Electronique");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Sante FROM ProduitReception WHERE TypeProduit = 'Sante' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Sante = rs.getInt("Sante");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Animeaux FROM ProduitReception WHERE TypeProduit = 'Animeaux' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Animeaux = rs.getInt("Animeaux");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Sport FROM ProduitReception WHERE TypeProduit = 'Sport' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Sport = rs.getInt("Sport");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Artisanal FROM ProduitReception WHERE TypeProduit = 'Artisanal' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Artisanal = rs.getInt("Artisanal");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Alimentation FROM ProduitReception WHERE TypeProduit = 'Alimentation' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Alimentation = rs.getInt("Alimentation");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Materiels FROM ProduitReception WHERE TypeProduit = 'Materiels' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Materiels = rs.getInt("Materiels");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        PieChart.Data slice1 = new PieChart.Data("Electronique", Electronique);
        PieChart.Data slice2 = new PieChart.Data("Sante", Sante);
        PieChart.Data slice3 = new PieChart.Data("Animeaux", Animeaux);
        PieChart.Data slice4 = new PieChart.Data("Sport", Sport);
        PieChart.Data slice5 = new PieChart.Data("Artisanal", Artisanal);
        PieChart.Data slice6 = new PieChart.Data("Alimentation", Alimentation);
        PieChart.Data slice7 = new PieChart.Data("Materiels", Materiels);

        categorieProduitPieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5, slice6, slice7);
    }

    void loadDemandeItems() {
        try {
            ObservableList<String> typeProduitList = FXCollections.observableArrayList("Electronique", "Sante", "Animeaux", "Sport", "Artisanal", "Alimentation", "Materiels");
            typeProduitOnDemande.setItems(typeProduitList);

        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    public ObservableList<Demande> getDemande() {

        ObservableList<Demande> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM DemandeReception";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Demande demande = new Demande();

                demande.setId(rs.getInt("IdDemande"));
                demande.setDate(rs.getString("DateDemande"));
                demande.setMontant(rs.getFloat("MontantDemande"));
                demande.setDescription(rs.getString("DescriptionDemande"));
                demande.setStatus(rs.getBoolean("StatusDemande"));

                Produitlist.add(demande);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public void afficheDemande() {
        try {

            ObservableList<Demande> list = getDemande();

            idDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            dateDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            montantDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            descriptionDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            statusDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

            tableDemande.setItems(list);

            FilteredList<Demande> filteredData = new FilteredList<>(list, b -> true);

            searchDemande.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Demande -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Demande.getDate().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
            SortedList<Demande> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableDemande.comparatorProperty());
            tableDemande.setItems(sortdeData);

            System.out.println("affiche  demande executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche demande erreur" + e.getMessage());

        }
    }

    public void insertDemande() {
        try {

            con = Connexion.getConnection();
            String insert = "INSERT INTO DemandeReception (DateDemande,MontantDemande,DescriptionDemande,StatusDemande,ServiceDemande)VALUES(?,?,?,?,?)";

            Demande demande = new Demande();

            LocalDate today = LocalDate.now();

            String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

            System.out.println(formattedDate);

            demande.setDate(formattedDate);
            demande.setMontant(montant);
            demande.setDescription(ficheDemande.getText());
            demande.setStatus(false);
            demande.setService("Reception");

            st = con.prepareStatement(insert);

            st.setString(1, demande.getDate());
            st.setFloat(2, demande.getMontant());
            st.setString(3, demande.getDescription());
            st.setBoolean(4, demande.getStatus());
            st.setString(5, demande.getService());

            st.executeUpdate();

            afficheDemande();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert du demande " + ex.getMessage());
        }
    }

    public void modifyDemande() {
        try {

            Demande demande = tableDemande.getSelectionModel().getSelectedItem();

            ficheDemande.setText(demande.getDescription());
            montant = demande.getMontant();

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement  du repas  " + ex.getMessage());
        }
    }

    public void deleteDemande() {
        try {

            con = Connexion.getConnection();

            String delete = "DELETE FROM DemandeReception where IdDemande = ?";

            if (tableDemande.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableDemande.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheDemande();
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

    public void updateDemande() {

        con = Connexion.getConnection();
        String update = "UPDATE DemandeReception SET DateDemande = ? , DescriptionDemande = ?, MontantDemande = ? , StausDemande = ?  ,ServiceDemande = ? where IdDemande  =?";
        try {

            st = con.prepareStatement(update);

            LocalDate today = LocalDate.now();

            String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

            st.setString(1, formattedDate);
            st.setString(2, ficheDemande.getText());
            st.setFloat(3, montant);
            st.setBoolean(4, false);
            st.setString(5, "Reception");

            st.setInt(6, tableDemande.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();

            System.out.println("Update Demande Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update du Demande " + ex.getMessage());

        }

    }

    void addProduitOnDemande() {
        try {

            demande = (" Titre Produit : " + produitOnDemande.getSelectionModel().getSelectedItem().getTitre() + "\n" + " Type Produit : " + typeProduitOnDemande.getSelectionModel().getSelectedItem() + "\n" + " Quantite Produit : " + quantiteProduit.getText() + "\n" + "---------------------------------------------");
            ficheDemande.setText(ficheDemande.getText() + "\n" + demande);

            montant += produitOnDemande.getSelectionModel().getSelectedItem().getPrix() * Float.valueOf(quantiteProduit.getText());

        } catch (NumberFormatException ex) {
            System.out.println("Erreur au cours de remplissage de la fiche demande " + ex.getMessage() + ex.getCause());
        }

    }

    void submitDemande() {
        insertDemande();
        montant = 0;

    }

    void clearDemande() {

        ficheDemande.setText(null);
    }

    @FXML
    void addDemandeProduitEvent(ActionEvent event) {
        try {
            addFacturePane.setVisible(true);
            addFacturePane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }
    }

    @FXML
    void loadProductsItems(ActionEvent event) {

        if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Electronique")) {
            produitOnDemande.setItems(getElectroniqueProductsItems().get(0).getTitre());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Sante")) {
            produitOnDemande.setItems(getSanteProductsItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Produits surgelés")) {
            produitOnDemande.setItems(getAnimeauxProductsItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Animeaux")) {
            produitOnDemande.setItems(getSportProductsItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Artisanal")) {
            produitOnDemande.setItems(getArtisanalProductsItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Alimentation")) {
            produitOnDemande.setItems(getAlimentationProductsItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Materiels")) {
            produitOnDemande.setItems(getMaterielsProductsItems());

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Aucun Selection Du Type Produit");
            alert.setContentText(" Veuillier Choisir Un Type Des Produits ");
            alert.show();
        }

    }

    @FXML
    void addProduitOnDemandeEvent(ActionEvent event) {
        addProduitOnDemande();

    }

    @FXML
    void submitDemandeEvent(ActionEvent event) {
        submitDemande();
        clearDemande();
        afficheDemande();
    }

    @FXML
    void clearDemandeEvent(ActionEvent event) {
        clearDemande();
    }

    @FXML
    void modifyDemandeEvent(ActionEvent event) {
        clearDemande();
        modifyDemande();
    }

    @FXML
    void saveDemandeEvent(ActionEvent event) {
        updateDemande();
        clearDemande();
        afficheDemande();
    }

    @FXML
    void deleteDemandeEvent(ActionEvent event) {
        deleteDemande();
    }

    @FXML
    void exitAddFactureEvent(ActionEvent event) {
        addFacturePane.setVisible(false);

    }

    @FXML
    void click(ActionEvent event) {
        if (event.getSource() == dashboardBtn) {
            dashboardPane.toFront();
        }
        if (event.getSource() == eventBtn) {
            eventPane.toFront();
        }
        if (event.getSource() == roomBtn) {
            roomPane.toFront();
        }
        if (event.getSource() == productBtn) {
            demandeProduitPane.toFront();
        }
        if (event.getSource() == signOutBtn) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        afficheEvent();
        afficheChambre();
        LoadItems();
        roomCount();
        roomCountOnDashboard();
        afficheEventOnDashborad();
        afficheNotificationOnDashboard();
        afficheDemande();
        loadDemandeItems();
        LoadPieChartItems();
        try {
            adminCercle.setFill(new ImagePattern(new Image(("/Icons/alexander-hipp-iEEBWgY_6lA-unsplash.jpeg"))));

        } catch (Exception ex) {
            System.out.println("Erreur" + ex.getMessage());
        }

    }

}
