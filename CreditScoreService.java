package src;
public class CreditScoreService {

    // Simulated AI credit score service
    public static int getCreditScore(int customerId) {
        // For demo: generate score between 650 and 750
        return 650 + (int)(Math.random() * 101);
    }
}
