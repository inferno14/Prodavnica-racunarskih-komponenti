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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
               // popuniComboBoxModel();
              

            } catch (SQLException ex) {
                Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        comboProizvodjacPretraga.getSelectionModel().selectedItemProperty().addListener((comboProizvodjacPretraga, oldValue, newValue) -> {
             String tip = comboTipPretraga.getSelectionModel().getSelectedItem();
             
             if(comboTipPretraga.getSelectionModel().getSelectedIndex() == -1){
                 try {
                     rezultatiSamoProizvodjaca(newValue);
                 } catch (SQLException ex) {
                     Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }else{
            try {

                ucitajModeleProizvodjaca(tip, newValue);
            } catch (SQLException ex) {
                Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }});
        comboModelPretraga.getSelectionModel().selectedItemProperty().addListener((comboModelPretraga, oldValue, newValue) -> {

            String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
            String tip = comboTipPretraga.getSelectionModel().getSelectedItem();
            
            try {
                ucitajRezultatePretrage(tip, proizvodjac, newValue);

            } catch (SQLException ex) {
                Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        
//        txtFieldCenaOdPretraga.focusedProperty().addListener(new ChangeListener<Boolean>(){
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//               
//                if(txtFieldCenaOdPretraga.getText()==null || txtFieldCenaOdPretraga.getText().isEmpty()){
//                    return true;
//                }
//                    
//                    
//                    
//                    
//                
//                if(newValue && newVa){
//                   try {
//                       prikaziCeneOd(txtFieldCenaOdPretraga.getText());
//                   } catch (SQLException ex) {
//                       Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
//                   }
//               }
//            }
//            
//        });
//        txtFieldCenaOdPretraga.textProperty().addListener((txtFieldCenaOdPretraga, oldValue, newValue)->{
//            
//            try {
//                
//                prikaziCeneOd(newValue);
//            } catch (SQLException ex) {
//                Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        });

    
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

//    public void selektujTip() throws SQLException {
//
//        Connection povezi = konekcijaSaBazom.poveziSe();
//        String selektovano = comboTipPretraga.getSelectionModel().getSelectedItem();
//
//        rs = povezi.createStatement().executeQuery(selektovano);
//
//        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
//    }

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

    public void ucitajModeleProizvodjaca(String tip,String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "' and tip='"+tip+"'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }
     

        comboModelPretraga.setItems(comboModelPodaci);

        ucitajRezultatePretrage(tip,proizvodjac);
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

    public void ucitajRezultatePretrage(String tip, String proizvodjac) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "' and tip='"+tip+"'");

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

    public void ucitajRezultatePretrage(String tip, String proizvodjac, String model) throws SQLException {
        podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();
        
//        if((tip != null && tip.length() > 0) && (proizvodjac != null && proizvodjac.length() > 0)){
//
//        rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and model='" + model + "' and tip = '"+tip+"' and proizvodjac='"+proizvodjac+"'");
//        }
//        else{
            rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and model='" + model + "'");
//        }
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
    
    private void rezultatiSamoProizvodjaca(String proizvodjac) throws SQLException{
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
    public void ucitajModeleProizvodjaca(String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }
        //String model = comboModelPodaci.get(0);

        comboModelPretraga.setItems(comboModelPodaci);

        ucitajRezultatePretrage(proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }
    public void ucitajRezultateSamoProizvodjac(String proizvodjac) throws SQLException{
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
    public void ucitajModeleOdTipova(String tip) throws SQLException{
        
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and tip='"+tip+"'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }
        //String model = comboModelPodaci.get(0);

        comboModelPretraga.setItems(comboModelPodaci);

      //  ucitajRezultatePretrage(tip,proizvodjac);
        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }
    public void prikaziCeneOd(String cenaString) throws SQLException{
        Connection povezi = konekcijaSaBazom.poveziSe();
         comboModelPodaci = FXCollections.observableArrayList();
        int cenaInt = Integer.valueOf(cenaString);
        String query = "select * from roba where cena >= "+cenaInt+"";
        
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
    private void prikazICenu(ActionEvent event) throws SQLException {
        int cenaOd=0;//Integer.parseInt(txtFieldCenaOdPretraga.getText());
        int cenaDo=0;//Integer.parseInt(txtFieldCenaDoPretraga.getText());
        String queryObeCene="select * from roba where cena >= "+cenaOd+" and cena <= "+cenaDo+"";
        if(txtFieldCenaOdPretraga.getText().equals("") && txtFieldCenaDoPretraga.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Niste uneli nijednu cenu!");
            
        }else if(txtFieldCenaOdPretraga.getText().equals("") && !txtFieldCenaDoPretraga.getText().equals("")){
            cenaDo=Integer.parseInt(txtFieldCenaDoPretraga.getText());
            String querySamoCenaDo="select * from roba where cena < " +cenaDo+ "";
            
             podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery(querySamoCenaDo);

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
        else if(!txtFieldCenaOdPretraga.getText().equals("") && txtFieldCenaDoPretraga.getText().equals("")){
            cenaOd=Integer.parseInt(txtFieldCenaOdPretraga.getText());
            String querySamoCenaOd="select * from roba where cena > " +cenaOd+ "";
            
             podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery(querySamoCenaOd);

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
            
        }else if(!txtFieldCenaOdPretraga.getText().equals("") && !txtFieldCenaDoPretraga.getText().equals("")){
            cenaDo=Integer.parseInt(txtFieldCenaDoPretraga.getText());
            cenaOd=Integer.parseInt(txtFieldCenaOdPretraga.getText());
            String proizvodjac= comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
             String tip= comboTipPretraga.getSelectionModel().getSelectedItem();
              String model= comboModelPretraga.getSelectionModel().getSelectedItem();
           // String queryCenaObe="select * from roba where cena <= " +cenaDo+ " and cena >= "+cenaOd+"";
            String queryCenaObe="select * from roba where cena <= " +cenaDo+ " and cena >= "+cenaOd+" and proizvodjac='"+proizvodjac+"' and tip='"+tip+"' and model='"+model+"'";
            

            
             podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();

        rs = povezi.createStatement().executeQuery(queryCenaObe);

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
        
    }
    
}