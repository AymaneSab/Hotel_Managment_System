package FXML;

import Classes.Demande;
import Classes.Event;
import Classes.Facture;
import Classes.Notif;
import Mysql.Connexion;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ChefComptabiliteController implements Initializable {

    PreparedStatement st = null;
    ResultSet rs = null;
    Connection con = null;

    @FXML
    private Pane dashboardPane;
    @FXML
    private Pane facturePane;
    @FXML
    private Pane chartPane;
    @FXML
    private Pane notificationPane;
    @FXML
    private Pane maintenanceProduitPane;
    @FXML
    private Pane receptionProduitPane;

    
    @FXML
    private JFXButton dashboardBtn;
    @FXML
    private JFXButton notificationBtn;
    @FXML
    private JFXButton factureBtn;
    @FXML
    private JFXButton chartBtn;
    @FXML
    private JFXButton signOutBtn;

    // Dashboard : 
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
    private PieChart demandePieChartOnDashboard;
    @FXML
    private PieChart facturePieChart;
    @FXML
    public LineChart factureLineChart;
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

            ObservableList<Event> list = getEventOnDashboard();

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

    public ObservableList<Notif> getNotif() {

        ObservableList<Notif> Notiflist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ComptableNotification";

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

    public void demandeCountOnDashboard() {

        int Reception = 0;
        int Cuisinier = 0;
        int Nettoyage = 0;
        int Maintenance = 0;

        try {

            String select = "SELECT  COUNT(*) AS Reception FROM DemandeReception  WHERE ServiceDemande = 'Reception' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Reception = rs.getInt("Reception");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en demandeCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Cuisinier FROM DemandeCuisine WHERE ServiceDemande = 'Cuisinier' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Cuisinier = rs.getInt("Cuisinier");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Maintenance FROM DemandeReception NATURAL JOIN DemandeCuisine WHERE ServiceDemande = 'Maintenance' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Maintenance = rs.getInt("Maintenance");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        /*  try {

            String select = "SELECT  COUNT(*) AS Nettoyage FROM DemandeReception NATURAL JOIN DemandeCuisine WHERE ServiceDemande = 'Nettoyage' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Nettoyage = rs.getInt("Nettoyage");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }*/

        // Service Chart :
        PieChart.Data slice1 = new PieChart.Data("Reception", Reception);
        PieChart.Data slice2 = new PieChart.Data("Cuisinier", Cuisinier);
        PieChart.Data slice3 = new PieChart.Data("Nettoyage", Nettoyage);
        PieChart.Data slice4 = new PieChart.Data("Maintenance", Maintenance);

        demandePieChartOnDashboard.getData().addAll(slice1, slice2, slice3, slice4);

    }

    // Facture Pane :
    @FXML
    private Pane addFacturePane;

    @FXML
    private TableView<Facture> tableFacture;
    @FXML
    private TableColumn<Facture, Integer> idFactureColumn;
    @FXML
    private TableColumn<Facture, ComboBox> serviceFactureColumn;
    @FXML
    private TableColumn<Facture, String> recepteurFactureColumn;
    @FXML
    private TableColumn<Facture, String> descriptionFactureColumn;
    @FXML
    private TableColumn<Facture, Float> montantFactureColumn;
    @FXML
    private TableColumn<Facture, String> dateFactureColumn;

    // Main Pane : 
    @FXML
    private JFXButton addFactureButton1;
    @FXML
    private JFXButton deleteFactureButton;
    @FXML
    private JFXButton modifyFactureButton;

    // Second Pane :
    @FXML
    private Button addFactureButton;
    @FXML
    private Button clearFactureButton;
    @FXML
    private Button saveFactureButton;

    //Second Pane Inforamtion : 
    @FXML
    private TextField recepteurFacture;
    @FXML
    private ComboBox<String> serviceFacture;
    @FXML
    private TextField montantFacture;
    @FXML
    private TextArea descriptionFacture;
    @FXML
    private DatePicker dateFacture;

    // For Show Facture Information : 
    @FXML
    private Label serviceFactureLabel;
    @FXML
    private Label responsableFactureLabel;
    @FXML
    private Label montantFactureLabel;
    @FXML
    private Label dateFactureLabel;

    // Charts : 
    // Traitement : 
    public ObservableList<Facture> getFacture() {

        ObservableList<Facture> Facturelist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Facture";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Facture facture = new Facture();

                facture.setId(rs.getInt("IdFacture"));
                facture.setService(rs.getString("ServiceFacture"));
                facture.setRecepteur(rs.getString("RecepteurFacture"));
                facture.setDescription(rs.getString("DescriptionFacture"));
                facture.setMontant(rs.getFloat("MontantFacture"));
                facture.setDate(rs.getString("DateFacture"));

                Facturelist.add(facture);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getFacture() " + ex.getMessage());

        }

        return Facturelist;

    }

    public void afficheFacture() {
        try {

            ObservableList<Facture> list = getFacture();

            idFactureColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            serviceFactureColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
            recepteurFactureColumn.setCellValueFactory(new PropertyValueFactory<>("recepteur"));
            descriptionFactureColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            montantFactureColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            dateFactureColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

            tableFacture.setItems(list);

            System.out.println("affiche  facture event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur " + e.getMessage());

        }
    }

    public void insertFacture() {
        con = Connexion.getConnection();
        String insert = "INSERT INTO Facture (ServiceFacture,RecepteurFacture,DescriptionFacture,MontantFacture,DateFacture)VALUES(?,?,?,?,?)";

        try {

            Facture facture = new Facture();

            facture.setService(serviceFacture.getSelectionModel().getSelectedItem());
            facture.setRecepteur(recepteurFacture.getText());
            facture.setDescription(descriptionFacture.getText());
            facture.setMontant(Float.valueOf(montantFacture.getText()));
            facture.setDate(dateFacture.getValue().toString());

            st = con.prepareStatement(insert);

            st.setString(1, facture.getService());
            st.setString(2, facture.getRecepteur());
            st.setString(3, facture.getDescription());
            st.setFloat(4, facture.getMontant());
            st.setString(5, facture.getDate());

            st.executeUpdate();

            afficheFacture();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert " + ex.getMessage());
        }
    }

    public void modifyFacture() {
        try {

            addFacturePane.setVisible(true);

            Facture facture = tableFacture.getSelectionModel().getSelectedItem();

            recepteurFacture.setText(facture.getRecepteur());
            serviceFacture.getSelectionModel().select(facture.getService());
            montantFacture.setText(String.valueOf(facture.getMontant()));
            descriptionFacture.setText(facture.getDescription());
            dateFacture.setValue(LocalDate.now());

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement  de la facture  " + ex.getMessage());
        }
    }

    public void deleteFacture() {
        con = Connexion.getConnection();

        String delete = "DELETE FROM Facture where IdFacture = ?";

        try {

            if (tableFacture.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableFacture.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheFacture();
                System.out.println(" Delete Facture  executed Succefful ");
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

    public void clearFacture() {

        recepteurFacture.setText(null);
        serviceFacture.getSelectionModel().select(null);
        montantFacture.setText(null);
        descriptionFacture.setText(null);
        dateFacture.setValue(LocalDate.now());

    }

    public void updateFacture() {

        con = Connexion.getConnection();
        String update = "UPDATE Facture SET  ServiceFacture = ?, RecepteurFacture = ? , DescriptionFacture = ? , MontantFacture = ? , DateFacture  = ? where IdFacture  =?";
        try {

            st = con.prepareStatement(update);

            st.setString(1, serviceFacture.getSelectionModel().getSelectedItem());
            st.setString(2, recepteurFacture.getText());
            st.setString(3, descriptionFacture.getText());
            st.setFloat(4, Float.valueOf(montantFacture.getText()));
            st.setString(5, dateFacture.getValue().toString());
            st.setInt(6, tableFacture.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();
            afficheFacture();

            System.out.println("Update Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update de la facture " + ex.getMessage());

        }

    }

    public void LoadItems() {

        try {
            ObservableList<String> serviceEmployeList = FXCollections.observableArrayList("Cuisine", "Reception", "Maintenance", "Nettoyage", "Comptabilite");
            serviceFacture.setItems(serviceEmployeList);
        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    public void factureCountOnDashboard() {

        try {

            XYChart.Series series = new XYChart.Series();
            //Defining X axis  
            NumberAxis xAxis = new NumberAxis(1960, 2020, 10);
            xAxis.setLabel("Facture Id");
            //Defining y axis 
            NumberAxis yAxis = new NumberAxis(0, 350, 50);
            yAxis.setLabel("Facture Prices");

            factureLineChart = new LineChart(xAxis, yAxis);

            factureLineChart.setTitle("Factures,Prices");

            series.getData().add(new XYChart.Data<>(1, 20));
            series.getData().add(new XYChart.Data<>(2, 100));
            series.getData().add(new XYChart.Data<>(3, 80));
            series.getData().add(new XYChart.Data<>(4, 180));
            series.getData().add(new XYChart.Data<>(5, 20));
            series.getData().add(new XYChart.Data<>(6, -10));

            factureLineChart.getData().add(series);

        } catch (Exception ex) {
            System.out.println("Erret at facture line chart " + ex.getMessage());
        }
    }

    // Main Pane :
    @FXML
    void addFactureEvent1(ActionEvent event) {
        try {
            addFacturePane.setVisible(true);
            addFacturePane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }
    }

    @FXML
    void modifyFactureEvent(ActionEvent event) {
        try {
            addFacturePane.setVisible(true);
            addFacturePane.toFront();

            modifyFacture();

        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }

    }

    @FXML
    void deleteFactureEvent(ActionEvent event) {
        deleteFacture();
        afficheFacture();
    }

    @FXML
    void showFactureInformation(MouseEvent event) {
        Facture facture = tableFacture.getSelectionModel().getSelectedItem();

        serviceFactureLabel.setText(facture.getService());
        responsableFactureLabel.setText(facture.getRecepteur());
        montantFactureLabel.setText(facture.getMontant().toString());
        dateFactureLabel.setText(facture.getDate().toString());
    }

    // Second Pane :
    @FXML
    void addFactureEvent(ActionEvent event) {
        insertFacture();
        clearFacture();
        afficheFacture();
        addFacturePane.setVisible(false);
    }

    @FXML
    void clearFactureEvent(ActionEvent event) {
        clearFacture();
        afficheFacture();
    }

    @FXML
    void saveFactureEvent(ActionEvent event) {
        updateFacture();
        clearFacture();
        afficheFacture();

        addFacturePane.setVisible(false);

    }

    @FXML
    void exitAddFactureEvent(ActionEvent event) {
        addFacturePane.setVisible(false);

    }

    // Traitement Demande : 
    @FXML
    private JFXButton verifyFactureButton;
    @FXML
    private JFXButton eliminateFactureButton;

    @FXML
    private Label idDemandeLabel;
    @FXML
    private Label dateDemandeLabel;
    @FXML
    private Label montantDemandeLabel;

    @FXML
    private TableView<Demande> tableDemandeOnComptable;
    @FXML
    private TableColumn<Demande, Integer> idDemandeOnComptableColumn;
    @FXML
    private TableColumn<Demande, Boolean> statusDemandeOnComptableColumn;
    @FXML
    private TableColumn<Demande, Date> dateDemandeOnComptableColumn;
    @FXML
    private TableColumn<Demande, Float> montantDemandeOnComptableColumn;
    @FXML
    private TableColumn<Demande, String> descriptionDemandeOnComptableColumn;
    @FXML
    private TableColumn<Demande, String> serviceDemandeOnComptableColumn;

    @FXML
    private TextArea factureTextArea;
    @FXML
    private TextField searchDemande;

    public ObservableList<Demande> getDemande() {

        ObservableList<Demande> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM DemandeReception ";

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
                demande.setService(rs.getString("ServiceDemande"));

                if (demande.getStatus() == false) {
                    Produitlist.add(demande);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getDemande() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public void afficheDemande() {
        try {

            ObservableList<Demande> list = getDemande();

            idDemandeOnComptableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            dateDemandeOnComptableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            montantDemandeOnComptableColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            descriptionDemandeOnComptableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            statusDemandeOnComptableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            serviceDemandeOnComptableColumn.setCellValueFactory(new PropertyValueFactory<>("service"));

            tableDemandeOnComptable.setItems(list);

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

            sortdeData.comparatorProperty().bind(tableDemandeOnComptable.comparatorProperty());
            tableDemandeOnComptable.setItems(sortdeData);

            System.out.println("affiche  demande executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche demande erreur" + e.getMessage());

        }
    }

    public void deleteDemandeOnComptable() {

        if (tableDemandeOnComptable.getSelectionModel().getSelectedItem().getService().equals("Cuisine")) {

            con = Connexion.getConnection();
            String insert = "INSERT INTO CuisineNotification (EmetteurNotification,TitleNotification,ContentNotification)VALUES(?,?,?)";

            try {

                Notif notif = new Notif();

                notif.setEmetteur("Maitre Hotel");
                notif.setTitle("Facture Information");
                notif.setContent("La Facture Portant Le Id : " + tableDemandeOnComptable.getSelectionModel().getSelectedItem().getId() + " A Ete Rejete ");

                st = con.prepareStatement(insert);

                st.setString(1, notif.getEmetteur());
                st.setString(2, notif.getTitle());
                st.setString(3, notif.getContent());

                st.executeUpdate();

                con = Connexion.getConnection();
                String update = "UPDATE DemandeCuisine SET  StatusDemande = ? where IdDemande  = ?";
                try {

                    st = con.prepareStatement(update);

                    st.setBoolean(1, true);
                    st.setInt(2, tableDemandeOnComptable.getSelectionModel().getSelectedItem().getId());

                    st.executeUpdate();

                    System.out.println("Update Demande Oncomptable Is Executed succefully");

                } catch (SQLException ex) {

                    System.out.println("Erreur Au Cours D‘execution de la methode update de la demande On Comptable " + ex.getMessage());

                }
            } catch (SQLException ex) {
                System.out.println("Erreur Au Cours D‘execution de la methode insert Notification  " + ex.getMessage());
            }

        } else {
            con = Connexion.getConnection();
            String insert = "INSERT INTO ReceptionNotification (EmetteurNotification,TitleNotification,ContentNotification)VALUES(?,?,?)";

            try {

                Notif notif = new Notif();

                notif.setEmetteur("Maitre Hotel");
                notif.setTitle("Facture Information");
                notif.setContent("La Facture Portant Le Id : " + tableDemandeOnComptable.getSelectionModel().getSelectedItem().getId() + " A Ete Rejete ");

                st = con.prepareStatement(insert);

                st.setString(1, notif.getEmetteur());
                st.setString(2, notif.getTitle());
                st.setString(3, notif.getContent());

                st.executeUpdate();

                con = Connexion.getConnection();
                String update = "UPDATE DemandeReception SET  StatusDemande = ? where IdDemande  = ?";
                try {

                    st = con.prepareStatement(update);

                    st.setBoolean(1, true);
                    st.setInt(2, tableDemandeOnComptable.getSelectionModel().getSelectedItem().getId());

                    st.executeUpdate();

                    System.out.println("Update Demande Oncomptable Is Executed succefully");

                } catch (SQLException ex) {

                    System.out.println("Erreur Au Cours D‘execution de la methode update de la demande On Comptable " + ex.getMessage());

                }
            } catch (SQLException ex) {
                System.out.println("Erreur Au Cours D‘execution de la methode insert Notification  " + ex.getMessage());
            }
        }
    }

    public void updateDemandeOnComptable() {

        con = Connexion.getConnection();

        try {
            String update = "UPDATE DemandeCuisine SET  StatusDemande = ? where IdDemande  =?";

            st = con.prepareStatement(update);

            st.setBoolean(1, true);
            st.setInt(2, tableDemandeOnComptable.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();
            afficheDemande();

            System.out.println("Update Demande Oncomptable Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update de la demande On Comptable " + ex.getMessage());

        }
        try {
            String update = "UPDATE DemandeReception SET  StatusDemande = ? where IdDemande  =?";

            st = con.prepareStatement(update);

            st.setBoolean(1, true);
            st.setInt(2, tableDemandeOnComptable.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();
            afficheDemande();

            System.out.println("Update Demande Oncomptable Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update de la demande On Comptable " + ex.getMessage());

        }

    }

    @FXML
    void showDemandeInformation(MouseEvent event) {
        try {

            Demande demande = tableDemandeOnComptable.getSelectionModel().getSelectedItem();

            idDemandeLabel.setText(String.valueOf(demande.getId()));
            dateDemandeLabel.setText(demande.getDate());
            montantDemandeLabel.setText(String.valueOf(demande.getMontant()));

            factureTextArea.setText("Description Facture : \n" + demande.getDescription());

        } catch (Exception ex) {
            System.out.println("Erreur au cours action de la tableRepas" + ex.getMessage());
        }

    }

    @FXML
    void verifiyFactureEvent(ActionEvent event) {
        updateDemandeOnComptable();
        afficheDemande();

    }

    @FXML
    void eliminateFactureEvent(ActionEvent event) {
        deleteDemandeOnComptable();
        afficheDemande();

    }

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

    @FXML
    private TableView<Notif> tableNotificationOnDashboard;
    @FXML
    private TableColumn<Notif, String> emetteurNotificationOnDashboardColumn;
    @FXML
    private TableColumn<Notif, String> contentNotificationOnDashboardColumn;

    @FXML
    private Label emetteurNotificationLabel;
    @FXML
    private Label titleNotificationLabel;
    @FXML
    private JFXTextArea contentNotificationLabel;

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
        if (event.getSource() == notificationBtn) {
            notificationPane.toFront();
        }
        if (event.getSource() == factureBtn) {
            facturePane.toFront();
        }
        if (event.getSource() == signOutBtn) {
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        LoadItems();
        afficheFacture();
        factureCountOnDashboard();
        afficheEventOnDashborad();
        afficheDemande();
        demandeCountOnDashboard();
        factureCountOnDashboard();

    }

}
