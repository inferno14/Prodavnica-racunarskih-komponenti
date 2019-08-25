
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Label labelCenaOdPretraga;
    @FXML
    private Label labelCenaDoPretraga;
    @FXML
    private TextField txtFieldCenaOdPretraga;
    @FXML
    private TextField textFieldCenaDoPretraga;
    @FXML
    private TextField txtFieldPretraziPretraga;
    
    private KonekcijaBaza konekcijaSaBazom;  
    private ObservableList<Komponenta> podaci; //lista koja omogucava onome koji osluskuje da prati izmene koje kada se dogode
     private ObservableList<String> comboTipPodaci, comboProizvodjacPodaci, comboModelPodaci;
   private ResultSet rs= null;
    @FXML
    private Button btnPretraziPretraga;
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         konekcijaSaBazom = new KonekcijaBaza(); //uspostavljanje veze sa bazom preko definisane klase za konekciju(KonekcijaBaza)
        try {
            ucitajPodatkeIzBaze(); //pocetno ucitaavanje tabele
        } catch (SQLException ex) {
            Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            popuniComboBoxove();
        } catch (SQLException ex) {
            Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // comboTipPretraga.getSelectionModel().selectedItemProperty().ad
        comboTipPretraga.getSelectionModel().selectedItemProperty().addListener((comboTipPretraga, oldValue,newValue)-> {
           
 
             try {
                  //          Platform.runLater(() -> comboTipPretraga.getSelectionModel().select(0));
//                  Platform.runLater(new Runnable(){
//                 @Override
//                 public void run() {
//                     comboProizvodjacPretraga.setPromptText("Proizvodjac");
//                   comboModelPretraga.setPromptText("Model");
//                 }
//            });
                 ucitajProizvodjace(newValue);
                 //ucitajUTabelu(newValue, comboProizvodjacPretraga.getSelectionModel().getSelectedItem(), comboModelPretraga.getSelectionModel().getSelectedItem());
               //  ucitajUTabeluTip(newValue);
             } catch (SQLException ex) {
                 Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
             }
//              comboProizvodjacPretraga.setPromptText("Proizvodjač");
//            
//             comboModelPretraga.setPromptText("Model");
             
        });
        comboProizvodjacPretraga.getSelectionModel().selectedItemProperty().addListener((comboProizvodjacPretraga, oldValue,newValue)-> {
            
            try {
                // ucitajProizvodjace(newValue);
                 //ucitajUTabelu(newValue, comboProizvodjacPretraga.getSelectionModel().getSelectedItem(), comboModelPretraga.getSelectionModel().getSelectedItem());
               //  ucitajUTabeluTip(newValue);
                 ucitajModeleProizvodjaca(newValue);
             } catch (SQLException ex) {
                 Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
             }
        });
        comboModelPretraga.getSelectionModel().selectedItemProperty().addListener((comboModelPretraga, oldValue,newValue)-> {
            
            String proizvodjac = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
            String tip = comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
             try {
                 ucitajRezultatePretrage(tip, proizvodjac,newValue);
//             try {
//                // ucitajProizvodjace(newValue);
//                 //ucitajUTabelu(newValue, comboProizvodjacPretraga.getSelectionModel().getSelectedItem(), comboModelPretraga.getSelectionModel().getSelectedItem());
//               //  ucitajUTabeluTip(newValue);
//                 ucitajModele(newValue);
//             } catch (SQLException ex) {
//                 Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
//             }
             } catch (SQLException ex) {
                 Logger.getLogger(PretragaController.class.getName()).log(Level.SEVERE, null, ex);
             }
        });
    }

    //vraca nazad na pocetni prozor aplikacije
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
      public void popuniComboBoxove() throws SQLException{
           Connection povezi = konekcijaSaBazom.poveziSe();
           String queryTip = "SELECT distinct tip from roba where Dostupnost='active'";
           String queryProizvodjac = "SELECT distinct proizvodjac from roba where Dostupnost='active'";
           String queryModel = "SELECT distinct model from roba where Dostupnost='active'";
           comboTipPodaci = FXCollections.observableArrayList();
           comboProizvodjacPodaci = FXCollections.observableArrayList();
           comboModelPodaci = FXCollections.observableArrayList();
           ResultSet rsTip=null, rsProizvodjac=null, rsModel=null;
          
          rsTip = povezi.createStatement().executeQuery(queryTip);
          rsProizvodjac = povezi.createStatement().executeQuery(queryProizvodjac);
          rsModel = povezi.createStatement().executeQuery(queryModel);
          
          while(rsTip.next()){
               comboTipPodaci.add(rsTip.getString("tip"));
          }
          while(rsProizvodjac.next()){
               comboProizvodjacPodaci.add(rsProizvodjac.getString("proizvodjac"));
          }
          while(rsModel.next()){    
                 comboModelPodaci.add(rsModel.getString("model"));
          }
          comboTipPretraga.setItems(comboTipPodaci);
          comboTipPretraga.setValue("Tip");
          comboProizvodjacPretraga.setItems(comboProizvodjacPodaci);
          comboProizvodjacPretraga.setValue("Proizvodjac");
          comboModelPretraga.setItems(comboModelPodaci);
          comboModelPretraga.setValue("Model");
        
         
          konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
      }
      
      public void selektujTip() throws SQLException{
          
           Connection povezi = konekcijaSaBazom.poveziSe();
           String selektovano = comboTipPretraga.getSelectionModel().getSelectedItem();
          
           rs = povezi.createStatement().executeQuery(selektovano);
           
           
          
          
           konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
      }
