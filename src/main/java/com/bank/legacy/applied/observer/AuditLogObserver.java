package com.bank.legacy.applied.observer;

import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observateur qui enregistre chaque transaction dans un journal d'audit
 */
public class AuditLogObserver implements TransactionObserver {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> auditLog = new ArrayList<>();

    @Override
    public void onTransactionCompleted(Transaction transaction, BankingService service) {
        String logEntry = String.format("[%s] %s | %s | %.2f EUR | %s",
            LocalDateTime.now().format(FORMATTER),
            transaction.getTransactionId(),
            transaction.getType(),
            transaction.getAmount(),
            transaction.getStatus());

        auditLog.add(logEntry);
        System.out.println("AUDIT LOG: " + logEntry);
    }

    /**
     * Retourne le journal d'audit complet
     */
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Affiche le journal d'audit
     */
    public void printAuditLog() {
        System.out.println("\n=== JOURNAL D'AUDIT ===");
        if (auditLog.isEmpty()) {
            System.out.println("Aucune entrée dans le journal");
        } else {
            for (String entry : auditLog) {
                System.out.println(entry);
            }
        }
        System.out.println("=======================\n");
    }

    /**
     * Retourne le nombre d'entrées dans le journal
     */
    public int getLogSize() {
        return auditLog.size();
    }
}
