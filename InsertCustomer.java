package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class InsertCustomer {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

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

            Connection con = DBUtil.getConnection();

            String sql = "INSERT INTO customer (Customer_ID, Name, DOB, Gender, Address, Phone, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, dob);
            ps.setString(4, gender);
            ps.setString(5, address);
            ps.setString(6, phone);
            ps.setString(7, email);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Customer inserted successfully!");
            }

            con.close();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
