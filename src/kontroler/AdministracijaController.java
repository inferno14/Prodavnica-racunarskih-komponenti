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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class AdministracijaController implements Initializable {

    @FXML
    private AnchorPane anchorRoditeljAdministracija;
    @FXML
    private AnchorPane anchorDesniAdministracija;
    @FXML
    private TableView<Komponenta> tableViewAdministracija;
    @FXML
    private TableColumn<?, ?> tableColumnFotografijaAdministracija;
    @FXML
    private TableColumn<Komponenta, String> tableColumnTipAdministracija;
    @FXML
    private TableColumn<Komponenta, String> tableColumnProizvodjacAdministracija;
    @FXML
    private TableColumn<Komponenta, String> tableColumnModelAdministracija;
    @FXML
    private TableColumn<Komponenta, Integer> tableColumnKolicinaAdministracija;
    @FXML
    private TableColumn<Komponenta, Float> tableColumnCenaAdministracija;
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
    @FXML
    private TextField txtFieldKolicinaAdministracija;
    private KonekcijaBaza konekcijaSaBazom;  
    private ObservableList<Komponenta> podaci; //lista koja omogucava onome koji osluskuje da prati izmene koje kada se dogode
    private boolean IZMENI_POSTOJECI = false, DODAJ_NOVI = true; // konstante koje ce nam pamtiti da li je kliknuto dugme novi ili dugme izmeni
    private ResultSet rs= null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            konekcijaSaBazom = new KonekcijaBaza(); //uspostavljanje veze sa bazom preko definisane klase za konekciju(KonekcijaBaza)
            ucitajPodatkeIzBaze(); //pocetno ucitaavanje tabele
        } catch (SQLException ex) {
            Logger.getLogger(AdministracijaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // postavljanje svih fieldova na disable, postaju enable samo kada se ide na dugme novi ili dugme izmeni
        txtFieldFotografijaAdministracija.setEditable(false);
        txtFieldTipAdministracija.setEditable(false);
        txtFieldProizvodjacAdministracija.setEditable(false);
        txtFieldModelAdministracija.setEditable(false);
        txtFieldCenaAdministracija.setEditable(false);
        txtFieldKolicinaAdministracija.setEditable(false);

        //kada se selektuje misem neki red u tabeli onda se ti podaci upisuju u polja sa leve strane
        tableViewAdministracija.setOnMouseClicked(e -> {
            upisiSelektovanRedUPolja();
        });
    }
    
    //vraca na pocetni prozor aplikacije
    @FXML
    public void nazad() throws IOException {
        Stage stage = (Stage) btnNazadAdministracija.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));

        Scene loginScene = new Scene(root);

        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();
       
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
        tableColumnTipAdministracija.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacAdministracija.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
        tableColumnModelAdministracija.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaAdministracija.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaAdministracija.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnDostupnostAdministracija.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));

        tableViewAdministracija.setItems(null);
        tableViewAdministracija.setItems(podaci);
       
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    @FXML
    public void novaKomponenta() {
        IZMENI_POSTOJECI = false;
        DODAJ_NOVI = true;

        txtFieldFotografijaAdministracija.setEditable(true);
        txtFieldTipAdministracija.setEditable(true);
        txtFieldProizvodjacAdministracija.setEditable(true);
        txtFieldModelAdministracija.setEditable(true);
        txtFieldCenaAdministracija.setEditable(true);
        txtFieldKolicinaAdministracija.setEditable(true);

        ocistiPolja();
    }

    @FXML
    public void obrisiKomponentu() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();
        String model = "";
        Komponenta selektovano = tableViewAdministracija.getSelectionModel().getSelectedItem();  
        model = selektovano.getModel(); 
        tableViewAdministracija.getItems().removeAll(tableViewAdministracija.getSelectionModel().getSelectedItem()); //uklanja iz tabele selektovani red
        String query = "UPDATE roba SET Dostupnost='deleted' WHERE model= '" + model + "'";
        povezi.createStatement().executeUpdate(query); //uklanja iz baze-soft delete(postavlja dostupnost na delete)

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);

    }

    //cisti vrednosti u svim poljima
    @FXML
    public void ocistiPolja() {
        txtFieldFotografijaAdministracija.clear();
        txtFieldTipAdministracija.clear();
        txtFieldProizvodjacAdministracija.clear();
        txtFieldModelAdministracija.clear();
        txtFieldCenaAdministracija.clear();
        txtFieldKolicinaAdministracija.clear();
    }

    @FXML
    public void upisiSelektovanRedUPolja() {
        Komponenta selektovano = tableViewAdministracija.getSelectionModel().getSelectedItem(); // pamti u promenljivoj vrednosti selektovanog reda
        
        //upisuje vrednosti iz selektovano promenljive u polja
        txtFieldTipAdministracija.setText(selektovano.getTip());
        txtFieldProizvodjacAdministracija.setText(selektovano.getProizvodjac());
        txtFieldModelAdministracija.setText(selektovano.getModel());
        txtFieldCenaAdministracija.setText(String.valueOf(selektovano.getCena()));
        txtFieldKolicinaAdministracija.setText(String.valueOf(selektovano.getKolicina()));

    }

    @FXML
    public void izmeni() {
        IZMENI_POSTOJECI = true;
        DODAJ_NOVI = false;
     
        //omogucava upis u polja,sem u model(jer je primarni kljuc tabele) 
        txtFieldFotografijaAdministracija.setEditable(true);
        txtFieldTipAdministracija.setEditable(true);
        txtFieldProizvodjacAdministracija.setEditable(true);
        txtFieldCenaAdministracija.setEditable(true);
        txtFieldKolicinaAdministracija.setEditable(true);
    }

    @FXML
    public void sacuvaj() throws SQLException {
        String tip, proizvodjac, model, dostupnostIzmeni, queryDodaj, queryIzmeni, queryIzbrisano, queryDaLiPostoji;
        float cena;
        int kolicina;

        txtFieldModelAdministracija.setEditable(false);
        Connection povezi = konekcijaSaBazom.poveziSe();

        
        //provera da li su sva polja popunjena
        if (txtFieldTipAdministracija.getText().equals("") || txtFieldProizvodjacAdministracija.getText().equals("") || txtFieldModelAdministracija.getText().equals("") || txtFieldCenaAdministracija.getText().equals("") || txtFieldKolicinaAdministracija.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Niste uneli sva polja!");
        } else {
            
            tip = txtFieldTipAdministracija.getText();
            proizvodjac = txtFieldProizvodjacAdministracija.getText();
            model = txtFieldModelAdministracija.getText();
            cena = Float.parseFloat(txtFieldCenaAdministracija.getText());
            kolicina = Integer.parseInt(txtFieldKolicinaAdministracija.getText());

            queryDodaj = "INSERT INTO roba VALUES('" + tip + "','" + proizvodjac + "','" + model + "'," + cena + "," + kolicina + ", 'active')";

            queryDaLiPostoji = "SELECT Dostupnost FROM roba where model='" + model + "'";
            queryIzbrisano = "UPDATE roba SET tip='" + tip + "', proizvodjac='" + proizvodjac + "', cena=" + cena + ",  kolicina=" + kolicina + ", Dostupnost='active' where model='" + model + "'";
            ResultSet rs1 = povezi.createStatement().executeQuery(queryDaLiPostoji);
            ResultSet rs2 = povezi.createStatement().executeQuery(queryDaLiPostoji);

            //ako je dugme izmeni kliknuto
            if (IZMENI_POSTOJECI) {
                Komponenta selektovano = tableViewAdministracija.getSelectionModel().getSelectedItem();
                dostupnostIzmeni = selektovano.getDostupnost(); // nalazimo dostupnost za model koji se menja i to upisujemo putem query-ja
                queryIzmeni = "UPDATE roba SET tip='" + tip + "', proizvodjac='" + proizvodjac + "', model='" + model + "', cena=" + cena + ",  kolicina=" + kolicina + ", Dostupnost='" + dostupnostIzmeni + "' where model='" + model + "'";
                povezi.createStatement().executeUpdate(queryIzmeni);
                JOptionPane.showMessageDialog(null, "Uspešno izmenjeni podaci");

            //ako je dugme novi kliknuto    
            } else if (DODAJ_NOVI) {

                if (rs1.next() && !(rs1.getString(1).equals("deleted"))) {   //proverava li postoji komponenta sa tim modelom i da joj je dostupnost active

                    JOptionPane.showMessageDialog(null, "Ne možete dodati komponentu jer već POSTOJI");

                } else if (rs2.next() && rs2.getString(1).equals("deleted")) {  //ako postoji i dostupnost joj je deleted, ne upisuje novu nego izmeni tu koja je izbrisana i vrati jos status na active

                    povezi.createStatement().executeUpdate(queryIzbrisano);
                    JOptionPane.showMessageDialog(null, "Uspešno dodata komponenta");

                } else { // ako model uspisani ne postoji ni kao active ni kao deleted, onda dodaj u bazu
                    povezi.createStatement().executeUpdate(queryDodaj);
                    JOptionPane.showMessageDialog(null, "Uspešno dodata komponenta");

                }
            }

            ucitajPodatkeIzBaze();  // osvezi tabelu nakon promena u baz
            ocistiPolja();
            txtFieldFotografijaAdministracija.setEditable(false);
            txtFieldTipAdministracija.setEditable(false);
            txtFieldProizvodjacAdministracija.setEditable(false);
            txtFieldModelAdministracija.setEditable(false);
            txtFieldCenaAdministracija.setEditable(false);
            txtFieldKolicinaAdministracija.setEditable(false);
        }
      konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }
    
    
}
