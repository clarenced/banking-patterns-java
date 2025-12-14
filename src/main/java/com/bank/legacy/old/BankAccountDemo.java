package com.bank.legacy.old;

import com.bank.patterns.decorator2.FeeCalculator;

import java.util.Calendar;

/**
 * Démonstration des méthodes d'affichage de BankingService
 */
public class BankAccountDemo {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║      DÉMONSTRATION - Affichage de Transactions         ║");
        System.out.println("║            via BankingService                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        // Créer le service bancaire
        BankingService service = new BankingService();

        // Créer un compte via le service
        BankAccount account = service.createAccount("COURANT", "Jean Dupont",
            "jean.dupont@email.fr", "0601020304", 1000.0);
        String accountNumber = account.getAccountNumber();

        System.out.println("\n=== Ajout de transactions ===");

        // Ajouter des transactions avec différents montants et dates
        Calendar cal = Calendar.getInstance();

        // Transaction 1 - il y a 5 jours
        cal.add(Calendar.DAY_OF_MONTH, -5);
        Transaction tx1 = new Transaction("TX001", "DEPOT", 500.0, null, accountNumber);
        tx1.setStatus("COMPLETED");
        account.addTransaction(tx1);
        System.out.println("✓ Ajout: DEPOT de 500€ (il y a 5 jours)");

        // Transaction 2 - il y a 4 jours
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Transaction tx2 = new Transaction("TX002", "RETRAIT", 100.0, accountNumber, null);
        tx2.setStatus("COMPLETED");
        account.addTransaction(tx2);
        System.out.println("✓ Ajout: RETRAIT de 100€ (il y a 4 jours)");

        // Transaction 3 - il y a 2 jours
        cal.add(Calendar.DAY_OF_MONTH, 2);
        Transaction tx3 = new Transaction("TX003", "DEPOT", 1500.0, null, accountNumber);
        tx3.setStatus("COMPLETED");
        account.addTransaction(tx3);
        System.out.println("✓ Ajout: DEPOT de 1500€ (il y a 2 jours)");

        // Transaction 4 - hier
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Transaction tx4 = new Transaction("TX004", "VIREMENT", 300.0, accountNumber, "FR987654321", FeeCalculator.Currency.EUR);
        tx4.setStatus("COMPLETED");
        account.addTransaction(tx4);
        System.out.println("✓ Ajout: VIREMENT de 300€ (hier)");

        // Transaction 5 - aujourd'hui
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Transaction tx5 = new Transaction("TX005", "RETRAIT", 50.0, accountNumber, null);
        tx5.setStatus("COMPLETED");
        account.addTransaction(tx5);
        System.out.println("✓ Ajout: RETRAIT de 50€ (aujourd'hui)");

        // Transaction 6 - aujourd'hui (plus tard)
        Transaction tx6 = new Transaction("TX006", "DEPOT", 800.0, null, accountNumber);
        tx6.setStatus("COMPLETED");
        account.addTransaction(tx6);
        System.out.println("✓ Ajout: DEPOT de 800€ (aujourd'hui)");

        // Transaction 7 - montant très élevé
        Transaction tx7 = new Transaction("TX007", "DEPOT", 5000.0, null, accountNumber);
        tx7.setStatus("COMPLETED");
        account.addTransaction(tx7);
        System.out.println("✓ Ajout: DEPOT de 5000€ (aujourd'hui)");

        // Affichage via BankingService
        System.out.println("\n" + "=".repeat(90));
        service.afficherTransactionsChronologique(accountNumber);

        System.out.println("\n" + "=".repeat(90));
        service.afficherTransactionsMontant(accountNumber);

        System.out.println("\n" + "=".repeat(90));
        service.afficherTransactionsParType(accountNumber, "DEPOT");

        System.out.println("\n" + "=".repeat(90));
        service.afficherTransactionsParType(accountNumber, "RETRAIT");

        // Statistiques
        System.out.println("\n" + "=".repeat(90));
        System.out.println("\n=== STATISTIQUES ===");
        System.out.println("Nombre total de transactions: " + account.getTransactions().size());
        System.out.println("Solde actuel du compte: " + account.getBalance() + "€");

        // Calculer total des dépôts et retraits
        double totalDepots = 0;
        double totalRetraits = 0;

        for (Transaction tx : account.getTransactions()) {
            if (tx.getType().equals("DEPOT")) {
                totalDepots += tx.getAmount();
            } else if (tx.getType().equals("RETRAIT") || tx.getType().equals("VIREMENT")) {
                totalRetraits += tx.getAmount();
            }
        }

        System.out.println("Total des dépôts: " + totalDepots + "€");
        System.out.println("Total des retraits/virements: " + totalRetraits + "€");
        System.out.println("\n" + "=".repeat(90));
    }
}
