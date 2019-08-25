
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class ProdajaController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljProdaja;
    @FXML
    private AnchorPane anchorSrednjiProdaja;
    @FXML
    private TableView<?> tableviewProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnFotografijaProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnTipProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnProizvodjacProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnModelProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnKolicinaProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnCenaProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnDostupnostProdaja;
    @FXML
    private Label labelKolicinaProdaja;
    @FXML
    private TextField txtFieldKolicinaProdaja;
    @FXML
    private Button btnDodajProdaja;
    @FXML
    private Button btnNazadProdaja;
    @FXML
    private AnchorPane anchorKonfigurator;
    @FXML
    private Label labelUkupnaCenaProdaja;
    @FXML
    private TextField txtFieldUkupnaCenaProdaja;
    @FXML
    private Label labelKonfigurator;
    @FXML
    private TableView<?> tableviewKonfigurator;
    @FXML
    private TableColumn<?, ?> tableColumnKonfiguratorNaziv;
    @FXML
    private TableColumn<?, ?> tableColumnKonfiguratorKolicina;
    @FXML
    private TableColumn<?, ?> tableColumnKonfiguratorCena;
    @FXML
    private Button btnProdaj;
    @FXML
    private Button btnExportujProdaja;
    @FXML
    private AnchorPane anchorLeviProdaja;
    @FXML
    private ComboBox<?> comboTipProdaja;
    @FXML
    private ComboBox<?> comboProizvodjacProdaja;
    @FXML
    private ComboBox<?> comboModelProdaja;
    @FXML
    private Label labelTipProdaja;
    @FXML
    private Label labelProizvodjacProdaja;
    @FXML
    private Label labelModelProdaja;
    @FXML
    private Label labelCenaOdProdaja;
    @FXML
    private Label labelCenaDoProdaja;
    @FXML
    private TextField txtFieldCenaOdProdaja;
    @FXML
    private TextField txtFieldCenaDoProdaja;
    @FXML
    private TextField txtFieldPretraziProdaja;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }   
    
    //vraca nazad na pocetni prozor
    @FXML
    public void nazad() throws IOException{
        Stage stage = (Stage) btnNazadProdaja.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));
        
        Scene loginScene = new Scene(root);
        
        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;
        
        
    }
    
    

}
