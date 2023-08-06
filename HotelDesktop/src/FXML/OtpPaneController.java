/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FXML;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OtpPaneController implements Initializable {

    @FXML
    private Pane OtpPane;
    @FXML
    private TextField otpCode;

    @FXML
    void nextEvent(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ChangePasswordPane.fxml"));

            Parent my_root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(new Scene(my_root));
            stage.show();

            //System.exit(0);
            
        } catch (IOException ex) {

            System.out.println("Erreur au cours de changement de mot de passe " + ex.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
