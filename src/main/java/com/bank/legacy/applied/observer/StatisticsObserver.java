package com.bank.legacy.applied.observer;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Observateur qui calcule des statistiques sur les transactions
 */
public class StatisticsObserver implements TransactionObserver {

    private int totalTransactions = 0;
    private double totalAmount = 0;
    private final Map<String, Integer> transactionsByType = new HashMap<>();
    private final Map<String, Double> amountsByType = new HashMap<>();

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        totalTransactions++;
        totalAmount += transaction.getAmount();

        String type = transaction.getType();
        transactionsByType.merge(type, 1, Integer::sum);
        amountsByType.merge(type, transaction.getAmount(), Double::sum);
    }

    /**
     * Affiche les statistiques collectées
     */
    public void printStatistics() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("STATISTIQUES DES TRANSACTIONS");
        System.out.println("=".repeat(50));
        System.out.println("Nombre total de transactions: " + totalTransactions);
        System.out.printf("Montant total: %.2f EUR%n", totalAmount);
        System.out.printf("Montant moyen: %.2f EUR%n",
                         totalTransactions > 0 ? totalAmount / totalTransactions : 0);

        System.out.println("\nPar type de transaction:");
        for (String type : transactionsByType.keySet()) {
            int count = transactionsByType.get(type);
            double amount = amountsByType.get(type);
            System.out.printf("  - %s: %d transactions (%.2f EUR)%n", type, count, amount);
        }
        System.out.println("=".repeat(50) + "\n");
    }

    // Getters pour les tests
    public int getTotalTransactions() {
        return totalTransactions;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Map<String, Integer> getTransactionsByType() {
        return new HashMap<>(transactionsByType);
    }

    /**
     * Réinitialise les statistiques
     */
    public void reset() {
        totalTransactions = 0;
        totalAmount = 0;
        transactionsByType.clear();
        amountsByType.clear();
    }
}
