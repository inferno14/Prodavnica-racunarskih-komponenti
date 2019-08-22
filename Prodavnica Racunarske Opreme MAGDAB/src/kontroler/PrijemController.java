
package kontroler;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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


public class PrijemController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljPrijem;
    @FXML
    private AnchorPane anchorDesniPrijem;
    @FXML
    private Button btnExportujPrijem;
    @FXML
    private Label labelListaPrimljenihKomponentiPrijem;
    @FXML
    private TableView<?> tableviewPrijem;
    @FXML
    private TableColumn<?, ?> tableColumnTipPrijem;
    @FXML
    private TableColumn<?, ?> tableColumnProizvodjacPrijem;
    @FXML
    private TableColumn<?, ?> tableColumnModelPrijem;
    @FXML
    private TableColumn<?, ?> tableColumnKolicinaPrijem;
    @FXML
    private Button btnNazadPrijem;
    @FXML
    private AnchorPane anchorLeviPrijem;
    @FXML
    private ComboBox<?> comboTipPrijem;
    @FXML
    private ComboBox<?> comboProizvodjacPrijem;
    @FXML
    private ComboBox<?> comboModelPrijem;
    @FXML
    private Label labelTipPrijem;
    @FXML
    private Label labelProizvodjacPrijem;
    @FXML
    private Label labelModelPrijem;
    @FXML
    private TextField txtFieldPretraziPrijem;
    @FXML
    private Button btnDodajNaStanje;
    @FXML
    private TableColumn<?, ?> tableColumnKolicinaPrijem1;
    @FXML
    private Label labelKolicinPrijem;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    public void nazad() throws IOException{
        Stage stage = (Stage) btnNazadPrijem.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));
        
        Scene loginScene = new Scene(root);
        
        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;
        
        
    }

    @FXML
    private void txtFieldKolicinaPrijem(ActionEvent event) {
    }
}
