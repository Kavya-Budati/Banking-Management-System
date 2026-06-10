/*package src;

public class TestConnection {
    public static void main(String[] args) {
        DBConnect.main(args);
    }
}*/
/*package src;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection con = DBUtil.getConnection();
            System.out.println("DBUtil connection SUCCESS!");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
/*package src;

public class TestConnection {
    public static void main(String[] args) {
        CustomerDAO.fetchAllCustomers();
    }
}*/
/*package src;

import java.util.Scanner;

public class TestConnection {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        AccountDAO.createAccount(sc);
        AccountDAO.viewBalance(sc);

        sc.close();
    }
}*/
package src;

import java.util.Scanner;

public class TestConnection {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        AccountDAO.deposit(sc);
        AccountDAO.withdraw(sc);
        AccountDAO.viewBalance(sc);

        sc.close();
    }
}