//      public void ucitajPodatkeIzBaze(String tip, String proizvodjac, String model) throws SQLException {
//       
//        podaci = FXCollections.observableArrayList(); //observable lista
//        Connection povezi = konekcijaSaBazom.poveziSe();
//        //prikazuje iz baze samo podatke koji su dostupni(active)
//         rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active'");
//
//        while (rs.next()) {
//            podaci.add(new Komponenta(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getInt(5), rs.getString(6))); //dodaje u observable listu kao tip Komponenta podatke iz result seta
//        }
//
//        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
//        tableColumnTipPretraga.setCellValueFactory(new PropertyValueFactory<>("tip"));
//        tableColumnProizvodjacPretraga.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
//        tableColumnModelPretraga.setCellValueFactory(new PropertyValueFactory<>("model"));
//        tableColumnCenaPretraga.setCellValueFactory(new PropertyValueFactory<>("cena"));
//        tableColumnKolicinaPretraga.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
//        tableColumnDostupnostPretraga.setCellValueFactory(new PropertyValueFactory<>("dostupnost"));
//
//        tableviewPretraga.setItems(null);
//        tableviewPretraga.setItems(podaci);
//       
//        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
//    }
//    
      
      public void prikaziIzabrano() throws SQLException{
          String tip = comboTipPretraga.getSelectionModel().getSelectedItem();
           String proizvodjac= comboProizvodjacPretraga.getSelectionModel().getSelectedItem();
            String model = comboModelPretraga.getSelectionModel().getSelectedItem();
                  
          if(tip!=null && proizvodjac!=null && model !=null)
          {
             // ucitajUTabelu(tip, proizvodjac, model);
              ucitajUTabeluTip(tip);
          }
                  
                 
      }
      public void ucitajUTabeluTip(String tip) throws SQLException{
         podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();
        
        //prikazuje iz baze samo podatke koji su dostupni(active)
        // rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='"+tip+"' and proizvodjac='"+proizvodjac+"' and model='"+model+"'");
            rs = povezi.createStatement().executeQuery("SELECT distinct from roba WHERE Dostupnost = 'active' and tip='"+tip+"'");

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
     public void ucitajProizvodjace(String tip) throws SQLException{
           Connection povezi = konekcijaSaBazom.poveziSe();
         
           comboProizvodjacPodaci = FXCollections.observableArrayList();
         
           
           rs = povezi.createStatement().executeQuery("SELECT distinct proizvodjac from roba WHERE Dostupnost = 'active' and tip='"+tip+"'");
           
          
           //String proizvodjac = rs.getString("proizvodjac");
          
          
          while(rs.next()){
               
                comboProizvodjacPodaci.add(rs.getString("proizvodjac"));
                 
          }
          
          comboProizvodjacPretraga.setItems(comboProizvodjacPodaci);
      //    comboProizvodjacPretraga.setValue(proizvodjac);
          
       //  ucitajModele(proizvodjac);
       
         ucitajRezultatePretrage(tip);
           konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
     }
     
     public void ucitajModeleProizvodjaca(String proizvodjac) throws SQLException{
         Connection povezi = konekcijaSaBazom.poveziSe();
         
           comboModelPodaci = FXCollections.observableArrayList();
         
           
           rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='"+proizvodjac+"'");
           
          
         //  String model = rs.getString("model");
          
          
          while(rs.next()){
               
                comboModelPodaci.add(rs.getString("model"));
                 
          }
          String model=comboModelPodaci.get(0);
          
          comboModelPretraga.setItems(comboModelPodaci);
      //    comboModelPretraga.setValue(model);
          
           ucitajRezultatePretrage(proizvodjac, model);
           konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
     }
     
//     public void ucitajModele(String modeli) throws SQLException{
//         Connection povezi = konekcijaSaBazom.poveziSe();
//         
//           comboModelPodaci = FXCollections.observableArrayList();
//           String query="SELECT model from roba WHERE Dostupnost = 'active' and model='"+modeli+"'";
//           
//           rs = povezi.createStatement().executeQuery(query);
//           
//          //System.out.println(query);
//           //String model = rs.getString("model");
//          
//          
//          while(rs.next()){
//               
//                comboModelPodaci.add(rs.getString("model"));
//                 
//          }
//          // comboModelPretraga.setItems(null);
//          comboModelPretraga.setItems(comboModelPodaci);
//       //   comboModelPretraga.setValue(modeli);
//          
//          
//           konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
//           
//     }
     
     public void ucitajRezultatePretrage(String tip) throws SQLException{
         podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();
       // String ticomboTipPretraga.getSelectionModel().getSelectedItem();
        //prikazuje iz baze samo podatke koji su dostupni(active)
        // rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='"+tip+"' and proizvodjac='"+proizvodjac+"' and model='"+model+"'");
            rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='"+tip+"'");

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
      public void ucitajRezultatePretrage(String proizvodjac, String model) throws SQLException{
         podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();
       // String ticomboTipPretraga.getSelectionModel().getSelectedItem();
        //prikazuje iz baze samo podatke koji su dostupni(active)
        // rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='"+tip+"' and proizvodjac='"+proizvodjac+"' and model='"+model+"'");
            rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and proizvodjac='"+proizvodjac+"'");

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
      
      public void ucitajRezultatePretrage(String tip, String proizvodjac, String model) throws SQLException{
            podaci = FXCollections.observableArrayList(); //observable lista
        Connection povezi = konekcijaSaBazom.poveziSe();
       // String ticomboTipPretraga.getSelectionModel().getSelectedItem();
        //prikazuje iz baze samo podatke koji su dostupni(active)
        // rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='"+tip+"' and proizvodjac='"+proizvodjac+"' and model='"+model+"'");
            rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and model='"+model+"'");

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
     public void pretragaUkucaj() throws SQLException{
         podaci = FXCollections.observableArrayList();
//         
//         if(txtFieldPretraziPretraga.getText().equals("")){
//              txtFieldPretraziPretraga.setPromptText("Unesi model");
//         }
          String ukucano = txtFieldPretraziPretraga.getText();
          
          
          
          String query = "select * from roba where model like '%"+ ukucano+"%'";
           Connection povezi = konekcijaSaBazom.poveziSe();
           System.out.println(query);
          
         //   rs = povezi.createStatement().executeQuery("SELECT * from roba WHERE Dostupnost = 'active' and tip='"+tip+"' and proizvodjac='"+proizvodjac+"' and model='"+model+"'");
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
     
}
