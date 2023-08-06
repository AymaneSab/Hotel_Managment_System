package FXML;

import Classes.Component;
import Classes.Demande;
import Classes.Event;
import Classes.Notif;
import Classes.Produit;
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
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class ChefMaintenanceController implements Initializable {

    PreparedStatement st = null;
    ResultSet rs = null;
    Connection con = null;

    File addFile;
    String demande;
    float montant;

    @FXML
    private Circle adminCercle;

    @FXML
    private Pane DashboardPane;
    @FXML
    private Pane ComponentPane;
    @FXML
    private Pane creationProduitPane;
    @FXML
    private Pane demandeProduitPane;
    @FXML
    private Pane notificationPane;
    @FXML
    private Pane ProduitPane;
    @FXML
    private Pane DemandePane;

    @FXML
    private JFXButton dashboardBtn;
    @FXML
    private JFXButton creationProduitBtn;
    @FXML
    private JFXButton demandeProduitBtn;
    @FXML
    private JFXButton notificationBtn;
    @FXML
    private JFXButton ComponentBtn;
    @FXML
    private JFXButton signOutBtn;

    // Traitement Dashboard : 
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
    private PieChart componentPieChart;
    @FXML
    private PieChart maintenancePiechartOnDashboard;
    @FXML
    private PieChart componentPieChartOnDashboard;
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

        String select = "SELECT * FROM MaintenanceNotification";

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
            System.out.println("Erreur Au Cours De La Methode getMaintenanceNotif() " + ex.getMessage() + ex.getCause());

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

    public void componentCountOnDashboard() {

        int Electronique = 0;
        int Immobilier = 0;
        int Saponification = 0;
        int Divers = 0;

        try {

            String select = "SELECT  COUNT(*) AS Electronique FROM Component WHERE TypeComponent = 'Electronique' ";

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

            String select = "SELECT  COUNT(*) AS Immobilier FROM Component WHERE TypeComponent = 'Immobilier' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Immobilier = rs.getInt("Immobilier");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Saponification FROM Component WHERE TypeComponent = 'Saponification' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Saponification = rs.getInt("Saponification");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Divers FROM Component WHERE TypeComponent = 'Divers' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Divers = rs.getInt("Divers");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // repas Chart :
        PieChart.Data slice1 = new PieChart.Data("Electronique", Electronique);
        PieChart.Data slice2 = new PieChart.Data("Immobilier", Immobilier);
        PieChart.Data slice3 = new PieChart.Data("Saponification", Saponification);
        PieChart.Data slice4 = new PieChart.Data("Divers", Divers);

        componentPieChartOnDashboard.getData().addAll(slice1, slice2, slice3, slice4);
    }

    public void demandeMaintenanceCountOnDashboard() {

        int Reception = 0;
        int Cuisinier = 0;
        int Nettoyage = 0;
        int Maintenance = 0;

        try {

            String select = "SELECT  COUNT(*) AS Reception FROM DemandeMaintenanceReception  WHERE ServiceDemande = 'Reception' ";

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

            String select = "SELECT  COUNT(*) AS Cuisinier FROM DemandeMaintenanceCuisine WHERE ServiceDemande = 'Cuisinier' ";

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

            String select = "SELECT  COUNT(*) AS Maintenance FROM DemandeMaintenanceReception NATURAL JOIN DemandeCuisine WHERE ServiceDemande = 'Maintenance' ";

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

        maintenancePiechartOnDashboard.getData().addAll(slice1, slice2, slice3, slice4);

    }

    // Menu Pane :
    @FXML
    private Pane addComponentPane;

    //Second Pane Inforamtion : 
    @FXML
    private JFXButton addComponentButton1;
    @FXML
    private JFXButton deleteComponentButton;
    @FXML
    private JFXButton modifyComponentButton;

    @FXML
    private Button addComponentButton;
    @FXML
    private Button clearcomponentButton;
    @FXML
    private Button savecomponentButton;

    @FXML
    private TextField titreComponent;
    @FXML
    private TextField numeroComponent;
    @FXML
    private TextField prixComponent;
    @FXML
    private TextArea descriptionComponent;
    @FXML
    private ComboBox<String> typeComponent;

    @FXML
    private Rectangle componentImageRectangle;
    @FXML
    private Rectangle componentImage;

    // La table :
    @FXML
    private TableView<Component> tableComponent;
    @FXML
    private TableColumn<Component, Integer> idComponentColumn;
    @FXML
    private TableColumn<Component, Integer> numeroComponentColumn;
    @FXML
    private TableColumn<Component, String> titreComponentColumn;
    @FXML
    private TableColumn<Component, ComboBox> typeComponentColumn;
    @FXML
    private TableColumn<Component, String> descriptionComponentColumn;
    @FXML
    private TableColumn<Component, Float> prixComponentColumn;

    @FXML
    private TextField searchComponent;

    public ObservableList<Component> getComponent() {

        ObservableList<Component> Componentlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Component";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Component component = new Component();

                Blob blob = (Blob) rs.getBlob("ImageComponent");
                InputStream in = blob.getBinaryStream();
                Image blobimage = new Image(in);

                component.setId(rs.getInt("IdComponent"));
                component.setNumero(rs.getInt("numeroComponent"));
                component.setTitre(rs.getString("TitreComponent"));
                component.setType(rs.getString("TypeComponent"));
                component.setDescription(rs.getString("DescriptionComponent"));
                component.setPrix(rs.getFloat("PrixComponent"));
                component.setImage(blobimage);

                Componentlist.add(component);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getComponent() " + ex.getMessage());

        }

        return Componentlist;

    }

    public void afficheComponent() {
        try {

            ObservableList<Component> list = getComponent();

            idComponentColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            numeroComponentColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
            titreComponentColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            typeComponentColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionComponentColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            prixComponentColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

            tableComponent.setItems(list);

            FilteredList<Component> filteredData = new FilteredList<>(list, b -> true);

            searchComponent.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Component -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Component.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Component.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Component> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableComponent.comparatorProperty());
            tableComponent.setItems(sortdeData);

            System.out.println("affiche  Component event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche Component Erreur" + e.getMessage());

        }
    }

    public void insertComponent() {
        con = Connexion.getConnection();
        String insert = "INSERT INTO Component (TitreComponent,NumeroComponent,TypeComponent,DescriptionComponent,PrixComponent,ImageComponent)VALUES(?,?,?,?,?,?)";

        try {

            Component component = new Component();

            component.setTitre(titreComponent.getText());
            component.setNumero(Integer.valueOf(numeroComponent.getText()));
            component.setType(typeComponent.getSelectionModel().getSelectedItem());
            component.setDescription(descriptionComponent.getText());
            component.setPrix(Float.valueOf(prixComponent.getText()));
            component.setImage(new Image(addFile.toURI().toURL().toString()));

            st = con.prepareStatement(insert);

            FileInputStream input;

            input = new FileInputStream(addFile);

            st.setString(1, component.getTitre());
            st.setInt(2, component.getNumero());
            st.setString(3, component.getType());
            st.setString(4, component.getDescription());
            st.setFloat(5, component.getPrix());
            st.setBinaryStream(6, (InputStream) input, (int) addFile.length());

            st.executeUpdate();

            afficheComponent();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert du Menu " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChefCuisineController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ChefCuisineController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modifyComponent() {
        try {

            addComponentPane.setVisible(true);

            Component component = tableComponent.getSelectionModel().getSelectedItem();

            titreComponent.setText(component.getTitre());
            numeroComponent.setText(String.valueOf(component.getNumero()));
            typeComponent.getSelectionModel().select(component.getType());
            prixComponent.setText(String.valueOf(component.getPrix()));
            descriptionComponent.setText(component.getDescription());
            componentImageRectangle.setFill(new ImagePattern(component.getImage()));

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement  du repas  " + ex.getMessage());
        }
    }

    public void deleteComponent() {
        con = Connexion.getConnection();

        String delete = "DELETE FROM Component where IdComponent = ?";

        try {

            if (tableComponent.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableComponent.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                System.out.println(" Delete component  executed Succefful ");
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

    public void clearComponent() {

        titreComponent.setText(null);
        numeroComponent.setText(null);
        typeComponent.getSelectionModel().select(null);
        descriptionComponent.setText(null);
        prixComponent.setText(null);

    }

    public void updateComponent() {

        con = Connexion.getConnection();
        String update = "UPDATE Component SET  TitreComponent = ?, NumeroComponent = ? , TypeComponent = ?  , DescriptionComponent = ? , Prixcomponent = ? , ImageComponent = ?  where IdComponent  =?";
        try {

            st = con.prepareStatement(update);

            FileInputStream input;

            input = new FileInputStream(addFile);

            st.setString(1, titreComponent.getText());
            st.setInt(2, Integer.valueOf(numeroComponent.getText()));
            st.setString(3, typeComponent.getSelectionModel().getSelectedItem());
            st.setString(4, descriptionComponent.getText());
            st.setFloat(5, Float.valueOf(prixComponent.getText()));
            st.setBinaryStream(6, (InputStream) input, (int) addFile.length());
            st.setInt(7, tableComponent.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();

            afficheComponent();

            System.out.println("Update Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update de la facture " + ex.getMessage());

        } catch (FileNotFoundException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode update de la facture " + ex.getMessage());
        }

    }

    public void LoadComponentItems() {

        try {
            ObservableList<String> typeComponentsList = FXCollections.observableArrayList("Electronique", "Immobilier", "Saponification", "Divers");
            typeComponent.setItems(typeComponentsList);

        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    public void componentCount() {

        int Electronique = 0;
        int Immobilier = 0;
        int Saponification = 0;
        int Divers = 0;

        try {

            String select = "SELECT  COUNT(*) AS Electronique FROM Component WHERE TypeComponent = 'Electronique' ";

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

            String select = "SELECT  COUNT(*) AS Immobilier FROM Component WHERE TypeComponent = 'Immobilier' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Immobilier = rs.getInt("Immobilier");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Saponification FROM Component WHERE TypeComponent = 'Saponification' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Saponification = rs.getInt("Saponification");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Divers FROM Component WHERE TypeComponent = 'Divers' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Divers = rs.getInt("Divers");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // repas Chart :
        PieChart.Data slice1 = new PieChart.Data("Electronique", Electronique);
        PieChart.Data slice2 = new PieChart.Data("Immobilier", Immobilier);
        PieChart.Data slice3 = new PieChart.Data("Saponification", Saponification);
        PieChart.Data slice4 = new PieChart.Data("Divers", Divers);

        componentPieChart.getData().addAll(slice1, slice2, slice3, slice4);
    }

    // Main Pane :
    @FXML
    void addComponentEvent1(ActionEvent event) {
        try {
            addComponentPane.setVisible(true);
            addComponentPane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }
    }

    @FXML
    void modifyComponentEvent(ActionEvent event) {
        try {
            addComponentPane.setVisible(true);
            addComponentPane.toFront();

            modifyComponent();

        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }

    }

    @FXML
    void deleteComponentEvent(ActionEvent event) {
        deleteComponent();
        afficheComponent();
    }

    // Second Pane :
    @FXML
    void addComponentEvent(ActionEvent event) {
        insertComponent();
        clearComponent();
        afficheComponent();
        addComponentPane.setVisible(false);
    }

    @FXML
    void clearComponentEvent(ActionEvent event) {
        clearComponent();
        afficheComponent();
    }

    @FXML
    void saveComponentEvent(ActionEvent event) {
        updateComponent();
        clearComponent();
        afficheComponent();

        addComponentPane.setVisible(false);

    }

    @FXML
    void addComponentImage(MouseEvent event) {

        try {
            FileChooser fileChooser = new FileChooser();
            addFile = fileChooser.showOpenDialog(null);
            componentImageRectangle.setFill(new ImagePattern(new Image(addFile.toURI().toURL().toString())));
            System.out.println(" L‘evenement addRoomImage executed succeffuly ");
        } catch (MalformedURLException ex) {
            System.out.println("Erreur au cours de load d‘image " + ex.getMessage());
        }

    }

    @FXML
    void showComponentInformation(MouseEvent event) {

        Component component = tableComponent.getSelectionModel().getSelectedItem();

        componentImage.setFill(new ImagePattern(component.getImage()));

    }

    @FXML
    void exitAddComponentEvent(ActionEvent event) {
        addComponentPane.setVisible(false);

    }

    // Creation Facture Pane : 
    @FXML
    private Button addProduitBtn;
    @FXML
    private Button saveProduitBtn;
    @FXML
    private Button clearProduitBtn;

    @FXML
    private Button modifyProduitBtn;
    @FXML
    private Button deleteProduitBtn;
    @FXML
    private Button addProduitBtn1;

    @FXML
    private Rectangle rectangleImageProduit;

    @FXML
    private TextField titreProduit;
    @FXML
    private TextField prixProduit;
    @FXML
    private ComboBox<String> typeProduit;
    @FXML
    private TextArea descriptionProduit;

    @FXML
    private JFXTextArea descriptionComponentOnCreationLabel;
    @FXML
    private PieChart categorieProduitPieChart;

    void LoadPieChartItems() {

        int Electronique = 0;
        int Immobilier = 0;
        int Nettoyage = 0;
        int Divers = 0;
        int Saponification = 0;

        try {

            String select = "SELECT  COUNT(*) AS Electronique FROM ProduitMaintenance WHERE TypeProduit = 'Electronique' ";

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

            String select = "SELECT  COUNT(*) AS Immobilier FROM ProduitMaintenance WHERE TypeProduit = 'Immobilier' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Immobilier = rs.getInt("Immobilier");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Nettoyage FROM ProduitMaintenance WHERE TypeProduit = 'Nettoyage' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Nettoyage = rs.getInt("Animeaux");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Saponification FROM ProduitMaintenance WHERE TypeProduit = 'Saponification' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Saponification = rs.getInt("Saponification");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Divers FROM ProduitMaintenance WHERE TypeProduit = 'Divers' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Divers = rs.getInt("Divers");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        PieChart.Data slice1 = new PieChart.Data("Electronique", Electronique);
        PieChart.Data slice2 = new PieChart.Data("Immobilier", Immobilier);
        PieChart.Data slice3 = new PieChart.Data("Nettoyage", Nettoyage);
        PieChart.Data slice4 = new PieChart.Data("Saponification", Saponification);
        PieChart.Data slice5 = new PieChart.Data("Divers", Divers);

        categorieProduitPieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5);
    }

    // La Table Produit : 
    @FXML
    private TableView<Produit> tableProduit;
    @FXML
    private TableColumn<Produit, Integer> idProduitColumn;
    @FXML
    private TableColumn<Produit, String> titreProduitColumn;
    @FXML
    private TableColumn<Produit, Float> prixProduitColumn;
    @FXML
    private TableColumn<Produit, String> typeProduitColumn;
    @FXML
    private TableColumn<Produit, String> descriptionProduitColumn;

    public ObservableList<Produit> getProduit() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitMaintenance";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Produit produit = new Produit();

                produit.setId(rs.getInt("IdProduit"));
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

    public void afficheProduit() {
        try {

            ObservableList<Produit> list = getProduit();

            idProduitColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreProduitColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            prixProduitColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
            typeProduitColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionProduitColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableProduit.setItems(list);

            System.out.println("affiche  event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche Produit erreur" + e.getMessage());

        }
    }

    public void insertProduit() {
        try {

            con = Connexion.getConnection();
            String insert = "INSERT INTO ProduitMaintenance (TitreProduit,PrixProduit,TypeProduit,DescriptionProduit)VALUES(?,?,?,?)";

            Produit produit = new Produit();

            produit.setTitre(titreProduit.getText());
            produit.setPrix(Float.valueOf(prixProduit.getText()));
            produit.setType(typeProduit.getSelectionModel().getSelectedItem());
            produit.setDescription(descriptionProduit.getText());

            st = con.prepareStatement(insert);

            st.setString(1, produit.getTitre());
            st.setFloat(2, produit.getPrix());
            st.setString(3, produit.getType());
            st.setString(4, produit.getDescription());

            st.executeUpdate();

            afficheProduit();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert du Menu " + ex.getMessage());
        }
    }

    public void modifyProduit() {
        try {

            Produit produit = tableProduit.getSelectionModel().getSelectedItem();

            titreProduit.setText(produit.getTitre());
            prixProduit.setText(String.valueOf(produit.getPrix()));
            typeProduit.getSelectionModel().select(produit.getType());
            descriptionProduit.setText(produit.getDescription());

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement  du repas  " + ex.getMessage());
        }
    }

    public void deleteProduit() {
        try {

            con = Connexion.getConnection();

            String delete = "DELETE FROM ProduitMaintenance where IdProduit = ?";

            if (tableProduit.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableProduit.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheProduit();
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

    public void clearProduit() {

        titreProduit.setText(null);
        prixProduit.setText(null);
        typeProduit.getSelectionModel().select(null);
        descriptionProduit.setText(null);

    }

    public void updateProduit() {

        con = Connexion.getConnection();
        String update = "UPDATE ProduitMaintenance SET  TitreProduit = ?, PrixProduit = ? , TypeProduit = ? , DescriptionProduit = ? where IdProduit  =?";
        try {

            st = con.prepareStatement(update);

            st.setString(1, titreProduit.getText());
            st.setFloat(2, Float.valueOf(prixProduit.getText()));
            st.setString(3, typeProduit.getSelectionModel().getSelectedItem());
            st.setString(4, descriptionProduit.getText());
            st.setInt(5, tableProduit.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();

            System.out.println("Update Produit Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update du Produit " + ex.getMessage());

        }

    }

    public void LoadProduitItems() {

        try {
            ObservableList<String> typeRepasList = FXCollections.observableArrayList("Electronique", "Immobilier", "Saponification", "Nettoyage", "Divers");
            typeProduit.setItems(typeRepasList);

        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    @FXML
    void addProduitEvent1(ActionEvent event) {
        try {

            ProduitPane.setVisible(true);
            ProduitPane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur en AddProduitEvent " + ex.getMessage() + ex.getCause());
        }

    }

    @FXML
    void addProduitEvent(ActionEvent event) {
        try {
            insertProduit();
            clearProduit();
            afficheProduit();
            ProduitPane.setVisible(false);

        } catch (Exception ex) {
            System.out.println("Erreur en AddProduitEvent " + ex.getMessage() + ex.getCause());
        }

    }

    @FXML
    void saveProduitEvent(ActionEvent event) {
        try {
            updateProduit();
            clearProduit();
            afficheProduit();
            ProduitPane.setVisible(false);

        } catch (Exception ex) {
            System.out.println(" erreur En UpdateProduitEvent " + ex.getMessage() + ex.getCause());
        }

    }

    @FXML
    void clearProduitEvent(ActionEvent event) {
        clearProduit();
    }

    @FXML
    void modifyProduitEvent(ActionEvent event) {
        clearProduit();
        modifyProduit();
        ProduitPane.setVisible(true);
        ProduitPane.toFront();

    }

    @FXML
    void deleteProduitEvent(ActionEvent event) {
        try {

            deleteProduit();
            afficheProduit();
        } catch (Exception ex) {
            System.out.println("Erreur au de suppression du produit " + ex.getMessage() + ex.getCause());
        }

    }

    @FXML
    void exitAddProduitEvent(ActionEvent event) {
        ProduitPane.setVisible(false);

    }

    // La Table Component en pane de creation produits : 
    @FXML
    private TableView<Component> tableComponentOnCreation;
    @FXML
    private TableColumn<Component, Integer> idComponentOnCreationColumn;
    @FXML
    private TableColumn<Component, String> titreComponentOnCreationColumn;
    @FXML
    private TableColumn<Component, String> descriptionComponentOnCreationColumn;

    public void afficheComponentOnCreation() {
        try {

            ObservableList<Component> list = getComponent();

            idComponentOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreComponentOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionComponentOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableComponentOnCreation.setItems(list);

            FilteredList<Component> filteredData = new FilteredList<>(list, b -> true);

            searchComponentOnCreation.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Component -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Component.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Component> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableComponentOnCreation.comparatorProperty());
            tableComponentOnCreation.setItems(sortdeData);

            System.out.println("affiche  Component event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche Component Erreur" + e.getMessage());

        }
    }

    @FXML
    void showComponentDescription(MouseEvent event) {
        try {

            Component component = tableComponentOnCreation.getSelectionModel().getSelectedItem();

            descriptionComponentOnCreationLabel.setText(component.getDescription());

        } catch (Exception ex) {
            System.out.println("Erreur au cours action de la tableRepas" + ex.getMessage());
        }

    }

    // La Pane De Demande Produits : 
    @FXML
    private Button addProduitOnDemandeBtn;
    @FXML
    private Button submitDemandeBtn;
    @FXML
    private Button clearDemandeBtn;
    @FXML
    private Button modifyDemandeBtn;
    @FXML
    private Button deleteDemandeBtn;
    @FXML
    private Button saveDemandeBtn;

    @FXML
    private Rectangle rectangleImageProduitOnDemande;

    @FXML
    private ComboBox<String> typeProduitOnDemande;
    @FXML
    private ComboBox<Produit> produitOnDemande;
    @FXML
    private TextField quantiteProduit;
    @FXML
    private TextArea ficheDemande;

    @FXML
    private TableView<Produit> tableProduitOnDemande;
    @FXML
    private TableColumn<Produit, Integer> idProduitOnDemandeColumn;
    @FXML
    private TableColumn<Produit, String> titreProduitOnDemandeColumn;
    @FXML
    private TableColumn<Produit, String> typeProduitOnDemandeColumn;
    @FXML
    private TableColumn<Produit, String> descriptionProduitOnDemandeColumn;

    public ObservableList<Produit> getProduitOnDemande() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Produit";

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

    public void afficheProduitOnDemande() {
        try {

            ObservableList<Produit> list = getProduit();

            idProduitOnDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreProduitOnDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            typeProduitOnDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionProduitOnDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableProduitOnDemande.setItems(list);

            System.out.println("affiche  event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche Produit erreur" + e.getMessage());

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
    private TextField searchComponentOnCreation;
    @FXML
    private TextField searchDemande;
    @FXML
    private PieChart produitPieChartOnDemande;

    void LoadPieChartItems1() {

        int Electronique = 0;
        int Immobilier = 0;
        int Nettoyage = 0;
        int Divers = 0;
        int Saponification = 0;

        try {

            String select = "SELECT  COUNT(*) AS Electronique FROM ProduitMaintenance WHERE TypeProduit = 'Electronique' ";

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

            String select = "SELECT  COUNT(*) AS Immobilier FROM ProduitMaintenance WHERE TypeProduit = 'Immobilier' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Immobilier = rs.getInt("Immobilier");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Nettoyage FROM ProduitMaintenance WHERE TypeProduit = 'Nettoyage' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Nettoyage = rs.getInt("Animeaux");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Saponification FROM ProduitMaintenance WHERE TypeProduit = 'Saponification' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Saponification = rs.getInt("Saponification");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Divers FROM ProduitMaintenance WHERE TypeProduit = 'Divers' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Divers = rs.getInt("Divers");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        PieChart.Data slice1 = new PieChart.Data("Electronique", Electronique);
        PieChart.Data slice2 = new PieChart.Data("Immobilier", Immobilier);
        PieChart.Data slice3 = new PieChart.Data("Nettoyage", Nettoyage);
        PieChart.Data slice4 = new PieChart.Data("Saponification", Saponification);
        PieChart.Data slice5 = new PieChart.Data("Divers", Divers);

        produitPieChartOnDemande.getData().addAll(slice1, slice2, slice3, slice4, slice5);
    }

    public ObservableList<Produit> getElectroniqueProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitMaintenance";

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

                System.out.println(produit.getId());
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getImmobilierProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitMaintenance WHERE TypeProduit = 'Immobilier";

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

    public ObservableList<Produit> getSaponificationProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitMaintenance WHERE TypeProduit = 'Saponification'";

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

    public ObservableList<Produit> getNettoyageProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitMaintenance WHERE TypeProduit = 'Nettoyage'";

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

    public ObservableList<Produit> getDiversProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM ProduitMaintenance WHERE TypeProduit = 'Divers'";

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

    public ObservableList<Demande> getDemande() {

        ObservableList<Demande> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM DemandeMaintenance";

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
            String insert = "INSERT INTO DemandeMaintenance (DateDemande,MontantDemande,DescriptionDemande,StatusDemande,ServiceDemande)VALUES(?,?,?,?,?)";

            Demande demande = new Demande();

            LocalDate today = LocalDate.now();

            String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

            System.out.println(formattedDate);

            demande.setDate(formattedDate);
            demande.setMontant(montant);
            demande.setDescription(ficheDemande.getText());
            demande.setStatus(false);
            demande.setService("Maintenance");

            st = con.prepareStatement(insert);

            st.setString(1, demande.getDate());
            st.setFloat(2, demande.getMontant());
            st.setString(3, demande.getDescription());
            st.setBoolean(4, demande.getStatus());
            st.setString(5, demande.getService());
            st.executeUpdate();

            afficheDemande();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert du Menu " + ex.getMessage());
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

            String delete = "DELETE FROM DemandeMaintenance where IdDemande = ?";

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
        String update = "UPDATE DemandeMaintenance SET DateDemande = ? , DescriptionDemande = ?, MontantDemande = ? , StausDemande = ? , ServiceDemande = ? where IdDemande  =?";
        try {

            st = con.prepareStatement(update);

            LocalDate today = LocalDate.now();

            String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

            st.setString(1, formattedDate);
            st.setString(2, ficheDemande.getText());
            st.setFloat(3, montant);
            st.setBoolean(4, false);
            st.setInt(4, tableDemande.getSelectionModel().getSelectedItem().getId());
            st.setString(5, "Cuisinier");
            st.executeUpdate();

            System.out.println("Update Demande Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update du Demande " + ex.getMessage());

        }

    }

    void submitDemande() {
        insertDemande();
        montant = 0;

    }

    void clearDemande() {

        ficheDemande.setText(null);
    }

    void loadDemandeItems() {
        try {
            ObservableList<String> typeProduitList = FXCollections.observableArrayList("Electronique", "Immobilier", "Sanponifation", "Nettoyage", "Divers");
            typeProduitOnDemande.setItems(typeProduitList);

        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    @FXML
    void loadProductsItems(ActionEvent event) {

        if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Electronique")) {
            produitOnDemande.setItems(getElectroniqueProduitOnDemandeItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Immobilier")) {
            produitOnDemande.setItems(getImmobilierProduitOnDemandeItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Saponification")) {
            produitOnDemande.setItems(getSaponificationProduitOnDemandeItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Nettoyage")) {
            produitOnDemande.setItems(getNettoyageProduitOnDemandeItems());

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Divers")) {
            produitOnDemande.setItems(getDiversProduitOnDemandeItems());

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Aucun Selection Du Type Produit");
            alert.setContentText(" Veuillier Choisir Un Type Des Produits ");
            alert.show();
        }

    }

    @FXML
    void addDemandeEvent1(ActionEvent event) {
        DemandePane.setVisible(true);
        DemandePane.toFront();
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
        DemandePane.setVisible(false);

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
    void exitAddDemandeEvent(ActionEvent event) {
        DemandePane.setVisible(false);

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

            String delete = "DELETE FROM MaintenanceNotification where IdNotification = ?";

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
            DashboardPane.toFront();
        }
        if (event.getSource() == ComponentBtn) {
            ComponentPane.toFront();
        }
        if (event.getSource() == creationProduitBtn) {
            creationProduitPane.toFront();
        }
        if (event.getSource() == demandeProduitBtn) {
            demandeProduitPane.toFront();
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

        LoadComponentItems();
        afficheComponent();
        componentCount();
        componentCountOnDashboard();
        afficheEventOnDashborad();
        loadDemandeItems();
        LoadProduitItems();
        afficheComponentOnCreation();
        afficheNotification();
        afficheNotificationOnDashboard();
        afficheProduit();
        LoadPieChartItems();
        LoadPieChartItems1();
        try {
            adminCercle.setFill(new ImagePattern(new Image(("/Icons/alexander-hipp-iEEBWgY_6lA-unsplash.jpeg"))));

        } catch (Exception ex) {
            System.out.println("Erreur" + ex.getMessage());
        }


        /* 
        LoadProduitItems();
        afficheDemande();
        afficheEventOnDashborad();
         */
    }

}
