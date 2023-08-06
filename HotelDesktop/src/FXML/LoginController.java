package FXML;

import Mysql.Connexion;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
// Les Classe Pour OTP Verification :
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    @FXML
    private PasswordField motDePasse;
    @FXML
    private TextField nomUtilisateur;
    @FXML
    private Button exitButton;
    @FXML
    private Button showPasswordButton;
    @FXML
    private Label passwordShow;
    @FXML
    private Button logIn;

    // Definition du variable de verification otp : 
    int OTP;

    PreparedStatement st = null;
    ResultSet rs = null;
    Connection con = null;

    private Stage stage;
    private Scene scene;
    private Parent root;

    void effect() {

        logIn.setOnMouseEntered(event -> {

            logIn.setScaleX(1);
            logIn.setScaleY(1);

        });

        logIn.setOnMouseExited(event -> {
            logIn.setScaleX(1);
            logIn.setScaleY(1);
        });

        exitButton.setOnMouseEntered(event -> {

            exitButton.setScaleX(1.1);
            exitButton.setScaleY(1.1);

        });

        exitButton.setOnMouseExited(event -> {
            exitButton.setScaleX(1);
            exitButton.setScaleY(1);
        });

        showPasswordButton.setOnMouseEntered(event -> {

            showPasswordButton.setScaleX(1.1);
            showPasswordButton.setScaleY(1.1);

        });

        showPasswordButton.setOnMouseExited(event -> {
            showPasswordButton.setScaleX(1);
            showPasswordButton.setScaleY(1);
        });
    }

    @FXML
    private void exitEvent(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void loginPressed(ActionEvent event) {

        passwordShow.setVisible(false);
        boolean status = false;

        String select1 = "SELECT * FROM AdminLogin";
        String select2 = "SELECT * FROM ComptabiliteLogin";
        String select3 = "SELECT * FROM CuisineLogin";
        String select4 = "SELECT * FROM MaintenanceLogin";
        String select5 = "SELECT * FROM ReceptionLogin";
        String select6 = "SELECT * FROM RecrutementLogin";

        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select1);
            rs = st.executeQuery();

            System.out.println("Query executed succeffuly !");

            while (rs.next()) {

                if (nomUtilisateur.getText().equals(rs.getString("NomUtilisateur")) && motDePasse.getText().equals(rs.getString("MotDePasse"))) {

                    root = FXMLLoader.load(getClass().getResource("/FXML/Admin.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    System.out.println(" Admin verification executed succeffuly");
                    status = true;
                    break;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Erreur au cours de login +AdminLogin+ " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select2);
            rs = st.executeQuery();

            System.out.println("Query executed succeffuly !!");

            while (rs.next()) {

                if (nomUtilisateur.getText().equals(rs.getString("NomUtilisateur")) && motDePasse.getText().equals(rs.getString("MotDePasse"))) {

                    root = FXMLLoader.load(getClass().getResource("/FXML/ChefComptabilite.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    System.out.println(" Bienvenu comptable ");
                    status = true;

                    break;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Erreur au cours de login +ComptabiliteLogin+ " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select3);
            rs = st.executeQuery();

            System.out.println("Query executed succeffuly !!!");

            while (rs.next()) {

                if (nomUtilisateur.getText().equals(rs.getString("NomUtilisateur")) && motDePasse.getText().equals(rs.getString("MotDePasse"))) {

                    Parent root = FXMLLoader.load(getClass().getResource("/FXML/ChefCuisine.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    System.out.println("Verification executed succeffuly executed sucefully");
                    status = true;

                    break;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Erreur au cours de login +CuisineLogin+ " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select4);
            rs = st.executeQuery();

            System.out.println("Query executed succeffuly !!!!!");

            while (rs.next()) {

                if (nomUtilisateur.getText().equals(rs.getString("NomUtilisateur")) && motDePasse.getText().equals(rs.getString("MotDePasse"))) {

                    Parent root = FXMLLoader.load(getClass().getResource("/FXML/ChefReception.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                    System.out.println("Verification executed succeffuly executed sucefully");
                    status = true;

                    break;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Erreur au cours de login +MaintenanceLogin+ " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select5);
            rs = st.executeQuery();

            System.out.println("Query executed succeffuly !!!!!!");

            while (rs.next()) {

                if (nomUtilisateur.getText().equals(rs.getString("NomUtilisateur")) && motDePasse.getText().equals(rs.getString("MotDePasse"))) {

                    System.out.println("Verification executed succeffuly executed sucefully");
                    status = true;

                    break;
                }
            }

        } catch (SQLException ex) {
            System.out.println("Erreur au cours de login +receptionLogin+ " + ex.getMessage());
        }
        try {

            con = Connexion.getConnection();
            st = con.prepareStatement(select6);
            rs = st.executeQuery();

            System.out.println("Query executed succeffuly !");

            while (rs.next()) {

                if (nomUtilisateur.getText().equals(rs.getString("NomUtilisateur")) && motDePasse.getText().equals(rs.getString("MotDePasse"))) {

                    System.out.println("Verification executed succeffuly executed sucefully");
                    status = true;

                    break;
                }
            }

        } catch (Exception ex) {
            System.out.println("Erreur au cours de login +RecrutementLogin+ " + ex.getMessage());
        }

        if (status == false) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR:");
            alert.setHeaderText("Aucun Compte N‘est Trouve");
            alert.setContentText(" Veuillier Contacter L‘administrateur Pour cree un compte ");
            alert.show();
            System.out.println("Erreur d‘alert");

        }

    }

    @FXML
    void showPasswordEvent(MouseEvent event) {

        passwordShow.setVisible(true);
        passwordShow.setText("Your Password Is : " + motDePasse.getText());

    }

    @FXML
    void MotDePasseOublieEvent(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/NumeroPane.fxml"));

            Parent my_root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(my_root));
            stage.show();

        } catch (IOException ex) {

            System.out.println("Erreur au cours de changement de mot de passe " + ex.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        passwordShow.setVisible(false);
        effect();

    }

}
