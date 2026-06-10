package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class LoanDAO {

    // EMI Calculator (Standard Formula)
    private static double calculateEMI(double principal, double annualRate, int years) {
        double monthlyRate = annualRate / 12 / 100;
        int months = years * 12;

        if (monthlyRate == 0) {
            return principal / months; // No interest case
        }

        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months)) /
                     (Math.pow(1 + monthlyRate, months) - 1);

        return emi;
    }

    // Apply for loan with AI credit score + EMI calculation
    public static void applyLoan(Scanner sc) {
        try {
            System.out.print("Enter Loan ID: ");
            int loanId = sc.nextInt();

            System.out.print("Enter Customer ID: ");
            int custId = sc.nextInt();

            System.out.print("Enter Loan Amount: ");
            double loanAmount = sc.nextDouble();

            System.out.print("Enter Annual Interest Rate (%): ");
            double rate = sc.nextDouble();

            System.out.print("Enter Tenure (Years): ");
            int years = sc.nextInt();

            int creditScore = CreditScoreService.getCreditScore(custId);
            System.out.println("📊 Credit Score (AI): " + creditScore);

            if (creditScore < 700) {
                System.out.println("❌ Loan Rejected (Low credit score)");
                return;
            }

            double emi = calculateEMI(loanAmount, rate, years);
            int tenureMonths = years * 12;

            Connection con = DBUtil.getConnection();
            String sql = "INSERT INTO loan (Loan_ID, Customer_ID, Loan_Amount, EMI, Tenure, Status, Interest_Rate) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, loanId);
            ps.setInt(2, custId);
            ps.setDouble(3, loanAmount);
            ps.setDouble(4, emi);
            ps.setInt(5, tenureMonths);
            ps.setString(6, "Active");
            ps.setDouble(7, rate);

            ps.executeUpdate();
            con.close();

            System.out.println("✅ Loan Approved & Created Successfully!");
            System.out.println("💸 Monthly EMI: " + String.format("%.2f", emi));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Loan Part Payment with EMI Recalculation
    public static void partPayment(Scanner sc) {
        try {
            System.out.print("Enter Loan ID: ");
            int loanId = sc.nextInt();

            Connection con = DBUtil.getConnection();

            String fetchSql = "SELECT Loan_Amount, Tenure, Interest_Rate FROM loan WHERE Loan_ID = ? AND Status = 'Active'";
            PreparedStatement fetchPs = con.prepareStatement(fetchSql);
            fetchPs.setInt(1, loanId);
            ResultSet rs = fetchPs.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Active loan not found!");
                con.close();
                return;
            }

            double principal = rs.getDouble("Loan_Amount");
            int tenureMonths = rs.getInt("Tenure");
            double rate = rs.getDouble("Interest_Rate");

            int years = Math.max(1, tenureMonths / 12); // avoid zero

            double currentEmi = calculateEMI(principal, rate, years);

            System.out.println("📌 Current Loan Amount: " + principal);
            System.out.println("📌 Interest Rate: " + rate + "%");
            System.out.println("📌 Remaining Tenure (Years): " + years);
            System.out.println("💸 Current Monthly EMI: " + String.format("%.2f", currentEmi));

            System.out.print("Enter Part Payment Amount: ");
            double partPay = sc.nextDouble();

            if (partPay <= 0 || partPay >= principal) {
                System.out.println("❌ Invalid part payment amount!");
                con.close();
                return;
            }

            double newPrincipal = principal - partPay;

            System.out.print("Enter New Remaining Years to Pay: ");
            int newYears = sc.nextInt();

            double newEmi = calculateEMI(newPrincipal, rate, newYears);
            int newTenureMonths = newYears * 12;

            String updateSql = "UPDATE loan SET Loan_Amount = ?, EMI = ?, Tenure = ? WHERE Loan_ID = ?";
            PreparedStatement ps = con.prepareStatement(updateSql);
            ps.setDouble(1, newPrincipal);
            ps.setDouble(2, newEmi);
            ps.setInt(3, newTenureMonths);
            ps.setInt(4, loanId);

            ps.executeUpdate();
            con.close();

            System.out.println("✅ Part payment successful!");
            System.out.println("📉 New Loan Amount: " + newPrincipal);
            System.out.println("📅 New Tenure (Years): " + newYears);
            System.out.println("💸 New Monthly EMI: " + String.format("%.2f", newEmi));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Loan Closure
    public static void closeLoan(Scanner sc) {
        try {
            System.out.print("Enter Loan ID to close: ");
            int loanId = sc.nextInt();

            Connection con = DBUtil.getConnection();

            String sql = "UPDATE loan SET EMI = 0, Tenure = 0, Status = 'Closed' WHERE Loan_ID = ? AND Status = 'Active'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, loanId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Loan closed successfully!");
            } else {
                System.out.println("❌ Active loan not found or already closed!");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
