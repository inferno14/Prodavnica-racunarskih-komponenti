
package kontroler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class UIpocetnaController implements Initializable {
    


    @FXML
    private Button btnAdministracija;

    @FXML
    private Button btnPretraga;

    @FXML
    private Button btnProdaja;
    @FXML
    private Button btnPrijem;
    @FXML
    private Button btnIzvestaji;
    @FXML
    private Label labelNazivRadnje;
    @FXML
    private AnchorPane anchorRoditeljPocetna;
    @FXML
    private VBox vboxPocetna;
    @FXML
    private ImageView imageviewPocetna;
    @FXML
    private Button btnIzlaz;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         btnIzlaz.setOnAction(e-> {izlaz();
         });
    }   
    
    @FXML
    public void izlaz(){
        Platform.exit();
    }
    
    
    @FXML
    public void startLogin(ActionEvent event) throws IOException{
        Parent pogledLogin = FXMLLoader.load(getClass().getResource("/pogled/Login.fxml"));
        
        Scene loginScene = new Scene(pogledLogin);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(loginScene);
        window.show();
    }
    @FXML
    public void startPretraga(ActionEvent event) throws IOException{
        Parent pogledLogin = FXMLLoader.load(getClass().getResource("/pogled/Pretraga.fxml"));
        
        Scene loginScene = new Scene(pogledLogin);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(loginScene);
        window.show();
    }
    @FXML
    public void startProdaja(ActionEvent event) throws IOException{
        Parent pogledLogin = FXMLLoader.load(getClass().getResource("/pogled/Prodaja.fxml"));
        
        Scene loginScene = new Scene(pogledLogin);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(loginScene);
        window.setMaximized(true);
        window.show();
    }
    @FXML
    public void startPrijem(ActionEvent event) throws IOException{
        Parent pogledLogin = FXMLLoader.load(getClass().getResource("/pogled/Prijem.fxml"));
        
        Scene loginScene = new Scene(pogledLogin);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(loginScene);
        window.show();
    }
    @FXML
    public void startIzvestaji(ActionEvent event) throws IOException{
        Parent pogledLogin = FXMLLoader.load(getClass().getResource("/pogled/Izvestaji.fxml"));
        
        Scene loginScene = new Scene(pogledLogin);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        
        window.setScene(loginScene);
        
        window.show();
    }
    
    
    
    
}
