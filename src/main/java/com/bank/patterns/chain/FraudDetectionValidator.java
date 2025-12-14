package com.bank.patterns.chain;

import com.bank.legacy.BankingService;
import com.bank.legacy.Transaction;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Validateur de détection de fraude
 */
public class FraudDetectionValidator extends AbstractTransactionValidator {

    private static final int MAX_TRANSACTIONS_PER_HOUR = 5;
    private static final double NIGHT_AMOUNT_LIMIT = 5000.0;
    private static final LocalTime NIGHT_START = LocalTime.of(23, 0);
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    // Suivi des transactions récentes par compte
    private final Map<String, List<LocalDateTime>> recentTransactions = new HashMap<>();

    public FraudDetectionValidator() {
        super("FraudDetectionValidator");
    }

    @Override
    public ValidationResult validate(Transaction transaction, BankingService service) {
        String sourceAccount = transaction.getSourceAccount();

        logValidation("Analyse des patterns suspects...");

        // Vérification des transactions nocturnes à montant élevé
        LocalTime currentTime = LocalTime.now();
        boolean isNightTime = currentTime.isAfter(NIGHT_START) || currentTime.isBefore(NIGHT_END);

        if (isNightTime && transaction.getAmount() > NIGHT_AMOUNT_LIMIT) {
            logValidation("ALERTE - Transaction nocturne de montant élevé (" +
                         transaction.getAmount() + " EUR à " + currentTime + ")");
            return ValidationResult.failure("Transaction suspecte: montant élevé pendant les heures de nuit");
        }

        // Vérification du nombre de transactions par heure
        if (sourceAccount != null && !sourceAccount.isEmpty()) {
            List<LocalDateTime> accountTransactions = recentTransactions.computeIfAbsent(
                sourceAccount, k -> new ArrayList<>());

            // Nettoyer les transactions de plus d'une heure
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            accountTransactions.removeIf(dt -> dt.isBefore(oneHourAgo));

            if (accountTransactions.size() >= MAX_TRANSACTIONS_PER_HOUR) {
                logValidation("ALERTE - Trop de transactions en une heure (" +
                             accountTransactions.size() + " transactions)");
                return ValidationResult.failure("Transaction suspecte: trop de transactions en une heure " +
                                               "(max " + MAX_TRANSACTIONS_PER_HOUR + ")");
            }

            // Ajouter la transaction actuelle
            accountTransactions.add(LocalDateTime.now());
            logValidation("OK - " + (accountTransactions.size()) + "/" +
                         MAX_TRANSACTIONS_PER_HOUR + " transactions cette heure");
        }

        logValidation("OK - Aucun pattern suspect détecté");
        return validateNext(transaction, service);
    }

    /**
     * Réinitialise l'historique des transactions (utile pour les tests)
     */
    public void reset() {
        recentTransactions.clear();
    }
}
