package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class CustomerDAO {

    public static void fetchAllCustomers() {
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM customer")) {

            System.out.println("----- CUSTOMER LIST -----");
            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Name");
                String dob = rs.getString("DOB");
                String gender = rs.getString("Gender");
                String address = rs.getString("Address");
                String phone = rs.getString("Phone");
                String email = rs.getString("Email");

                System.out.println(id + " | " + name + " | " + dob + " | " + gender
                        + " | " + address + " | " + phone + " | " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add Customer
    public static void addCustomer(Scanner sc) {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO customer (Customer_ID, Name, DOB, Gender, Address, Phone, Email) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            System.out.print("Enter Customer ID: ");
            int id = sc.nextInt();
            sc.nextLine(); // consume newline

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter DOB (YYYY-MM-DD): ");
            String dob = sc.nextLine();

            System.out.print("Enter Gender: ");
            String gender = sc.nextLine();

            System.out.print("Enter Address: ");
            String address = sc.nextLine();

            System.out.print("Enter Phone: ");
            String phone = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, dob);
            ps.setString(4, gender);
            ps.setString(5, address);
            ps.setString(6, phone);
            ps.setString(7, email);

            ps.executeUpdate();
            System.out.println("✅ Customer added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update mobile number
    public static void updateMobile(Scanner sc) {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE customer SET Phone = ? WHERE Customer_ID = ?")) {

            System.out.print("Enter Customer ID: ");
            int custId = sc.nextInt();
            sc.nextLine(); // consume newline

            System.out.print("Enter New Mobile Number: ");
            String newPhone = sc.nextLine();

            ps.setString(1, newPhone);
            ps.setInt(2, custId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Mobile number updated successfully!");
            } else {
                System.out.println("❌ Customer not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update DOB
    public static void updateDOB(Scanner sc) {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE customer SET DOB = ? WHERE Customer_ID = ?")) {

            System.out.print("Enter Customer ID: ");
            int custId = sc.nextInt();
            sc.nextLine(); // consume newline

            System.out.print("Enter New DOB (YYYY-MM-DD): ");
            String newDob = sc.nextLine();

            ps.setString(1, newDob);
            ps.setInt(2, custId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ DOB updated successfully!");
            } else {
                System.out.println("❌ Customer not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
