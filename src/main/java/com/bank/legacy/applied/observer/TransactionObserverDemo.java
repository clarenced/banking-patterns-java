package com.bank.legacy.applied.observer;

import com.bank.legacy.old.BankAccount;
import com.bank.legacy.old.BankingService;
import com.bank.legacy.old.Transaction;

/**
 * Démonstration du pattern Observer pour les transactions
 */
public class TransactionObserverDemo {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("DÉMONSTRATION DU PATTERN OBSERVER (TRANSACTIONS)");
        System.out.println("=".repeat(60));

        // Création du service et des comptes
        BankingService service = new BankingService();
        BankAccount compte1 = service.createAccount("COURANT", "Jean Dupont",
            "jean@email.com", "+33600000001", 5000.0);
        BankAccount compte2 = service.createAccount("EPARGNE", "Marie Martin",
            "marie@email.com", "+33600000002", 10000.0);

        // Création du subject et des observateurs
        TransactionSubject subject = new TransactionSubject();

        EmailNotificationObserver emailObserver = new EmailNotificationObserver();
        SMSNotificationObserver smsObserver = new SMSNotificationObserver();
        AuditLogObserver auditObserver = new AuditLogObserver();
        FraudDetectionObserver fraudObserver = new FraudDetectionObserver();
        StatisticsObserver statsObserver = new StatisticsObserver();

        // Configuration des observateurs
        System.out.println("\n--- Configuration des observateurs ---");
        subject.attach(emailObserver);
        subject.attach(smsObserver);
        subject.attach(auditObserver);
        subject.attach(fraudObserver);
        subject.attach(statsObserver);

        // Simulation de transactions
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 1: Dépôt normal");
        System.out.println("=".repeat(60));
        Transaction tx1 = new Transaction("TX001", "DEPOT", 500.0, null, compte1.getAccountNumber());
        tx1.setStatus("COMPLETED");
        subject.notifyObservers(tx1, service);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 2: Virement important (>1000 EUR)");
        System.out.println("=".repeat(60));
        Transaction tx2 = new Transaction("TX002", "VIREMENT", 2000.0,
            compte1.getAccountNumber(), compte2.getAccountNumber());
        tx2.setStatus("COMPLETED");
        subject.notifyObservers(tx2, service);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 3: Retrait provoquant un découvert");
        System.out.println("=".repeat(60));
        // Simuler le découvert
        compte1.setBalance(-100);
        Transaction tx3 = new Transaction("TX003", "RETRAIT", 3000.0,
            compte1.getAccountNumber(), null);
        tx3.setStatus("COMPLETED");
        subject.notifyObservers(tx3, service);

        // Restaurer le solde
        compte1.setBalance(1000);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 4: Plusieurs transactions rapides (test fraude)");
        System.out.println("=".repeat(60));
        for (int i = 1; i <= 6; i++) {
            Transaction txRapide = new Transaction("TXRAPID" + i, "RETRAIT", 100.0,
                compte2.getAccountNumber(), null);
            txRapide.setStatus("COMPLETED");
            System.out.println("\n--- Transaction rapide " + i + " ---");
            subject.notifyObservers(txRapide, service);
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCÉNARIO 5: Retrait d'un observateur");
        System.out.println("=".repeat(60));
        subject.detach(smsObserver);

        Transaction tx4 = new Transaction("TX004", "DEPOT", 300.0, null, compte1.getAccountNumber());
        tx4.setStatus("COMPLETED");
        System.out.println("\n--- Transaction après retrait SMS observer ---");
        subject.notifyObservers(tx4, service);

        // Affichage du journal d'audit
        System.out.println("\n" + "=".repeat(60));
        System.out.println("JOURNAL D'AUDIT COMPLET");
        System.out.println("=".repeat(60));
        auditObserver.printAuditLog();

        // Affichage des statistiques
        statsObserver.printStatistics();

        System.out.println("=".repeat(60));
        System.out.println("FIN DE LA DÉMONSTRATION");
        System.out.println("=".repeat(60));
    }
}
