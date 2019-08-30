package kontroler;

import aplikacija.Komponenta;
import aplikacija.KonekcijaBaza;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private TableView<Komponenta> tableviewPrijem;
    @FXML
    private TableColumn<Komponenta, String> tableColumnTipPrijem;
    @FXML
    private TableColumn<Komponenta, String> tableColumnProizvodjacPrijem;
    @FXML
    private TableColumn<Komponenta, String> tableColumnModelPrijem;
    @FXML
    private TableColumn<Komponenta, Integer> tableColumnKolicinaPrijem;
    @FXML
    private Button btnNazadPrijem;
    @FXML
    private AnchorPane anchorLeviPrijem;
    @FXML
    private ComboBox<String> comboTipPrijem;
    @FXML
    private ComboBox<String> comboProizvodjacPrijem;
    @FXML
    private ComboBox<String> comboModelPrijem;
    @FXML
    private Label labelTipPrijem;
    @FXML
    private Label labelProizvodjacPrijem;
    @FXML
    private Label labelModelPrijem;
    private KonekcijaBaza konekcijaSaBazom;
    private ObservableList<Komponenta> podaci; //lista koja omogucava onome koji osluskuje da prati izmene koje kada se dogode
    private ObservableList<String> comboTipPodaci, comboProizvodjacPodaci, comboModelPodaci;
    private ResultSet rs = null;
    @FXML
    private TextField txtFieldKolicinaPrijem;
    @FXML
    private Label labelKolicinaPrijem;
    @FXML
    private Button dodajNaStanje;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        konekcijaSaBazom = new KonekcijaBaza(); //uspostavljanje veze sa bazom preko definisane klase za konekciju(KonekcijaBaza)

        //popunjava pokretanjem prozora Prijem sve combo boxove
        try {
            popuniComboBoxTip();
            popuniComboBoxProizvodjac();
            popuniComboBoxModel();
        } catch (SQLException ex) {
            Logger.getLogger(PrijemController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //osluskuje promene vrednosti combo boxa Tip i na osnovu te vrednosti popunjava combo boxove Proizvodjac i Model
        comboTipPrijem.getSelectionModel().selectedItemProperty().addListener((comboTipPrijem, oldValue, newValue) -> {

            try {

                ucitajProizvodjace(newValue);
                ucitajModeleOdTipova(newValue);

            } catch (SQLException ex) {
                Logger.getLogger(PrijemController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        //osluskuje promene vrednosti combo boxa Proizvodjac i na osnovu te vrednosti popunjava combo box Model
        comboProizvodjacPrijem.getSelectionModel().selectedItemProperty().addListener((comboProizvodjacPrijem, oldValue, newValue) -> {
            String tip = comboTipPrijem.getSelectionModel().getSelectedItem();

            try {
                ucitajModeleProizvodjaca(tip, newValue);
            } catch (SQLException ex) {
                Logger.getLogger(PrijemController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    //vraca nazad na pocetni prozor aplikacije
    @FXML
    public void nazad() throws IOException {
        Stage stage = (Stage) btnNazadPrijem.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));

        Scene loginScene = new Scene(root);

        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;

    }

    public void popuniComboBoxTip() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();
        String queryTip = "SELECT distinct tip from roba where Dostupnost='active'";

        comboTipPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery(queryTip);

        while (rs.next()) {
            comboTipPodaci.add(rs.getString("tip"));
        }

        comboTipPrijem.setItems(comboTipPodaci);     //popunjava svim tipovima combo box
        comboTipPrijem.setPromptText("Tip");             //postavlja pocetni combo box-a

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void popuniComboBoxProizvodjac() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        String queryProizvodjac = "SELECT distinct proizvodjac from roba where Dostupnost='active'";

        comboProizvodjacPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery(queryProizvodjac);

        while (rs.next()) {
            comboProizvodjacPodaci.add(rs.getString("proizvodjac"));
        }

        comboProizvodjacPrijem.setItems(comboProizvodjacPodaci);     //popunjava svi proizvodjacima combo box
        comboProizvodjacPrijem.setPromptText("Proizvodjac");        //postavlja pocetni combo box-a

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    public void popuniComboBoxModel() throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        String queryModel = "SELECT distinct model from roba where Dostupnost='active'";

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery(queryModel);

        while (rs.next()) {
            comboModelPodaci.add(rs.getString("model"));
        }

        comboModelPrijem.setItems(comboModelPodaci);  //popunjava svi modelima combo box
        comboModelPrijem.setPromptText("Model");    //postavlja pocetni combo box-a

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    //ucitava proizvodjace na osnovu izabranog tipa
    public void ucitajProizvodjace(String tip) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboProizvodjacPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT distinct proizvodjac from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {

            comboProizvodjacPodaci.add(rs.getString("proizvodjac"));

        }

        //popunjava combo sadrzajem liste
        comboProizvodjacPrijem.setItems(comboProizvodjacPodaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    //popunjava u combo box Modeli vrednosti, na osnovu izabranog Tipa i Proizvodjaca
    public void ucitajModeleProizvodjaca(String tip, String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "' and tip='" + tip + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        //popunjava combo sadrzajem liste
        comboModelPrijem.setItems(comboModelPodaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    //popunjava u combo box Modeli vrednosti, samo na osnovu izabranog Proizvodjaca, bez Tipa izabranog
    public void ucitajModeleProizvodjaca(String proizvodjac) throws SQLException {
        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and proizvodjac='" + proizvodjac + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        //popunjava combo sadrzajem liste
        comboModelPrijem.setItems(comboModelPodaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    //popunjava u combo box Modeli vrednosti, samo na osnovu izabranog Tipa, bez Proizvodjaca izabranog
    public void ucitajModeleOdTipova(String tip) throws SQLException {

        Connection povezi = konekcijaSaBazom.poveziSe();

        comboModelPodaci = FXCollections.observableArrayList();

        rs = povezi.createStatement().executeQuery("SELECT model from roba WHERE Dostupnost = 'active' and tip='" + tip + "'");

        while (rs.next()) {

            comboModelPodaci.add(rs.getString("model"));

        }

        //popunjava combo sadrzajem liste
        comboModelPrijem.setItems(comboModelPodaci);

        konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
    }

    //dodaje u tabelu i menja u bazi izabrane vrednosti
    @FXML
    private void dodaj() throws SQLException {

        //ako nije unet bilo koji od podataka
        if (comboTipPrijem.getSelectionModel().getSelectedIndex() == -1 || comboProizvodjacPrijem.getSelectionModel().getSelectedIndex() == -1 || comboModelPrijem.getSelectionModel().getSelectedIndex() == -1 || txtFieldKolicinaPrijem.getText().equals("")) {

            JOptionPane.showMessageDialog(null, "Niste uneli sve podatke!");

            //ako su uneti svi podaci
        } else {
            podaci = FXCollections.observableArrayList(); //observable lista
            String tip = comboTipPrijem.getSelectionModel().getSelectedItem();
            String proizvodjac = comboProizvodjacPrijem.getSelectionModel().getSelectedItem();
            String model = comboModelPrijem.getSelectionModel().getSelectedItem();
            int kolicina = Integer.parseInt(txtFieldKolicinaPrijem.getText());

            Connection povezi = konekcijaSaBazom.poveziSe();

            //menja podatke u bazi na osnovu izabranih vrednosti
            povezi.createStatement().execute("UPDATE roba SET kolicina=kolicina+" + kolicina + " WHERE Dostupnost = 'active' and model='" + model + "' and tip = '" + tip + "' and proizvodjac='" + proizvodjac + "'");

            //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
            tableColumnTipPrijem.setCellValueFactory(new PropertyValueFactory<>("tip"));
            tableColumnProizvodjacPrijem.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));
            tableColumnModelPrijem.setCellValueFactory(new PropertyValueFactory<>("model"));
            tableColumnKolicinaPrijem.setCellValueFactory(new PropertyValueFactory<>("kolicina"));

            //dodaje u tabelu novi red sa novim objektom
            tableviewPrijem.getItems().add(new Komponenta(tip, proizvodjac, model, kolicina));

            konekcijaSaBazom.zatvoriKonekciju(povezi, rs);
        }
        
    }
         

    @FXML
    private void exportPdf(ActionEvent event) throws FileNotFoundException, DocumentException {
            if (tableviewPrijem.getItems().size() == 0) {
                
              JOptionPane.showMessageDialog(null, "Tabela je prazna!");
        } else {
            Komponenta selektovano = new Komponenta();
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("c:/prijem_robe.pdf"));
            document.open();
            Paragraph paragraf = new Paragraph();

            paragraf.add("PRIMLJENA ROBA");
            paragraf.setAlignment(Element.ALIGN_CENTER);
            paragraf.setSpacingAfter(30);
            document.add(paragraf);

            PdfPTable table = new PdfPTable(4);

            table.setWidthPercentage(100);

            PdfPCell kol_1 = new PdfPCell(new Paragraph("TIP"));
            table.addCell(kol_1);
            PdfPCell kol_2 = new PdfPCell(new Paragraph("PROIZVODJAC"));

            table.addCell(kol_2);
            PdfPCell kol_3 = new PdfPCell(new Paragraph("MODEL"));
            table.addCell(kol_3);
            PdfPCell kol_4 = new PdfPCell(new Paragraph("KOLICINA"));
            table.addCell(kol_4);
            

            for (int i = 0; i < tableviewPrijem.getItems().size(); i++) {
                selektovano = tableviewPrijem.getItems().get(i);

                String tip = selektovano.getTip();
                String proizvodjac = selektovano.getProizvodjac();
                String model = selektovano.getModel();
                int kolicina = selektovano.getKolicina();
               

                PdfPCell kol_5 = new PdfPCell(new Paragraph(tip));
                table.addCell(kol_5);
                PdfPCell kol_6 = new PdfPCell(new Paragraph(proizvodjac));
                table.addCell(kol_6);
                PdfPCell kol_7 = new PdfPCell(new Paragraph(model));
                table.addCell(kol_7);
                PdfPCell kol_8 = new PdfPCell(new Paragraph(String.valueOf(kolicina)));
                table.addCell(kol_8);
              

            }
            document.add(table);
            document.close();

    }
     

}
}