
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class IzvestajiController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljIzvestaji;
    @FXML
    private AnchorPane anchorDesniIzvestaji;
    @FXML
    private Button btnExportujIzestaji;
    @FXML
    private Label labelListaProdateRobe;
    @FXML
    private TableView<?> tableviewIzvestaji;
    @FXML
    private TableColumn<?, ?> tableColumnTipIzvestaji;
    @FXML
    private TableColumn<?, ?> tableColumnProizvodjacIzvestaji;
    @FXML
    private TableColumn<?, ?> tableColumnModelIzvestaji;
    @FXML
    private TableColumn<?, ?> tableColumnKolicinaIzvestaji;
    @FXML
    private TableColumn<?, ?> tableColumnPazarIzvestaji;
    @FXML
    private TableColumn<?, ?> tableColumnDatumOdIzvestaji;
    @FXML
    private Button btnNazadIzvestaji;
    @FXML
    private AnchorPane anchorLeviIzbestaji;
    @FXML
    private Button btnGodisnji;
    @FXML
    private Button btnMesecni;
    @FXML
    private Button btnNedeljni;
    @FXML
    private Button btniDnevni;
    @FXML
    private DatePicker datumOd;
    @FXML
    private DatePicker datumDo;
    @FXML
    private Label labelDatumOd;
    @FXML
    private Label labelDatumDo;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    //vraca nazad na pocetni prozor
    @FXML
    public void nazad() throws IOException{
        Stage stage = (Stage) btnNazadIzvestaji.getScene().getWindow();
        stage.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));
        
        Scene loginScene = new Scene(root);
        
        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;
        
        
    }
}
