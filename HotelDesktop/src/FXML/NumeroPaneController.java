package FXML;

import Classes.Otp;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NumeroPaneController implements Initializable {

    
    String randomNumber(){
      int min = 50;
      int max = 100;
        
      //Generate random int value from 50 to 100 
      System.out.println("Random value in int from "+min+" to "+max+ ":");
      int random = (int)Math.floor(Math.random()*(max-min+1)+min);
      
      return "355" + random;
    }
   
    @FXML
    private TextField numeroDeTelephone;
    
     String  number = randomNumber();

    @FXML
    void nextEvent(ActionEvent event) {

        try {      

            try{
                String userName;
                String password;
                String to;
                String message;
                
                
                userName = "aymanesabri";
                password = "Aymane.1234/";
                to = numeroDeTelephone.getText();
                message = "Code De Verification : " + "\n" + "Admin : Aymane Sabri" + randomNumber();
                
               Otp pt = new Otp();
               pt.sms(userName, password, to, message);  

            }catch(Exception ex){
                System.out.println("Message not sended " + ex.getMessage());
            }
           
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/OtpPane.fxml"));

            Parent my_root = (Parent) loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(new Scene(my_root));
            stage.show();
            
            
        } catch (IOException ex) {

            System.out.println("Erreur au cours de changement de mot de passe " + ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
