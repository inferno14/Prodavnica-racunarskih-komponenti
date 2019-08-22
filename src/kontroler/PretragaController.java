
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


public class PretragaController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljPretraga;
    @FXML
    private AnchorPane anchorDesniPretraga;
    @FXML
    private TableView<?> tableviewPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnFotografijaPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnTipPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnProizvodjacPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnModelPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnKolicinaPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnCenaPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnDostupnostPretraga;
    @FXML
    private Button btnExportujPretraga;
    @FXML
    private Button btnNazadPretraga;
    @FXML
    private AnchorPane anchorLeviPretraga;
    @FXML
    private ComboBox<?> comboTipPretraga;
    @FXML
    private ComboBox<?> comboProizvodjacPretraga;
    @FXML
    private ComboBox<?> comboModelPretraga;
    @FXML
    private Label labelTipPretraga;
    @FXML
    private Label labelProizvodjacPretraga;
    @FXML
    private Label labelModelPretraga;
    @FXML
    private Label labelCenaOdPretraga;
    @FXML
    private Label labelCenaDoPretraga;
    @FXML
    private TextField txtFieldCenaOdPretraga;
    @FXML
    private TextField textFieldCenaDoPretraga;
    @FXML
    private TextField txtFieldPretraziPretraga;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    public void nazad() throws IOException{
        Stage stage = (Stage) btnNazadPretraga.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));
        
        Scene loginScene = new Scene(root);
        
        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;
        
        
    }
    
}
