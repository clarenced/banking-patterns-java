package com.bank.patterns.chain;

import com.bank.legacy.BankAccount;
import com.bank.legacy.BankingService;
import com.bank.legacy.Transaction;

/**
 * Démonstration du pattern Chain of Responsibility pour la validation des transactions
 */
public class ChainDemo {

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("DÉMONSTRATION DU PATTERN CHAIN OF RESPONSIBILITY");
        System.out.println("=".repeat(70));

        // Création du service et des comptes
        BankingService service = new BankingService();
        BankAccount compte1 = service.createAccount("COURANT", "Jean Dupont",
            "jean@email.com", "+33600000001", 1000.0);
        BankAccount compte2 = service.createAccount("EPARGNE", "Marie Martin",
            "marie@email.com", "+33600000002", 5000.0);

        // Construction de la chaîne de validation
        TransactionValidator chain = ValidationChainBuilder.buildChain();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 1: Transaction valide (dépôt de 500 EUR)");
        System.out.println("=".repeat(70));
        Transaction tx1 = new Transaction("TX001", "DEPOT", 500.0, null, compte1.getAccountNumber());
        ValidationResult result1 = chain.validate(tx1, service);
        System.out.println("Résultat: " + result1);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 2: Montant invalide (négatif)");
        System.out.println("=".repeat(70));
        Transaction tx2 = new Transaction("TX002", "DEPOT", -100.0, null, compte1.getAccountNumber());
        ValidationResult result2 = chain.validate(tx2, service);
        System.out.println("Résultat: " + result2);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 3: Montant trop élevé (60000 EUR)");
        System.out.println("=".repeat(70));
        Transaction tx3 = new Transaction("TX003", "VIREMENT", 60000.0,
            compte1.getAccountNumber(), compte2.getAccountNumber());
        ValidationResult result3 = chain.validate(tx3, service);
        System.out.println("Résultat: " + result3);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 4: Compte inexistant");
        System.out.println("=".repeat(70));
        Transaction tx4 = new Transaction("TX004", "DEPOT", 100.0, null, "ACC999");
        ValidationResult result4 = chain.validate(tx4, service);
        System.out.println("Résultat: " + result4);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 5: Solde insuffisant");
        System.out.println("=".repeat(70));
        Transaction tx5 = new Transaction("TX005", "RETRAIT", 50000.0,
            compte1.getAccountNumber(), null);
        ValidationResult result5 = chain.validate(tx5, service);
        System.out.println("Résultat: " + result5);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 6: Virement valide");
        System.out.println("=".repeat(70));
        Transaction tx6 = new Transaction("TX006", "VIREMENT", 200.0,
            compte1.getAccountNumber(), compte2.getAccountNumber());
        ValidationResult result6 = chain.validate(tx6, service);
        System.out.println("Résultat: " + result6);

        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 7: Limite quotidienne (plusieurs transactions)");
        System.out.println("=".repeat(70));

        // Reconstruire la chaîne pour réinitialiser les compteurs
        chain = ValidationChainBuilder.buildChain();

        // Simuler plusieurs transactions jusqu'à atteindre la limite
        for (int i = 1; i <= 5; i++) {
            Transaction txLimit = new Transaction("TXLIMIT" + i, "RETRAIT", 2500.0,
                compte2.getAccountNumber(), null);
            System.out.println("\nTransaction " + i + " de 2500 EUR:");
            ValidationResult resultLimit = chain.validate(txLimit, service);
            System.out.println("Résultat: " + resultLimit);
            if (!resultLimit.isValid()) {
                break;
            }
        }

        // Résumé de la chaîne
        System.out.println("\n" + "=".repeat(70));
        System.out.println("STRUCTURE DE LA CHAÎNE DE VALIDATION");
        System.out.println("=".repeat(70));
        System.out.println("""

            1. AmountValidator
               └─ Vérifie: montant > 0 ET montant <= 50000 EUR

            2. AccountExistsValidator
               └─ Vérifie: les comptes source/destination existent

            3. AccountStateValidator
               └─ Vérifie: les comptes sont dans un état permettant l'opération

            4. BalanceValidator
               └─ Vérifie: solde suffisant (avec découvert) pour les débits

            5. DailyLimitValidator
               └─ Vérifie: limite quotidienne de 10000 EUR par compte

            6. FraudDetectionValidator
               └─ Vérifie: max 5 transactions/heure, pas de gros montants la nuit
            """);

        System.out.println("=".repeat(70));
        System.out.println("FIN DE LA DÉMONSTRATION");
        System.out.println("=".repeat(70));
    }
}
