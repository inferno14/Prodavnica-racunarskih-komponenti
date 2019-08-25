package aplikacija;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class KonekcijaBaza {

    private String urlDrajvera = "jdbc:mysql://localhost:3306/isprodavnice_final";
    private String username = "root";
    private String password = "";
    private Connection povezi;

    public KonekcijaBaza() {
    }

    public Connection poveziSe() {
        try {
            povezi = DriverManager.getConnection(urlDrajvera, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return povezi;
    }

    public void zatvoriKonekciju(Connection povezi, ResultSet rs) {
        try {
            if (povezi != null) {
                povezi.close();
            }

            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
