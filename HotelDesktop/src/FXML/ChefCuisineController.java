package FXML;

import Classes.Demande;
import Classes.Event;
import Classes.Menu;
import Classes.Notif;
import Classes.Produit;
import Classes.Repas;
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

public class ChefCuisineController implements Initializable {

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
    private Pane MenuPane;
    @FXML
    private Pane chartPane;
    @FXML
    private Pane creationProduitPane;
    @FXML
    private Pane demandeProduitPane;
    @FXML
    private Pane addProduitPane;
    @FXML
    private Pane RepasPane;
    @FXML
    private Pane notificationPane;
    @FXML
    private Pane addDemandePane;

    @FXML
    private JFXButton dashboardBtn;
    @FXML
    private JFXButton creationProduitBtn;
    @FXML
    private JFXButton demandeProduitBtn;
    @FXML
    private JFXButton notificationBtn;
    @FXML
    private JFXButton menuBtn;
    @FXML
    private JFXButton chartBtn;
    @FXML
    private JFXButton signOutBtn;
    @FXML
    private JFXButton repasBtn;

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
    private PieChart menuPieChart2;
    @FXML
    private PieChart repasPieChart2;

    @FXML
    private TextField searchNotifOnDashboard;
    @FXML
    private TextField searchEventOnDashboard;

    @FXML
    private TableView<Notif> tableNotificationOnDashboard;
    @FXML
    private TableColumn<Notif, String> emetteurNotificationOnDashboardColumn;
    @FXML
    private TableColumn<Notif, String> contentNotificationOnDashboardColumn;

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

