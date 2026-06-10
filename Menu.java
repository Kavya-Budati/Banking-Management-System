package src;
import java.util.Scanner;
public class Menu {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== BANKING MANAGEMENT SYSTEM =====");
            System.out.println("1. View Customers");
            System.out.println("2. Add Customer");
            System.out.println("3. Create Account");
            System.out.println("4. Fund Transfer (includes Deposit & Withdraw)");
            System.out.println("5. Check Balance");
            System.out.println("6. Update Mobile Number");
            System.out.println("7. Update DOB");
            System.out.println("8. Apply for Loan");
            System.out.println("9. Loan Part Payment");
            System.out.println("10. Close Loan");
            System.out.println("11. View Account Statement");
            
            System.out.println("12. Exit");


            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    CustomerDAO.fetchAllCustomers();
                    break;
                case 2:
                    CustomerDAO.addCustomer(sc);   // we’ll wire this if not already present
                    break;
                case 3:
                    AccountDAO.createAccount(sc);
                    break;
                case 4:
                    AccountDAO.transferFunds(sc);
                    break;
                case 5:
                    AccountDAO.viewBalance(sc);
                    break;
                case 6:
                    CustomerDAO.updateMobile(sc);
                    break;
                case 7:
                    CustomerDAO.updateDOB(sc);
                    break;
                case 8:
                    LoanDAO.applyLoan(sc);
                    break;
                case 9:
                    LoanDAO.partPayment(sc);
                    break;
                case 10:
                    LoanDAO.closeLoan(sc);
                    break;
                case 11:
                    AccountDAO.viewStatement(sc);
                    break;
                case 12:
                    System.out.println("👋 Exiting BMS. Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
