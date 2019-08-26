package aplikacija;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Komponenta {

    // property svojstvo sluzi nam za povezivanje(binding) podataka, automatski se menja kad se model u view(komponenta) menja
    private SimpleStringProperty tip;
    private SimpleStringProperty proizvodjac;
    private SimpleStringProperty model;
    private SimpleIntegerProperty kolicina;
    private SimpleFloatProperty cena;
    private SimpleStringProperty dostupnost;

    public Komponenta(String tip, String proizvodjac, String model, float cena, int kolicina, String dostupnost) {

        
        this.tip = new SimpleStringProperty(tip);
        this.proizvodjac = new SimpleStringProperty(proizvodjac);
        this.model = new SimpleStringProperty(model);
        this.kolicina = new SimpleIntegerProperty(kolicina);
        this.cena = new SimpleFloatProperty(cena);
        this.dostupnost = new SimpleStringProperty(dostupnost);
    }
    public Komponenta(String tip, String proizvodjac, String model,  int kolicina) {

        
        this.tip = new SimpleStringProperty(tip);
        this.proizvodjac = new SimpleStringProperty(proizvodjac);
        this.model = new SimpleStringProperty(model);
        this.kolicina = new SimpleIntegerProperty(kolicina);
        
    }

    
    public String getTip() {
        return tip.get();
    }

    public void setTip(String tip) {
        this.tip.set(tip);
    }

    public String getProizvodjac() {
        return proizvodjac.get();
    }

    public void setProizvodjac(String proizvodjac) {
        this.proizvodjac.set(proizvodjac);
    }

    public String getModel() {
        return model.get();
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public int getKolicina() {
        return kolicina.get();
    }

    public void setKolicina(int kolicina) {
        this.kolicina.set(kolicina);
    }

    public float getCena() {
        return cena.get();
    }

    public void setCena(float cena) {
        this.cena.set(cena);
    }

    public String getDostupnost() {
        return dostupnost.get();
    }

    public void setDostupnost(String dostupnost) {
        this.dostupnost.set(dostupnost);
    }

}
