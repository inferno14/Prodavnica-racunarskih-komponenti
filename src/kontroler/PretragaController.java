package kontroler;

import aplikacija.Komponenta;
import aplikacija.KonekcijaBaza;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class PretragaController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljPretraga;
    @FXML
    private AnchorPane anchorDesniPretraga;
    @FXML
    private TableView<Komponenta> tableviewPretraga;
    @FXML
    private TableColumn<?, ?> tableColumnFotografijaPretraga;
    @FXML
    private TableColumn<Komponenta, String> tableColumnTipPretraga;
    @FXML
    private TableColumn<Komponenta, String> tableColumnProizvodjacPretraga;
    @FXML
    private TableColumn<Komponenta, String> tableColumnModelPretraga;
    @FXML
    private TableColumn<Komponenta, Integer> tableColumnKolicinaPretraga;
    @FXML
    private TableColumn<Komponenta, Float> tableColumnCenaPretraga;
    @FXML
    private TableColumn<Komponenta, String> tableColumnDostupnostPretraga;
    @FXML
    private Button btnExportujPretraga;
    @FXML
    private Button btnNazadPretraga;
    @FXML
    private AnchorPane anchorLeviPretraga;
    @FXML
    private ComboBox<String> comboTipPretraga;
    @FXML
    private ComboBox<String> comboProizvodjacPretraga;
    @FXML
    private ComboBox<String> comboModelPretraga;
    @FXML
    private Label labelTipPretraga;
    @FXML
    private Label labelProizvodjacPretraga;
    @FXML
    private Label labelModelPretraga;
    @FXML
    private TextField txtFieldCenaOdPretraga;
    @FXML
    private TextField txtFieldPretraziPretraga;

    private KonekcijaBaza konekcijaSaBazom;
    private ObservableList<Komponenta> podaci; //lista koja omogucava onome koji osluskuje da prati izmene koje kada se dogode
    private ObservableList<String> comboTipPodaci, comboProizvodjacPodaci, comboModelPodaci;
    private ResultSet rs = null;
    @FXML
    private Button btnPretraziPretraga;
    @FXML
    private Button osveziTabeluPretraga;
    @FXML
    private TextField txtFieldCenaDoPretraga;
    @FXML
    private Button btnPotrdiPretraga;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        konekcijaSaBazom = new KonekcijaBaza(); //uspostavljanje veze sa bazom preko definisane klase za konekciju(KonekcijaBaza)
        try {
            ucitajPodatkeIzBaze(); //pocetno ucitaavanje tabele
        } catch (SQLException ex) {
            Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            popuniComboBoxTip();
            popuniComboBoxProizvodjac();
            popuniComboBoxModel();
        } catch (SQLException ex) {
            Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        comboTipPretraga.getSelectionModel().selectedItemProperty().addListener((comboTipPretraga, oldValue, newValue) -> {

            try {

                ucitajProizvodjace(newValue);
                ucitajModeleOdTipova(newValue);

            } catch (SQLException ex) {
                Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ocistiPoljaCene();
        });
        comboProizvodjacPretraga.getSelectionModel().selectedItemProperty().addListener((comboProizvodjacPretraga, oldValue, newValue) -> {
            String tip = comboTipPretraga.getSelectionModel().getSelectedItem();

            if (comboTipPretraga.getSelectionModel().getSelectedIndex() == -1) {
                try {
                    rezultatiSamoProizvodjaca(newValue);
                } catch (SQLException ex) {
                    Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {

                    ucitajModeleProizvodjaca(tip, newValue);
                } catch (SQLException ex) {
                    Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ocistiPoljaCene();

        });
        comboModelPretraga.getSelectionModel().selectedItemProperty().addListener((comboModelPretraga, oldValue, newValue) -> {

            String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
            String tip = comboTipPretraga.getSelectionModel().getSelectedItem();

            try {
                ucitajRezultatePretrage(tip, proizvodjac, newValue);

            } catch (SQLException ex) {
                Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ocistiPoljaCene();
        });

    }

    //vraca nazad na pocetni prozor aplikacije
    @FXML
    public void nazad() throws IOException {
        Stage stage = (Stage) btnNazadPretraga.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));

        Scene loginScene = new Scene(root);

        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;

    }

    @FXML
    public void ucitajPodatkeIzBaze() throws SQLException {

        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();
        //prikazuje iz baze samo podatke koji su dostupni(active)
        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewPretraga.setItems(null);
        tableviewPretraga.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void popuniComboBoxTip() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();
        String queryTip = "SELECT distinct tip from roba where Dostupnost='active'";

        comboTipPodaci = FXCollections.observableArrayList();

        ResultSet rsTip = null;

        rsTip = povezi.createStatement().executeQuery(queryTip);

        while (rsTip.next()) {
            comboTipPodaci.add(rsTip.getString("tip"));
        }

        comboTipPretraga.setItems(comboTipPodaci);
        comboTipPretraga.setValue("Tip");

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void popuniComboBoxProizvodjac() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        String queryProizvodjac = "SELECT distinct proizvodjac from roba where Dostupnost='active'";

        comboProizvodjacPodaci = FXCollections.observableArrayList();
        ResultSet rsProizvodjac = null;

        rsProizvodjac = povezi.createStatement().executeQuery(queryProizvodjac);

        while (rsProizvodjac.next()) {
            comboProizvodjacPodaci.add(rsProizvodjac.getString("proizvodjac"));
        }

        comboProizvodjacPretraga.setItems(comboProizvodjacPodaci);
        comboProizvodjacPretraga.setValue("Proizvodjac");

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void popuniComboBoxModel() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        String queryModel = "SELECT distinct model from roba where Dostupnost='active'";

        comboModelPodaci = FXCollections.observableArrayList();
        ResultSet rsModel = null;

        rsModel = povezi.createStatement().executeQuery(queryModel);

        while (rsModel.next()) {
            comboModelPodaci.add(rsModel.getString("model"));
        }

        comboModelPretraga.setItems(comboModelPodaci);
        comboModelPretraga.setValue("Model");

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }
    @FXML
    public void ucitajProizvodjace(String tip) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboProizvodjacPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT distinct proizvodjac from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {

            comboProizvodjacPodaci.add(rs.getString("proizvodjac"));

        }

        comboProizvodjacPretraga.setItems(comboProizvodjacPodaci);

        ucitajRezultatePretrage(tip);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void ucitajModeleProizvodjaca(String tip, String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "' and tip='" + tip + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        comboModelPretraga.setItems(comboModelPodaci);

        ucitajRezultatePretrage(tip, proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }
    @FXML
    public void ucitajRezultatePretrage(String tip) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewPretraga.setItems(null);
        tableviewPretraga.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }
    @FXML
    public void ucitajRezultatePretrage(String tip, String proizvodjac) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "' and tip='" + tip + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewPretraga.setItems(null);
        tableviewPretraga.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void ucitajRezultatePretrage(String tip, String proizvodjac, String model) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and model='" + model + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewPretraga.setItems(null);
        tableviewPretraga.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void pretragaUkucaj() throws SQLException {
        podaci = FXCollections.observableArrayList();

        String ukucano = txtFieldPretraziPretraga.getText();

        String query = "select * from roba where model like '%" + ukucano + "%'";
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery(query);
        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewPretraga.setItems(null);
        tableviewPretraga.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    private void osveziTabelu(ActionEvent event) throws SQLException {
        ucitajPodatkeIzBaze();
    }

    @FXML
    private void rezultatiSamoProizvodjaca(String proizvodjac) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewPretraga.setItems(null);
        tableviewPretraga.setItems(podaci);

        ucitajModeleProizvodjaca(proizvodjac);
        ucitajRezultateSamoProizvodjac(proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }
    
    @FXML
    public void ucitajModeleProizvodjaca(String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        comboModelPretraga.setItems(comboModelPodaci);

        ucitajRezultatePretrage(proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void ucitajRezultateSamoProizvodjac(String proizvodjac) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewPretraga.setItems(null);
        tableviewPretraga.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void ucitajModeleOdTipova(String tip) throws SQLException {

        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        comboModelPretraga.setItems(comboModelPodaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    private void prikazICenu(ActionEvent event) throws SQLException {

        Connection povezi = konekcijaSaBazom.poveziSe();
        podaci = FXCollections.observableArrayList();
        int cenaOd = 0;
        int cenaDo = 0;

        String queryTipIliProizvodjac = "";

        if (txtFieldCenaOdPretraga.getText().equals("") && txtFieldCenaDoPretraga.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Niste uneli nijednu cenu!");

        } else {

            if (txtFieldCenaOdPretraga.getText().equals("") && !txtFieldCenaDoPretraga.getText().equals("")) {
                cenaDo = Integer.parseInt(txtFieldCenaDoPretraga.getText());

                if (comboTipPretraga.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() != -1) {
                    String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
                    cenaDo = Integer.parseInt(txtFieldCenaDoPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and proizvodjac= '" + proizvodjac + "' and cena <= " + cenaDo + "";
                    System.out.println(queryTipIliProizvodjac);

                } else if (comboTipPretraga.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() == -1) {
                    String tip = comboTipPretraga.getSelectionModel().getSelectedItem();
                    cenaDo = Integer.parseInt(txtFieldCenaDoPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and tip= '" + tip + "' and cena <= " + cenaDo + "";
                    System.out.println(queryTipIliProizvodjac);
                } else if ((comboTipPretraga.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() == -1)) { //|| (comboTipPretraga.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex()!=-1) ){

                    cenaDo = Integer.parseInt(txtFieldCenaDoPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena <= " + cenaDo + "";

                } else if ((comboTipPretraga.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() != -1)) { //|| (comboTipPretraga.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex()!=-1) ){

                    String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
                    String tip = comboTipPretraga.getSelectionModel().getSelectedItem();
                    cenaDo = Integer.parseInt(txtFieldCenaDoPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena <= " + cenaDo + " and proizvodjac='" + proizvodjac + "' and tip ='" + tip + "'";

                }
                rs = povezi.createStatement().executeQuery(queryTipIliProizvodjac);

                while (rs.next()) {
                    podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
                }

                //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
                tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
                tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
                tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
                tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
                tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
                tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

                tableviewPretraga.setItems(null);
                tableviewPretraga.setItems(podaci);

            } else if (!txtFieldCenaOdPretraga.getText().equals("") && txtFieldCenaDoPretraga.getText().equals("")) {
                cenaOd = Integer.parseInt(txtFieldCenaOdPretraga.getText());

                if (comboTipPretraga.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() != -1) {
                    String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
                    cenaOd = Integer.parseInt(txtFieldCenaOdPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and proizvodjac= '" + proizvodjac + "' and cena >= " + cenaOd + "";

                } else if (comboTipPretraga.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() == -1) {
                    String tip = comboTipPretraga.getSelectionModel().getSelectedItem();
                    cenaOd = Integer.parseInt(txtFieldCenaOdPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and tip= '" + tip + "' and cena >= " + cenaOd + "";

                } else if ((comboTipPretraga.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() == -1)) { //|| (comboTipPretraga.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex()!=-1) ){

                    cenaOd = Integer.parseInt(txtFieldCenaOdPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaOd + "";

                } else if ((comboTipPretraga.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() != -1)) { //|| (comboTipPretraga.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex()!=-1) ){

                    String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
                    String tip = comboTipPretraga.getSelectionModel().getSelectedItem();
                    cenaOd = Integer.parseInt(txtFieldCenaOdPretraga.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaOd + " and proizvodjac='" + proizvodjac + "' and tip ='" + tip + "'";

                }

                rs = povezi.createStatement().executeQuery(queryTipIliProizvodjac);
                while (rs.next()) {
                    podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
                }

                //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
                tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
                tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
                tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
                tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
                tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
                tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

                tableviewPretraga.setItems(null);
                tableviewPretraga.setItems(podaci);

            } else if (!txtFieldCenaOdPretraga.getText().equals("") && !txtFieldCenaDoPretraga.getText().equals("")) {
                cenaDo = Integer.parseInt(txtFieldCenaDoPretraga.getText());
                cenaOd = Integer.parseInt(txtFieldCenaOdPretraga.getText());

                if (comboTipPretraga.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() != -1) {
                    String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and proizvodjac= '" + proizvodjac + "' and cena <= " + cenaDo + " and cena>= '" + cenaOd + "'";

                } else if (comboTipPretraga.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() == -1) {
                    String tip = comboTipPretraga.getSelectionModel().getSelectedItem();

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and tip= '" + tip + "' and cena <= " + cenaDo + " and cena>= '" + cenaOd + "'";

                } else if ((comboTipPretraga.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() == -1)) { //|| (comboTipPretraga.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex()!=-1) ){

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaDo + " and cena <=" + cenaDo + "";

                } else if ((comboTipPretraga.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex() != -1)) { //|| (comboTipPretraga.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacPretraga.getSelectionModel().getSelectedIndex()!=-1) ){

                    String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
                    String tip = comboTipPretraga.getSelectionModel().getSelectedItem();

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaDo + " and cena <=" + cenaDo + " and proizvodjac='" + proizvodjac + "' and tip ='" + tip + "'";

                }

                rs = povezi.createStatement().executeQuery(queryTipIliProizvodjac);

                while (rs.next()) {
                    podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
                }

                //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
                tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
                tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
                tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
                tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
                tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
                tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

                tableviewPretraga.setItems(null);
                tableviewPretraga.setItems(podaci);

            }
        }
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);

    }

    @FXML
    public void ocistiPoljaCene() {
        txtFieldCenaDoPretraga.clear();
        txtFieldCenaOdPretraga.clear();
        txtFieldPretraziPretraga.clear();
    }
}
