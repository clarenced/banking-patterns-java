package com.bank.patterns.chain;

import com.bank.legacy.BankingService;
import com.bank.legacy.Transaction;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Validateur de la limite quotidienne par compte
 */
public class DailyLimitValidator extends AbstractTransactionValidator {

    private static final double DAILY_LIMIT = 10000.0;

    // Suivi des transactions quotidiennes par compte
    private final Map<String, DailyTracker> dailyTrackers = new HashMap<>();

    public DailyLimitValidator() {
        super("DailyLimitValidator");
    }

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        String type = transaction.getType();

        // Le dépôt n'a pas de limite quotidienne
        if ("DEPOT".equals(type)) {
            logValidation("Dépôt - pas de limite quotidienne");
            return validateNext(transaction, service);
        }

        String sourceAccount = transaction.getSourceAccount();
        if (sourceAccount == null || sourceAccount.isEmpty()) {
            return validateNext(transaction, service);
        }

        DailyTracker tracker = dailyTrackers.computeIfAbsent(sourceAccount, k -> new DailyTracker());
        tracker.resetIfNewDay();

        double currentTotal = tracker.getTotalAmount();
        double newTotal = currentTotal + transaction.getAmount();

        logValidation("Vérification limite quotidienne pour " + sourceAccount +
                     " (actuel: " + currentTotal + " EUR, demandé: " + transaction.getAmount() + " EUR)");

        if (newTotal > DAILY_LIMIT) {
            logValidation("ÉCHEC - Limite quotidienne dépassée (" + DAILY_LIMIT + " EUR)");
            return ValidationResult.failure("Limite quotidienne dépassée. Maximum: " +
                                           DAILY_LIMIT + " EUR, déjà utilisé: " + currentTotal + " EUR");
        }

        // Mettre à jour le tracker
        tracker.addTransaction(transaction.getAmount());
        logValidation("OK - Nouveau total quotidien: " + newTotal + " EUR");

        return validateNext(transaction, service);
    }

    /**
     * Classe interne pour suivre les transactions quotidiennes
     */
    private static class DailyTracker {
        private LocalDate date;
        private double totalAmount;

        public DailyTracker() {
            this.date = LocalDate.now();
            this.totalAmount = 0;
        }

        public void resetIfNewDay() {
            LocalDate today = LocalDate.now();
            if (!today.equals(date)) {
                date = today;
                totalAmount = 0;
            }
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void addTransaction(double amount) {
            totalAmount += amount;
        }
    }

    /**
     * Réinitialise les compteurs (utile pour les tests)
     */
    public void reset() {
        dailyTrackers.clear();
    }
}
