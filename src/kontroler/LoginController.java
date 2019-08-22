
package kontroler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    @FXML
    public void nazad() throws IOException{
        Stage stage = (Stage) btnNazadLogin.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));
        
        Scene loginScene = new Scene(root);
        
        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;
        
        
    }
    
}