    public void menuCountOnDashboard() {

        int Gastronomique = 0;
        int Dégustation = 0;
        int Fixe = 0;
        int Complet = 0;
        int Fermé = 0;

        try {

            String select = "SELECT  COUNT(*) AS Gastronomique FROM Menu WHERE TypeMenu = 'Gastronomique' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Gastronomique = rs.getInt("Gastronomique");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Dégustation FROM Menu WHERE TypeMenu = 'Dégustation' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Dégustation = rs.getInt("Dégustation");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Fixe FROM Menu WHERE TypeMenu = 'Fixe' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Fixe = rs.getInt("Fixe");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Complet FROM Menu WHERE TypeMenu = 'Complet' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Complet = rs.getInt("Complet");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Fermé FROM Menu WHERE TypeMenu = 'Fermé' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Fermé = rs.getInt("Fermé");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // Service Chart :
        PieChart.Data slice1 = new PieChart.Data("Gastronomique", Gastronomique);
        PieChart.Data slice2 = new PieChart.Data("Dégustation", Dégustation);
        PieChart.Data slice3 = new PieChart.Data("Fixe", Fixe);
        PieChart.Data slice4 = new PieChart.Data("Complet", Complet);
        PieChart.Data slice5 = new PieChart.Data("Fermé", Fermé);

        menuPieChart2.getData().addAll(slice1, slice2, slice3, slice4, slice5);

    }

    public void repasCountOnDashboard() {

        int Marocaine = 0;
        int Indien = 0;
        int Mexicain = 0;
        int Chinois = 0;
        int Traditionnel = 0;

        try {

            String select = "SELECT  COUNT(*) AS Marocaine FROM Repas WHERE TypeRepas = 'Marocaine' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Marocaine = rs.getInt("Marocaine");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Indien FROM Repas WHERE TypeRepas = 'Indien' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Indien = rs.getInt("Indien");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Mexicain FROM Repas WHERE TypeRepas = 'Mexicain' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Mexicain = rs.getInt("Mexicain");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Chinois FROM Repas WHERE TypeRepas = 'Chinois' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Chinois = rs.getInt("Chinois");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Traditionnel FROM Repas WHERE TypeRepas = 'Traditionnel' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Traditionnel = rs.getInt("Traditionnel");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // repas Chart :
        PieChart.Data slice1 = new PieChart.Data("Marocaine", Marocaine);
        PieChart.Data slice2 = new PieChart.Data("Indien", Indien);
        PieChart.Data slice3 = new PieChart.Data("Mexicain", Mexicain);
        PieChart.Data slice4 = new PieChart.Data("Chinois", Chinois);
        PieChart.Data slice5 = new PieChart.Data("Traditionnel", Traditionnel);

        repasPieChart2.getData().addAll(slice1, slice2, slice3, slice4, slice5);
    }

    // Menu Pane :
    @FXML
    private Pane addMenuPane;

    @FXML
    private JFXButton addMenuButton1;
    @FXML
    private JFXButton deleteMenuButton;
    @FXML
    private JFXButton modifyMenuButton;

    @FXML
    private Button addMenuButton;
    @FXML
    private Button clearMenuButton;
    @FXML
    private Button saveMenuButton;

    //Second Pane Inforamtion : 
    @FXML
    private TextArea descriptionMenu;
    @FXML
    private TextField nombreRepasMenu;
    @FXML
    private TextField titreMenu;
    @FXML
    private ComboBox<String> typeMenu;

    @FXML
    private Label titreMenuLabel;
    @FXML
    private Label typeMenuLabel;
    @FXML
    private Label nombreRepasLabel;

    @FXML
    private PieChart menuPieChart;
    @FXML
    private TextField searchMenu;
    @FXML
    private TextField searchRepasOnMenu;

    @FXML
    private TableView<Menu> tableMenu;
    @FXML
    private TableColumn<Menu, Integer> idMenuColumn;
    @FXML
    private TableColumn<Menu, ComboBox> typeMenuColumn;
    @FXML
    private TableColumn<Menu, String> titreMenuColumn;
    @FXML
    private TableColumn<Menu, Integer> nombreRepasMenuColumn;
    @FXML
    private TableColumn<Menu, String> descriptionMenuColumn;

    public ObservableList<Menu> getMenu() {

        ObservableList<Menu> Menulist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Menu";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Menu menu = new Menu();

                menu.setId(rs.getInt("IdMenu"));
                menu.setTitre(rs.getString("TitreMenu"));
                menu.setType(rs.getString("TypeMenu"));
                menu.setNombreRepas(rs.getInt("NombreRepasMenu"));
                menu.setDescription(rs.getString("DescriptionMenu"));

                Menulist.add(menu);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getMenu() " + ex.getMessage());

        }

        return Menulist;

    }

    public void afficheMenu() {
        try {

            ObservableList<Menu> list = getMenu();

            idMenuColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            typeMenuColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            titreMenuColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            nombreRepasMenuColumn.setCellValueFactory(new PropertyValueFactory<>("nombreRepas"));
            descriptionMenuColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableMenu.setItems(list);

            FilteredList<Menu> filteredData = new FilteredList<>(list, b -> true);

            searchMenu.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Menu -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Menu.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Menu.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Menu> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableMenu.comparatorProperty());
            tableMenu.setItems(sortdeData);

            System.out.println("affiche  Menuevent executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }
    }

    public void insertMenu() {
        con = Connexion.getConnection();
        String insert = "INSERT INTO Menu (TitreMenu,TypeMenu,DescriptionMenu,NombreRepasMenu)VALUES(?,?,?,?)";

        try {

            Menu menu = new Menu();

            menu.setTitre(titreMenu.getText());
            menu.setType(typeMenu.getSelectionModel().getSelectedItem());
            menu.setDescription(descriptionMenu.getText());
            menu.setNombreRepas(Integer.valueOf(nombreRepasMenu.getText()));

            st = con.prepareStatement(insert);

            st.setString(1, menu.getTitre());
            st.setString(2, menu.getType());
            st.setString(3, menu.getDescription());
            st.setFloat(4, menu.getNombreRepas());

            st.executeUpdate();

            afficheMenu();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert du Menu " + ex.getMessage());
        }
    }

    public void modifyMenu() {
        try {

            addMenuPane.setVisible(true);

            Menu menu = tableMenu.getSelectionModel().getSelectedItem();

            titreMenu.setText(menu.getTitre());
            typeMenu.getSelectionModel().select(menu.getType());
            descriptionMenu.setText(menu.getDescription());
            nombreRepasMenu.setText(String.valueOf(menu.getNombreRepas()));

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement  de la facture  " + ex.getMessage());
        }
    }

    public void deleteMenu() {
        con = Connexion.getConnection();

        String delete = "DELETE FROM Menu where IdMenu = ?";

        try {

            if (tableMenu.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableMenu.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheMenu();
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

    public void clearMenu() {

        titreMenu.setText(null);
        typeMenu.getSelectionModel().select(null);
        descriptionMenu.setText(null);
        nombreRepasMenu.setText(null);

    }

    public void updateMenu() {

        con = Connexion.getConnection();
        String update = "UPDATE Menu SET  TitreMenu = ?, TypeMenu = ? , DescriptionMenu = ? , NombreRepasMenu = ?  where IdMenu  =?";
        try {

            st = con.prepareStatement(update);

            st.setString(1, titreMenu.getText());
            st.setString(2, typeMenu.getSelectionModel().getSelectedItem());
            st.setString(3, descriptionMenu.getText());
            st.setInt(4, Integer.valueOf(nombreRepasMenu.getText()));
            st.setInt(5, tableMenu.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();
            afficheMenu();

            System.out.println("Update Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update de la facture " + ex.getMessage());

        }

    }

    public void LoadItems() {

        try {
            ObservableList<String> typeMenuList = FXCollections.observableArrayList("Gastronomique", "Degustation", "Fixe", "Complet", "Ferme");
            typeMenu.setItems(typeMenuList);
        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    public void menuCount() {

        int Gastronomique = 0;
        int Dégustation = 0;
        int Fixe = 0;
        int Complet = 0;
        int Fermé = 0;

        try {

            String select = "SELECT  COUNT(*) AS Gastronomique FROM Menu WHERE TypeMenu = 'Gastronomique' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Gastronomique = rs.getInt("Gastronomique");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Dégustation FROM Menu WHERE TypeMenu = 'Dégustation' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Dégustation = rs.getInt("Dégustation");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Fixe FROM Menu WHERE TypeMenu = 'Fixe' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Fixe = rs.getInt("Fixe");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Complet FROM Menu WHERE TypeMenu = 'Complet' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Complet = rs.getInt("Complet");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Fermé FROM Menu WHERE TypeMenu = 'Fermé' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Fermé = rs.getInt("Fermé");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // Service Chart :
        PieChart.Data slice1 = new PieChart.Data("Gastronomique", Gastronomique);
        PieChart.Data slice2 = new PieChart.Data("Dégustation", Dégustation);
        PieChart.Data slice3 = new PieChart.Data("Fixe", Fixe);
        PieChart.Data slice4 = new PieChart.Data("Complet", Complet);
        PieChart.Data slice5 = new PieChart.Data("Fermé", Fermé);

        menuPieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5);

    }

    @FXML
    private TableView<Repas> tableRepasOnMenu;
    @FXML
    private TableColumn<Repas, String> titreRepasColumnOnMenu;
    @FXML
    private TableColumn<Repas, String> descriptionRepasColumnOnMenu;

    public ObservableList<Repas> getGastronomique() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Gastronomique' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getDegustation() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Degustation' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getFixe() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Fixe' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getComplet() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Complet' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getFerme() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Ferme' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public void afficheGastronomique() {

        try {

            ObservableList<Repas> list = getGastronomiqueRepas();

            titreRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnMenu.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnMenu.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Repas.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnMenu.comparatorProperty());
            tableRepasOnMenu.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheDegustation() {

        try {

            ObservableList<Repas> list = getDegustationRepas();

            titreRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnMenu.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnMenu.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnMenu.comparatorProperty());
            tableRepasOnMenu.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheFixe() {

        try {

            ObservableList<Repas> list = getFixeRepas();

            titreRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnMenu.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnMenu.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Repas.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnMenu.comparatorProperty());
            tableRepasOnMenu.setItems(sortdeData);
            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheComplet() {

        try {

            ObservableList<Repas> list = getCompletRepas();

            titreRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnMenu.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnMenu.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Repas.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnMenu.comparatorProperty());
            tableRepasOnMenu.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheFerme() {

        try {

            ObservableList<Repas> list = getFermeRepas();

            titreRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasColumnOnMenu.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnMenu.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnMenu.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Repas.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnMenu.comparatorProperty());
            tableRepasOnMenu.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    // Main Pane :
    @FXML
    void addMenuEvent1(ActionEvent event) {
        try {
            addMenuPane.setVisible(true);
            addMenuPane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }
    }

    @FXML
    void modifyMenuEvent(ActionEvent event) {
        try {
            addMenuPane.setVisible(true);
            addMenuPane.toFront();

            modifyMenu();

        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }

    }

    @FXML
    void deleteMenuEvent(ActionEvent event) {
        deleteMenu();
        afficheMenu();
    }

    // Second Pane :
    @FXML
    void addMenuEvent(ActionEvent event) {
        insertMenu();
        clearMenu();
        afficheMenu();
        addMenuPane.setVisible(false);
    }

    @FXML
    void clearMenuEvent(ActionEvent event) {
        clearMenu();
        afficheMenu();
    }

    @FXML
    void saveMenuEvent(ActionEvent event) {
        updateMenu();
        clearMenu();
        afficheMenu();

        addMenuPane.setVisible(false);

    }

    @FXML
    void showMenuInformation(MouseEvent event) {

        try {

            Menu menu = tableMenu.getSelectionModel().getSelectedItem();

            if (menu.getType().equals("Gastronomique")) {
                afficheGastronomique();
                System.out.println(" affiche Gastronomique repas executed ");
            }
            if (menu.getType().equals("Degustation")) {
                afficheDegustation();
                System.out.println(" affiche degustation repas executed ");

            }
            if (menu.getType().equals("Fixe")) {
                afficheFixe();
                System.out.println(" affiche fixe repas executed ");

            }
            if (menu.getType().equals("Complet")) {
                afficheComplet();
                System.out.println(" affiche complet repas executed ");

            }
            if (menu.getType().equals("Ferme")) {
                afficheFerme();
                System.out.println(" affiche ferme repas executed ");

            }

        } catch (Exception ex) {
            System.out.println("Erreur au cours action de la tableRepas" + ex.getMessage());
        }

    }

    @FXML
    void exitAddMenuEvent(ActionEvent event) {
        addMenuPane.setVisible(false);

    }

    // Add Repas Pane
    @FXML
    private Pane addRepasPane;

    @FXML
    private JFXButton addRepasButton1;
    @FXML
    private JFXButton deleteRepasButton;
    @FXML
    private JFXButton modifyRepasButton;

    @FXML
    private Button addRepasButton;
    @FXML
    private Button clearRepasButton;
    @FXML
    private Button saveRepasButton;

    @FXML
    private TextField titreRepas;
    @FXML
    private TextField numeroRepas;
    @FXML
    private TextField prixRepas;
    @FXML
    private TextArea descriptionRepas;
    @FXML
    private ComboBox<String> typeRepas;
    @FXML
    private ComboBox<String> menuRepas;
    @FXML
    private Rectangle repasImageRectangle;
    @FXML
    private Rectangle mealImage;

    @FXML
    private PieChart repasPieChart;
    @FXML
    private TextField searchRepas;

    // La table :
    @FXML
    private TableView<Repas> tableRepas;
    @FXML
    private TableColumn<Repas, Integer> idRepasColumn;
    @FXML
    private TableColumn<Repas, Integer> numeroRepasColumn;
    @FXML
    private TableColumn<Repas, String> titreRepasColumn;
    @FXML
    private TableColumn<Repas, ComboBox> typeRepasColumn;
    @FXML
    private TableColumn<Repas, ComboBox> menuRepasColumn;
    @FXML
    private TableColumn<Repas, String> descriptionRepasColumn;
    @FXML
    private TableColumn<Repas, Float> prixRepasColumn;

    public ObservableList<Repas> getRepas() {

        ObservableList<Repas> Menulist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                Blob blob = (Blob) rs.getBlob("ImageRepas");
                InputStream in = blob.getBinaryStream();
                Image blobimage = new Image(in);

                repas.setId(rs.getInt("IdRepas"));
                repas.setNumero(rs.getInt("numeroRepas"));
                repas.setTitre(rs.getString("TitreRepas"));
                repas.setType(rs.getString("TypeRepas"));
                repas.setMenu(rs.getString("MenuRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));
                repas.setPrix(rs.getFloat("PrixRepas"));
                repas.setImage(blobimage);

                Menulist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getMenu() " + ex.getMessage());

        }

        return Menulist;

    }

    public void afficheRepas() {
        try {

            ObservableList<Repas> list = getRepas();

            idRepasColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            numeroRepasColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
            titreRepasColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            typeRepasColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            menuRepasColumn.setCellValueFactory(new PropertyValueFactory<>("menu"));
            descriptionRepasColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            prixRepasColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

            tableRepas.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepas.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Repas.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Repas.getMenu().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepas.comparatorProperty());
            tableRepas.setItems(sortdeData);

            System.out.println("affiche  Menuevent executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }
    }

    public void insertRepas() {
        con = Connexion.getConnection();
        String insert = "INSERT INTO Repas (TitreRepas,NumeroRepas,TypeRepas,MenuRepas,DescriptionRepas,PrixRepas,ImageRepas)VALUES(?,?,?,?,?,?,?)";

        try {

            Repas repas = new Repas();

            repas.setTitre(titreRepas.getText());
            repas.setNumero(Integer.valueOf(numeroRepas.getText()));
            repas.setType(typeRepas.getSelectionModel().getSelectedItem());
            repas.setMenu(menuRepas.getSelectionModel().getSelectedItem());
            repas.setDescription(descriptionRepas.getText());
            repas.setPrix(Float.valueOf(prixRepas.getText()));
            repas.setImage(new Image(addFile.toURI().toURL().toString()));

            st = con.prepareStatement(insert);

            FileInputStream input;

            input = new FileInputStream(addFile);

            st.setString(1, repas.getTitre());
            st.setInt(2, repas.getNumero());
            st.setString(3, repas.getType());
            st.setString(4, repas.getMenu());
            st.setString(5, repas.getDescription());
            st.setFloat(6, repas.getPrix());
            st.setBinaryStream(7, (InputStream) input, (int) addFile.length());

            st.executeUpdate();

            afficheRepas();

        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode insert du Menu " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChefCuisineController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ChefCuisineController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void modifyRepas() {
        try {

            addRepasPane.setVisible(true);

            Repas repas = tableRepas.getSelectionModel().getSelectedItem();

            titreRepas.setText(repas.getTitre());
            numeroRepas.setText(String.valueOf(repas.getNumero()));
            typeRepas.getSelectionModel().select(repas.getType());
            menuRepas.getSelectionModel().select(repas.getMenu());
            descriptionRepas.setText(repas.getDescription());
            prixRepas.setText(String.valueOf(repas.getPrix()));
            repasImageRectangle.setFill(new ImagePattern(repas.getImage()));

        } catch (Exception ex) {

            System.out.println("Erreur au cours de changement  du repas  " + ex.getMessage());
        }
    }

    public void deleteRepas() {
        con = Connexion.getConnection();

        String delete = "DELETE FROM Repas where IdRepas = ?";

        try {

            if (tableRepas.getSelectionModel().getSelectedItem().getId() != 0) {
                st = con.prepareStatement(delete);
                st.setInt(1, tableRepas.getSelectionModel().getSelectedItem().getId());
                st.executeUpdate();
                afficheMenu();
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

    public void clearRepas() {

        titreRepas.setText(null);
        numeroRepas.setText(null);
        typeRepas.getSelectionModel().select(null);
        menuRepas.getSelectionModel().select(null);
        descriptionRepas.setText(null);
        prixRepas.setText(null);

    }

    public void updateRepas() {

        con = Connexion.getConnection();
        String update = "UPDATE Repas SET  TitreRepas = ?, NumeroRepas = ? , TypeRepas = ? , MenuRepas = ? , DescriptionRepas = ? , PrixRepas = ? , ImageRepas = ?  where IdRepas  =?";
        try {

            st = con.prepareStatement(update);

            FileInputStream input;

            input = new FileInputStream(addFile);

            st.setString(1, titreRepas.getText());
            st.setInt(2, Integer.valueOf(numeroRepas.getText()));
            st.setString(3, typeRepas.getSelectionModel().getSelectedItem());
            st.setString(4, menuRepas.getSelectionModel().getSelectedItem());
            st.setString(5, descriptionRepas.getText());
            st.setFloat(6, Float.valueOf(prixRepas.getText()));
            st.setBinaryStream(7, (InputStream) input, (int) addFile.length());
            st.setInt(8, tableRepas.getSelectionModel().getSelectedItem().getId());

            st.executeUpdate();

            afficheRepas();

            System.out.println("Update Is Executed succefully");

        } catch (SQLException ex) {

            System.out.println("Erreur Au Cours D‘execution de la methode update de la facture " + ex.getMessage());

        } catch (FileNotFoundException ex) {
            System.out.println("Erreur Au Cours D‘execution de la methode update de la facture " + ex.getMessage());
        }

    }

    public void LoadRepasItems() {

        try {
            ObservableList<String> typeRepasList = FXCollections.observableArrayList("Marocaine", "Indien", "Mexicain", "Chinois", "Traditionnel");
            typeRepas.setItems(typeRepasList);
            ObservableList<String> menuRepasList = FXCollections.observableArrayList("Gastronomique", "Degustation", "Fixe", "Complet", "Ferme");
            menuRepas.setItems(menuRepasList);

        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    public void repasCount() {

        int Marocaine = 0;
        int Indien = 0;
        int Mexicain = 0;
        int Chinois = 0;
        int Traditionnel = 0;

        try {

            String select = "SELECT  COUNT(*) AS Marocaine FROM Repas WHERE TypeRepas = 'Marocaine' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Marocaine = rs.getInt("Marocaine");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Indien FROM Repas WHERE TypeRepas = 'Indien' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Indien = rs.getInt("Indien");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Mexicain FROM Repas WHERE TypeRepas = 'Mexicain' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Mexicain = rs.getInt("Mexicain");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Chinois FROM Repas WHERE TypeRepas = 'Chinois' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Chinois = rs.getInt("Chinois");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Traditionnel FROM Repas WHERE TypeRepas = 'Traditionnel' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Traditionnel = rs.getInt("Traditionnel");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        // repas Chart :
        PieChart.Data slice1 = new PieChart.Data("Marocaine", Marocaine);
        PieChart.Data slice2 = new PieChart.Data("Indien", Indien);
        PieChart.Data slice3 = new PieChart.Data("Mexicain", Mexicain);
        PieChart.Data slice4 = new PieChart.Data("Chinois", Chinois);
        PieChart.Data slice5 = new PieChart.Data("Traditionnel", Traditionnel);

        repasPieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5);
    }

    // Main Pane :
    @FXML
    void addRepasEvent1(ActionEvent event) {
        try {
            addRepasPane.setVisible(true);
            addRepasPane.toFront();
        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }
    }

    @FXML
    void modifyRepasEvent(ActionEvent event) {
        try {
            addRepasPane.setVisible(true);
            addRepasPane.toFront();

            modifyRepas();

        } catch (Exception ex) {
            System.out.println("Erreur au cours de lancement de la pande däjout d‘employe" + ex.getMessage());
        }

    }

    @FXML
    void deleteRepasEvent(ActionEvent event) {
        deleteRepas();
        afficheRepas();
    }

    // Second Pane :
    @FXML
    void addRepasEvent(ActionEvent event) {
        insertRepas();
        clearRepas();
        afficheRepas();
        addRepasPane.setVisible(false);
    }

    @FXML
    void clearRepasEvent(ActionEvent event) {
        clearRepas();
        afficheRepas();
    }

    @FXML
    void saveRepasEvent(ActionEvent event) {
        updateRepas();
        clearRepas();
        afficheRepas();

        addRepasPane.setVisible(false);

    }

    @FXML
    void addRepasImage(MouseEvent event) {

        try {
            FileChooser fileChooser = new FileChooser();
            addFile = fileChooser.showOpenDialog(null);
            repasImageRectangle.setFill(new ImagePattern(new Image(addFile.toURI().toURL().toString())));
            System.out.println(" L‘evenement addRoomImage executed succeffuly ");
        } catch (MalformedURLException ex) {
            System.out.println("Erreur au cours de load d‘image " + ex.getMessage());
        }

    }

    @FXML
    void showRepasInformation(MouseEvent event) {

        Repas repas = tableRepas.getSelectionModel().getSelectedItem();

        mealImage.setFill(new ImagePattern(repas.getImage()));

    }

    @FXML
    void exitAddRepasEvent(ActionEvent event) {
        addRepasPane.setVisible(false);

    }

    // Creation Facture Pane : 
    @FXML
    private Button addProduitBtn;
    @FXML
    private Button saveProduitBtn;
    @FXML
    private Button clearProduitBtn;

    @FXML
    private Button addProduitBtn1;
    @FXML
    private Button modifyProduitBtn;
    @FXML
    private Button deleteProduitBtn;

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
    private JFXTextArea descriptionRepasOnCreationLabel;
    @FXML
    private TextField searchMenuOnCreation;
    @FXML
    private TextField searchRepasOnCreation;
    @FXML
    private TextField searchProduit;
    @FXML
    private PieChart produitPieChart;
    @FXML
    private PieChart ProduitPieChartOnDemande;

    void LoadPieChartItems() {
        ObservableList<String> typeRepasList = FXCollections.observableArrayList("Frais", "Conserves", "Surgelés", "Crus ", "Cuits");

        int Frais = 0;
        int Conserves = 0;
        int Surgelés = 0;
        int Crus = 0;
        int Cuits = 0;

        try {

            String select = "SELECT  COUNT(*) AS Electronique FROM Produit WHERE TypeProduit = 'Frais' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Frais = rs.getInt("Frais");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Sante FROM Produit WHERE TypeProduit = 'Conserves' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Conserves = rs.getInt("Conserves");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Surgelés FROM Produit WHERE TypeProduit = 'Surgelés' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Surgelés = rs.getInt("Surgelés");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Crus FROM Produit WHERE TypeProduit = 'Crus' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Crus = rs.getInt("Crus");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Cuits FROM Produit WHERE TypeProduit = 'Artisanal' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Cuits = rs.getInt("Cuits");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        PieChart.Data slice1 = new PieChart.Data("Frais", Frais);
        PieChart.Data slice2 = new PieChart.Data("Conserves", Conserves);
        PieChart.Data slice3 = new PieChart.Data("Surgelés", Surgelés);
        PieChart.Data slice4 = new PieChart.Data("Crus", Crus);
        PieChart.Data slice5 = new PieChart.Data("Cuits", Cuits);

        produitPieChart.getData().addAll(slice1, slice2, slice3, slice4, slice5);
    }

    void LoadPieDemandeItems() {
        ObservableList<String> typeRepasList = FXCollections.observableArrayList("Frais", "Conserves", "Surgelés", "Crus ", "Cuits");

        int Frais = 0;
        int Conserves = 0;
        int Surgelés = 0;
        int Crus = 0;
        int Cuits = 0;

        try {

            String select = "SELECT  COUNT(*) AS Electronique FROM Produit WHERE TypeProduit = 'Frais' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();

            while (rs.next()) {
                Frais = rs.getInt("Frais");

            }
            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Sante FROM Produit WHERE TypeProduit = 'Conserves' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Conserves = rs.getInt("Conserves");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Surgelés FROM Produit WHERE TypeProduit = 'Surgelés' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Surgelés = rs.getInt("Surgelés");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }
        try {

            String select = "SELECT  COUNT(*) AS Crus FROM Produit WHERE TypeProduit = 'Crus' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);

            rs = st.executeQuery();
            while (rs.next()) {
                Crus = rs.getInt("Crus");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        try {

            String select = "SELECT  COUNT(*) AS Cuits FROM Produit WHERE TypeProduit = 'Artisanal' ";

            con = Connexion.getConnection();
            st = con.prepareStatement(select);
            rs = st.executeQuery();
            while (rs.next()) {
                Cuits = rs.getInt("Cuits");

            }

            System.out.println("Query Executed ");

        } catch (SQLException ex) {
            System.out.println("Exception en serviceCount()" + ex.getMessage());
        }

        PieChart.Data slice1 = new PieChart.Data("Frais", Frais);
        PieChart.Data slice2 = new PieChart.Data("Conserves", Conserves);
        PieChart.Data slice3 = new PieChart.Data("Surgelés", Surgelés);
        PieChart.Data slice4 = new PieChart.Data("Crus", Crus);
        PieChart.Data slice5 = new PieChart.Data("Cuits", Cuits);

        ProduitPieChartOnDemande.getData().addAll(slice1, slice2, slice3, slice4, slice5);
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

        String select = "SELECT * FROM Produit";

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

            FilteredList<Produit> filteredData = new FilteredList<>(list, b -> true);

            searchProduit.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Produit -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Produit.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Produit.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
            SortedList<Produit> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableProduit.comparatorProperty());
            tableProduit.setItems(sortdeData);

            System.out.println("affiche  event executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche Produit erreur" + e.getMessage());

        }
    }

    public void insertProduit() {
        try {

            con = Connexion.getConnection();
            String insert = "INSERT INTO Produit (TitreProduit,PrixProduit,TypeProduit,DescriptionProduit)VALUES(?,?,?,?)";

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

            String delete = "DELETE FROM Produit where IdProduit = ?";

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
        String update = "UPDATE Produit SET  TitreProduit = ?, PrixProduit = ? , TypeProduit = ? , DescriptionProduit = ? where IdProduit  =?";
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
            ObservableList<String> typeRepasList = FXCollections.observableArrayList("Frais", "Conserves", "Surgelés", "Crus ", "Cuits");
            typeProduit.setItems(typeRepasList);

        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    @FXML
    void exitAddProduitEvent(ActionEvent event) {
        addProduitPane.setVisible(false);

    }

    @FXML
    void addProduitEvent1(ActionEvent event) {
        try {

            addProduitPane.setVisible(true);
            addProduitPane.toFront();

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
            addProduitPane.setVisible(false);

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

    // La Table Menu en pane de creation produits : 
    @FXML
    private TableView<Menu> tableMenuOnCreation;
    @FXML
    private TableColumn<Menu, Integer> idMenuOnCreationColumn;
    @FXML
    private TableColumn<Menu, String> titreMenuOnCreationColumn;
    @FXML
    private TableColumn<Menu, String> typeMenuOnCreationColumn;
    @FXML
    private TableColumn<Menu, String> descriptionMenuOnCreationColumn;

    public ObservableList<Menu> getMenuOnCreation() {

        ObservableList<Menu> Menulist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Menu";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Menu menu = new Menu();

                menu.setId(rs.getInt("IdMenu"));
                menu.setTitre(rs.getString("TitreMenu"));
                menu.setType(rs.getString("TypeMenu"));
                menu.setDescription(rs.getString("DescriptionMenu"));

                Menulist.add(menu);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getMenu() " + ex.getMessage());

        }

        return Menulist;

    }

    public void afficheMenuOnCreation() {
        try {

            ObservableList<Menu> list = getMenu();

            idMenuOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreMenuOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            typeMenuOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            descriptionMenuOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableMenuOnCreation.setItems(list);

            FilteredList<Menu> filteredData = new FilteredList<>(list, b -> true);

            searchMenuOnCreation.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Menu -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Menu.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else if (Menu.getType().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }

                });
            });
            SortedList<Menu> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableMenuOnCreation.comparatorProperty());
            tableMenuOnCreation.setItems(sortdeData);

            System.out.println("affiche  Menuevent executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }
    }

    // La Table Repas en pane de creation produits : 
    @FXML
    private TableView<Repas> tableRepasOnCreation;
    @FXML
    private TableColumn<Repas, Integer> idRepasOnCreationColumn;
    @FXML
    private TableColumn<Repas, String> titreRepasOnCreationColumn;
    @FXML
    private TableColumn<Repas, String> descriptionRepasOnCreationColumn;

    public ObservableList<Repas> getGastronomiqueRepas() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Gastronomique' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getDegustationRepas() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Degustation' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getFixeRepas() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Fixe' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getCompletRepas() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Complet' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public ObservableList<Repas> getFermeRepas() {

        ObservableList<Repas> Repaslist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Repas WHERE MenuRepas = 'Ferme' ";

        con = Connexion.getConnection();

        try {

            st = con.prepareStatement(select);
            rs = st.executeQuery();

            System.out.println("Query Executed ");

            while (rs.next()) {

                Repas repas = new Repas();

                repas.setTitre(rs.getString("TitreRepas"));
                repas.setDescription(rs.getString("DescriptionRepas"));

                Repaslist.add(repas);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getEmploye() " + ex.getMessage());

        }

        return Repaslist;

    }

    public void afficheGastronomiqueRepas() {

        try {

            ObservableList<Repas> list = getGastronomiqueRepas();

            titreRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnCreation.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnCreation.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnCreation.comparatorProperty());
            tableRepasOnCreation.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheDegustationRepas() {

        try {

            ObservableList<Repas> list = getDegustationRepas();

            titreRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnCreation.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnCreation.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnCreation.comparatorProperty());
            tableRepasOnCreation.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheFixeRepas() {

        try {

            ObservableList<Repas> list = getFixeRepas();

            titreRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnCreation.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnCreation.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnCreation.comparatorProperty());
            tableRepasOnCreation.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheCompletRepas() {

        try {

            ObservableList<Repas> list = getCompletRepas();

            titreRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnCreation.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnCreation.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnCreation.comparatorProperty());
            tableRepasOnCreation.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    public void afficheFermeRepas() {

        try {

            ObservableList<Repas> list = getFermeRepas();

            titreRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
            descriptionRepasOnCreationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            tableRepasOnCreation.setItems(list);

            FilteredList<Repas> filteredData = new FilteredList<>(list, b -> true);

            searchRepasOnCreation.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Repas -> {

                    String searchKeyword = newValue.toLowerCase();

                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    } else if (Repas.getTitre().toLowerCase().indexOf(searchKeyword) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            });
            SortedList<Repas> sortdeData = new SortedList<>(filteredData);

            sortdeData.comparatorProperty().bind(tableRepasOnCreation.comparatorProperty());
            tableRepasOnCreation.setItems(sortdeData);

            System.out.println("affiche executed succefully");

        } catch (Exception e) {

            System.out.println(" affiche erreur" + e.getMessage());

        }

    }

    @FXML
    void showMenuRepas(MouseEvent event) {
        try {

            Menu menu = tableMenuOnCreation.getSelectionModel().getSelectedItem();

            if (menu.getType().equals("Gastronomique")) {
                afficheGastronomiqueRepas();
                System.out.println(" affiche Gastronomique repas executed ");
            }
            if (menu.getType().equals("Degustation")) {
                afficheDegustationRepas();
                System.out.println(" affiche degustation repas executed ");

            }
            if (menu.getType().equals("Fixe")) {
                afficheFixeRepas();
                System.out.println(" affiche fixe repas executed ");

            }
            if (menu.getType().equals("Complet")) {
                afficheCompletRepas();
                System.out.println(" affiche complet repas executed ");

            }
            if (menu.getType().equals("Ferme")) {
                afficheFermeRepas();
                System.out.println(" affiche ferme repas executed ");

            }

        } catch (Exception ex) {
            System.out.println("Erreur au cours action de la tableRepas" + ex.getMessage());
        }

    }

    @FXML
    void showRepasDescription(MouseEvent event) {
        try {

            Repas repas = tableRepasOnCreation.getSelectionModel().getSelectedItem();

            descriptionRepasOnCreationLabel.setText(repas.getDescription());

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
    private Button addDemandeBtn;
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
    private TextField searchDemande;

    public ObservableList<Produit> getFraisEtBrutsProduitOnDemandeItems() {

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

                System.out.println(produit.getId());
            }
        } catch (SQLException ex) {
            System.out.println("Erreur Au Cours De La Methode getProduit() " + ex.getMessage() + ex.getCause());

        }

        return Produitlist;
    }

    public ObservableList<Produit> getConserveProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Produit WHERE TypeProduit = 'Conserves et semi-conserves'";

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

    public ObservableList<Produit> getSurgeleProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Produit WHERE TypeProduit = 'Produits surgelés'";

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

    public ObservableList<Produit> getCrusProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Produit WHERE TypeProduit = 'Produits crus prêts à l'emploi'";

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

    public ObservableList<Produit> getPretProduitOnDemandeItems() {

        ObservableList<Produit> Produitlist = FXCollections.observableArrayList();

        String select = "SELECT * FROM Produit WHERE TypeProduit = 'Produits crus prêts à l'emploi'";

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

        String select = "SELECT * FROM DemandeCuisine";

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
            String insert = "INSERT INTO DemandeCuisine (DateDemande,MontantDemande,DescriptionDemande,StatusDemande,ServiceDemande)VALUES(?,?,?,?,?)";

            Demande demande = new Demande();

            LocalDate today = LocalDate.now();

            String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));

            System.out.println(formattedDate);

            demande.setDate(formattedDate);
            demande.setMontant(montant);
            demande.setDescription(ficheDemande.getText());
            demande.setStatus(false);
            demande.setService("Cuisinier");

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

            String delete = "DELETE FROM DemandeCuisine where IdDemande = ?";

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
        String update = "UPDATE DemandeCuisine SET DateDemande = ? , DescriptionDemande = ?, MontantDemande = ? , StausDemande = ? , ServiceDemande = ? where IdDemande  =?";
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
            ObservableList<String> typeRepasList = FXCollections.observableArrayList("Produits frais et bruts", "Conserves et semi-conserves", "Produits surgelés", "Produits crus prêts à l'emploi", "Produits cuits sous vide");
            typeProduitOnDemande.setItems(typeRepasList);

            //produitOnDemande.setItems(getProduitOnDemandeItems());
            //  getProduitOnDemandeItems();
        } catch (Exception ex) {
            System.out.println("Erreur ! you know it " + ex.getMessage());
        }
    }

    @FXML
    void loadProductsItems(ActionEvent event) {

        if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Produits frais et bruts")) {
            produitOnDemande.setItems(getFraisEtBrutsProduitOnDemandeItems());
            System.out.println("produit frais sont present ");
            System.out.println(produitOnDemande.getItems());
        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Conserves et semi-conserves")) {
            produitOnDemande.setItems(getConserveProduitOnDemandeItems());
            System.out.println("produit conserves sont present ");

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Produits surgelés")) {
            produitOnDemande.setItems(getSurgeleProduitOnDemandeItems());
            System.out.println("produit surgeles sont present ");

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Produits crus prêts à l'emploi")) {
            produitOnDemande.setItems(getCrusProduitOnDemandeItems());
            System.out.println("produit crus sont present ");

        } else if (typeProduitOnDemande.getSelectionModel().getSelectedItem().equals("Produits cuits sous vide")) {
            produitOnDemande.setItems(getPretProduitOnDemandeItems());
            System.out.println("produit cuits sont present ");

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Aucun Selection Du Type Produit");
            alert.setContentText(" Veuillier Choisir Un Type Des Produits ");
            alert.show();
        }

    }

    @FXML
    void addDemandeEvent(ActionEvent event) {
        addDemandePane.setVisible(true);
        addDemandePane.toFront();
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
        addDemandePane.setVisible(false);

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
    void exitDemandeEvent(ActionEvent event) {
        addDemandePane.setVisible(false);

    }

    @FXML
    void click(ActionEvent event) {
        if (event.getSource() == dashboardBtn) {
            DashboardPane.toFront();
        }
        if (event.getSource() == chartBtn) {
            chartPane.toFront();
        }
        if (event.getSource() == menuBtn) {
            MenuPane.toFront();
        }
        if (event.getSource() == repasBtn) {
            RepasPane.toFront();
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

        try {

            LoadItems();
            afficheMenu();
            menuCount();
            LoadRepasItems();
            repasCount();
            afficheRepas();
            afficheProduit();
            LoadProduitItems();
            afficheMenuOnCreation();
            loadDemandeItems();
            afficheDemande();
            menuCountOnDashboard();
            repasCountOnDashboard();
            afficheNotificationOnDashboard();
            afficheEventOnDashborad();
            LoadPieChartItems();
            LoadPieDemandeItems();

            adminCercle.setFill(new ImagePattern(new Image(("/Icons/alexander-hipp-iEEBWgY_6lA-unsplash.jpeg"))));

        } catch (Exception ex) {
            System.out.println("Erreur " + ex.getMessage());
        }

    }

}
