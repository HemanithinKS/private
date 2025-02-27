package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.DataBaseConnection;

public class SqlCreate {

    public void createTable() {
        Connection conn = DataBaseConnection.getConnection();
        PreparedStatement pstmt = null;

        try {
            // Customer Table
            String createCustomerTable = "CREATE TABLE Customer ("
                    + "customer_id NUMBER PRIMARY KEY, "
                    + "name VARCHAR2(100) NOT NULL, "
                    + "address VARCHAR2(255), "
                    + "contact VARCHAR2(20) NOT NULL)";

            // Tariff Table
            String createTariffTable = "CREATE TABLE Tariff ("
                    + "type VARCHAR2(20) PRIMARY KEY, "
                    + "rate NUMBER(10, 2) NOT NULL)";

            // TelecomActivity Table (Modified for repeated logs)
            String createTelecomActivityTable = "CREATE TABLE TelecomActivity ("
                    + "customer_id NUMBER NOT NULL, "
                    + "type VARCHAR2(20) NOT NULL, "
                    + "value NUMBER NOT NULL, "
                    + "timestamp NUMBER NOT NULL, "  // Store timestamp as long (System.currentTimeMillis())
                    + "CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES Customer(customer_id), "
                    + "CONSTRAINT fk_tariff FOREIGN KEY (type) REFERENCES Tariff(type))";

            // Execute queries
            pstmt = conn.prepareStatement(createCustomerTable);
            pstmt.executeUpdate();
            System.out.println("Customer table created successfully.");

            pstmt = conn.prepareStatement(createTariffTable);
            pstmt.executeUpdate();
            System.out.println("Tariff table created successfully.");

            pstmt = conn.prepareStatement(createTelecomActivityTable);
            pstmt.executeUpdate();
            System.out.println("TelecomActivity table created successfully.");

        } catch (SQLException e) {
            System.err.println("Error while creating tables: " + e.getMessage());
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

