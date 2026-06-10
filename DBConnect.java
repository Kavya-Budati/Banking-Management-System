package src;

import java.sql.*;
public class DBConnect {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/bms?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "12@07@2007";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("CONNECTED TO BMS DATABASE SUCCESSFULLY!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
