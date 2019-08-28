package kontroler;

import aplikacija.Komponenta;
import aplikacija.KonekcijaBaza;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
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
    private TableView<Komponenta> tableviewIzvestaji;
    @FXML
    private TableColumn<Komponenta, String> tableColumnTipIzvestaji;
    @FXML
    private TableColumn<Komponenta, String> tableColumnProizvodjacIzvestaji;
    @FXML
    private TableColumn<Komponenta, String> tableColumnModelIzvestaji;
    @FXML
    private TableColumn<Komponenta, Integer> tableColumnKolicinaIzvestaji;
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
    private DatePicker datumOd;
    @FXML
    private DatePicker datumDo;
    @FXML
    private Label labelDatumOd;
    @FXML
    private Label labelDatumDo;
    private KonekcijaBaza konekcijaSaBazom;
    private ObservableList<Komponenta> podaci; //lista koja omogucava onome koji osluskuje da prati izmene koje kada se dogode
    private ResultSet rs = null;
    @FXML
    private TextField txtUkupniPazar;
    @FXML
    private Label labelUkupniPazar;
    @FXML
    private Label labelTekstDatuma;
    @FXML
    private Button btnDnevni;
    @FXML
    private TableColumn<Komponenta, Float> tableColumnCenaIzvestaji;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelTekstDatuma.setVisible(false);

        //osluskuje promene u datepickeru datumOd
        datumOd.valueProperty().addListener((datumOd, oldValue, newValue) -> {
            try {
                //ako je izabrano samo datumOd
                if ((this.datumOd.valueProperty().getValue() != null) && (this.datumDo.valueProperty().getValue() == null)) {
                    prikaziDatumOd();

                    //ako je izabrano samo datumDo
                } else if ((this.datumOd.valueProperty().getValue() == null) && (this.datumDo.valueProperty().getValue() != null)) {
                    prikaziDatumDo();

                    //ako su izabrani oba datuma   
                } else if ((this.datumOd.valueProperty().getValue() == null) && (this.datumDo.valueProperty().getValue() != null)) {
                    prikaziDatumOdDo();

                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(IzvestajiController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(IzvestajiController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //osluskuje promene u datepickeru datumDo
        datumDo.valueProperty().addListener((datumDo, oldValue, newValue) -> {
            try {
                if ((this.datumOd.valueProperty().getValue() != null) && (this.datumDo.valueProperty().getValue() == null)) {
                    //ako je izabrano samo datumDo
                    prikaziDatumOd();

                    //ako je izabrano samo datumDo
                } else if ((this.datumOd.valueProperty().getValue() == null) && (this.datumDo.valueProperty().getValue() != null)) {

                    prikaziDatumDo();

                    //ako su izabrani oba datuma   
                } else if ((this.datumOd.valueProperty().getValue() != null) && (this.datumDo.valueProperty().getValue() != null)) {
                    prikaziDatumOdDo();

                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(IzvestajiController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(IzvestajiController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    //vraca nazad na pocetni prozor
    @FXML
    public void nazad() throws IOException {
        Stage stage = (Stage) btnNazadIzvestaji.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/pogled/UIpocetna.fxml"));

        Scene loginScene = new Scene(root);

        stage.setScene(loginScene);
        stage.setMaximized(false);
        stage.show();;

    }

    //prikazuje pocetni datum
    @FXML
    public void prikaziDatumDo() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        Connection povezi = DriverManager.getConnection("jdbc:mysql://localhost:3306/isprodavnice_final", "root", "");

        double ukupniPazar = 0;
        podaci = FXCollections.observableArrayList(); //observable lista
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   //formater datuma
        java.sql.Date datumDoDatePicker = java.sql.Date.valueOf(datumDo.getValue()); //izvlacimo vrednost datumaOd iz datepickera
        java.util.Date datumDoKonvertovan = new Date(datumDoDatePicker.getTime()); //izvlacimo vrednost datumaDo iz datepickera

        String datumDoString = formatter.format(datumDoKonvertovan); //konvertujemo iz objekta u string
        String queryDatumDo = " select * from prodaja where datum_prodaje <= '" + datumDoKonvertovan + "'";  //upit koji izvlaci sve datume manje od krajnje datuma

        labelTekstDatuma.setVisible(true); //postavljamo na vidljiv label
        labelTekstDatuma.setText("Period: " + datumDoString + ""); // postavljamo labelu tekst
        labelTekstDatuma.textFillProperty().set(Paint.valueOf("orange"));  //postavljamo boju
        labelTekstDatuma.setStyle("-fx-font: 24 arial;"); //postavljamo font

        String queryUkupniPazar = "select sum(cena) from  prodaja where datum_prodaje <= '" + datumDoKonvertovan + "'"; //upit koji izvlaci ukupnu sumu svih cena za datum manji od kranjeg

        rs = povezi.createStatement().executeQuery(queryDatumDo);

        //dodajemo nov objekat u listu podaci iz result seta
        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(2), rs.getFloat(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnModelIzvestaji.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnTipIzvestaji.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacIzvestaji.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));

        tableviewIzvestaji.setItems(null);
        tableviewIzvestaji.setItems(podaci);  //postavljamo sve iz liste podaci u tabelu

        rs = povezi.createStatement().executeQuery(queryUkupniPazar); //

        //
        while (rs.next()) {

            ukupniPazar = rs.getFloat(1);
            txtUkupniPazar.setText(String.valueOf(ukupniPazar)); //postavlja vrednost ukupne cene pazara u text field ukupna cena

        }

        //zatvara sve konekcije
        rs.close();
        povezi.close();
    }

    //prikazuje pazar od-do datuma
    @FXML
    public void prikaziDatumOdDo() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        Connection povezi = DriverManager.getConnection("jdbc:mysql://localhost:3306/isprodavnice_final", "root", "");

        double ukupniPazar = 0;
        podaci = FXCollections.observableArrayList(); //observable lista
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date datumOdDatePicker = java.sql.Date.valueOf(datumOd.getValue()); //izvlacimo vrednost iz datepickera 
        java.util.Date datumOdKonvertovan = new Date(datumOdDatePicker.getTime()); //konvertujemo sql to util
        java.sql.Date datumDoDatePicker = java.sql.Date.valueOf(datumDo.getValue());
        java.util.Date datumDoKonvertovan = new Date(datumDoDatePicker.getTime());

        String datumOdString = formatter.format(datumOdKonvertovan);
        String queryDatumOdDo = " select * from prodaja where datum_prodaje >= '" + datumOdKonvertovan + "' and datum_prodaje <= '" + datumDoKonvertovan + "'";
        String datumDoString = formatter.format(datumDoKonvertovan);

        labelTekstDatuma.setVisible(true);
        labelTekstDatuma.setText("Period:\nOD " + datumOdString + "   DO " + datumDoString + "");
        labelTekstDatuma.textFillProperty().set(Paint.valueOf("orange"));
        labelTekstDatuma.setStyle("-fx-font: 15 arial;");

        String queryUkupniPazar = "select sum(cena) from  prodaja where datum_prodaje >= '" + datumOdKonvertovan + "'and datum_prodaje <= '" + datumDoKonvertovan + "'";

        rs = povezi.createStatement().executeQuery(queryDatumOdDo);

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(2), rs.getFloat(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnModelIzvestaji.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnTipIzvestaji.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacIzvestaji.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));

        tableviewIzvestaji.setItems(null);
        tableviewIzvestaji.setItems(podaci);

        rs = povezi.createStatement().executeQuery(queryUkupniPazar);
        while (rs.next()) {

            ukupniPazar = rs.getFloat(1);
            txtUkupniPazar.setText(String.valueOf(ukupniPazar));

        }

        rs.close();
        povezi.close();

    }

    //prikazuje pazare od pocetnog datuma koji se zadaje
    @FXML
    public void prikaziDatumOd() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        Connection povezi = DriverManager.getConnection("jdbc:mysql://localhost:3306/isprodavnice_final", "root", "");

        double ukupniPazar = 0;
        podaci = FXCollections.observableArrayList(); //observable lista
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date datumOdDatePicker = java.sql.Date.valueOf(datumOd.getValue());
        java.util.Date datumOdKonvertovan = new Date(datumOdDatePicker.getTime());

        String datumOdString = formatter.format(datumOdKonvertovan);
        String queryDatumOd = " select * from prodaja where datum_prodaje >= '" + datumOdKonvertovan + "'";

        labelTekstDatuma.setVisible(true);
        labelTekstDatuma.setText("Period: " + datumOdString + "");
        labelTekstDatuma.textFillProperty().set(Paint.valueOf("orange"));
        labelTekstDatuma.setStyle("-fx-font: 24 arial;");

        String queryUkupniPazar = "select sum(cena) from  prodaja where datum_prodaje >= '" + datumOdKonvertovan + "'";

        rs = povezi.createStatement().executeQuery(queryDatumOd);

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(2), rs.getFloat(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnModelIzvestaji.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnTipIzvestaji.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacIzvestaji.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));

        tableviewIzvestaji.setItems(null);
        tableviewIzvestaji.setItems(podaci);

        rs = povezi.createStatement().executeQuery(queryUkupniPazar);
        while (rs.next()) {

            ukupniPazar = rs.getFloat(1);
            txtUkupniPazar.setText(String.valueOf(ukupniPazar));

        }

        rs.close();
        povezi.close();
    }

    @FXML
    private void godisnji(ActionEvent event) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        Connection povezi = DriverManager.getConnection("jdbc:mysql://localhost:3306/isprodavnice_final", "root", "");

        double ukupniPazar = 0;
        podaci = FXCollections.observableArrayList(); //observable lista
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datumTrenutniDan = new Date(System.currentTimeMillis()); //smestamo trenutni datum u promeljivu

        Calendar cal = Calendar.getInstance(); // promenljiva klase Kalendar
        cal.add(Calendar.DAY_OF_MONTH, -364); //dodajemo broj dana na danasnji dan

        String datumStringGodina = formatter.format(cal.getTime());
        String datumStringDan = formatter.format(datumTrenutniDan);
        String queryGodina = " select * from prodaja where datum_prodaje >= DATE(NOW()) + INTERVAL -364 DAY AND datum_prodaje < NOW() + INTERVAL 0 DAY";

        labelTekstDatuma.setVisible(true);
        labelTekstDatuma.setText("Period:\nOD " + datumStringGodina + "   DO " + datumStringDan + "");
        labelTekstDatuma.textFillProperty().set(Paint.valueOf("orange"));
        labelTekstDatuma.setStyle("-fx-font: 15 arial;");

        String queryUkupniPazar = "select sum(cena) from  prodaja where datum_prodaje >= DATE(NOW()) + INTERVAL -364 DAY AND datum_prodaje < NOW() + INTERVAL 0 DAY";

        rs = povezi.createStatement().executeQuery(queryGodina);

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(2), rs.getFloat(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnModelIzvestaji.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnTipIzvestaji.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacIzvestaji.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));

        tableviewIzvestaji.setItems(null);
        tableviewIzvestaji.setItems(podaci);

        rs = povezi.createStatement().executeQuery(queryUkupniPazar);
        while (rs.next()) {

            ukupniPazar = rs.getFloat(1);
            txtUkupniPazar.setText(String.valueOf(ukupniPazar));

        }

        rs.close();
        povezi.close();
    }

    @FXML
    private void mesecni(ActionEvent event) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        Connection povezi = DriverManager.getConnection("jdbc:mysql://localhost:3306/isprodavnice_final", "root", "");

        double ukupniPazar = 0;
        podaci = FXCollections.observableArrayList(); //observable lista
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datumTrenutniDan = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -29);

        String datumStringMesec = formatter.format(cal.getTime());
        String datumStringDan = formatter.format(datumTrenutniDan);
        String querySedmica = " select * from prodaja where datum_prodaje >= DATE(NOW()) + INTERVAL -29 DAY AND datum_prodaje < NOW() + INTERVAL 0 DAY";

        labelTekstDatuma.setVisible(true);
        labelTekstDatuma.setText("Period:\nOD " + datumStringMesec + "   DO " + datumStringDan + "");
        labelTekstDatuma.textFillProperty().set(Paint.valueOf("orange"));
        labelTekstDatuma.setStyle("-fx-font: 15 arial;");

        String queryUkupniPazar = "select sum(cena) from  prodaja where datum_prodaje >= DATE(NOW()) + INTERVAL -29 DAY AND datum_prodaje < NOW() + INTERVAL 0 DAY";

        rs = povezi.createStatement().executeQuery(querySedmica);

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(2), rs.getFloat(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnModelIzvestaji.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnTipIzvestaji.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacIzvestaji.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));

        tableviewIzvestaji.setItems(null);
        tableviewIzvestaji.setItems(podaci);

        rs = povezi.createStatement().executeQuery(queryUkupniPazar);
        while (rs.next()) {

            ukupniPazar = rs.getFloat(1);
            txtUkupniPazar.setText(String.valueOf(ukupniPazar));

        }

        rs.close();
        povezi.close();
    }

    @FXML
    private void nedeljni(ActionEvent event) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        Connection povezi = DriverManager.getConnection("jdbc:mysql://localhost:3306/isprodavnice_final", "root", "");

        double ukupniPazar = 0;
        podaci = FXCollections.observableArrayList(); //observable lista
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datumTrenutniDan = new Date(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -6);

        String datumStringSedmica = formatter.format(cal.getTime());
        String datumStringDan = formatter.format(datumTrenutniDan);
        String querySedmica = " select * from prodaja where datum_prodaje >= DATE(NOW()) + INTERVAL -6 DAY AND datum_prodaje < NOW() + INTERVAL 0 DAY";

        labelTekstDatuma.setVisible(true);
        labelTekstDatuma.setText("Period:\nOD " + datumStringSedmica + "   DO " + datumStringDan + "");
        labelTekstDatuma.textFillProperty().set(Paint.valueOf("orange"));
        labelTekstDatuma.setStyle("-fx-font: 15 arial;");

        //izvlacimo podatke izmedju danasnjeg dana i prethodnih 6
        String queryUkupniPazar = "select sum(cena) from  prodaja where datum_prodaje >= DATE(NOW()) + INTERVAL -6 DAY AND datum_prodaje < NOW() + INTERVAL 0 DAY";

        rs = povezi.createStatement().executeQuery(querySedmica);

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(2), rs.getFloat(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnModelIzvestaji.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnTipIzvestaji.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacIzvestaji.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));

        tableviewIzvestaji.setItems(null);
        tableviewIzvestaji.setItems(podaci);

        rs = povezi.createStatement().executeQuery(queryUkupniPazar);
        while (rs.next()) {

            ukupniPazar = rs.getFloat(1);
            txtUkupniPazar.setText(String.valueOf(ukupniPazar));

        }

        rs.close();
        povezi.close();

    }

    @FXML
    private void dnevniIzvestaj(ActionEvent event) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");

        Connection povezi = DriverManager.getConnection("jdbc:mysql://localhost:3306/isprodavnice_final", "root", "");

        double ukupniPazar = 0;
        podaci = FXCollections.observableArrayList(); //observable lista
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date datumTrenutniDan = new Date(System.currentTimeMillis());
        String datumString = formatter.format(datumTrenutniDan);
        String queryDnevni = " select * from prodaja where datum_prodaje = '" + datumTrenutniDan + "'";

        labelTekstDatuma.setVisible(true);
        labelTekstDatuma.setText("Period: " + datumString + "");
        labelTekstDatuma.textFillProperty().set(Paint.valueOf("orange"));
        labelTekstDatuma.setStyle("-fx-font: 24 arial;");

        String queryUkupniPazar = "select sum(cena) from  prodaja where datum_prodaje = '" + datumTrenutniDan + "'";
        rs = povezi.createStatement().executeQuery(queryDnevni);

        while (rs.next()) {
            podaci.add(new Komponenta(rs.getString(2), rs.getFloat(3), rs.getInt(5), rs.getString(6), rs.getString(7)));
        }

        //neophodno setovanje cell value factory-ja kako bi se sve celije popunile u jednoj koloni 
        tableColumnModelIzvestaji.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableColumnCenaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("cena"));
        tableColumnKolicinaIzvestaji.setCellValueFactory(new PropertyValueFactory<>("kolicina"));
        tableColumnTipIzvestaji.setCellValueFactory(new PropertyValueFactory<>("tip"));
        tableColumnProizvodjacIzvestaji.setCellValueFactory(new PropertyValueFactory<>("proizvodjac"));

        tableviewIzvestaji.setItems(null);
        tableviewIzvestaji.setItems(podaci);

        rs = povezi.createStatement().executeQuery(queryUkupniPazar);

        while (rs.next()) {

            ukupniPazar = rs.getFloat(1);
            txtUkupniPazar.setText(String.valueOf(ukupniPazar));

        }

        rs.close();
        povezi.close();

    }

}
