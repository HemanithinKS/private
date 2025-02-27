package controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.DataBaseConnection;

public class InsertData {
    public static void main(String[] args) {
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement("INSERT INTO Customer (customer_id, name, address, contact) VALUES (?, ?, ?, ?)");
            pstmt.setInt(1, 101);
            pstmt.setString(2, "John Doe");
            pstmt.setString(3, "123 Main St, NY");
            pstmt.setString(4, "9876543210");
            pstmt.executeUpdate();

            pstmt.setInt(1, 102);
            pstmt.setString(2, "Jane Smith");
            pstmt.setString(3, "456 Elm St, CA");
            pstmt.setString(4, "9123456789");
            pstmt.executeUpdate();

            pstmt.setInt(1, 103);
            pstmt.setString(2, "Alice Brown");
            pstmt.setString(3, "789 Oak St, TX");
            pstmt.setString(4, "9234567891");
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("INSERT INTO Tariff (type, rate) VALUES (?, ?)");
            pstmt.setString(1, "Call");
            pstmt.setDouble(2, 0.05);
            pstmt.executeUpdate();

            pstmt.setString(1, "SMS");
            pstmt.setDouble(2, 0.01);
            pstmt.executeUpdate();

            pstmt.setString(1, "Data");
            pstmt.setDouble(2, 0.10);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("INSERT INTO TelecomActivity (customer_id, type, value, timestamp) VALUES (?, ?, ?, ?)");
            pstmt.setInt(1, 101);
            pstmt.setString(2, "Call");
            pstmt.setInt(3, 10);
            pstmt.setLong(4, System.currentTimeMillis());
            pstmt.executeUpdate();

            pstmt.setInt(1, 101);
            pstmt.setString(2, "SMS");
            pstmt.setInt(3, 5);
            pstmt.setLong(4, System.currentTimeMillis());
            pstmt.executeUpdate();

            pstmt.setInt(1, 102);
            pstmt.setString(2, "Data");
            pstmt.setInt(3, 500);
            pstmt.setLong(4, System.currentTimeMillis());
            pstmt.executeUpdate();

            pstmt.setInt(1, 103);
            pstmt.setString(2, "Call");
            pstmt.setInt(3, 20);
            pstmt.setLong(4, System.currentTimeMillis());
            pstmt.executeUpdate();

            pstmt.setInt(1, 103);
            pstmt.setString(2, "SMS");
            pstmt.setInt(3, 10);
            pstmt.setLong(4, System.currentTimeMillis());
            pstmt.executeUpdate();

            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
