package kontroler;

import aplikacija.Komponenta;
import aplikacija.KonekcijaBaza;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

public class ProdajaController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljProdaja;
    @FXML
    private AnchorPane anchorSrednjiProdaja;
    @FXML
    private TableView<Komponenta> tableviewProdaja;
    @FXML
    private TableColumn<?, ?> tableColumnFotografijaProdaja;
    @FXML
    private TableColumn<Komponenta, String> tableColumnTipProdaja;
    @FXML
    private TableColumn<Komponenta, String> tableColumnProizvodjacProdaja;
    @FXML
    private TableColumn<Komponenta, String> tableColumnModelProdaja;
    @FXML
    private TableColumn<Komponenta, Float> tableColumnKolicinaProdaja;
    @FXML
    private TableColumn<Komponenta, Integer> tableColumnCenaProdaja;
    @FXML
    private TableColumn<Komponenta, String> tableColumnDostupnostProdaja;
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
    private TableView<Komponenta> tableviewKonfigurator;
    @FXML
    private TableColumn<Komponenta, String> tableColumnKonfiguratorNaziv;
    @FXML
    private TableColumn<Komponenta, Integer> tableColumnKonfiguratorKolicina;
    @FXML
    private TableColumn<Komponenta, Float> tableColumnKonfiguratorCena;
    @FXML
    private Button btnProdaj;
    @FXML
    private Button btnExportujProdaja;
    @FXML
    private AnchorPane anchorLeviProdaja;
    @FXML
    private ComboBox<String> comboTipProdaja;
    @FXML
    private ComboBox<String> comboProizvodjacProdaja;
    @FXML
    private ComboBox<String> comboModelProdaja;
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
    private KonekcijaBaza konekcijaSaBazom;
    private ObservableList<Komponenta> podaci; //lista koja omogucava onome koji osluskuje da prati izmene koje kada se dogode
    private ObservableList<String> comboTipPodaci, comboProizvodjacPodaci, comboModelPodaci;
    private ResultSet rs = null;
    @FXML
    private Button btnPretraziProdaja;
    @FXML
    private Button obrisiKonfigurator;
    static float ukupnaCena = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        konekcijaSaBazom = new KonekcijaBaza(); //uspostavljanje veze sa bazom preko definisane klase za konekciju(KonekcijaBaza)
        try {
            ucitajPodatkeIzBaze(); //pocetno ucitaavanje tabele
        } catch (SQLException ex) {
            Logger.getLogger(ProdajaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //popunjava combo boxove pri pokretanju prozora
        try {
            popuniComboBoxTip();
            popuniComboBoxProizvodjac();
            popuniComboBoxModel();
        } catch (SQLException ex) {
            Logger.getLogger(ProdajaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //pri svakoj promeni vrednosti combo boxa Tip
        comboTipProdaja.getSelectionModel().selectedItemProperty().addListener((comboTipProdaja, oldValue, newValue) -> {

            ocistiPoljaCeneIPretrage(); //cisti oba polja cene
            try {

                //popunjava combo boxove Proizvodjac i Modeli na osnovu izabrane vrednosti combo boxa Tip
                ucitajProizvodjace(newValue);
                ucitajModeleOdTipova(newValue);

            } catch (SQLException ex) {
                Logger.getLogger(ProdajaController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        //pri svakoj promeni vrednosti combo boxa Proizvodjac
        comboProizvodjacProdaja.getSelectionModel().selectedItemProperty().addListener((comboProizvodjacProdaja, oldValue, newValue) -> {

            String tip = comboTipProdaja.getSelectionModel().getSelectedItem(); //vrednnost selektovanog u combo boxu Tip

            ocistiPoljaCeneIPretrage();

            //ako nista nije izabrano u combo boxu Tip
            if (comboTipProdaja.getSelectionModel().getSelectedIndex() == -1) {
                try {
                    //prikazuje u tabeli rezultate samo izabranog proizvodjaca
                    rezultatiSamoProizvodjaca(newValue);
                } catch (SQLException ex) {
                    Logger.getLogger(ProdajaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    //popunjava modele na osnovu tipa i proizvodjaca
                    ucitajModeleProizvodjaca(tip, newValue);
                } catch (SQLException ex) {
                    Logger.getLogger(ProdajaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        // //pri svakoj promeni vrednosti combo boxa Proizvodjac
        comboModelProdaja.getSelectionModel().selectedItemProperty().addListener((comboModelProdaja, oldValue, newValue) -> {

            String proizvodjac = comboProizvodjacProdaja.getSelectionModel().getSelectedItem(); //vrednost izabranog proizvodjaca
            String tip = comboTipProdaja.getSelectionModel().getSelectedItem(); //vrednost izabranog tipa

            ocistiPoljaCeneIPretrage();

            try {
                //ucitava u tabelu rezultate konkretnog modela
                ucitajRezultatePretrage(tip, proizvodjac, newValue);

            } catch (SQLException ex) {
                Logger.getLogger(ProdajaController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    //vraca nazad na pocetni prozor
    @FXML
    public void nazad() throws IOException {
        Stage stage = (Stage) btnNazadProdaja.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));

        Scene loginScene = new Scene(root);

        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();

    }

    public void ucitajPodatkeIzBaze() throws SQLException {

        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe(); // konekcija sa bazom
        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active'");  //prikazuje iz baze samo podatke koji su dostupni(active)

        //sve dok ima nesto u result setu popunjavaj listu podaci novim objektom Komponente
        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewProdaja.setItems(null);
        tableviewProdaja.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs); //zatvara konekekciju
    }

    public void popuniComboBoxTip() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();
        String queryTip = "SELECT distinct tip from roba where Dostupnost='active'";

        comboTipPodaci = FXCollections.observableArrayList();

        ResultSet rsTip = null;

        rsTip = povezi.createStatement().executeQuery(queryTip);

        while (rsTip.next()) {
            comboTipPodaci.add(rsTip.getString("tip"));
        }

        comboTipProdaja.setItems(comboTipPodaci);
        comboTipProdaja.setValue("Tip");

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void popuniComboBoxProizvodjac() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        String queryProizvodjac = "SELECT distinct proizvodjac from roba where Dostupnost='active'";

        comboProizvodjacPodaci = FXCollections.observableArrayList();
        ResultSet rsProizvodjac = null;

        rsProizvodjac = povezi.createStatement().executeQuery(queryProizvodjac);

        while (rsProizvodjac.next()) {
            comboProizvodjacPodaci.add(rsProizvodjac.getString("proizvodjac"));
        }

        comboProizvodjacProdaja.setItems(comboProizvodjacPodaci);
        comboProizvodjacProdaja.setValue("Proizvodjac");

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void popuniComboBoxModel() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        String queryModel = "SELECT distinct model from roba where Dostupnost='active'";

        comboModelPodaci = FXCollections.observableArrayList();
        ResultSet rsModel = null;

        rsModel = povezi.createStatement().executeQuery(queryModel);

        while (rsModel.next()) {
            comboModelPodaci.add(rsModel.getString("model"));
        }

        comboModelProdaja.setItems(comboModelPodaci);
        comboModelProdaja.setValue("Model");

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajProizvodjace(String tip) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboProizvodjacPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT distinct proizvodjac from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {

            comboProizvodjacPodaci.add(rs.getString("proizvodjac"));

        }

        comboProizvodjacProdaja.setItems(comboProizvodjacPodaci);

        ucitajRezultatePretrage(tip);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajModeleProizvodjaca(String tip, String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "' and tip='" + tip + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        comboModelProdaja.setItems(comboModelPodaci);

        ucitajRezultatePretrage(tip, proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajRezultatePretrage(String tip) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewProdaja.setItems(null);
        tableviewProdaja.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajRezultatePretrage(String tip, String proizvodjac) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "' and tip='" + tip + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewProdaja.setItems(null);
        tableviewProdaja.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajRezultatePretrage(String tip, String proizvodjac, String model) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and model='" + model + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewProdaja.setItems(null);
        tableviewProdaja.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void pretragaUkucaj() throws SQLException {
        podaci = FXCollections.observableArrayList();

        String ukucano = txtFieldPretraziProdaja.getText();

        //izvlaci podatke modela koji u nazivu sadrze ukucano slovo ili rec
        String query = "select * from roba where model like '%" + ukucano + "%'";
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery(query);

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewProdaja.setItems(null);
        tableviewProdaja.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    private void osveziTabelu(ActionEvent event) throws SQLException {
        ucitajPodatkeIzBaze();
    }

    private void rezultatiSamoProizvodjaca(String proizvodjac) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewProdaja.setItems(null);
        tableviewProdaja.setItems(podaci);

        ucitajModeleProizvodjaca(proizvodjac);
        ucitajRezultateSamoProizvodjac(proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajModeleProizvodjaca(String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        comboModelProdaja.setItems(comboModelPodaci);

        ucitajRezultatePretrage(proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajRezultateSamoProizvodjac(String proizvodjac) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableviewProdaja.setItems(null);
        tableviewProdaja.setItems(podaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void ucitajModeleOdTipova(String tip) throws SQLException {

        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        comboModelProdaja.setItems(comboModelPodaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    private void prikazICenu(ActionEvent event) throws SQLException {

        Connection povezi = konekcijaSaBazom.poveziSe();
        podaci = FXCollections.observableArrayList();
        int cenaOd = 0;
        int cenaDo = 0;

        String queryTipIliProizvodjac = "";

        //ako su oba polja cene prazna
        if (txtFieldCenaOdProdaja.getText().equals("") && txtFieldCenaDoProdaja.getText().equals("")) {

            //ispisi odgovarajucu poruku
            JOptionPane.showMessageDialog(null, "Niste uneli nijednu cenu!");

            //ako bilo koje od dva polja cene nije prazno
        } else {

            txtFieldPretraziProdaja.clear();

            //ako je popunjeno samo polje Maksimalna cena
            if (txtFieldCenaOdProdaja.getText().equals("") && !txtFieldCenaDoProdaja.getText().equals("")) {
                cenaDo = Integer.parseInt(txtFieldCenaDoProdaja.getText());

                //ako je izabran proizvodjac i upisana maksimalna cena
                if (comboTipProdaja.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() != -1) {
                    String proizvodjac = comboProizvodjacProdaja.getSelectionModel().getSelectedItem();
                    cenaDo = Integer.parseInt(txtFieldCenaDoProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and proizvodjac= '" + proizvodjac + "' and cena <= " + cenaDo + "";
                    System.out.println(queryTipIliProizvodjac);

                    //ako je izabran tip i upisana maksimalna cena    
                } else if (comboTipProdaja.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() == -1) {
                    String tip = comboTipProdaja.getSelectionModel().getSelectedItem();
                    cenaDo = Integer.parseInt(txtFieldCenaDoProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and tip= '" + tip + "' and cena <= " + cenaDo + "";
                    System.out.println(queryTipIliProizvodjac);

                    //ako nije izabran ni tip ni proizvodjac   
                } else if ((comboTipProdaja.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() == -1)) { //|| (comboTipProdaja.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex()!=-1) ){

                    cenaDo = Integer.parseInt(txtFieldCenaDoProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena <= " + cenaDo + "";

                    //ako su izabrani i tip i proizvojac   
                } else if ((comboTipProdaja.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() != -1)) { //|| (comboTipProdaja.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex()!=-1) ){

                    String proizvodjac = comboProizvodjacProdaja.getSelectionModel().getSelectedItem();
                    String tip = comboTipProdaja.getSelectionModel().getSelectedItem();
                    cenaDo = Integer.parseInt(txtFieldCenaDoProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena <= " + cenaDo + " and proizvodjac='" + proizvodjac + "' and tip ='" + tip + "'";

                }
                rs = povezi.createStatement().executeQuery(queryTipIliProizvodjac);

                while (rs.next()) {
                    podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
                }

                //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
                tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
                tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
                tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
                tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
                tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
                tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

                tableviewProdaja.setItems(null);
                tableviewProdaja.setItems(podaci);

                //ako je upisana samo minimalna cena
            } else if (!txtFieldCenaOdProdaja.getText().equals("") && txtFieldCenaDoProdaja.getText().equals("")) {
                cenaOd = Integer.parseInt(txtFieldCenaOdProdaja.getText());

                //ako je izabran proizvodjac i upisana minimalana cena
                if (comboTipProdaja.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() != -1) {
                    String proizvodjac = comboProizvodjacProdaja.getSelectionModel().getSelectedItem();
                    cenaOd = Integer.parseInt(txtFieldCenaOdProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and proizvodjac= '" + proizvodjac + "' and cena >= " + cenaOd + "";

                    //ako je izabran tip i upisana minimalana cena
                } else if (comboTipProdaja.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() == -1) {
                    String tip = comboTipProdaja.getSelectionModel().getSelectedItem();
                    cenaOd = Integer.parseInt(txtFieldCenaOdProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and tip= '" + tip + "' and cena >= " + cenaOd + "";

                    //ako nije izabran ni tip ni proizvodjac
                } else if ((comboTipProdaja.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() == -1)) { //|| (comboTipProdaja.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex()!=-1) ){

                    cenaOd = Integer.parseInt(txtFieldCenaOdProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaOd + "";

                    //ako je izabran i tip i proizvodjac
                } else if ((comboTipProdaja.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() != -1)) { //|| (comboTipProdaja.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex()!=-1) ){

                    String proizvodjac = comboProizvodjacProdaja.getSelectionModel().getSelectedItem();
                    String tip = comboTipProdaja.getSelectionModel().getSelectedItem();
                    cenaOd = Integer.parseInt(txtFieldCenaOdProdaja.getText());
                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaOd + " and proizvodjac='" + proizvodjac + "' and tip ='" + tip + "'";

                }

                rs = povezi.createStatement().executeQuery(queryTipIliProizvodjac);
                while (rs.next()) {
                    podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
                }

                //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
                tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
                tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
                tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
                tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
                tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
                tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

                tableviewProdaja.setItems(null);
                tableviewProdaja.setItems(podaci);

                //ako su upisane obe cene
            } else if (!txtFieldCenaOdProdaja.getText().equals("") && !txtFieldCenaDoProdaja.getText().equals("")) {
                cenaDo = Integer.parseInt(txtFieldCenaDoProdaja.getText());
                cenaOd = Integer.parseInt(txtFieldCenaOdProdaja.getText());

                //ako je izabran samo proizvodjac
                if (comboTipProdaja.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() != -1) {
                    String proizvodjac = comboProizvodjacProdaja.getSelectionModel().getSelectedItem();

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and proizvodjac= '" + proizvodjac + "' and cena <= " + cenaDo + " and cena>= '" + cenaOd + "'";

                    //ako je izabran samo tip
                } else if (comboTipProdaja.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() == -1) {
                    String tip = comboTipProdaja.getSelectionModel().getSelectedItem();

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and tip= '" + tip + "' and cena <= " + cenaDo + " and cena>= '" + cenaOd + "'";

                    //ako nisu izabrani ni tip ni proizvodjac   
                } else if ((comboTipProdaja.getSelectionModel().getSelectedIndex() == -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() == -1)) { //|| (comboTipProdaja.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex()!=-1) ){

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaDo + " and cena <=" + cenaDo + "";

                    //ako su izabrani i tip i proizvodjac
                } else if ((comboTipProdaja.getSelectionModel().getSelectedIndex() != -1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex() != -1)) { //|| (comboTipProdaja.getSelectionModel().getSelectedIndex()==-1 && comboProizvodjacProdaja.getSelectionModel().getSelectedIndex()!=-1) ){

                    String proizvodjac = comboProizvodjacProdaja.getSelectionModel().getSelectedItem();
                    String tip = comboTipProdaja.getSelectionModel().getSelectedItem();

                    queryTipIliProizvodjac = "select * from roba where Dostupnost = 'active' and cena >= " + cenaDo + " and cena <=" + cenaDo + " and proizvodjac='" + proizvodjac + "' and tip ='" + tip + "'";

                }

                rs = povezi.createStatement().executeQuery(queryTipIliProizvodjac);

                while (rs.next()) {
                    podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
                }

                //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
                tableColumnTipProdaja.setCellValueFactory(new PropertyValueFactory<>("tip"));
                tableColumnProizvodjacProdaja.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
                tableColumnModelProdaja.setCellValueFactory(new PropertyValueFactory<>("model"));
                tableColumnCenaProdaja.setCellValueFactory(new PropertyValueFactory<>("cena"));
                tableColumnKolicinaProdaja.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
                tableColumnDostupnostProdaja.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

                tableviewProdaja.setItems(null);
                tableviewProdaja.setItems(podaci);

            }
        }
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);

    }

    public void ocistiPoljaCeneIPretrage() {
        txtFieldCenaDoProdaja.clear();
        txtFieldCenaOdProdaja.clear();
        txtFieldPretraziProdaja.clear();
    }

    @FXML
    public void obrisiKomponentu() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        Komponenta selektovano = tableviewKonfigurator.getSelectionModel().getSelectedItem();
        tableviewKonfigurator.getItems().removeAll(tableviewKonfigurator.getSelectionModel().getSelectedItem()); //uklanja iz tabele selektovani red
        txtFieldUkupnaCenaProdaja.clear();

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);

    }

    @FXML
    public void dodajUKonfigurator() {

        //ako je prazno polje za kolicinu
        if (txtFieldKolicinaProdaja.getText().equals("")) {

            JOptionPane.showMessageDialog(null, "Morate izabrati komponentu i upisati količinu");

            //ako su uneti svi podaci
        } else {
            podaci = FXCollections.observableArrayList(); //observable lista
            Komponenta selektovano = tableviewProdaja.getSelectionModel().getSelectedItem(); // pamti u promenljivoj vrednosti selektovanog reda
            String model = selektovano.getModel();
            int kolicinaNaStanju = selektovano.getKolicina();
            int kolicinaUneta = Integer.parseInt(txtFieldKolicinaProdaja.getText());
            float cena = selektovano.getCena();
            String poruka = "Komponentu koju ste izabrali nema u tolikoj količini. Na stanju ima " + kolicinaNaStanju + "";
            if (kolicinaUneta <= kolicinaNaStanju) {

                ukupnaCena = ukupnaCena + (kolicinaUneta * cena);
                txtFieldUkupnaCenaProdaja.setText(String.valueOf(ukupnaCena));

                tableColumnKonfiguratorNaziv.setCellValueFactory(new PropertyValueFactory<>("model"));
                tableColumnKonfiguratorKolicina.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
                tableColumnKonfiguratorCena.setCellValueFactory(new PropertyValueFactory<>("cena"));

                tableviewKonfigurator.getItems().add(new Komponenta(model, kolicinaUneta, cena));
            } else {
                JOptionPane.showMessageDialog(null, poruka);
            }
            txtFieldKolicinaProdaja.clear();
        }

    }

    @FXML
    public void prodaj() throws SQLException {

        Komponenta komponenta = new Komponenta();
        Connection povezi = konekcijaSaBazom.poveziSe();
        String model;
        int kolicina;
        float ukupnaCena;
        String queryTipIProizvodjac = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //formater datuma
        Date datumDanasnji = new Date(System.currentTimeMillis()); //uzima trenutni datum
        formatter.format(datumDanasnji);  //formatira ga na denisan nacin
        String tip = null, proizvodjac = null;
        String queryUmanjiUBazi = null;

        //ako je tabela konfigurator prazna
        if (tableviewKonfigurator.getItems().size() == 0) {
            JOptionPane.showMessageDialog(null, "Konfigurator je prazan!");
        } else {

            for (int i = 0; i < tableviewKonfigurator.getItems().size(); i++) {

                komponenta = tableviewKonfigurator.getItems().get(i);
                model = komponenta.getModel();
                kolicina = komponenta.getKolicina();
                ukupnaCena = Float.parseFloat(txtFieldUkupnaCenaProdaja.getText());

                queryTipIProizvodjac = "select * from roba where Dostupnost='active' and model='" + model + "'";
                queryUmanjiUBazi = "UPDATE roba SET kolicina=kolicina-" + kolicina + " where model = '" + model + "'";
                rs = povezi.createStatement().executeQuery(queryTipIProizvodjac);

                if (rs.next()) {
                    tip = rs.getString(1);
                    proizvodjac = rs.getString(2);
                }

                String queryDodajUProdaju = "insert into prodaja (model, cena, datum_prodaje,kolicina, tip, proizvodjac) values(?, ?, ?, ?, ?, ?)";

                PreparedStatement ps = povezi.prepareStatement(queryDodajUProdaju);

                ps.setString(1, model);
                ps.setFloat(2, ukupnaCena);
                ps.setDate(3, datumDanasnji);
                ps.setInt(4, kolicina);
                ps.setString(5, tip);
                ps.setString(6, proizvodjac);
                ps.execute();

                povezi.createStatement().executeUpdate(queryUmanjiUBazi);

            }
            ucitajPodatkeIzBaze();
            JOptionPane.showMessageDialog(null, "Uspesno izvrsena prodaja!");
            tableviewKonfigurator.getItems().clear();
            txtFieldUkupnaCenaProdaja.clear();
        }
    }
}
