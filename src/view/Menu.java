package view;

import java.util.Scanner;
import controller.CustomerController;

public class Menu {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CustomerController customerController = new CustomerController();

        while (true) {
            System.out.println("\n=========================");
            System.out.println("  Telecom Management System");
            System.out.println("=========================");
            System.out.println("1. Add Customer");
            System.out.println("2. Log Telecom Activity");
            System.out.println("3. Update and View Tariff Rates");
            System.out.println("4. View Customer Details");
            System.out.println("5. View Activity Log for Customer");
            System.out.println("6. Generate Bill for One Customer");
            System.out.println("7. Generate Bills for All Customers (Between Timestamps)");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    customerController.registerCustomer();
                    break;

                case 2:
                    customerController.logActivity();
                    break;

                case 3:
                    customerController.updateTariff();
                    break;

                case 4:
                    customerController.viewCustomerDetails();
                    break;

                case 5:
                    customerController.viewActivityLog();
                    break;

                case 6:
                    customerController.generateBillForSingleCustomer();
                    break;

                case 7:
                    System.out.print("Enter Start Timestamp (milliseconds since epoch): ");
                    long startTimestamp = sc.nextLong();
                    System.out.print("Enter End Timestamp (milliseconds since epoch): ");
                    long endTimestamp = sc.nextLong();
                    customerController.generateBillsForAllCustomersBetweenTimestamps(startTimestamp, endTimestamp);
                    break;

                case 8:
                    System.out.println("Exiting... Thank you!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
                    break;
            }
        }
    }
}
