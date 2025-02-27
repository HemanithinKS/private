package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnection {
    private static Connection con;
    
    static {
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String user = "demo";
        String pass = "demo";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Database Connected");
        } catch (Exception e) {
            System.out.println("Database Error");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return con;
    }
}
