
package kontroler;

import aplikacija.KonekcijaBaza;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class LoginController implements Initializable {

    @FXML
    private Label labelLogin;
    @FXML
    private HBox hboxUsername;
    @FXML
    private TextField txtFieldUsername;
    @FXML
    private HBox hboxPassword;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button btnLogin;
    @FXML
    private AnchorPane anchorRoditeljLogin;
    @FXML
    private VBox vboxLogin;
    @FXML
    private Button btnNazadLogin;
    private KonekcijaBaza konekcijaSaBazom;
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       konekcijaSaBazom = new KonekcijaBaza(); //uspostavlja vezu za bazom
    }    
    
    //vraca na pocetni prozor
    @FXML
    public void nazad() throws IOException{
        Stage stage = (Stage) btnNazadLogin.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));
        
        Scene loginScene = new Scene(root);
        
        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();
        
        
    }
    
    //provera paremetara amina
   public void proveraLogin() throws SQLException, ClassNotFoundException, IOException{
       
       
       Connection povezi = konekcijaSaBazom.poveziSe();
       
       //smesta u promenljive vrednosti iz text fieldova za username i password
       String username = txtFieldUsername.getText();
       String password = passwordField.getText();
      

        if(username.equals("") && password.equals("")){
            
            JOptionPane.showMessageDialog(null, "Morate uneti username i password!");
            
        }else if (username.equals("")){
            JOptionPane.showMessageDialog(null, "Niste uneli username!");
        }else if (password.equals("")){
            JOptionPane.showMessageDialog(null, "Niste uneli password!");
        }
        
        else{
              int nasao = 0;       
              Statement s = povezi.createStatement();
              ResultSet rs = s.executeQuery("SELECT username, password FROM admin");
            
            while (rs.next()) {
                
                if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
                    Stage stage = (Stage) btnLogin.getScene().getWindow();
                    stage.close();

                     Parent root = FXMLLoader.load(getClass().getResource("/pogled/Administracija.fxml"));

                     Scene loginScene = new Scene(root);

                     stage.setScene(loginScene);
                     stage.setMaximized(false);
                     stage.show();
                     nasao = 1;
                }
               
                   
                
                
                  
            }
            if(nasao==0){
            JOptionPane.showMessageDialog(null, "Netacni podaci!");
                   txtFieldUsername.setText("");
                   passwordField.setText("");
            }
            rs.close();
            s.close();
            povezi.close();
        }
   }
   
  


    
    
    
}
