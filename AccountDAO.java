package src;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccountDAO {

    // Create new account
    public static void createAccount(Scanner sc) {
        try {
            System.out.print("Enter Account No: ");
            int accNo = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Account Type (Savings/Current): ");
            String type = sc.nextLine();

            System.out.print("Enter Opening Balance: ");
            double balance = sc.nextDouble();
            sc.nextLine();

            System.out.print("Enter Opening Date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            System.out.print("Enter Status (Active/Closed): ");
            String status = sc.nextLine();

            System.out.print("Enter Customer ID (existing): ");
            int custId = sc.nextInt();

            Connection con = DBUtil.getConnection();

            String sql = "INSERT INTO account (Account_No, Account_Type, Balance, Opening_Date, Status, Customer_ID) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, accNo);
            ps.setString(2, type);
            ps.setDouble(3, balance);
            ps.setString(4, date);
            ps.setString(5, status);
            ps.setInt(6, custId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Account created successfully!");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // View balance by account number
    public static void viewBalance(Scanner sc) {
        try {
            System.out.print("Enter Account No to check balance: ");
            int accNo = sc.nextInt();

            Connection con = DBUtil.getConnection();

            String sql = "SELECT Balance FROM account WHERE Account_No = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, accNo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("Balance");
                System.out.println("💰 Current Balance: " + balance);
            } else {
                System.out.println("❌ Account not found!");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Deposit money
    public static void deposit(Scanner sc) {
        try {
            System.out.print("Enter Account No: ");
            int accNo = sc.nextInt();

            System.out.print("Enter Deposit Amount: ");
            double amount = sc.nextDouble();

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            String updateSql = "UPDATE account SET Balance = Balance + ? WHERE Account_No = ?";
            PreparedStatement ps1 = con.prepareStatement(updateSql);
            ps1.setDouble(1, amount);
            ps1.setInt(2, accNo);

            int rows = ps1.executeUpdate();
            if (rows == 0) {
                System.out.println("❌ Account not found!");
                con.rollback();
                con.close();
                return;
            }

            String insertSql = "INSERT INTO bank_transaction (Transaction_ID, Amount, Transaction_Type, Transaction_Date, Description, Account_No) "
                             + "VALUES (?, ?, ?, CURDATE(), ?, ?)";
            PreparedStatement ps2 = con.prepareStatement(insertSql);

            System.out.print("Enter Transaction ID: ");
            int txnId = sc.nextInt();
            sc.nextLine();

            ps2.setInt(1, txnId);
            ps2.setDouble(2, amount);
            ps2.setString(3, "DEPOSIT");
            ps2.setString(4, "Cash deposit");
            ps2.setInt(5, accNo);

            ps2.executeUpdate();
            con.commit();
            con.close();

            System.out.println("✅ Deposit successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Withdraw money
    public static void withdraw(Scanner sc) {
        try {
            System.out.print("Enter Account No: ");
            int accNo = sc.nextInt();

            System.out.print("Enter Withdraw Amount: ");
            double amount = sc.nextDouble();

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            String checkSql = "SELECT Balance FROM account WHERE Account_No = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, accNo);
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Account not found!");
                con.rollback();
                con.close();
                return;
            }

            double currentBalance = rs.getDouble("Balance");
            if (currentBalance < amount) {
                System.out.println("❌ Insufficient balance!");
                con.rollback();
                con.close();
                return;
            }

            String updateSql = "UPDATE account SET Balance = Balance - ? WHERE Account_No = ?";
            PreparedStatement ps1 = con.prepareStatement(updateSql);
            ps1.setDouble(1, amount);
            ps1.setInt(2, accNo);
            ps1.executeUpdate();

            String insertSql = "INSERT INTO bank_transaction (Transaction_ID, Amount, Transaction_Type, Transaction_Date, Description, Account_No) "
                             + "VALUES (?, ?, ?, CURDATE(), ?, ?)";
            PreparedStatement ps2 = con.prepareStatement(insertSql);

            System.out.print("Enter Transaction ID: ");
            int txnId = sc.nextInt();
            sc.nextLine();

            ps2.setInt(1, txnId);
            ps2.setDouble(2, amount);
            ps2.setString(3, "WITHDRAW");
            ps2.setString(4, "Cash withdrawal");
            ps2.setInt(5, accNo);

            ps2.executeUpdate();
            con.commit();
            con.close();

            System.out.println("✅ Withdrawal successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fund Transfer: from one account to another
    public static void transferFunds(Scanner sc) {
        
        try {
            System.out.print("Enter FROM Account No: ");
            int fromAcc = sc.nextInt();

            System.out.print("Enter TO Account No: ");
            int toAcc = sc.nextInt();

            System.out.print("Enter Transfer Amount: ");
            double amount = sc.nextDouble();

            Connection con = DBUtil.getConnection();
            con.setAutoCommit(false);

            String checkSql = "SELECT Balance FROM account WHERE Account_No = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, fromAcc);
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ From Account not found!");
                con.rollback();
                con.close();
                return;
            }

            double fromBalance = rs.getDouble("Balance");
            if (fromBalance < amount) {
                System.out.println("❌ Insufficient balance in From Account!");
                con.rollback();
                con.close();
                return;
            }

            String deductSql = "UPDATE account SET Balance = Balance - ? WHERE Account_No = ?";
            PreparedStatement ps1 = con.prepareStatement(deductSql);
            ps1.setDouble(1, amount);
            ps1.setInt(2, fromAcc);
            ps1.executeUpdate();

            String addSql = "UPDATE account SET Balance = Balance + ? WHERE Account_No = ?";
            PreparedStatement ps2 = con.prepareStatement(addSql);
            ps2.setDouble(1, amount);
            ps2.setInt(2, toAcc);
            int rows = ps2.executeUpdate();

            if (rows == 0) {
                System.out.println("❌ To Account not found!");
                con.rollback();
                con.close();
                return;
            }

            String insertSql = "INSERT INTO bank_transaction (Transaction_ID, Amount, Transaction_Type, Transaction_Date, Description, Account_No) "
                             + "VALUES (?, ?, ?, CURDATE(), ?, ?)";
            PreparedStatement ps3 = con.prepareStatement(insertSql);

            System.out.print("Enter Transaction ID for FROM account: ");
            int txnIdFrom = sc.nextInt();

            ps3.setInt(1, txnIdFrom);
            ps3.setDouble(2, amount);
            ps3.setString(3, "TRANSFER_OUT");
            ps3.setString(4, "Transfer to Acc " + toAcc);
            ps3.setInt(5, fromAcc);
            ps3.executeUpdate();

            PreparedStatement ps4 = con.prepareStatement(insertSql);

            System.out.print("Enter Transaction ID for TO account: ");
            int txnIdTo = sc.nextInt();

            ps4.setInt(1, txnIdTo);
            ps4.setDouble(2, amount);
            ps4.setString(3, "TRANSFER_IN");
            ps4.setString(4, "Transfer from Acc " + fromAcc);
            ps4.setInt(5, toAcc);
            ps4.executeUpdate();

            con.commit();
            con.close();

            System.out.println("✅ Fund transfer successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void viewStatement(Scanner sc) {
    try {
        System.out.print("Enter Account No: ");
        int accNo = sc.nextInt();

        Connection con = DBUtil.getConnection();
        String sql = "SELECT Transaction_ID, Transaction_Date, Transaction_Type, Amount, Description " +
                     "FROM bank_transaction WHERE Account_No = ? ORDER BY Transaction_Date DESC";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, accNo);

        ResultSet rs = ps.executeQuery();
        System.out.println("----- Account Statement -----");
        while (rs.next()) {
            System.out.println(
                rs.getInt("Transaction_ID") + " | " +
                rs.getDate("Transaction_Date") + " | " +
                rs.getString("Transaction_Type") + " | " +
                rs.getDouble("Amount") + " | " +
                rs.getString("Description")
            );
        }
        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
