package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tariff {
    private String type;
    private double rate;
    private static Connection conn = DataBaseConnection.getConnection();

    public Tariff(String type, double rate) {
        this.type = type;
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public double getRate() {
        return rate;
    }

    public static double getRate(String type) {
        double rate = 0.0;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT rate FROM Tariff WHERE type = ?");
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                rate = rs.getDouble("rate");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error fetching tariff rate: " + e.getMessage());
        }
        return rate;
    }

    public static boolean updateRate(String type, double newRate) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Tariff SET rate = ? WHERE type = ?");
            stmt.setDouble(1, newRate);
            stmt.setString(2, type);
            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating tariff rate: " + e.getMessage());
            return false;
        }
    }
}
