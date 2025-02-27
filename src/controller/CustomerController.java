package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import model.DataBaseConnection;
import model.Tariff;

public class CustomerController {
	private Connection conn;
	private Scanner scanner;
	private ExecutorService executor;

	public CustomerController() {
		conn = DataBaseConnection.getConnection();
		scanner = new Scanner(System.in);
		executor = Executors.newFixedThreadPool(5); // Using 5 threads for concurrent bill generation
	}

	public void registerCustomer() {
		System.out.print("ENter the customer id:");
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter Customer Name: ");
		String name = scanner.nextLine();
		System.out.print("Enter Address: ");
		String address = scanner.nextLine();
		System.out.print("Enter Contact: ");
		String contact = scanner.nextLine();

		try {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO Customer (customer_id, name, address, contact) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.setString(3, address);
			stmt.setString(4, contact);
			stmt.executeUpdate();
			stmt.close();
			System.out.println("Customer registered successfully!");
		} catch (SQLException e) {
			System.out.println("Error registering customer: " + e.getMessage());
		}
	}

	public void logActivity() {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Enter Activity Type (Call/Data/SMS): ");
		String type = scanner.nextLine();
		System.out.print("Enter Value (minutes/MB/SMS count): ");
		int value = scanner.nextInt();

		try {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO TelecomActivity (customer_id, type, value, timestamp) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, customerId);
			stmt.setString(2, type);
			stmt.setInt(3, value);
			stmt.setLong(4, System.currentTimeMillis()); // Store current time in milliseconds
			stmt.executeUpdate();
			stmt.close();
			System.out.println("Activity logged successfully!");
		} catch (SQLException e) {
			System.out.println("Error logging activity: " + e.getMessage());
		}
	}

	public void updateTariff() {
		System.out.print("Enter Tariff Type (Call/Data/SMS): ");
		String type = scanner.nextLine();
		System.out.print("Enter New Rate: ");
		double rate = scanner.nextDouble();

		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE Tariff SET rate = ? WHERE type = ?");
			stmt.setDouble(1, rate);
			stmt.setString(2, type);
			stmt.executeUpdate();
			stmt.close();
			System.out.println("Tariff updated successfully!");
		} catch (SQLException e) {
			System.out.println("Error updating tariff: " + e.getMessage());
		}
	}

	public void viewCustomerDetails() {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();

		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Customer WHERE customer_id = ?");
			stmt.setInt(1, customerId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				System.out.println("Customer ID: " + rs.getInt("customer_id"));
				System.out.println("Name: " + rs.getString("name"));
				System.out.println("Address: " + rs.getString("address"));
				System.out.println("Contact: " + rs.getString("contact"));
			} else {
				System.out.println("Customer not found.");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Error retrieving customer details: " + e.getMessage());
		}
	}

	public void viewActivityLog() {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();

		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM TelecomActivity WHERE customer_id = ?");
			stmt.setInt(1, customerId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println("Activity: " + rs.getString("type") + ", Value: " + rs.getInt("value")
						+ ", Timestamp: " + new Timestamp(rs.getLong("timestamp")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Error retrieving activity log: " + e.getMessage());
		}
	}

	public void generateBillForSingleCustomer() {
		System.out.print("Enter Customer ID: ");
		int customerId = scanner.nextInt();

		executor.execute(() -> {
			generateBillForCustomer(customerId, 0, Long.MAX_VALUE);
		});
	}

	public void generateBillsForAllCustomersBetweenTimestamps(long startTimestamp, long endTimestamp) {
		executor.execute(() -> {
			try {
				PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT customer_id FROM TelecomActivity");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					int customerId = rs.getInt("customer_id");
					generateBillForCustomer(customerId, startTimestamp, endTimestamp);
				}
				rs.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error generating bills: " + e.getMessage());
			}
		});
	}

	private void generateBillForCustomer(int customerId, long startTimestamp, long endTimestamp) {
		double total = 0;
		StringBuilder billContent = new StringBuilder();
		billContent.append("Bill for Customer ID: ").append(customerId).append("\n");

		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT type, value FROM TelecomActivity WHERE customer_id = ? AND timestamp BETWEEN ? AND ?");
			stmt.setInt(1, customerId);
			stmt.setLong(2, startTimestamp);
			stmt.setLong(3, endTimestamp);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String type = rs.getString("type");
				int value = rs.getInt("value");
				double rate = Tariff.getRate(type);
				double cost = value * rate;
				total += cost;

				billContent.append(type).append(": ").append(value).append(" units @ ").append(rate).append("/unit = $")
						.append(cost).append("\n");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Error calculating bill: " + e.getMessage());
			return;
		}

		billContent.append("Total Amount Due: $").append(total).append("\n");
		writeBillToFile(customerId, billContent.toString());
	}

	private void writeBillToFile(int customerId, String content) {
		String fileName = "D:/java workspace/Main/src/bills/Customer_" + customerId + "_Bill.txt";
		try (FileWriter writer = new FileWriter(fileName)) {
			writer.write(content);
			System.out.println("Bill generated for Customer ID: " + customerId + " -> " + fileName);
		} catch (IOException e) {
			System.out.println("Error writing bill to file: " + e.getMessage());
		}
	}
}
