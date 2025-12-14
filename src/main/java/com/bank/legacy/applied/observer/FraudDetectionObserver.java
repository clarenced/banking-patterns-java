package com.bank.legacy.applied.observer;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Observateur de détection de fraude
 * Détecte les patterns suspects dans les transactions
 */
public class FraudDetectionObserver implements TransactionObserver {

    private static final int MAX_TRANSACTIONS_PER_HOUR = 5;
    private static final double AVERAGE_MULTIPLIER = 5.0;

    // Historique des transactions récentes par compte
    private final Map<String, List<TransactionRecord>> transactionHistory = new HashMap<>();

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        String accountNumber = transaction.getSourceAccount();
        if (accountNumber == null || accountNumber.isEmpty()) {
            accountNumber = transaction.getDestinationAccount();
        }

        if (accountNumber == null) {
            return;
        }

        // Enregistrer la transaction
        List<TransactionRecord> history = transactionHistory.computeIfAbsent(
            accountNumber, k -> new ArrayList<>());

        history.add(new TransactionRecord(transaction.getAmount(), LocalDateTime.now()));

        // Nettoyer les anciennes transactions (plus d'une heure)
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        history.removeIf(record -> record.timestamp.isBefore(oneHourAgo));

        // Vérification 1: Trop de transactions par heure
        if (history.size() > MAX_TRANSACTIONS_PER_HOUR) {
            System.out.println("FRAUD ALERT: Trop de transactions sur le compte " +
                              accountNumber + " (" + history.size() + " en 1 heure)");
        }

        // Vérification 2: Montant inhabituellement élevé
        if (history.size() > 1) {
            double average = calculateAverage(history);
            if (transaction.getAmount() > average * AVERAGE_MULTIPLIER) {
                System.out.println("FRAUD ALERT: Montant inhabituellement élevé sur le compte " +
                                  accountNumber + " (" + transaction.getAmount() +
                                  " EUR vs moyenne de " + String.format("%.2f", average) + " EUR)");
            }
        }
    }

    private double calculateAverage(List<TransactionRecord> history) {
        if (history.isEmpty()) return 0;
        double sum = 0;
        for (TransactionRecord record : history) {
            sum += record.amount;
        }
        return sum / history.size();
    }

    /**
     * Réinitialise l'historique (utile pour les tests)
     */
    public void reset() {
        transactionHistory.clear();
    }

    /**
     * Classe interne pour enregistrer une transaction
     */
    private static class TransactionRecord {
        final double amount;
        final LocalDateTime timestamp;

        TransactionRecord(double amount, LocalDateTime timestamp) {
            this.amount = amount;
            this.timestamp = timestamp;
        }
    }
}
