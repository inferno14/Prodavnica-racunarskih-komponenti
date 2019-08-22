
package kontroler;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;


public class AdministracijaController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljAdministracija;
    @FXML
    private AnchorPane anchorDesniAdministracija;
    @FXML
    private TableView<?> tableViewAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnFotografijaAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnTipAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnProizvodjacAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnModelAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnKolicinaAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnCenaAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnDostupnostAdministracija;
    @FXML
    private Button btnNazadAdministracija;
    @FXML
    private AnchorPane anchorLeviAdministracija;
    @FXML
    private TextField txtFieldFotografijaAdministracija;
    @FXML
    private TextField txtFieldTipAdministracija;
    @FXML
    private TextField txtFieldProizvodjacAdministracija;
    @FXML
    private TextField txtFieldModelAdministracija;
    @FXML
    private TextField txtFieldCenaAdministracija;
    @FXML
    private HBox hboxAdministracija;
    @FXML
    private Button btnNoviAdministracija;
    @FXML
    private Button btnSacuvajAdministracija;
    @FXML
    private Button btnIzmeniAdministracija;
    @FXML
    private Button btnObrisiAdministracija;
    @FXML
    private Button btnOcistiAdministracija;


    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }  
    

   
}
